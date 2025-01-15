package com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands;

import com.alexlatkin.twitchclipstgbot.controller.*;
import com.alexlatkin.twitchclipstgbot.exception.BroadcasterNotFoundException;
import com.alexlatkin.twitchclipstgbot.keyboard.KeyboardCasterClipsCommand;
import com.alexlatkin.twitchclipstgbot.service.dto.TwitchClip;
import com.alexlatkin.twitchclipstgbot.service.dto.TwitchUser;
import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.alexlatkin.twitchclipstgbot.telegramBotCommands.buttonCommands.BlockButtonCommand;
import com.alexlatkin.twitchclipstgbot.telegramBotCommands.buttonCommands.FollowButtonCommand;
import com.alexlatkin.twitchclipstgbot.telegramBotCommands.buttonCommands.commandsWIthAnswer.NextClipButtonCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/*
 Класс команды /caster_clips
 Относится к командам, которые обрабатывают несколько сообщений пользователя, сообщения этой команды поддерживают клавиатуру
 Отправляет пользователю клипы стримера созданные за сутки
*/
@Slf4j
@RequiredArgsConstructor
@Component
public class CasterClipsCommand implements BotButtonCommands, BotCommandsWithSecondMessage {
    final ClipsController clipsController;
    final UserController userController;
    final BroadcasterController broadcasterController;
    final TwitchController twitchController;
    final FollowButtonCommand followButtonCommand;
    final BlockButtonCommand blockButtonCommand;
    final NextClipButtonCommand nextClipButtonCommand;
    final CacheClipsController cacheClipsController;
    final CacheBroadcasterController cacheBroadcasterController;

    /*
      Метод обрабатывает вызов команды (Сообщение /caster_clips)
      Возвращает SendMessage объект, уточняет у пользователя ник стримера, клипы которого пользователь хочет получить
    */
    @Override
    public SendMessage firstMessage(Update update) {
        var chatId = update.getMessage().getChatId().toString();
        var answerText = "Введите ник стримера";

        return new SendMessage(chatId, answerText);
    }

    /*
      Метод вызывается только после метода firstMessage
      Метод получает ник стримера из сообщения пользователя, делает запрос через ClipsController, получает клипы,
      сортирует их, после кэширует их и стримера в Redis
      Метод возвращает объект SendMessage (И назначает клавиатуру), где текст это url на первый клип
    */
    @Override
    public SendMessage secondMessage(Update update) {
        var chatId = update.getMessage().getChatId();
        var chatIdString = chatId.toString();
        var broadcasterName = update.getMessage().getText();
        Broadcaster caster = new Broadcaster();

        List<TwitchUser> bcList;

        /*
          В ветвлении попытка присвоить значение переменной caster
          Отправляется запрос в дб, если стримера там нет, то делается запрос к твичу через TwitchController
          В случае неудачи выходит из метода и просит пользователя повторить команду указав корректные данные...
        */
        if (broadcasterController.existsBroadcasterByBroadcasterName(broadcasterName)) {
            caster.setBroadcasterId(broadcasterController.getBroadcasterByBroadcasterName(broadcasterName).getBroadcasterId());
            caster.setBroadcasterName(broadcasterController.getBroadcasterByBroadcasterName(broadcasterName).getBroadcasterName());
        } else {

            try {
                bcList = twitchController.getCasterByCasterName(broadcasterName);
            } catch (BroadcasterNotFoundException e) {
                log.error("Пользователь ввел некорректное имя стримера "  + broadcasterName + ", " + e.getMessage());
                return new SendMessage(chatIdString, "Некорректное имя стримера, отправьте команду /caster_clips снова и напишите имя стримера корректно");
            }

            caster.setBroadcasterId(bcList.get(0).getId());
            caster.setBroadcasterName(bcList.get(0).getLogin());
            broadcasterController.addBroadcaster(caster);
        }

        if (userController.getUserBlackListByUserChatId(chatId).contains(caster)) {
            return new SendMessage(chatIdString
                    , String.format("%S находится у вас в чёрном списке, если хотите увидеть клипы %S, удалите его из чёрного списка командой /delete и снова повторите команду /caster_clips", broadcasterName, broadcasterName));
        }

        /*
          Все что ниже это просто запрос к твичу за клипами стримера, сортировка и кэширования этих клипов и стримера...
        */
        List<TwitchClip> clipList = new ArrayList<>(clipsController.getClipsByBroadcaster(caster).getData());

        if (clipList.isEmpty()) {
            return new SendMessage(chatIdString, String.format("Клипов %S сегодня нет", broadcasterName));
        }

        clipList.sort(Comparator.comparing(TwitchClip::getViewCount));

        cacheClipsController.addClipListByUserChatId(chatIdString, clipList);

        var clip = cacheClipsController.getClipByUserChatId(chatIdString);

        cacheBroadcasterController.cacheCaster(chatIdString + "CASTER_CLIPS_COMMAND_CASTER", caster);

        // Подготовка SendMessage объекта (Установка клавиатуры и текста сообщения) и выход из метода
        var msg = new SendMessage(chatIdString, clip.getUrl());

        if (userController.getUserFollowListByUserChatId(chatId).contains(caster)) {
            msg.setReplyMarkup(KeyboardCasterClipsCommand.createKeyboardWithNextButton());
        } else {
            msg.setReplyMarkup(KeyboardCasterClipsCommand.createFullKeyboard(broadcasterName));
        }

        return msg;
    }

    /*
      Метод срабатывает при нажатии пользователем на кнопку клавиатуры одного из сообщений принадлежащих команде /caster_clips
      Определяет какая из кнопок была нажата и вызывает соответствующий метод...
      Возвращает SenMessage или EditMessageText объект в зависимости от нажатой кнопки...
    */
    @Override
    public BotApiMethod clickButton(Update update) {
        var buttonKey = update.getCallbackQuery().getData();
        var chatIdString = update.getCallbackQuery().getMessage().getChatId().toString();
        List<TwitchClip> twitchClipList = cacheClipsController.getClipListByUserChatId(chatIdString);

        var keyForFollowOrBlockCaster = chatIdString + "CASTER_CLIPS_COMMAND_CASTER";
        var caster = cacheBroadcasterController.getCacheCaster(keyForFollowOrBlockCaster);

        if (twitchClipList.isEmpty() && buttonKey.equals("CASTER_CLIPS_NEXT")) {
            return new SendMessage(chatIdString, String.format("Клипы %S закончились", caster.getBroadcasterName()));
        } else {

            BotApiMethod answer = null;

            switch (buttonKey) {
                case "CASTER_CLIPS_FOLLOW" -> answer = followButtonCommand.actionButtonInCurrentMessage(update, caster);
                case "CASTER_CLIPS_BLOCK" -> answer = blockButtonCommand.actionButtonInCurrentMessage(update, caster);
                case "CASTER_CLIPS_NEXT" -> {
                    var clip = cacheClipsController.getClipByUserChatId(chatIdString);
                    answer = nextClipButtonCommand.actionWithMessage(update, clip);
                }
            }
            return answer;
        }
    }
}

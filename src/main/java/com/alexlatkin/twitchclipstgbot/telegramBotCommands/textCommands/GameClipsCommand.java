package com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands;

import com.alexlatkin.twitchclipstgbot.controller.*;
import com.alexlatkin.twitchclipstgbot.keyboard.KeyboardGameClipsCommand;
import com.alexlatkin.twitchclipstgbot.service.dto.TwitchClip;
import com.alexlatkin.twitchclipstgbot.service.dto.TwitchGameDto;
import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.alexlatkin.twitchclipstgbot.model.entity.Game;
import com.alexlatkin.twitchclipstgbot.telegramBotCommands.buttonCommands.BlockButtonCommand;
import com.alexlatkin.twitchclipstgbot.telegramBotCommands.buttonCommands.FollowButtonCommand;
import com.alexlatkin.twitchclipstgbot.telegramBotCommands.buttonCommands.commandsWIthAnswer.NextClipButtonCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/*
 Класс команды /game_clips
 Относится к командам, которые обрабатывают несколько сообщений пользователя, сообщения этой команды поддерживают клавиатуру
 Отправляет пользователю клипы созданные за сутки в выбранной категории или игры
*/
@RequiredArgsConstructor
@Component
public class GameClipsCommand implements BotButtonCommands, BotCommandsWithSecondMessage {
    final ClipsController clipsController;
    final UserController userController;
    final GameController gameController;
    final TwitchController twitchController;
    final FollowButtonCommand followButtonCommand;
    final BlockButtonCommand blockButtonCommand;
    final NextClipButtonCommand nextClipButtonCommand;
    final CacheClipsController cacheClipsController;
    final CacheBroadcasterController cacheBroadcasterController;

    /*
      Метод обрабатывает вызов команды (Сообщение /game_clips)
      Возвращает SendMessage объект, уточняет у пользователя категорию или игру, клипы которой пользователь хочет получить
    */
    @Override
    public SendMessage firstMessage(Update update) {
        var chatId = update.getMessage().getChatId().toString();
        var answerText = "Введите название игры или категории";

        return new SendMessage(chatId, answerText);
    }

    /*
      Метод вызывается только после метода firstMessage
      Метод получает название игры или категории из сообщения пользователя, делает запрос через ClipsController, получает клипы,
      фильтрует их и сортирует, после кэширует их в Redis
      Кэшируется стример первого клипа
      Метод возвращает объект SendMessage (И назначает клавиатуру), где текст это url на первый клип
    */
    @Override
    public SendMessage secondMessage(Update update) {
        var chatId = update.getMessage().getChatId();
        var chatIdString = chatId.toString();
        var gameName = update.getMessage().getText().toLowerCase();

        Game game = new Game();
        List<TwitchGameDto> gameListDto;
        List<TwitchClip> clipList;

        /*
          В ветвлении попытка присвоить значение переменной game
          Делается запрос в дб (В том числе с проверкой на опечатку), если игры нет в дб то делается запрос к твичу через TwitchController
          В случае неудачи выходит из метода и просит пользователя повторить команду указав корректные данные...
        */
        if (gameController.existsGameByGameName(gameName)) {
            game = gameController.getGameByGameName(gameName);
        } else if (gameController.existsGameByMisprintGameName(gameName)) {
            List<Game> games = gameController.findGameByMisprintGameName(gameName);
            game = games.get(0);
        } else {

            gameListDto = twitchController.getGameByGameName(gameName);

            if (gameListDto.isEmpty()) {
                return new SendMessage(chatIdString
                        , "Некорректное название игры, отправьте команду /game_clips снова и напишите название игры корректно");
            }

            game.setGameId(gameListDto.get(0).getId());
            game.setGameName(gameListDto.get(0).getName().toLowerCase());
            gameController.addGame(game);
        }

        /*
          Все что ниже это просто запрос к твичу за клипами по игре, фильтрация, сортировка и кэширования этих клипов и стримера...
        */
        clipList = clipsController.getClipsByGame(game).getData();

        var filterClips = new ArrayList<>(clipList.stream().filter(clip -> !userController.getUserBlackListByUserChatId(chatId).contains(
                new Broadcaster(clip.getBroadcasterId(), clip.getBroadcasterName()))).toList());

        filterClips.sort(Comparator.comparing(TwitchClip::getViewCount));

        cacheClipsController.addClipListByUserChatId(chatIdString, filterClips);

        var firstClip = cacheClipsController.getClipByUserChatId(chatIdString);
        var firstBc = new Broadcaster(firstClip.getBroadcasterId(), firstClip.getBroadcasterName());

        cacheBroadcasterController.cacheCaster(chatIdString + "GAME_CLIPS_COMMAND_CASTER", firstBc);

        // Подготовка SendMessage объекта (Установка клавиатуры и текста сообщения) и выход из метода
        var msg = new SendMessage(chatIdString, firstClip.getUrl());

        if (userController.getUserFollowListByUserChatId(chatId).contains(firstBc)) {
            msg.setReplyMarkup(KeyboardGameClipsCommand.createKeyboardWithNextButton());
        } else {
            msg.setReplyMarkup(KeyboardGameClipsCommand.createFullKeyboard(firstBc.getBroadcasterName()));
        }

        return msg;
    }

    /*
      Метод срабатывает при нажатии пользователем на кнопку клавиатуры одного из сообщений принадлежащих команде /game_clips
      Определяет какая из кнопок была нажата и вызывает соответствующий метод...
      Возвращает SenMessage или EditMessageText объект в зависимости от нажатой кнопки...
    */
    @Override
    public BotApiMethod clickButton(Update update) {
        var buttonKey = update.getCallbackQuery().getData();
        var chatIdString = update.getCallbackQuery().getMessage().getChatId().toString();
        List<TwitchClip> twitchClipList = cacheClipsController.getClipListByUserChatId(chatIdString);

        if (twitchClipList.isEmpty() && buttonKey.equals("GAME_CLIPS_NEXT")) {
            return new SendMessage(chatIdString, "Клипы закончились");
        } else {

            var keyForPreviousCaster = chatIdString + "GAME_CLIPS_COMMAND_CASTER";
            var caster = new Broadcaster();

            BotApiMethod answer = null;

            switch (buttonKey) {
                case "GAME_CLIPS_FOLLOW" -> {
                    caster = cacheBroadcasterController.getCacheCaster(keyForPreviousCaster);
                    answer = followButtonCommand.actionButtonInCurrentMessage(update, caster);
                }
                case "GAME_CLIPS_BLOCK" -> {
                    caster = cacheBroadcasterController.getCacheCaster(keyForPreviousCaster);
                    answer = blockButtonCommand.actionButtonInCurrentMessage(update, caster);
                }
                case "GAME_CLIPS_NEXT" -> {
                    var clip = cacheClipsController.getClipByUserChatId(chatIdString);
                    cacheBroadcasterController.cacheCaster(keyForPreviousCaster, new Broadcaster(clip.getBroadcasterId(), clip.getBroadcasterName()));
                    answer = nextClipButtonCommand.actionWithMessage(update, clip);
                }
            }

            return answer;
        }
    }
}

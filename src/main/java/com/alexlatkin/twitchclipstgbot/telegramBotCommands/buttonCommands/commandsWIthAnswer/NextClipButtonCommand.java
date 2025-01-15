package com.alexlatkin.twitchclipstgbot.telegramBotCommands.buttonCommands.commandsWIthAnswer;

import com.alexlatkin.twitchclipstgbot.controller.UserController;
import com.alexlatkin.twitchclipstgbot.keyboard.KeyboardCasterClipsCommand;
import com.alexlatkin.twitchclipstgbot.keyboard.KeyboardFollowListClipsCommand;
import com.alexlatkin.twitchclipstgbot.keyboard.KeyboardGameClipsCommand;
import com.alexlatkin.twitchclipstgbot.service.dto.TwitchClip;
import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/*
  Класс кнопки "Следующий клип"
*/
@Component
@RequiredArgsConstructor
public class NextClipButtonCommand implements ButtonCommandsWithAnswer {
    final UserController userController;

    /*
      Метод срабатывает когда в одном из сообщений нажимают кнопку "Следующий клип"
      Метод возвращает SendMessage объект, где текст это клип из кэша Redis, назначает клавиатуру в зависимости
      от команды из которой он был вызван.
      При назначении клавиатуры проверятся наличие или отсутствием стримера в списке подписок пользователя
    */
    @Override
    public SendMessage actionWithMessage(Update update, TwitchClip clip) {
        var chatId = update.getCallbackQuery().getMessage().getChatId();
        var casterName = clip.getBroadcasterName();
        var answerText = clip.getUrl();
        var buttonKey = update.getCallbackQuery().getData();

        Broadcaster caster = new Broadcaster();
        caster.setBroadcasterId(clip.getBroadcasterId());
        caster.setBroadcasterName(casterName);
        SendMessage answer = new SendMessage();

        switch (buttonKey) {
            case "GAME_CLIPS_NEXT" -> {
                if (userController.getUserFollowListByUserChatId(chatId).contains(caster)) {
                    answer.setReplyMarkup(KeyboardGameClipsCommand.createKeyboardWithNextButton());
                } else {
                    answer.setReplyMarkup(KeyboardGameClipsCommand.createFullKeyboard(casterName));
                }
            }
            case "CASTER_CLIPS_NEXT" -> {
                if (userController.getUserFollowListByUserChatId(chatId).contains(caster)) {
                    answer.setReplyMarkup(KeyboardCasterClipsCommand.createKeyboardWithNextButton());
                } else {
                    answer.setReplyMarkup(KeyboardCasterClipsCommand.createFullKeyboard(casterName));
                }
            }
            case "FOLLOW_LIST_CLIPS_NEXT" ->
                    answer.setReplyMarkup(KeyboardFollowListClipsCommand.createKeyboardWithNextButton());
        }

        answer.setChatId(chatId.toString());
        answer.setText(answerText);

        return answer;
    }

}

package com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands;

import com.alexlatkin.twitchclipstgbot.controller.UserController;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class ClearFollowListCommand implements BotCommands  {
    final UserController userController;
    @Override
    public SendMessage firstMessage(Update update) {
        var chatId = update.getMessage().getChatId();

        SendMessage answer = new SendMessage();
        answer.setChatId(chatId.toString());

        if (userController.getUserFollowListByUserChatId(chatId).isEmpty()) {
            answer.setText("В ваших подписках нет стримеров");
        } else {
            userController.clearUserFollowList(chatId);
            answer.setText("Список ваших подписок теперь чист");
        }

        return answer;
    }
}

package com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands;

import com.alexlatkin.twitchclipstgbot.controller.UserController;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class ClearBlackListCommand implements BotCommands {
    final UserController userController;
    @Override
    public SendMessage firstMessage(Update update) {
        var chatId = update.getMessage().getChatId();

        SendMessage answer = new SendMessage();
        answer.setChatId(chatId.toString());

        if (userController.getUserBlackListByUserChatId(chatId).isEmpty()) {
            answer.setText("В вашем чёрном списке никого нет");
        } else {
            userController.clearUserBlackList(chatId);
            answer.setText("Ваш чёрный список теперь чист");
        }

        return answer;
    }
}

package com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands;

import com.alexlatkin.twitchclipstgbot.controller.UserController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/*
 Класс команды /clear_black_list
 Относится к командам, которые обрабатывают одно сообщение пользователя, сообщения этой команды не имеют клавиатуры
*/
@RequiredArgsConstructor
@Component
public class ClearBlackListCommand implements BotCommands {
    final UserController userController;

    //  Очищает чёрный список пользователя
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

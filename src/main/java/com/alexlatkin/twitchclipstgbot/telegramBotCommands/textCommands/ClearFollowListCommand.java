package com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands;

import com.alexlatkin.twitchclipstgbot.controller.UserController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/*
 Класс команды /clear_follow_list
 Относится к командам, которые обрабатывают одно сообщение пользователя, сообщения этой команды не имеют клавиатуры
*/
@RequiredArgsConstructor
@Component
public class ClearFollowListCommand implements BotCommands  {
    final UserController userController;

    // Очищает список подписок пользователя
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

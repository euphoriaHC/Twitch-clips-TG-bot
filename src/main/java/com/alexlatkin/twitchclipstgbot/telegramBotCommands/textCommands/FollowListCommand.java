package com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands;

import com.alexlatkin.twitchclipstgbot.controller.UserController;
import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/*
 Класс команды /follow_list
 Относится к командам, которые обрабатывают одно сообщение пользователя, сообщения этой команды не имеют клавиатуры
*/
@RequiredArgsConstructor
@Component
public class FollowListCommand implements BotCommands {
    final UserController userController;

    //  Выводит список подписок пользователя
    @Override
    public SendMessage firstMessage(Update update) {
        var chatId = update.getMessage().getChatId();
        var userFollowList = userController.getUserFollowListByUserChatId(chatId);
        var answer = new SendMessage();
        var chatIdString = chatId.toString();

        answer.setChatId(chatIdString);

        if (userFollowList.isEmpty()) {
            answer.setText("Ваш список подписок пуст");
        } else {
            var bcNames = userFollowList.stream()
                    .map(Broadcaster::getBroadcasterName)
                    .map(e -> e.concat("\n"))
                    .toList()
                    .toString()
                    .replace("[", "")
                    .replace("]", "")
                    .replace(",", "");
            answer.setText(bcNames);
        }

        return answer;
    }
}

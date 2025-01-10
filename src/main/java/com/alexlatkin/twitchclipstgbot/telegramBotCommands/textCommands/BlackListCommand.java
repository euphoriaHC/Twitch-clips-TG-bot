package com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands;

import com.alexlatkin.twitchclipstgbot.controller.UserController;
import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/*
 Класс команды /black_list
 Относится к командам, которые обрабатывают одно сообщение пользователя, сообщения этой команды не имеют клавиатуры
*/
@RequiredArgsConstructor
@Component
public class BlackListCommand implements BotCommands {
    final UserController userController;

    //  Выводит чёрный список пользователя
    @Override
    public SendMessage firstMessage(Update update) {
        var chatId = update.getMessage().getChatId();
        var userBlackList = userController.getUserBlackListByUserChatId(chatId);
        var answer = new SendMessage();
        var chatIdString = chatId.toString();

        answer.setChatId(chatIdString);

        if (userBlackList.isEmpty()) {
            answer.setText("Ваш чёрный лист пуст");
        } else {
            var bcNames = userBlackList.stream()
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

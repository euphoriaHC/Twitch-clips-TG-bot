package com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands;

import com.alexlatkin.twitchclipstgbot.controller.UserController;
import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class BlackListCommand implements BotCommands {
    final UserController userController;
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

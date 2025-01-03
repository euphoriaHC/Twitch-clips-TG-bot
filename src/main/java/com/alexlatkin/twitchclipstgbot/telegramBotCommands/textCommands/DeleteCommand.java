package com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands;

import com.alexlatkin.twitchclipstgbot.controller.BroadcasterController;
import com.alexlatkin.twitchclipstgbot.controller.UserController;
import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class DeleteCommand implements BotCommands, BotCommandsWithSecondMessage {
    final UserController userController;
    final BroadcasterController broadcasterController;
    @Override
    public SendMessage firstMessage(Update update) {
        var chatId = update.getMessage().getChatId();

        var userFollowList = userController.getUserFollowListByUserChatId(chatId);
        var userBlackList = userController.getUserBlackListByUserChatId(chatId);

        var userFollowListAsString = userFollowList.stream()
                                .map(Broadcaster::getBroadcasterName)
                                .map(e -> e.concat("\n"))
                                .toList()
                                .toString()
                                .replace("["," ")
                                .replace("]","")
                                .replace(",","");


        var userBlackListAsString = userBlackList.stream()
                                .map(Broadcaster::getBroadcasterName)
                                .map(e -> e.concat("\n"))
                                .toList()
                                .toString()
                                .replace("["," ")
                                .replace("]","")
                                .replace(",","");


        SendMessage answer = new SendMessage();
        answer.setChatId(chatId.toString());

        if (userFollowList.isEmpty() && userBlackList.isEmpty()) {
            answer.setText("Ваш список подписок и чёрный лист пусты");
        } else if (userFollowList.isEmpty()) {
            answer.setText(" \n Находится у вас в чёрном списке: \n" + userBlackListAsString + " \n Если хотите удалить стримера введите его ник");
        } else if (userBlackList.isEmpty()) {
            answer.setText( "Вы подписаны на: \n" + userFollowListAsString + " \n Если хотите удалить стримера введите его ник");
        } else {
            answer.setText("Вы подписаны на: \n" + userFollowListAsString + " \nНаходятся у вас в чёрном списке: \n" + userBlackListAsString + " \n Если хотите удалить стримера введите его ник");
        }

        return answer;
    }

    @Override
    public SendMessage secondMessage(Update update) {
        var chatId = update.getMessage().getChatId();
        var bcName = update.getMessage().getText().toLowerCase();
        SendMessage answer = new SendMessage();
        answer.setChatId(chatId.toString());

        if (!broadcasterController.existsBroadcasterByBroadcasterName(bcName)) {
            answer.setText("Вы ввели неверное имя стримера");
            return answer;
        }

        var broadcaster = broadcasterController.getBroadcasterByBroadcasterName(bcName);
        var userFollowList = userController.getUserFollowListByUserChatId(chatId);
        var userBlackList = userController.getUserBlackListByUserChatId(chatId);

        if (userFollowList.contains(broadcaster)) {
            userController.deleteBroadcasterFromUserFollowList(chatId, broadcaster);
            answer.setText(bcName + " удален из ваших подписок");
        } else if (userBlackList.contains(broadcaster)) {
            userController.deleteBroadcasterFromUserBlackList(chatId, broadcaster);
            answer.setText(bcName + " удален из вашего черного списка");
        } else {
            answer.setText("Вы ввели неверное имя стримера");
        }

        return answer;
    }

}

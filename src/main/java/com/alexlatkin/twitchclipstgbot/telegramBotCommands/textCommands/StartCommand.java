package com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands;

import com.alexlatkin.twitchclipstgbot.controller.UserController;
import com.alexlatkin.twitchclipstgbot.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class StartCommand implements BotCommands {
    final UserController userController;
    final HelpCommand helpCommand;
    @Override
    public SendMessage firstMessage(Update update) {
        var chatId = update.getMessage().getChatId();

        if (!userController.existsUserByChatId(chatId)) {
            var userName = update.getMessage().getChat().getFirstName();
            User user = new User(chatId, userName);
            userController.addUser(user);
        }

        return helpCommand.firstMessage(update);
    }

}

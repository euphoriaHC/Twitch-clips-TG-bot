package com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands;

import com.alexlatkin.twitchclipstgbot.controller.UserController;
import com.alexlatkin.twitchclipstgbot.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/*
 Класс команды /start
 Относится к командам, которые обрабатывают одно сообщение пользователя, сообщения этой команды не имеют клавиатуры
*/
@RequiredArgsConstructor
@Component
public class StartCommand implements BotCommands {
    final UserController userController;
    final HelpCommand helpCommand;

    // Сохраняет пользователя в дб и отправляет на команду /help
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

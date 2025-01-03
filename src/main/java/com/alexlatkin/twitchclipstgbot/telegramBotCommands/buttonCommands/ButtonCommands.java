package com.alexlatkin.twitchclipstgbot.telegramBotCommands.buttonCommands;

import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;


public interface ButtonCommands {
    EditMessageText actionButtonInCurrentMessage(Update update, Broadcaster broadcaster);
}

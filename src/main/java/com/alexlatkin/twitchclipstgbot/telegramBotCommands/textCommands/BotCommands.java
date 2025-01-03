package com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotCommands {
    SendMessage firstMessage(Update update);
}

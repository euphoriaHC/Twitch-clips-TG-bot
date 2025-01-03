package com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotButtonCommands extends BotCommands {
    BotApiMethod clickButton(Update update);
}

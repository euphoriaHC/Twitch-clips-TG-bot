package com.alexlatkin.twitchclipstgbot.telegramBotCommands.buttonCommands.commandsWIthAnswer;

import com.alexlatkin.twitchclipstgbot.service.dto.TwitchClip;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


public interface ButtonCommandsWithAnswer {
    SendMessage actionWithMessage(Update update, TwitchClip clip);
}

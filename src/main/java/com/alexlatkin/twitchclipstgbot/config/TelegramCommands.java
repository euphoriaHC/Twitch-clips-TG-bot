package com.alexlatkin.twitchclipstgbot.config;

import com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands.BotButtonCommands;
import com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands.BotCommands;
import com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands.BotCommandsWithSecondMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Configuration
public class TelegramCommands {
    private Map<String, BotCommands> textCommandsFirstMessage;
    private Map<String, BotCommandsWithSecondMessage> textCommandWithSecondMessage;
    private Map<String, BotButtonCommands> buttonCommands;
}

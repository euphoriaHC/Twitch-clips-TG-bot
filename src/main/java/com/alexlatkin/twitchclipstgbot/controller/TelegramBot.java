package com.alexlatkin.twitchclipstgbot.controller;

import com.alexlatkin.twitchclipstgbot.config.BotConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig botConfig;
    final ConcurrentMap<String, String> cacheChatIdAndUserCommandMessage = new ConcurrentHashMap<>();
    final ClipsController clipsController;
    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            var userMessageText = update.getMessage().getText();
            var chatId = update.getMessage().getChatId().toString();
            var textCommands = botConfig.getTelegramCommands().getTextCommandsFirstMessage();
            var textCommandsWithSecondMessage = botConfig.getTelegramCommands().getTextCommandWithSecondMessage();
            var cacheCommand = cacheChatIdAndUserCommandMessage.get(chatId);
            log.info("Пользователь " + chatId + " отправил сообщение. Текст сообщения - " + userMessageText);

            if (textCommands.containsKey(userMessageText)) {
                var response = textCommands.get(userMessageText).firstMessage(update);
                cacheChatIdAndUserCommandMessage.put(chatId, userMessageText);
                sendAnswerMessage(response);

            } else if (cacheChatIdAndUserCommandMessage.containsKey(chatId) && textCommandsWithSecondMessage.containsKey(cacheCommand)) {
                var response = textCommandsWithSecondMessage.get(cacheCommand).secondMessage(update);
                cacheChatIdAndUserCommandMessage.remove(chatId);
                sendAnswerMessage(response);

            } else {
                var response = new SendMessage();
                response.setChatId(chatId);
                response.setText("Команда не поддерживается");
                sendAnswerMessage(response);
            }

        } else if (update.hasCallbackQuery()) {
            var buttonKey = update.getCallbackQuery().getData();
            var buttonCommands = botConfig.getTelegramCommands().getButtonCommands();
            log.info("Пользователь " + update.getCallbackQuery().getMessage().getChatId() + " нажал кнопку. buttonKey - " + buttonKey);

            if (buttonCommands.containsKey(buttonKey)) {
                var response = buttonCommands.get(buttonKey).clickButton(update);
                sendAnswerMessage(response);
            }
        }
    }

    public void sendAnswerMessage(BotApiMethod message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}

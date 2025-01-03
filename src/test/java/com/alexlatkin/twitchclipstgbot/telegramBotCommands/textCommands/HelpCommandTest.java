package com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HelpCommandTest {
    private static final long chatId = 1L;
    HelpCommand helpCommand;
    @BeforeEach
    void setUp() {
        helpCommand = new HelpCommand();
    }

    @Test
    void firstMessage_ValidResponse() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);

        String expectedText = """
                Добро пожаловать в Twitch Clips bot!
                
                С помощью бота ты можешь получать самые популярные клипы игр или стримеров за день, а так же отслеживать стримеров и блокировать их.
                
                Вот полный список команд:
                
                /help - Описание всех команд
                
                /game_clips - Получить клипы по названию игры или категории
                
                /caster_clips - Получить клипы по никнейму стримера
                
                /follow_list_clips - Получить клипы стримеров, которых вы отслеживаете
                
                /follow_list - Ваш список подписок стримеров
                
                /black_list - Ваш чёрный лист стримеров
                
                /delete - Удалить стримера из фоллоу листа или чёрного списка
                
                /clear_follow_list - Очищает ваш список подписок
                
                /clear_black_list - Очищает ваш чёрный лист
                """;

        var result = helpCommand.firstMessage(updateMock);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId, Long.parseLong(result.getChatId()));
        assertEquals(expectedText, result.getText());
    }
}
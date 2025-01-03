package com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands;

import com.alexlatkin.twitchclipstgbot.controller.UserController;
import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlackListCommandTest {
    @InjectMocks
    BlackListCommand blackListCommand;
    @Mock
    UserController userController;

    @Test
    void firstMessage_UserBlackListIsEmpty() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(userController.getUserBlackListByUserChatId(chatId)).thenReturn(Collections.emptyList());

        var result = blackListCommand.firstMessage(updateMock);

        assertEquals(chatId.toString(), result.getChatId());
        assertEquals("Ваш чёрный лист пуст", result.getText());
    }

    @Test
    void firstMessage_UserBlackListIsNotEmpty() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(userController.getUserBlackListByUserChatId(chatId)).thenReturn(List.of(new Broadcaster(1, "firstBc")
                                                                                    , new Broadcaster(2, "secondBc")));

        String expectedText = "firstBc\n secondBc\n";
        var result = blackListCommand.firstMessage(updateMock);

        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(expectedText, result.getText());
    }
}
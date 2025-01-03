package com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands;

import com.alexlatkin.twitchclipstgbot.controller.UserController;
import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClearBlackListCommandTest {
    private static final long chatId = 1L;
    @InjectMocks
    ClearBlackListCommand clearBlackListCommand;
    @Mock
    private UserController userController;

    @Test
    void firstMessage_BlackListIsEmpty() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(userController.getUserBlackListByUserChatId(chatId)).thenReturn(Collections.emptyList());

        var result = clearBlackListCommand.firstMessage(updateMock);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId, Long.parseLong(result.getChatId()));
        assertEquals("В вашем чёрном списке никого нет", result.getText());

        verify(userController, never()).clearUserBlackList(chatId);
    }

    @Test
    void firstMessage_BlackListIsNotEmpty() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(userController.getUserBlackListByUserChatId(chatId)).thenReturn(List.of(new Broadcaster(1337, "pudge")));

        var result = clearBlackListCommand.firstMessage(updateMock);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId, Long.parseLong(result.getChatId()));
        assertEquals("Ваш чёрный список теперь чист", result.getText());

        verify(userController, times(1)).clearUserBlackList(chatId);
    }
}
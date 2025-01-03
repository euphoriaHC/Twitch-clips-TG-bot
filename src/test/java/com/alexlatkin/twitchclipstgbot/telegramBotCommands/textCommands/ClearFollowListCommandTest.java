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
class ClearFollowListCommandTest {
    private static final long chatId = 1L;
    @InjectMocks
    ClearFollowListCommand clearFollowListCommand;
    @Mock
    UserController userController;
    @Test
    void firstMessage_FollowListIsEmpty() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(userController.getUserFollowListByUserChatId(chatId)).thenReturn(Collections.emptyList());

        var result = clearFollowListCommand.firstMessage(updateMock);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId, Long.parseLong(result.getChatId()));
        assertEquals("В ваших подписках нет стримеров", result.getText());

        verify(userController, never()).clearUserFollowList(chatId);
    }

    @Test
    void firstMessage_FollowListIsNotEmpty() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(userController.getUserFollowListByUserChatId(chatId)).thenReturn(List.of(new Broadcaster(1337, "pudge")));

        var result = clearFollowListCommand.firstMessage(updateMock);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId, Long.parseLong(result.getChatId()));
        assertEquals("Список ваших подписок теперь чист", result.getText());

        verify(userController, times(1)).clearUserFollowList(chatId);
    }
}
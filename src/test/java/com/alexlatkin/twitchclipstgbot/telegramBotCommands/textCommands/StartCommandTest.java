package com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands;

import com.alexlatkin.twitchclipstgbot.controller.UserController;
import com.alexlatkin.twitchclipstgbot.model.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StartCommandTest {
    @InjectMocks
    StartCommand startCommand;
    @Mock
    UserController userController;
    @Mock
    HelpCommand helpCommand;

    @Test
    void firstMessage_UserExists() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(userController.existsUserByChatId(chatId)).thenReturn(true);

        SendMessage helpMessage = new SendMessage(chatId.toString(), "HelpCommand text");
        when(helpCommand.firstMessage(updateMock)).thenReturn(helpMessage);

        var result = startCommand.firstMessage(updateMock);

        assertEquals(helpMessage, result);
        verify(userController, never()).addUser(any(User.class));
        verify(helpCommand, times(1)).firstMessage(updateMock);
    }

    @Test
    void firstMessage_UserNotExists() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Chat chatMock = mock(Chat.class);
        Long chatId = 1L;

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(userController.existsUserByChatId(chatId)).thenReturn(false);
        when(messageMock.getChat()).thenReturn(chatMock);
        when(chatMock.getFirstName()).thenReturn("pudge");

        SendMessage helpMessage = new SendMessage(chatId.toString(), "HelpCommand text");
        when(helpCommand.firstMessage(updateMock)).thenReturn(helpMessage);

        var result = startCommand.firstMessage(updateMock);

        assertEquals(helpMessage, result);
        verify(userController, times(1)).addUser(any(User.class));
        verify(helpCommand, times(1)).firstMessage(updateMock);
    }
}
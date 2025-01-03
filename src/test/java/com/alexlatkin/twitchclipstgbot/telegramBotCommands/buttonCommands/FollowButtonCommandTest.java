package com.alexlatkin.twitchclipstgbot.telegramBotCommands.buttonCommands;

import com.alexlatkin.twitchclipstgbot.controller.BroadcasterController;
import com.alexlatkin.twitchclipstgbot.controller.UserController;
import com.alexlatkin.twitchclipstgbot.keyboard.KeyboardCasterClipsCommand;
import com.alexlatkin.twitchclipstgbot.keyboard.KeyboardGameClipsCommand;
import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowButtonCommandTest {
    @InjectMocks
    FollowButtonCommand followButtonCommand;
    @Mock
    UserController userController;
    @Mock
    BroadcasterController broadcasterController;
    @Test
    void actionButtonInCurrentMessage_BroadcasterExistsInDB_ButtonKeyIsGAME_CLIPS_FOLLOW() {
        Update updateMock = mock(Update.class);
        CallbackQuery callbackQueryMock = mock(CallbackQuery.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        int messageId = 1;
        String buttonKey = "GAME_CLIPS_FOLLOW";

        String bcName = "pudge";
        Broadcaster broadcaster = new Broadcaster(1, bcName);

        when(updateMock.getCallbackQuery()).thenReturn(callbackQueryMock);
        when(callbackQueryMock.getMessage()).thenReturn(messageMock);
        when(callbackQueryMock.getData()).thenReturn(buttonKey);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getMessageId()).thenReturn(messageId);
        when(broadcasterController.existsBroadcasterByBroadcasterName(bcName)).thenReturn(true);
        when(broadcasterController.getBroadcasterByBroadcasterName(bcName)).thenReturn(broadcaster);

        var result = followButtonCommand.actionButtonInCurrentMessage(updateMock, broadcaster);

        assertEquals(EditMessageText.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(" \n Вы подписались на " + bcName, result.getText());
        assertEquals(KeyboardGameClipsCommand.createKeyboardWithNextButton(), result.getReplyMarkup());
        verify(broadcasterController, times(1)).existsBroadcasterByBroadcasterName(bcName);
        verify(broadcasterController, times(1)).getBroadcasterByBroadcasterName(bcName);
        verify(userController, times(1)).addBroadcasterInUserFollowList(chatId, broadcasterController.getBroadcasterByBroadcasterName(bcName));
    }

    @Test
    void actionButtonInCurrentMessage_BroadcasterNotExistsInDB_ButtonKeyIsGAME_CLIPS_FOLLOW() {
        Update updateMock = mock(Update.class);
        CallbackQuery callbackQueryMock = mock(CallbackQuery.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        int messageId = 1;
        String buttonKey = "GAME_CLIPS_FOLLOW";

        String bcName = "pudge";
        Broadcaster broadcaster = new Broadcaster(1, bcName);

        when(updateMock.getCallbackQuery()).thenReturn(callbackQueryMock);
        when(callbackQueryMock.getMessage()).thenReturn(messageMock);
        when(callbackQueryMock.getData()).thenReturn(buttonKey);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getMessageId()).thenReturn(messageId);
        when(broadcasterController.existsBroadcasterByBroadcasterName(bcName)).thenReturn(false);

        var result = followButtonCommand.actionButtonInCurrentMessage(updateMock, broadcaster);

        assertEquals(EditMessageText.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(" \n Вы подписались на " + bcName, result.getText());
        assertEquals(KeyboardGameClipsCommand.createKeyboardWithNextButton(), result.getReplyMarkup());
        verify(broadcasterController, times(1)).existsBroadcasterByBroadcasterName(bcName);
        verify(broadcasterController, times(1)).addBroadcaster(broadcaster);
        verify(userController, times(1)).addBroadcasterInUserFollowList(chatId, broadcaster);
    }

    @Test
    void actionButtonInCurrentMessage_BroadcasterExistsInDB_ButtonKeyIsCASTER_CLIPS_FOLLOW() {
        Update updateMock = mock(Update.class);
        CallbackQuery callbackQueryMock = mock(CallbackQuery.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        int messageId = 1;
        String buttonKey = "CASTER_CLIPS_FOLLOW";

        String bcName = "pudge";
        Broadcaster broadcaster = new Broadcaster(1, bcName);

        when(updateMock.getCallbackQuery()).thenReturn(callbackQueryMock);
        when(callbackQueryMock.getMessage()).thenReturn(messageMock);
        when(callbackQueryMock.getData()).thenReturn(buttonKey);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getMessageId()).thenReturn(messageId);
        when(broadcasterController.existsBroadcasterByBroadcasterName(bcName)).thenReturn(true);
        when(broadcasterController.getBroadcasterByBroadcasterName(bcName)).thenReturn(broadcaster);

        var result = followButtonCommand.actionButtonInCurrentMessage(updateMock, broadcaster);

        assertEquals(EditMessageText.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(" \n Вы подписались на " + bcName, result.getText());
        assertEquals(KeyboardCasterClipsCommand.createKeyboardWithNextButton(), result.getReplyMarkup());
        verify(broadcasterController, times(1)).existsBroadcasterByBroadcasterName(bcName);
        verify(broadcasterController, times(1)).getBroadcasterByBroadcasterName(bcName);
        verify(userController, times(1)).addBroadcasterInUserFollowList(chatId, broadcasterController.getBroadcasterByBroadcasterName(bcName));
    }
    @Test
    void actionButtonInCurrentMessage_BroadcasterNotExistsInDB_ButtonKeyIsCASTER_CLIPS_FOLLOW() {
        Update updateMock = mock(Update.class);
        CallbackQuery callbackQueryMock = mock(CallbackQuery.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        int messageId = 1;
        String buttonKey = "CASTER_CLIPS_FOLLOW";

        String bcName = "pudge";
        Broadcaster broadcaster = new Broadcaster(1, bcName);

        when(updateMock.getCallbackQuery()).thenReturn(callbackQueryMock);
        when(callbackQueryMock.getMessage()).thenReturn(messageMock);
        when(callbackQueryMock.getData()).thenReturn(buttonKey);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getMessageId()).thenReturn(messageId);
        when(broadcasterController.existsBroadcasterByBroadcasterName(bcName)).thenReturn(false);

        var result = followButtonCommand.actionButtonInCurrentMessage(updateMock, broadcaster);

        assertEquals(EditMessageText.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(" \n Вы подписались на " + bcName, result.getText());
        assertEquals(KeyboardCasterClipsCommand.createKeyboardWithNextButton(), result.getReplyMarkup());
        verify(broadcasterController, times(1)).existsBroadcasterByBroadcasterName(bcName);
        verify(broadcasterController, times(1)).addBroadcaster(broadcaster);
        verify(userController, times(1)).addBroadcasterInUserFollowList(chatId, broadcaster);
    }
}
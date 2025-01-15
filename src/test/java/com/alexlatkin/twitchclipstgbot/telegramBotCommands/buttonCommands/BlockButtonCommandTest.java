package com.alexlatkin.twitchclipstgbot.telegramBotCommands.buttonCommands;

import com.alexlatkin.twitchclipstgbot.controller.BroadcasterController;
import com.alexlatkin.twitchclipstgbot.controller.CacheClipsController;
import com.alexlatkin.twitchclipstgbot.controller.UserController;
import com.alexlatkin.twitchclipstgbot.keyboard.KeyboardCasterClipsCommand;
import com.alexlatkin.twitchclipstgbot.keyboard.KeyboardGameClipsCommand;
import com.alexlatkin.twitchclipstgbot.service.dto.TwitchClip;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlockButtonCommandTest {
    @InjectMocks
    BlockButtonCommand blockButtonCommand;
    @Mock
    UserController userController;
    @Mock
    BroadcasterController broadcasterController;
    @Mock
    CacheClipsController cacheClipsController;

    @Test
    void actionButtonInCurrentMessage_BroadcasterExistsInDB_ButtonKeyIsGAME_CLIPS_BLOCK() {
        Update updateMock = mock(Update.class);
        CallbackQuery callbackQueryMock = mock(CallbackQuery.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        int messageId = 1;
        String buttonKey = "GAME_CLIPS_BLOCK";

        String bcName = "pudge";
        Broadcaster broadcaster = new Broadcaster(1, bcName);
        List<TwitchClip> clipList = List.of(new TwitchClip(), new TwitchClip());

        when(updateMock.getCallbackQuery()).thenReturn(callbackQueryMock);
        when(callbackQueryMock.getMessage()).thenReturn(messageMock);
        when(callbackQueryMock.getData()).thenReturn(buttonKey);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getMessageId()).thenReturn(messageId);
        when(broadcasterController.existsBroadcasterByBroadcasterName(bcName)).thenReturn(true);
        when(broadcasterController.getBroadcasterByBroadcasterName(bcName)).thenReturn(broadcaster);
        when(cacheClipsController.getClipListByUserChatId(chatId.toString())).thenReturn(clipList);

        var result = blockButtonCommand.actionButtonInCurrentMessage(updateMock, broadcaster);

        assertEquals(EditMessageText.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(String.format("\n %S добавлен в чёрный список", bcName), result.getText());
        assertEquals(KeyboardGameClipsCommand.createKeyboardWithNextButton(), result.getReplyMarkup());
        verify(broadcasterController, times(1)).existsBroadcasterByBroadcasterName(bcName);
        verify(broadcasterController, times(1)).getBroadcasterByBroadcasterName(bcName);
        verify(userController, times(1)).addBroadcasterInUserBlackList(chatId, broadcasterController.getBroadcasterByBroadcasterName(bcName));
        verify(cacheClipsController, times(1)).getClipListByUserChatId(chatId.toString());
        verify(cacheClipsController, times(1)).deleteAllClipsByUserChatId(chatId.toString());
        verify(cacheClipsController, times(1)).addClipListByUserChatId(chatId.toString(), clipList);
    }

    @Test
    void actionButtonInCurrentMessage_BroadcasterNotExistsInDB_ButtonKeyIsGAME_CLIPS_BLOCK() {
        Update updateMock = mock(Update.class);
        CallbackQuery callbackQueryMock = mock(CallbackQuery.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        int messageId = 1;
        String buttonKey = "GAME_CLIPS_BLOCK";

        String bcName = "pudge";
        Broadcaster broadcaster = new Broadcaster(1, bcName);
        List<TwitchClip> clipList = List.of(new TwitchClip(), new TwitchClip());

        when(updateMock.getCallbackQuery()).thenReturn(callbackQueryMock);
        when(callbackQueryMock.getMessage()).thenReturn(messageMock);
        when(callbackQueryMock.getData()).thenReturn(buttonKey);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getMessageId()).thenReturn(messageId);
        when(broadcasterController.existsBroadcasterByBroadcasterName(bcName)).thenReturn(false);
        when(cacheClipsController.getClipListByUserChatId(chatId.toString())).thenReturn(clipList);

        var result = blockButtonCommand.actionButtonInCurrentMessage(updateMock, broadcaster);

        assertEquals(EditMessageText.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(String.format("\n %S добавлен в чёрный список", bcName), result.getText());
        assertEquals(KeyboardGameClipsCommand.createKeyboardWithNextButton(), result.getReplyMarkup());
        verify(broadcasterController, times(1)).existsBroadcasterByBroadcasterName(bcName);
        verify(broadcasterController, times(1)).addBroadcaster(broadcaster);
        verify(userController, times(1)).addBroadcasterInUserBlackList(chatId, broadcaster);
        verify(cacheClipsController, times(1)).getClipListByUserChatId(chatId.toString());
        verify(cacheClipsController, times(1)).deleteAllClipsByUserChatId(chatId.toString());
        verify(cacheClipsController, times(1)).addClipListByUserChatId(chatId.toString(), clipList);
    }

    @Test
    void actionButtonInCurrentMessage_BroadcasterExistsInDB_ButtonKeyIsCASTER_CLIPS_BLOCK() {
        Update updateMock = mock(Update.class);
        CallbackQuery callbackQueryMock = mock(CallbackQuery.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        int messageId = 1;
        String buttonKey = "CASTER_CLIPS_BLOCK";

        String bcName = "pudge";
        Broadcaster broadcaster = new Broadcaster(1, bcName);
        List<TwitchClip> clipList = List.of(new TwitchClip(), new TwitchClip());

        when(updateMock.getCallbackQuery()).thenReturn(callbackQueryMock);
        when(callbackQueryMock.getMessage()).thenReturn(messageMock);
        when(callbackQueryMock.getData()).thenReturn(buttonKey);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getMessageId()).thenReturn(messageId);
        when(broadcasterController.existsBroadcasterByBroadcasterName(bcName)).thenReturn(true);
        when(broadcasterController.getBroadcasterByBroadcasterName(bcName)).thenReturn(broadcaster);
        when(cacheClipsController.getClipListByUserChatId(chatId.toString())).thenReturn(clipList);

        var result = blockButtonCommand.actionButtonInCurrentMessage(updateMock, broadcaster);

        assertEquals(EditMessageText.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(String.format("\n %S добавлен в чёрный список", bcName), result.getText());
        assertEquals(KeyboardCasterClipsCommand.createKeyboardWithNextButton(), result.getReplyMarkup());
        verify(broadcasterController, times(1)).existsBroadcasterByBroadcasterName(bcName);
        verify(broadcasterController, times(1)).getBroadcasterByBroadcasterName(bcName);
        verify(userController, times(1)).addBroadcasterInUserBlackList(chatId, broadcasterController.getBroadcasterByBroadcasterName(bcName));
        verify(cacheClipsController, times(1)).getClipListByUserChatId(chatId.toString());
        verify(cacheClipsController, times(1)).deleteAllClipsByUserChatId(chatId.toString());
        verify(cacheClipsController, times(1)).addClipListByUserChatId(chatId.toString(), clipList);
    }

    @Test
    void actionButtonInCurrentMessage_BroadcasterNotExistsInDB_ButtonKeyIsCASTER_CLIPS_BLOCK() {
        Update updateMock = mock(Update.class);
        CallbackQuery callbackQueryMock = mock(CallbackQuery.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        int messageId = 1;
        String buttonKey = "CASTER_CLIPS_BLOCK";

        String bcName = "pudge";
        Broadcaster broadcaster = new Broadcaster(1, bcName);
        List<TwitchClip> clipList = List.of(new TwitchClip(), new TwitchClip());

        when(updateMock.getCallbackQuery()).thenReturn(callbackQueryMock);
        when(callbackQueryMock.getMessage()).thenReturn(messageMock);
        when(callbackQueryMock.getData()).thenReturn(buttonKey);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getMessageId()).thenReturn(messageId);
        when(broadcasterController.existsBroadcasterByBroadcasterName(bcName)).thenReturn(false);
        when(cacheClipsController.getClipListByUserChatId(chatId.toString())).thenReturn(clipList);

        var result = blockButtonCommand.actionButtonInCurrentMessage(updateMock, broadcaster);

        assertEquals(EditMessageText.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(String.format("\n %S добавлен в чёрный список", bcName), result.getText());
        assertEquals(KeyboardCasterClipsCommand.createKeyboardWithNextButton(), result.getReplyMarkup());
        verify(broadcasterController, times(1)).existsBroadcasterByBroadcasterName(bcName);
        verify(broadcasterController, times(1)).addBroadcaster(broadcaster);
        verify(userController, times(1)).addBroadcasterInUserBlackList(chatId, broadcaster);
        verify(cacheClipsController, times(1)).getClipListByUserChatId(chatId.toString());
        verify(cacheClipsController, times(1)).deleteAllClipsByUserChatId(chatId.toString());
        verify(cacheClipsController, times(1)).addClipListByUserChatId(chatId.toString(), clipList);
    }
}
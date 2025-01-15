package com.alexlatkin.twitchclipstgbot.telegramBotCommands.buttonCommands.commandsWIthAnswer;

import com.alexlatkin.twitchclipstgbot.controller.UserController;
import com.alexlatkin.twitchclipstgbot.keyboard.KeyboardCasterClipsCommand;
import com.alexlatkin.twitchclipstgbot.keyboard.KeyboardFollowListClipsCommand;
import com.alexlatkin.twitchclipstgbot.keyboard.KeyboardGameClipsCommand;
import com.alexlatkin.twitchclipstgbot.service.dto.TwitchClip;
import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NextClipButtonCommandTest {
    @InjectMocks
    NextClipButtonCommand nextClipButtonCommand;
    @Mock
    UserController userController;
    @Test
    void actionWithMessage_ButtonKeyIsGAME_CLIPS_NEXT_KeyboardWithNextButton() {
        Update updateMock = mock(Update.class);
        CallbackQuery callbackQueryMock = mock(CallbackQuery.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        String buttonKey = "GAME_CLIPS_NEXT";

        TwitchClip clip = new TwitchClip("url", 1, "pudge", 100);

        when(updateMock.getCallbackQuery()).thenReturn(callbackQueryMock);
        when(callbackQueryMock.getMessage()).thenReturn(messageMock);
        when(callbackQueryMock.getData()).thenReturn(buttonKey);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(userController.getUserFollowListByUserChatId(chatId)).thenReturn(List.of(new Broadcaster(1, "pudge")));

        var result = nextClipButtonCommand.actionWithMessage(updateMock, clip);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(clip.getUrl(), result.getText());
        assertEquals(KeyboardGameClipsCommand.createKeyboardWithNextButton(), result.getReplyMarkup());
        verify(userController, times(1)).getUserFollowListByUserChatId(chatId);
    }

    @Test
    void actionWithMessage_ButtonKeyIsGAME_CLIPS_NEXT_FullKeyboard() {
        Update updateMock = mock(Update.class);
        CallbackQuery callbackQueryMock = mock(CallbackQuery.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        String buttonKey = "GAME_CLIPS_NEXT";

        TwitchClip clip = new TwitchClip("url", 1, "pudge", 100);

        when(updateMock.getCallbackQuery()).thenReturn(callbackQueryMock);
        when(callbackQueryMock.getMessage()).thenReturn(messageMock);
        when(callbackQueryMock.getData()).thenReturn(buttonKey);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(userController.getUserFollowListByUserChatId(chatId)).thenReturn(List.of(new Broadcaster()));

        var result = nextClipButtonCommand.actionWithMessage(updateMock, clip);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(clip.getUrl(), result.getText());
        assertEquals(KeyboardGameClipsCommand.createFullKeyboard(clip.getBroadcasterName()), result.getReplyMarkup());
        verify(userController, times(1)).getUserFollowListByUserChatId(chatId);
    }
    @Test
    void actionWithMessage_ButtonKeyIsCASTER_CLIPS_NEXT_KeyboardWithNextButton() {
        Update updateMock = mock(Update.class);
        CallbackQuery callbackQueryMock = mock(CallbackQuery.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        String buttonKey = "CASTER_CLIPS_NEXT";

        TwitchClip clip = new TwitchClip("url", 1, "pudge", 100);

        when(updateMock.getCallbackQuery()).thenReturn(callbackQueryMock);
        when(callbackQueryMock.getMessage()).thenReturn(messageMock);
        when(callbackQueryMock.getData()).thenReturn(buttonKey);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(userController.getUserFollowListByUserChatId(chatId)).thenReturn(List.of(new Broadcaster(1, "pudge")));

        var result = nextClipButtonCommand.actionWithMessage(updateMock, clip);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(clip.getUrl(), result.getText());
        assertEquals(KeyboardCasterClipsCommand.createKeyboardWithNextButton(), result.getReplyMarkup());
        verify(userController, times(1)).getUserFollowListByUserChatId(chatId);
    }
    @Test
    void actionWithMessage_ButtonKeyIsCASTER_CLIPS_NEXT_FullKeyboard() {
        Update updateMock = mock(Update.class);
        CallbackQuery callbackQueryMock = mock(CallbackQuery.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        String buttonKey = "CASTER_CLIPS_NEXT";

        TwitchClip clip = new TwitchClip("url", 1, "pudge", 100);

        when(updateMock.getCallbackQuery()).thenReturn(callbackQueryMock);
        when(callbackQueryMock.getMessage()).thenReturn(messageMock);
        when(callbackQueryMock.getData()).thenReturn(buttonKey);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(userController.getUserFollowListByUserChatId(chatId)).thenReturn(List.of(new Broadcaster()));

        var result = nextClipButtonCommand.actionWithMessage(updateMock, clip);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(clip.getUrl(), result.getText());
        assertEquals(KeyboardCasterClipsCommand.createFullKeyboard(clip.getBroadcasterName()), result.getReplyMarkup());
        verify(userController, times(1)).getUserFollowListByUserChatId(chatId);
    }

    @Test
    void actionWithMessage_ButtonKeyIsFOLLOW_LIST_CLIPS_NEXT_KeyboardWithNextButton() {
        Update updateMock = mock(Update.class);
        CallbackQuery callbackQueryMock = mock(CallbackQuery.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        String buttonKey = "FOLLOW_LIST_CLIPS_NEXT";

        TwitchClip clip = new TwitchClip("url", 1, "pudge", 100);

        when(updateMock.getCallbackQuery()).thenReturn(callbackQueryMock);
        when(callbackQueryMock.getMessage()).thenReturn(messageMock);
        when(callbackQueryMock.getData()).thenReturn(buttonKey);
        when(messageMock.getChatId()).thenReturn(chatId);

        var result = nextClipButtonCommand.actionWithMessage(updateMock, clip);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(clip.getUrl(), result.getText());
        assertEquals(KeyboardFollowListClipsCommand.createKeyboardWithNextButton(), result.getReplyMarkup());
    }
}
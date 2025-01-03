package com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands;

import com.alexlatkin.twitchclipstgbot.controller.CacheClipsController;
import com.alexlatkin.twitchclipstgbot.controller.ClipsController;
import com.alexlatkin.twitchclipstgbot.controller.UserController;
import com.alexlatkin.twitchclipstgbot.keyboard.KeyboardFollowListClipsCommand;
import com.alexlatkin.twitchclipstgbot.model.dto.TwitchClip;
import com.alexlatkin.twitchclipstgbot.model.dto.TwitchClipsDto;
import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.alexlatkin.twitchclipstgbot.telegramBotCommands.buttonCommands.commandsWIthAnswer.NextClipButtonCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowListClipsCommandTest {
    @InjectMocks
    FollowListClipsCommand followListClipsCommand;
    @Mock
    UserController userController;
    @Mock
    ClipsController clipsController;
    @Mock
    NextClipButtonCommand nextClipButtonCommand;
    @Mock
    CacheClipsController cacheClipsController;

    @Test
    void firstMessage() throws Exception {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        List<Broadcaster> userFollowList = List.of(new Broadcaster(1, "firstBc")
                                                , new Broadcaster(2, "secondBc"));
        List<CompletableFuture<TwitchClipsDto>> futureClipList = List.of(
                CompletableFuture.completedFuture(new TwitchClipsDto(List.of(new TwitchClip("url", 1, "firstClip", 100)))),
                CompletableFuture.completedFuture(new TwitchClipsDto(List.of(new TwitchClip("url", 2, "secondClip", 200)))));

        List<TwitchClip> clipList = new ArrayList<>();

        for (CompletableFuture<TwitchClipsDto> clip : futureClipList) {

            clipList.addAll(clip.get().getData());

        }

        TwitchClip clip = clipList.get(0);

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(userController.getUserFollowListByUserChatId(chatId)).thenReturn(userFollowList);
        when(clipsController.getClipsByUserFollowList(userFollowList)).thenReturn(futureClipList);
        when(cacheClipsController.getClipByUserChatId(chatId.toString())).thenReturn(clip);

        var result = followListClipsCommand.firstMessage(updateMock);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(clip.getUrl(), result.getText());
        assertEquals(KeyboardFollowListClipsCommand.createKeyboardWithNextButton(), result.getReplyMarkup());
        verify(userController, times(1)).getUserFollowListByUserChatId(chatId);
        verify(clipsController, times(1)).getClipsByUserFollowList(userFollowList);
        verify(cacheClipsController, times(1)).addClipListByUserChatId(chatId.toString(), clipList);
        verify(cacheClipsController, times(1)).getClipByUserChatId(chatId.toString());
    }

    @Test
    void firstMessage_FollowListEmpty() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(userController.getUserFollowListByUserChatId(chatId)).thenReturn(Collections.emptyList());

        var result = followListClipsCommand.firstMessage(updateMock);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals("У вас нет отслеживаемых стримеров", result.getText());
        verify(userController, times(1)).getUserFollowListByUserChatId(chatId);
    }

    @Test
    void firstMessage_ExecutionException() throws Exception {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;

        List<Broadcaster> userFollowList = List.of(new Broadcaster(1, "firstBc")
                                                , new Broadcaster(2, "secondBc"));

        CompletableFuture<TwitchClipsDto> failedFutureMock = mock(CompletableFuture.class);

        List<CompletableFuture<TwitchClipsDto>> clipsFutures = List.of(failedFutureMock);

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(userController.getUserFollowListByUserChatId(chatId)).thenReturn(userFollowList);
        when(failedFutureMock.get()).thenThrow(new ExecutionException(new RuntimeException()));
        when(clipsController.getClipsByUserFollowList(userFollowList)).thenReturn(clipsFutures);

        assertThrows(RuntimeException.class, () -> followListClipsCommand.firstMessage(updateMock));
    }

    @Test
    void clickButton_NextCase() {
        Update updateMock = mock(Update.class);
        CallbackQuery callbackQueryMock = mock(CallbackQuery.class);
        Message messageMock = mock(Message.class);
        var buttonKey = "FOLLOW_LIST_CLIPS_NEXT";
        Long chatId = 1L;

        List<TwitchClip> clipsList = List.of(new TwitchClip("url", 1, "bcName", 100));

        when(updateMock.getCallbackQuery()).thenReturn(callbackQueryMock);
        when(updateMock.getCallbackQuery().getMessage()).thenReturn(messageMock);
        when(updateMock.getCallbackQuery().getData()).thenReturn(buttonKey);
        when(updateMock.getCallbackQuery().getMessage().getChatId()).thenReturn(chatId);
        when(cacheClipsController.getClipListByUserChatId(chatId.toString())).thenReturn(clipsList);
        when(cacheClipsController.getClipByUserChatId(chatId.toString())).thenReturn(clipsList.get(0));
        when(nextClipButtonCommand.actionWithMessage(updateMock, clipsList.get(0))).thenReturn(new SendMessage(chatId.toString(), clipsList.get(0).getUrl()));

        SendMessage result = (SendMessage) followListClipsCommand.clickButton(updateMock);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(clipsList.get(0).getUrl(), result.getText());
        verify(cacheClipsController, times(1)).getClipListByUserChatId(chatId.toString());
        verify(cacheClipsController, times(1)).getClipByUserChatId(chatId.toString());
        verify(nextClipButtonCommand, times(1)).actionWithMessage(updateMock, clipsList.get(0));
    }
    @Test
    void clickButton_NextCaseWithEmptyList() {
        Update updateMock = mock(Update.class);
        CallbackQuery callbackQueryMock = mock(CallbackQuery.class);
        Message messageMock = mock(Message.class);
        var buttonKey = "FOLLOW_LIST_CLIPS_NEXT";
        Long chatId = 1L;

        when(updateMock.getCallbackQuery()).thenReturn(callbackQueryMock);
        when(updateMock.getCallbackQuery().getMessage()).thenReturn(messageMock);
        when(updateMock.getCallbackQuery().getData()).thenReturn(buttonKey);
        when(updateMock.getCallbackQuery().getMessage().getChatId()).thenReturn(chatId);
        when(cacheClipsController.getClipListByUserChatId(chatId.toString())).thenReturn(Collections.emptyList());

        SendMessage result = (SendMessage) followListClipsCommand.clickButton(updateMock);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals("Клипы из фоллоу листа законичилсь", result.getText());
        verify(cacheClipsController, times(1)).getClipListByUserChatId(chatId.toString());
    }
}
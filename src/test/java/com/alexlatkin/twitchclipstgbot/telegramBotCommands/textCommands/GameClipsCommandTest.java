package com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands;

import com.alexlatkin.twitchclipstgbot.controller.*;
import com.alexlatkin.twitchclipstgbot.model.dto.TwitchClip;
import com.alexlatkin.twitchclipstgbot.model.dto.TwitchClipsDto;
import com.alexlatkin.twitchclipstgbot.model.dto.TwitchGameDto;
import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.alexlatkin.twitchclipstgbot.model.entity.Game;
import com.alexlatkin.twitchclipstgbot.telegramBotCommands.buttonCommands.BlockButtonCommand;
import com.alexlatkin.twitchclipstgbot.telegramBotCommands.buttonCommands.FollowButtonCommand;
import com.alexlatkin.twitchclipstgbot.telegramBotCommands.buttonCommands.commandsWIthAnswer.NextClipButtonCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameClipsCommandTest {
    @InjectMocks
    GameClipsCommand gameClipsCommand;
    @Mock
    ClipsController clipsController;
    @Mock
    UserController userController;
    @Mock
    GameController gameController;
    @Mock
    TwitchController twitchController;
    @Mock
    FollowButtonCommand followButtonCommand;
    @Mock
    BlockButtonCommand blockButtonCommand;
    @Mock
    NextClipButtonCommand nextClipButtonCommand;
    @Mock
    CacheClipsController cacheClipsController;
    @Mock
    CacheBroadcasterController cacheBroadcasterController;

    @Test
    void firstMessage() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);

        var result = gameClipsCommand.firstMessage(updateMock);

        assertEquals(chatId.toString(), result.getChatId());
        assertEquals("Введите название игры или категории", result.getText());
    }

    @Test
    void secondMessage_GameExistsInDB() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        String gameName = "Dota 2";

        Game game = new Game(29595, "Dota 2");

        TwitchClipsDto clipList = new TwitchClipsDto(List.of(new TwitchClip("url", 1, "firstBc", 100)
                                                            , new TwitchClip("url2", 2, "secondBc", 200)));

        Broadcaster caster = new Broadcaster(clipList.getData().get(0).getBroadcasterId(), clipList.getData().get(0).getBroadcasterName());

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getText()).thenReturn(gameName);
        when(gameController.existsGameByGameName(gameName)).thenReturn(true);
        when(gameController.getGameByGameName(gameName)).thenReturn(game);
        when(clipsController.getClipsByGame(game)).thenReturn(clipList);
        when(userController.getUserFollowListByUserChatId(chatId)).thenReturn(Collections.emptyList());
        when(cacheClipsController.getClipByUserChatId(chatId.toString())).thenReturn(clipList.getData().get(0));

        var result = gameClipsCommand.secondMessage(updateMock);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(clipList.getData().get(0).getUrl(), result.getText());
        verify(gameController, times(1)).existsGameByGameName(gameName);
        verify(gameController, times(1)).getGameByGameName(gameName);
        verify(clipsController, times(1)).getClipsByGame(game);
        verify(userController, times(1)).getUserFollowListByUserChatId(chatId);
        verify(cacheClipsController, times(1)).addClipListByUserChatId(chatId.toString(), clipList.getData());
        verify(cacheClipsController, times(1)).getClipByUserChatId(chatId.toString());
        verify(cacheBroadcasterController, times(1)).cacheCaster(chatId + "GAME_CLIPS_COMMAND_CASTER", caster);
        verify(userController, times(1)).getUserFollowListByUserChatId(chatId);
    }

    @Test
    void secondMessage_GameExistsInDbWithMisprintInQuery() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        String gameName = "Dota 2";

        Game game = new Game(29595, "Dota 2");

        TwitchClipsDto clipList = new TwitchClipsDto(List.of(new TwitchClip("url", 1, "firstBc", 100)
                , new TwitchClip("url2", 2, "secondBc", 200)));

        Broadcaster caster = new Broadcaster(clipList.getData().get(0).getBroadcasterId(), clipList.getData().get(0).getBroadcasterName());

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getText()).thenReturn(gameName);
        when(gameController.existsGameByGameName(gameName)).thenReturn(false);
        when(gameController.existsGameByMisprintGameName(gameName)).thenReturn(true);
        when(gameController.findGameByMisprintGameName(gameName)).thenReturn(List.of(game));
        when(clipsController.getClipsByGame(game)).thenReturn(clipList);
        when(userController.getUserFollowListByUserChatId(chatId)).thenReturn(Collections.emptyList());
        when(cacheClipsController.getClipByUserChatId(chatId.toString())).thenReturn(clipList.getData().get(0));

        var result = gameClipsCommand.secondMessage(updateMock);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(clipList.getData().get(0).getUrl(), result.getText());
        verify(gameController, times(1)).existsGameByGameName(gameName);
        verify(gameController, times(1)).existsGameByMisprintGameName(gameName);
        verify(gameController, times(1)).findGameByMisprintGameName(gameName);
        verify(clipsController, times(1)).getClipsByGame(game);
        verify(userController, times(1)).getUserFollowListByUserChatId(chatId);
        verify(cacheClipsController, times(1)).addClipListByUserChatId(chatId.toString(), clipList.getData());
        verify(cacheClipsController, times(1)).getClipByUserChatId(chatId.toString());
        verify(cacheBroadcasterController, times(1)).cacheCaster(chatId + "GAME_CLIPS_COMMAND_CASTER", caster);
        verify(userController, times(1)).getUserFollowListByUserChatId(chatId);
    }

    @Test
    void secondMessage_GameNotExistsInDb_CallTwitchController() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        String gameName = "Dota 2";

        Game game = new Game(29595, "Dota 2");

        TwitchClipsDto clipList = new TwitchClipsDto(List.of(new TwitchClip("url", 1, "firstBc", 100)
                , new TwitchClip("url2", 2, "secondBc", 200)));

        List<TwitchGameDto> listTwitchGameDto = List.of(new TwitchGameDto(29595, "Dota 2"));

        Broadcaster caster = new Broadcaster(clipList.getData().get(0).getBroadcasterId(), clipList.getData().get(0).getBroadcasterName());

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getText()).thenReturn(gameName);
        when(gameController.existsGameByGameName(gameName)).thenReturn(false);
        when(gameController.existsGameByMisprintGameName(gameName)).thenReturn(false);
        when(twitchController.getGameByGameName(gameName)).thenReturn(listTwitchGameDto);
        when(clipsController.getClipsByGame(game)).thenReturn(clipList);
        when(userController.getUserFollowListByUserChatId(chatId)).thenReturn(Collections.emptyList());
        when(cacheClipsController.getClipByUserChatId(chatId.toString())).thenReturn(clipList.getData().get(0));

        var result = gameClipsCommand.secondMessage(updateMock);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(clipList.getData().get(0).getUrl(), result.getText());
        verify(gameController, times(1)).existsGameByGameName(gameName);
        verify(gameController, times(1)).existsGameByMisprintGameName(gameName);
        verify(twitchController, times(1)).getGameByGameName(gameName);
        verify(gameController, times(1)).addGame(game);
        verify(clipsController, times(1)).getClipsByGame(game);
        verify(userController, times(1)).getUserFollowListByUserChatId(chatId);
        verify(cacheClipsController, times(1)).addClipListByUserChatId(chatId.toString(), clipList.getData());
        verify(cacheClipsController, times(1)).getClipByUserChatId(chatId.toString());
        verify(cacheBroadcasterController, times(1)).cacheCaster(chatId + "GAME_CLIPS_COMMAND_CASTER", caster);
        verify(userController, times(1)).getUserFollowListByUserChatId(chatId);
    }

    @Test
    void secondMessage_GameNotFound() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        String gameName = "Dota 2";

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getText()).thenReturn(gameName);
        when(gameController.existsGameByGameName(gameName)).thenReturn(false);
        when(gameController.existsGameByMisprintGameName(gameName)).thenReturn(false);
        when(twitchController.getGameByGameName(gameName)).thenReturn(Collections.emptyList());

        var result = gameClipsCommand.secondMessage(updateMock);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals("Некорректное название игры, отправте команду /game_clips снова и напишите название игры корректно", result.getText());
        verify(gameController, times(1)).existsGameByGameName(gameName);
        verify(gameController, times(1)).existsGameByMisprintGameName(gameName);
        verify(twitchController, times(1)).getGameByGameName(gameName);
    }

    @Test
    void clickButton_FollowCase() {
        Update updateMock = mock(Update.class);
        CallbackQuery callbackQueryMock = mock(CallbackQuery.class);
        Message messageMock = mock(Message.class);
        var buttonKey = "GAME_CLIPS_FOLLOW";
        Long chatId = 1L;
        var keyForPreviousCaster = chatId + "GAME_CLIPS_COMMAND_CASTER";

        List<TwitchClip> clipsList = List.of(new TwitchClip("url", 1, "bcName", 100));
        Broadcaster broadcaster = new Broadcaster(1, "bcName");
        String expectedText = " \n Вы подписались на " + broadcaster.getBroadcasterName();
        EditMessageText expectedObject = new EditMessageText();
        expectedObject.setChatId(chatId);
        expectedObject.setText(expectedText);


        when(updateMock.getCallbackQuery()).thenReturn(callbackQueryMock);
        when(updateMock.getCallbackQuery().getMessage()).thenReturn(messageMock);
        when(updateMock.getCallbackQuery().getData()).thenReturn(buttonKey);
        when(updateMock.getCallbackQuery().getMessage().getChatId()).thenReturn(chatId);
        when(cacheClipsController.getClipListByUserChatId(chatId.toString())).thenReturn(clipsList);
        when(cacheBroadcasterController.getCacheCaster(keyForPreviousCaster)).thenReturn(broadcaster);
        when(followButtonCommand.actionButtonInCurrentMessage(updateMock, broadcaster)).thenReturn(expectedObject);

        EditMessageText result = (EditMessageText) gameClipsCommand.clickButton(updateMock);

        assertEquals(EditMessageText.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(expectedText, result.getText());
        verify(cacheClipsController, times(1)).getClipListByUserChatId(chatId.toString());
        verify(cacheBroadcasterController, times(1)).getCacheCaster(keyForPreviousCaster);
        verify(followButtonCommand, times(1)).actionButtonInCurrentMessage(updateMock, broadcaster);
    }
    @Test
    void clickButton_BlockCase() {
        Update updateMock = mock(Update.class);
        CallbackQuery callbackQueryMock = mock(CallbackQuery.class);
        Message messageMock = mock(Message.class);
        var buttonKey = "GAME_CLIPS_BLOCK";
        Long chatId = 1L;
        var keyForPreviousCaster = chatId + "GAME_CLIPS_COMMAND_CASTER";

        List<TwitchClip> clipsList = List.of(new TwitchClip("url", 1, "bcName", 100));
        Broadcaster broadcaster = new Broadcaster(1, "bcName");
        String expectedText = String.format("\n %S добавлен в чёрный список", broadcaster.getBroadcasterName());
        EditMessageText expectedObject = new EditMessageText();
        expectedObject.setChatId(chatId);
        expectedObject.setText(expectedText);


        when(updateMock.getCallbackQuery()).thenReturn(callbackQueryMock);
        when(updateMock.getCallbackQuery().getMessage()).thenReturn(messageMock);
        when(updateMock.getCallbackQuery().getData()).thenReturn(buttonKey);
        when(updateMock.getCallbackQuery().getMessage().getChatId()).thenReturn(chatId);
        when(cacheClipsController.getClipListByUserChatId(chatId.toString())).thenReturn(clipsList);
        when(cacheBroadcasterController.getCacheCaster(keyForPreviousCaster)).thenReturn(broadcaster);
        when(blockButtonCommand.actionButtonInCurrentMessage(updateMock, broadcaster)).thenReturn(expectedObject);

        EditMessageText result = (EditMessageText) gameClipsCommand.clickButton(updateMock);

        assertEquals(EditMessageText.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(expectedText, result.getText());
        verify(cacheClipsController, times(1)).getClipListByUserChatId(chatId.toString());
        verify(cacheBroadcasterController, times(1)).getCacheCaster(keyForPreviousCaster);
        verify(blockButtonCommand, times(1)).actionButtonInCurrentMessage(updateMock, broadcaster);
    }
    @Test
    void clickButton_NextCase() {
        Update updateMock = mock(Update.class);
        CallbackQuery callbackQueryMock = mock(CallbackQuery.class);
        Message messageMock = mock(Message.class);
        var buttonKey = "GAME_CLIPS_NEXT";
        Long chatId = 1L;
        var keyForPreviousCaster = chatId + "GAME_CLIPS_COMMAND_CASTER";

        List<TwitchClip> clipsList = List.of(new TwitchClip("url", 1, "bcName", 100));

        when(updateMock.getCallbackQuery()).thenReturn(callbackQueryMock);
        when(updateMock.getCallbackQuery().getMessage()).thenReturn(messageMock);
        when(updateMock.getCallbackQuery().getData()).thenReturn(buttonKey);
        when(updateMock.getCallbackQuery().getMessage().getChatId()).thenReturn(chatId);
        when(cacheClipsController.getClipListByUserChatId(chatId.toString())).thenReturn(clipsList);
        when(cacheClipsController.getClipByUserChatId(chatId.toString())).thenReturn(clipsList.get(0));
        when(nextClipButtonCommand.actionWithMessage(updateMock, clipsList.get(0))).thenReturn(new SendMessage(chatId.toString(), clipsList.get(0).getUrl()));

        SendMessage result = (SendMessage) gameClipsCommand.clickButton(updateMock);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(clipsList.get(0).getUrl(), result.getText());
        verify(cacheClipsController, times(1)).getClipListByUserChatId(chatId.toString());
        verify(cacheClipsController, times(1)).getClipByUserChatId(chatId.toString());
        verify(cacheBroadcasterController, times(1)).cacheCaster(keyForPreviousCaster
                , new Broadcaster(clipsList.get(0).getBroadcasterId(), clipsList.get(0).getBroadcasterName()));
        verify(nextClipButtonCommand, times(1)).actionWithMessage(updateMock, clipsList.get(0));
    }
    @Test
    void clickButton_NextCaseWithEmptyList() {
        Update updateMock = mock(Update.class);
        CallbackQuery callbackQueryMock = mock(CallbackQuery.class);
        Message messageMock = mock(Message.class);
        var buttonKey = "GAME_CLIPS_NEXT";
        Long chatId = 1L;

        when(updateMock.getCallbackQuery()).thenReturn(callbackQueryMock);
        when(updateMock.getCallbackQuery().getMessage()).thenReturn(messageMock);
        when(updateMock.getCallbackQuery().getData()).thenReturn(buttonKey);
        when(updateMock.getCallbackQuery().getMessage().getChatId()).thenReturn(chatId);
        when(cacheClipsController.getClipListByUserChatId(chatId.toString())).thenReturn(Collections.emptyList());

        SendMessage result = (SendMessage) gameClipsCommand.clickButton(updateMock);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals("Клипы законичились", result.getText());
        verify(cacheClipsController, times(1)).getClipListByUserChatId(chatId.toString());
    }
}
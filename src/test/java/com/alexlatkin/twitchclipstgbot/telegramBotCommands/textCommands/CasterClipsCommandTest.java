package com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands;

import com.alexlatkin.twitchclipstgbot.controller.*;
import com.alexlatkin.twitchclipstgbot.exception.BroadcasterNotFoundException;
import com.alexlatkin.twitchclipstgbot.model.dto.TwitchClip;
import com.alexlatkin.twitchclipstgbot.model.dto.TwitchClipsDto;
import com.alexlatkin.twitchclipstgbot.model.dto.TwitchUser;
import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
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
class CasterClipsCommandTest {
    @InjectMocks
    CasterClipsCommand casterClipsCommand;
    @Mock
    ClipsController clipsController;
    @Mock
    UserController userController;
    @Mock
    BroadcasterController broadcasterController;
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

        var result = casterClipsCommand.firstMessage(updateMock);

        assertEquals(chatId.toString(), result.getChatId());
        assertEquals("Введите ник стримера", result.getText());
    }

    @Test
    void secondMessage_BroadcasterExistsInDb() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        String bcName = "pudge";

        Broadcaster caster = new Broadcaster(1, bcName);

        TwitchClipsDto clipListDto = new TwitchClipsDto(List.of(new TwitchClip("url", 1, "pudge", 100)
                                                        , new TwitchClip("url2", 1, "pudge", 200)));

        List<TwitchClip> clipList = clipListDto.getData();

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getText()).thenReturn(bcName);
        when(broadcasterController.existsBroadcasterByBroadcasterName(bcName)).thenReturn(true);
        when(broadcasterController.getBroadcasterByBroadcasterName(bcName)).thenReturn(caster);
        when(userController.getUserBlackListByUserChatId(chatId)).thenReturn(Collections.emptyList());
        when(clipsController.getClipsByBroadcaster(caster)).thenReturn(clipListDto);
        when(cacheClipsController.getClipByUserChatId(chatId.toString())).thenReturn(clipList.get(0));

        var result = casterClipsCommand.secondMessage(updateMock);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(clipList.get(0).getUrl(), result.getText());
        verify(broadcasterController, times(1)).existsBroadcasterByBroadcasterName(bcName);
        verify(broadcasterController, times(2)).getBroadcasterByBroadcasterName(bcName);
        verify(userController, times(1)).getUserBlackListByUserChatId(chatId);
        verify(clipsController, times(1)).getClipsByBroadcaster(caster);
        verify(cacheClipsController, times(1)).addClipListByUserChatId(chatId.toString(), clipList);
        verify(cacheClipsController, times(1)).getClipByUserChatId(chatId.toString());
        verify(cacheBroadcasterController, times(1)).cacheCaster(chatId + "CASTER_CLIPS_COMMAND_CASTER", caster);
        verify(userController, times(1)).getUserFollowListByUserChatId(chatId);
    }

    @Test
    void secondMessage_BroadcasterNotExistsInDb_CallTwitchController() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        String bcName = "pudge";

        Broadcaster caster = new Broadcaster(1, bcName);

        TwitchClipsDto clipListDto = new TwitchClipsDto(List.of(new TwitchClip("url", 1, "pudge", 100)
                , new TwitchClip("url2", 1, "pudge", 200)));

        List<TwitchClip> clipList = clipListDto.getData();

        List<TwitchUser> twitchCaster = List.of(new TwitchUser(1, "pudge"));

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getText()).thenReturn(bcName);
        when(broadcasterController.existsBroadcasterByBroadcasterName(bcName)).thenReturn(false);
        when(twitchController.getCasterByCasterName(bcName)).thenReturn(twitchCaster);
        when(userController.getUserBlackListByUserChatId(chatId)).thenReturn(Collections.emptyList());
        when(clipsController.getClipsByBroadcaster(caster)).thenReturn(clipListDto);
        when(cacheClipsController.getClipByUserChatId(chatId.toString())).thenReturn(clipList.get(0));

        var result = casterClipsCommand.secondMessage(updateMock);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(clipList.get(0).getUrl(), result.getText());
        verify(broadcasterController, times(1)).existsBroadcasterByBroadcasterName(bcName);
        verify(twitchController, times(1)).getCasterByCasterName(bcName);
        verify(broadcasterController, times(1)).addBroadcaster(caster);
        verify(userController, times(1)).getUserBlackListByUserChatId(chatId);
        verify(clipsController, times(1)).getClipsByBroadcaster(caster);
        verify(cacheClipsController, times(1)).addClipListByUserChatId(chatId.toString(), clipList);
        verify(cacheClipsController, times(1)).getClipByUserChatId(chatId.toString());
        verify(cacheBroadcasterController, times(1)).cacheCaster(chatId + "CASTER_CLIPS_COMMAND_CASTER", caster);
        verify(userController, times(1)).getUserFollowListByUserChatId(chatId);
    }

    @Test
    void secondMessage_BroadcasterNotExistsInDb_CallTwitchController_NoClips() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        String bcName = "pudge";

        Broadcaster caster = new Broadcaster(1, bcName);

        List<TwitchUser> twitchCaster = List.of(new TwitchUser(1, "pudge"));

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getText()).thenReturn(bcName);
        when(broadcasterController.existsBroadcasterByBroadcasterName(bcName)).thenReturn(false);
        when(twitchController.getCasterByCasterName(bcName)).thenReturn(twitchCaster);
        when(userController.getUserBlackListByUserChatId(chatId)).thenReturn(Collections.emptyList());
        when(clipsController.getClipsByBroadcaster(caster)).thenReturn(new TwitchClipsDto(Collections.emptyList()));

        var result = casterClipsCommand.secondMessage(updateMock);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(String.format("Клипов %S сегодня нет", bcName), result.getText());
        verify(broadcasterController, times(1)).existsBroadcasterByBroadcasterName(bcName);
        verify(twitchController, times(1)).getCasterByCasterName(bcName);
        verify(broadcasterController, times(1)).addBroadcaster(caster);
        verify(userController, times(1)).getUserBlackListByUserChatId(chatId);
        verify(clipsController, times(1)).getClipsByBroadcaster(caster);
    }

    @Test
    void secondMessage_BroadcasterNotExistsInDb_CallTwitchController_BcInBlackUserBlackList() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        String bcName = "pudge";

        Broadcaster caster = new Broadcaster(1, bcName);

        List<TwitchUser> twitchCaster = List.of(new TwitchUser(1, "pudge"));
        List<Broadcaster> userBlackList = List.of(caster);

        String expectedText = String.format("%S находится у вас в чёрном списке, если хотите увидеть клипы %S, удалите его из чёрного списка командой /delete и снова повторите команду /caster_clips", bcName, bcName);

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getText()).thenReturn(bcName);
        when(broadcasterController.existsBroadcasterByBroadcasterName(bcName)).thenReturn(false);
        when(twitchController.getCasterByCasterName(bcName)).thenReturn(twitchCaster);
        when(userController.getUserBlackListByUserChatId(chatId)).thenReturn(userBlackList);

        var result = casterClipsCommand.secondMessage(updateMock);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(expectedText, result.getText());
        verify(broadcasterController, times(1)).existsBroadcasterByBroadcasterName(bcName);
        verify(twitchController, times(1)).getCasterByCasterName(bcName);
        verify(broadcasterController, times(1)).addBroadcaster(caster);
        verify(userController, times(1)).getUserBlackListByUserChatId(chatId);
    }

    @Test
    void secondMessage_BroadcasterNotFoundException() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        String bcName = "pudge";

        String expectedText = "Некорректное имя стримера, отправте команду /caster_clips снова и напишите имя стримера корректно";

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getText()).thenReturn(bcName);
        when(broadcasterController.existsBroadcasterByBroadcasterName(bcName)).thenReturn(false);
        when(twitchController.getCasterByCasterName(bcName)).thenThrow(new BroadcasterNotFoundException("bc not found"));

        var result = casterClipsCommand.secondMessage(updateMock);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(expectedText, result.getText());
        verify(broadcasterController, times(1)).existsBroadcasterByBroadcasterName(bcName);
        verify(twitchController, times(1)).getCasterByCasterName(bcName);
    }

    @Test
    void clickButton_FollowCase() {
        Update updateMock = mock(Update.class);
        CallbackQuery callbackQueryMock = mock(CallbackQuery.class);
        Message messageMock = mock(Message.class);
        var buttonKey = "CASTER_CLIPS_FOLLOW";
        Long chatId = 1L;
        var keyForPreviousCaster = chatId + "CASTER_CLIPS_COMMAND_CASTER";

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

        EditMessageText result = (EditMessageText) casterClipsCommand.clickButton(updateMock);

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
        var buttonKey = "CASTER_CLIPS_BLOCK";
        Long chatId = 1L;
        var keyForPreviousCaster = chatId + "CASTER_CLIPS_COMMAND_CASTER";

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

        EditMessageText result = (EditMessageText) casterClipsCommand.clickButton(updateMock);

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
        var buttonKey = "CASTER_CLIPS_NEXT";
        Long chatId = 1L;
        var keyForPreviousCaster = chatId + "CASTER_CLIPS_COMMAND_CASTER";

        Broadcaster broadcaster = new Broadcaster(1, "bcName");
        List<TwitchClip> clipsList = List.of(new TwitchClip("url", 1, "bcName", 100));

        when(updateMock.getCallbackQuery()).thenReturn(callbackQueryMock);
        when(updateMock.getCallbackQuery().getMessage()).thenReturn(messageMock);
        when(updateMock.getCallbackQuery().getData()).thenReturn(buttonKey);
        when(updateMock.getCallbackQuery().getMessage().getChatId()).thenReturn(chatId);
        when(cacheClipsController.getClipListByUserChatId(chatId.toString())).thenReturn(clipsList);
        when(cacheBroadcasterController.getCacheCaster(keyForPreviousCaster)).thenReturn(broadcaster);
        when(cacheClipsController.getClipByUserChatId(chatId.toString())).thenReturn(clipsList.get(0));
        when(nextClipButtonCommand.actionWithMessage(updateMock, clipsList.get(0))).thenReturn(new SendMessage(chatId.toString(), clipsList.get(0).getUrl()));

        SendMessage result = (SendMessage) casterClipsCommand.clickButton(updateMock);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(clipsList.get(0).getUrl(), result.getText());
        verify(cacheClipsController, times(1)).getClipListByUserChatId(chatId.toString());
        verify(cacheBroadcasterController, times(1)).getCacheCaster(keyForPreviousCaster);
        verify(cacheClipsController, times(1)).getClipByUserChatId(chatId.toString());
        verify(nextClipButtonCommand, times(1)).actionWithMessage(updateMock, clipsList.get(0));
    }
    @Test
    void clickButton_NextCaseWithEmptyList() {
        Update updateMock = mock(Update.class);
        CallbackQuery callbackQueryMock = mock(CallbackQuery.class);
        Message messageMock = mock(Message.class);
        var buttonKey = "CASTER_CLIPS_NEXT";
        Long chatId = 1L;
        var keyForFollowOrBlockCaster = chatId + "CASTER_CLIPS_COMMAND_CASTER";

        Broadcaster broadcaster = new Broadcaster(1, "bcName");

        when(updateMock.getCallbackQuery()).thenReturn(callbackQueryMock);
        when(updateMock.getCallbackQuery().getMessage()).thenReturn(messageMock);
        when(updateMock.getCallbackQuery().getData()).thenReturn(buttonKey);
        when(updateMock.getCallbackQuery().getMessage().getChatId()).thenReturn(chatId);
        when(cacheBroadcasterController.getCacheCaster(keyForFollowOrBlockCaster)).thenReturn(broadcaster);
        when(cacheClipsController.getClipListByUserChatId(chatId.toString())).thenReturn(Collections.emptyList());

        SendMessage result = (SendMessage) casterClipsCommand.clickButton(updateMock);

        assertEquals(SendMessage.class, result.getClass());
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(String.format("Клипы %S закончились", broadcaster.getBroadcasterName()), result.getText());
        verify(cacheBroadcasterController,times(1)).getCacheCaster(keyForFollowOrBlockCaster);
        verify(cacheClipsController, times(1)).getClipListByUserChatId(chatId.toString());
    }
}
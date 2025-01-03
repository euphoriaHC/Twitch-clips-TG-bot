package com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands;

import com.alexlatkin.twitchclipstgbot.controller.BroadcasterController;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCommandTest {
    @InjectMocks
    DeleteCommand deleteCommand;
    @Mock
    UserController userController;
    @Mock
    BroadcasterController broadcasterController;
    @Test
    void firstMessage_BothListsIsEmpty() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(userController.getUserFollowListByUserChatId(chatId)).thenReturn(Collections.emptyList());
        when(userController.getUserBlackListByUserChatId(chatId)).thenReturn(Collections.emptyList());

        var result = deleteCommand.firstMessage(updateMock);

        assertEquals(chatId.toString(), result.getChatId());
        assertEquals("Ваш список подписок и чёрный лист пусты", result.getText());
    }

    @Test
    void firstMessage_FollowListIsEmpty_BlackListIsNotEmpty() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;

        Broadcaster broadcaster = new Broadcaster(1, "pudge");
        List<Broadcaster> userBlackList = List.of(broadcaster);
        var userBlackListAsString = userBlackList.stream()
                                    .map(Broadcaster::getBroadcasterName)
                                    .map(e -> e.concat("\n"))
                                    .toList()
                                    .toString()
                                    .replace("["," ")
                                    .replace("]","")
                                    .replace(",","");

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(userController.getUserFollowListByUserChatId(chatId)).thenReturn(Collections.emptyList());
        when(userController.getUserBlackListByUserChatId(chatId)).thenReturn(userBlackList);

        String expectedText = " \n Находится у вас в чёрном списке: \n" + userBlackListAsString + " \n Если хотите удалить стримера введите его ник";
        var result = deleteCommand.firstMessage(updateMock);

        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(expectedText, result.getText());
    }

    @Test
    void firstMessage_BlackListIsEmpty_FollowListIsNotEmpty() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;

        Broadcaster broadcaster = new Broadcaster(1, "pudge");
        List<Broadcaster> userFollowList = List.of(broadcaster);
        var userFollowListAsString = userFollowList.stream()
                                    .map(Broadcaster::getBroadcasterName)
                                    .map(e -> e.concat("\n"))
                                    .toList()
                                    .toString()
                                    .replace("["," ")
                                    .replace("]","")
                                    .replace(",","");

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(userController.getUserFollowListByUserChatId(chatId)).thenReturn(userFollowList);
        when(userController.getUserBlackListByUserChatId(chatId)).thenReturn(Collections.emptyList());

        String expectedText = "Вы подписаны на: \n" + userFollowListAsString + " \n Если хотите удалить стримера введите его ник";
        var result = deleteCommand.firstMessage(updateMock);

        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(expectedText, result.getText());
    }

    @Test
    void firstMessage_BothListsIsNotEmpty() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;

        Broadcaster broadcaster = new Broadcaster(1, "firstBc");
        Broadcaster secondBroadcaster = new Broadcaster(2, "secondBc");
        List<Broadcaster> userFollowList = List.of(broadcaster);
        List<Broadcaster> userBlackList = List.of(secondBroadcaster);

        var userFollowListAsString = userFollowList.stream()
                .map(Broadcaster::getBroadcasterName)
                .map(e -> e.concat("\n"))
                .toList()
                .toString()
                .replace("["," ")
                .replace("]","")
                .replace(",","");

        var userBlackListAsString = userBlackList.stream()
                                .map(Broadcaster::getBroadcasterName)
                                .map(e -> e.concat("\n"))
                                .toList()
                                .toString()
                                .replace("["," ")
                                .replace("]","")
                                .replace(",","");

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(userController.getUserFollowListByUserChatId(chatId)).thenReturn(userFollowList);
        when(userController.getUserBlackListByUserChatId(chatId)).thenReturn(userBlackList);

        String expectedText = "Вы подписаны на: \n" + userFollowListAsString + " \nНаходятся у вас в чёрном списке: \n" + userBlackListAsString + " \n Если хотите удалить стримера введите его ник";
        var result = deleteCommand.firstMessage(updateMock);

        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(expectedText, result.getText());
    }

    @Test
    void secondMessage_NonExistentBroadcaster() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        String bcName = "pudge";

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getText()).thenReturn(bcName);
        when(broadcasterController.existsBroadcasterByBroadcasterName(bcName)).thenReturn(false);

        var result = deleteCommand.secondMessage(updateMock);

        assertEquals("Вы ввели неверное имя стримера", result.getText());
        verify(broadcasterController, times(1)).existsBroadcasterByBroadcasterName(bcName);
    }

    @Test
    void secondMessage_BroadcasterInFollowList() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        String bcName = "pudge";
        var broadcaster = new Broadcaster(1, bcName);

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getText()).thenReturn(bcName);

        when(broadcasterController.existsBroadcasterByBroadcasterName(bcName)).thenReturn(true);
        when(broadcasterController.getBroadcasterByBroadcasterName(bcName)).thenReturn(broadcaster);
        when(userController.getUserFollowListByUserChatId(chatId)).thenReturn(List.of(broadcaster));
        when(userController.getUserBlackListByUserChatId(chatId)).thenReturn(Collections.emptyList());

        var result = deleteCommand.secondMessage(updateMock);

        assertEquals(bcName + " удален из ваших подписок", result.getText());
        verify(userController, times(1)).deleteBroadcasterFromUserFollowList(chatId, broadcaster);
    }

    @Test
    void secondMessage_BroadcasterInBlackList() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        String bcName = "pudge";
        var broadcaster = new Broadcaster(1, bcName);

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getText()).thenReturn(bcName);

        when(broadcasterController.existsBroadcasterByBroadcasterName(bcName)).thenReturn(true);
        when(broadcasterController.getBroadcasterByBroadcasterName(bcName)).thenReturn(broadcaster);
        when(userController.getUserFollowListByUserChatId(chatId)).thenReturn(Collections.emptyList());
        when(userController.getUserBlackListByUserChatId(chatId)).thenReturn(List.of(broadcaster));

        var result = deleteCommand.secondMessage(updateMock);

        assertEquals(bcName + " удален из вашего черного списка", result.getText());
        verify(userController, times(1)).deleteBroadcasterFromUserBlackList(chatId, broadcaster);
    }

    @Test
    void secondMessage_BroadcasterNotInAnyList() {
        Update updateMock = mock(Update.class);
        Message messageMock = mock(Message.class);
        Long chatId = 1L;
        String bcName = "pudge";
        var broadcaster = new Broadcaster(1, bcName);

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getText()).thenReturn(bcName);

        when(broadcasterController.existsBroadcasterByBroadcasterName(bcName)).thenReturn(true);
        when(broadcasterController.getBroadcasterByBroadcasterName(bcName)).thenReturn(broadcaster);
        when(userController.getUserFollowListByUserChatId(chatId)).thenReturn(Collections.emptyList());
        when(userController.getUserBlackListByUserChatId(chatId)).thenReturn(Collections.emptyList());

        var result = deleteCommand.secondMessage(updateMock);

        assertEquals("Вы ввели неверное имя стримера", result.getText());
    }
}

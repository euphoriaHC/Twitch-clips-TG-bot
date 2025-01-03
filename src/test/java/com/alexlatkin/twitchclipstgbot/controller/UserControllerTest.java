package com.alexlatkin.twitchclipstgbot.controller;

import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.alexlatkin.twitchclipstgbot.model.entity.User;
import com.alexlatkin.twitchclipstgbot.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @InjectMocks
    UserController userController;
    @Mock
    UserService userService;

    @Test
    void existsUserByChatId_ShouldCallUserService() {
        Long chatId = 1L;

        when(userService.existsUserByChatId(chatId)).thenReturn(true);

        var result = userController.existsUserByChatId(chatId);

        assertTrue(result);
        verify(userService, times(1)).existsUserByChatId(chatId);
    }

    @Test
    void addUser_ShouldCallUserService() {
        User userMock = mock(User.class);

        userController.addUser(userMock);

        verify(userService, times(1)).addUser(userMock);
    }

    @Test
    void addBroadcasterInUserFollowList_ShouldCallUserService() {
        Broadcaster broadcasterMock = mock(Broadcaster.class);
        Long userId = 1L;

        userController.addBroadcasterInUserFollowList(userId, broadcasterMock);

        verify(userService, times(1)).addBroadcasterInUserFollowList(userId, broadcasterMock);
    }

    @Test
    void addBroadcasterInUserBlackList_ShouldCallUserService() {
        Broadcaster broadcasterMock = mock(Broadcaster.class);
        Long userId = 1L;

        userController.addBroadcasterInUserBlackList(userId, broadcasterMock);

        verify(userService, times(1)).addBroadcasterInUserBlackList(userId, broadcasterMock);
    }

    @Test
    void getUserFollowListByUserChatId_ShouldCallUserService() {
        Long chatId = 1L;
        List<Broadcaster> expectedFollowList = List.of(new Broadcaster(1, "firstBc")
                                                    , new Broadcaster(2, "secondBc"));

        when(userService.getUserFollowListByUserChatId(chatId)).thenReturn(expectedFollowList);

        List<Broadcaster> result = userController.getUserFollowListByUserChatId(chatId);

        assertEquals(expectedFollowList, result);
        verify(userService, times(1)).getUserFollowListByUserChatId(chatId);
    }

    @Test
    void getUserBlackListByUserChatId_ShouldCallUserService() {
        Long chatId = 1L;
        List<Broadcaster> expectedBlackList = List.of(new Broadcaster(1, "firstBc")
                , new Broadcaster(2, "secondBc"));

        when(userService.getUserBlackListByUserChatId(chatId)).thenReturn(expectedBlackList);

        List<Broadcaster> result = userController.getUserBlackListByUserChatId(chatId);

        assertEquals(expectedBlackList, result);
        verify(userService, times(1)).getUserBlackListByUserChatId(chatId);
    }

    @Test
    void deleteBroadcasterFromUserFollowList_ShouldCallUserService() {
        Broadcaster broadcasterMock = mock(Broadcaster.class);
        Long chatId = 1L;

        userController.deleteBroadcasterFromUserFollowList(chatId, broadcasterMock);

        verify(userService, times(1)).deleteBroadcasterFromUserFollowList(chatId, broadcasterMock);
    }

    @Test
    void deleteBroadcasterFromUserBlackList_ShouldCallUserService() {
        Broadcaster broadcasterMock = mock(Broadcaster.class);
        Long chatId = 1L;

        userController.deleteBroadcasterFromUserBlackList(chatId, broadcasterMock);

        verify(userService, times(1)).deleteBroadcasterFromUserBlackList(chatId, broadcasterMock);
    }

    @Test
    void clearUserFollowList_ShouldCallUserService() {
        Long chatId = 1L;

        userController.clearUserFollowList(chatId);

        verify(userService, times(1)).clearUserFollowList(chatId);
    }

    @Test
    void clearUserBlackList_ShouldCallUserService() {
        Long chatId = 1L;

        userController.clearUserBlackList(chatId);

        verify(userService, times(1)).clearUserBlackList(chatId);
    }
}
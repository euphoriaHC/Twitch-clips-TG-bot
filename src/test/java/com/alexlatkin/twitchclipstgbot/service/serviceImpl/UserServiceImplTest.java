package com.alexlatkin.twitchclipstgbot.service.serviceImpl;

import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.alexlatkin.twitchclipstgbot.model.entity.User;
import com.alexlatkin.twitchclipstgbot.model.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UserRepository userRepository;

    @Test
    void getUserByUserChatI_shouldCallRepository() {
        User userMock = mock(User.class);
        Long chatId = 1L;

        when(userRepository.findById(chatId)).thenReturn(Optional.ofNullable(userMock));

        var result = userService.getUserByUserChatId(chatId);

        assertEquals(userMock, result);
        verify(userRepository, times(1)).findById(chatId);
    }

    @Test
    void existsUserByChatId_shouldCallRepository() {
        Long chatId = 1L;

        when(userRepository.existsUserByChatId(chatId)).thenReturn(true);

        var result = userService.existsUserByChatId(chatId);

        assertTrue(result);
        verify(userRepository, times(1)).existsUserByChatId(chatId);
    }

    @Test
    void addUser_shouldCallRepository() {
        User userMock = mock(User.class);

        userService.addUser(userMock);

        verify(userRepository, times(1)).save(userMock);
    }

    @Test
    void addBroadcasterInUserFollowList_shouldCallRepository() {
        User userMock = mock(User.class);
        Broadcaster broadcasterMock = mock(Broadcaster.class);
        Long chatId = 1L;

        when(userRepository.findById(chatId)).thenReturn(Optional.ofNullable(userMock));

        userService.addBroadcasterInUserFollowList(chatId, broadcasterMock);

        verify(userRepository, times(1)).findById(chatId);
        verify(userRepository, times(1)).save(userMock);
    }

    @Test
    void addBroadcasterInUserBlackList_shouldCallRepository() {
        User userMock = mock(User.class);
        Broadcaster broadcasterMock = mock(Broadcaster.class);
        Long chatId = 1L;

        when(userRepository.findById(chatId)).thenReturn(Optional.ofNullable(userMock));

        userService.addBroadcasterInUserBlackList(chatId, broadcasterMock);

        verify(userRepository, times(1)).findById(chatId);
        verify(userRepository, times(1)).save(userMock);
    }

    @Test
    void getUserFollowListByUserChatId_shouldCallRepository() {
        User userMock = mock(User.class);
        List<Broadcaster> expectedList = List.of(new Broadcaster(1, "firstBc")
                                                , new Broadcaster(2, "secondBc"));
        Long chatId = 1L;

        when(userMock.getFollowList()).thenReturn(expectedList);
        when(userRepository.findById(chatId)).thenReturn(Optional.ofNullable(userMock));

        var result = userService.getUserFollowListByUserChatId(chatId);

        assertEquals(expectedList, result);
        verify(userRepository, times(1)).findById(chatId);
    }

    @Test
    void getUserBlackListByUserChatId_shouldCallRepository() {
        User userMock = mock(User.class);
        List<Broadcaster> expectedList = List.of(new Broadcaster(1, "firstBc")
                                                , new Broadcaster(2, "secondBc"));
        Long chatId = 1L;

        when(userMock.getBlackList()).thenReturn(expectedList);
        when(userRepository.findById(chatId)).thenReturn(Optional.ofNullable(userMock));

        var result = userService.getUserBlackListByUserChatId(chatId);

        assertEquals(expectedList, result);
        verify(userRepository, times(1)).findById(chatId);
    }

    @Test
    void deleteBroadcasterFromUserFollowList_shouldCallRepository() {
        User userMock = mock(User.class);
        Broadcaster broadcasterMock = mock(Broadcaster.class);
        Long chatId = 1L;

        when(userRepository.findById(chatId)).thenReturn(Optional.ofNullable(userMock));

        userService.deleteBroadcasterFromUserFollowList(chatId, broadcasterMock);

        verify(userRepository, times(1)).findById(chatId);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void deleteBroadcasterFromUserBlackList_shouldCallRepository() {
        User userMock = mock(User.class);
        Broadcaster broadcasterMock = mock(Broadcaster.class);
        Long chatId = 1L;

        when(userRepository.findById(chatId)).thenReturn(Optional.ofNullable(userMock));

        userService.deleteBroadcasterFromUserBlackList(chatId, broadcasterMock);

        verify(userRepository, times(1)).findById(chatId);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void clearUserFollowList_shouldCallRepository() {
        User userMock = mock(User.class);
        Long chatId = 1L;

        when(userRepository.findById(chatId)).thenReturn(Optional.ofNullable(userMock));

        userService.clearUserFollowList(chatId);

        verify(userRepository, times(1)).findById(chatId);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void clearUserBlackList_shouldCallRepository() {
        User userMock = mock(User.class);
        Long chatId = 1L;

        when(userRepository.findById(chatId)).thenReturn(Optional.ofNullable(userMock));

        userService.clearUserBlackList(chatId);

        verify(userRepository, times(1)).findById(chatId);
        verify(userRepository, times(1)).save(any());
    }
}
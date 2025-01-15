package com.alexlatkin.twitchclipstgbot.controller;

import com.alexlatkin.twitchclipstgbot.service.dto.TwitchClip;
import com.alexlatkin.twitchclipstgbot.model.repository.CacheClipsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CacheClipsControllerTest {
    @InjectMocks
    CacheClipsController cacheClipsController;
    @Mock
    CacheClipsRepository cacheClips;

    @Test
    void addClipListByUserChatId() {
        String chatId = "1";
        var clipList = List.of(new TwitchClip("url", 1, "firstBc", 100)
                            , new TwitchClip("url", 2, "secondBc", 200));

        cacheClipsController.addClipListByUserChatId(chatId, clipList);

        verify(cacheClips, times(1)).addClipListByUserChatId(chatId, clipList);
    }

    @Test
    void getClipByUserChatId() {
        String chatId = "1";
        TwitchClip clipMock = mock(TwitchClip.class);

        when(cacheClips.getClipByUserChatId(chatId)).thenReturn(clipMock);

        var result = cacheClipsController.getClipByUserChatId(chatId);

        assertEquals(clipMock, result);
        verify(cacheClips, times(1)).getClipByUserChatId(chatId);
    }

    @Test
    void getClipListByUserChatId() {
        String chatId = "1";
        List<TwitchClip> expectedList = List.of(new TwitchClip("url", 1, "firstBc", 100)
                                            , new TwitchClip("url", 2, "secondBc", 200));

        when(cacheClips.getClipListByUserChatId(chatId)).thenReturn(expectedList);

        var result = cacheClipsController.getClipListByUserChatId(chatId);

        assertEquals(expectedList, result);
        verify(cacheClips, times(1)).getClipListByUserChatId(chatId);
    }

    @Test
    void deleteAllClipsByUserChatId() {
        String chatId = "1";

        cacheClipsController.deleteAllClipsByUserChatId(chatId);

        verify(cacheClips, times(1)).deleteAllClipsByUserChatId(chatId);
    }
}
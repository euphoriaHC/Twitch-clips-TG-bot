package com.alexlatkin.twitchclipstgbot.service.serviceImpl;

import com.alexlatkin.twitchclipstgbot.model.dto.TwitchClip;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CacheClipsImplTest {
    @InjectMocks
    CacheClipsImpl cacheClips;
    @Mock
    RedisTemplate<String, Object> redisTemplate;
    @Mock
    ListOperations<String, Object> listOperations;

    @Test
    void addClipByUserChatId() throws JsonProcessingException {
        String chatId = "1";
        TwitchClip clip = new TwitchClip("url", 1, "bcName", 100);
        ObjectMapper mapper = new ObjectMapper();

        String clipAsString = mapper.writeValueAsString(clip);

        when(redisTemplate.opsForList()).thenReturn(listOperations);

        cacheClips.addClipByUserChatId(chatId, clip);

        verify(listOperations, times(1)).leftPush(chatId, clipAsString);
    }

    @Test
    void addClipByUserChatId_JsonProcessingException() {
        String chatId = "1";
        TwitchClip clip = new TwitchClip("url", 1, "bcName", 100);

        CacheClipsImpl cacheClipsSpy = spy(cacheClips);

        doThrow(new RuntimeException()).when(cacheClipsSpy).addClipByUserChatId(chatId, clip);

        assertThrows(RuntimeException.class, () -> cacheClipsSpy.addClipByUserChatId(chatId, clip));
    }

    @Test
    void addClipListByUserChatId() throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        String chatId = "1";
        List<TwitchClip> twitchClipList = List.of(new TwitchClip("url", 1, "bcName", 100)
                                                , new TwitchClip("url", 2, "secondBcName", 200));

        when(redisTemplate.opsForList()).thenReturn(listOperations);

        cacheClips.addClipListByUserChatId(chatId, twitchClipList);

        for (TwitchClip clip : twitchClipList) {
            String clipAsString = mapper.writeValueAsString(clip);
            verify(listOperations).leftPush(chatId, clipAsString);
        }
        verify(redisTemplate, times(1)).delete(chatId);
    }

    @Test
    void getClipByUserChatId() throws JsonProcessingException {
        String chatId = "1";
        TwitchClip clip = new TwitchClip("url", 1, "bcName", 100);
        ObjectMapper mapper = new ObjectMapper();
        String twitchClipAsString = mapper.writeValueAsString(clip);

        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(listOperations.leftPop(chatId)).thenReturn(twitchClipAsString);

        TwitchClip result = cacheClips.getClipByUserChatId(chatId);

        assertEquals(clip, result);
        verify(listOperations, times(1)).leftPop(chatId);
    }

    @Test
    void getClipByUserChatId_JsonProcessingException() {
        String chatId = "1";

        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(listOperations.leftPop(chatId)).thenReturn(new RuntimeException());

        assertThrows(RuntimeException.class, () -> cacheClips.getClipByUserChatId(chatId));
        verify(listOperations, times(1)).leftPop(chatId);
    }

    @Test
    void getClipListByUserChatId() throws JsonProcessingException {
        String chatId = "1";
        ObjectMapper mapper = new ObjectMapper();
        var firstClip = new TwitchClip("url", 1, "bcName", 100);
        var secondClip = new TwitchClip("url", 2, "secondBcName", 200);

        List<TwitchClip> expectedList = List.of(firstClip, secondClip);
        String firstClipAsString = mapper.writeValueAsString(firstClip);
        String secondClipAsString = mapper.writeValueAsString(secondClip);

        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(listOperations.range(chatId, 0, -1)).thenReturn(List.of(firstClipAsString, secondClipAsString));

        List<TwitchClip> result = cacheClips.getClipListByUserChatId(chatId);

        assertEquals(expectedList, result);
        verify(listOperations, times(1)).range(chatId, 0, -1);
    }

    @Test
    void getClipListByUserChatId_JsonProcessingException() {
        String chatId = "1";

        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(listOperations.range(chatId, 0, -1)).thenReturn(List.of(new RuntimeException()));

        assertThrows(RuntimeException.class, () -> cacheClips.getClipListByUserChatId(chatId));
        verify(listOperations, times(1)).range(chatId, 0, -1);
    }

    @Test
    void deleteAllClipsByUserChatId() {
        String chatId = "1";

        cacheClips.deleteAllClipsByUserChatId(chatId);

        verify(redisTemplate, times(1)).delete(chatId);
    }
}
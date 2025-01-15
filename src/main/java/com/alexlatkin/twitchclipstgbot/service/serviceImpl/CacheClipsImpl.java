package com.alexlatkin.twitchclipstgbot.service.serviceImpl;

import com.alexlatkin.twitchclipstgbot.service.dto.TwitchClip;
import com.alexlatkin.twitchclipstgbot.model.repository.CacheClipsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CacheClipsImpl implements CacheClipsRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private ListOperations listOperations;
    @Override
    public void addClipByUserChatId(String userChatId, TwitchClip twitchClip) {
        listOperations = redisTemplate.opsForList();
        var key = userChatId;
        ObjectMapper mapper = new ObjectMapper();
        String twitchClipAsString;

        try {
            twitchClipAsString = mapper.writeValueAsString(twitchClip);
        } catch (JsonProcessingException e) {
            log.error("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }

        listOperations.leftPush(key, twitchClipAsString);
    }

    @Override
    public void addClipListByUserChatId(String userChatId, List<TwitchClip> twitchClipList) {
        redisTemplate.delete(userChatId);

        for (TwitchClip clip : twitchClipList) {
            addClipByUserChatId(userChatId, clip);
        }
    }

    @Override
    public TwitchClip getClipByUserChatId(String userChatId) {
        listOperations = redisTemplate.opsForList();
        var key = userChatId;
        ObjectMapper mapper = new ObjectMapper();
        TwitchClip clip;

        try {
            clip = mapper.readValue(listOperations.leftPop(key).toString(), TwitchClip.class);
        } catch (JsonProcessingException e) {
            log.error("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return clip;
    }

    @Override
    public List<TwitchClip> getClipListByUserChatId(String userChatId) {
        listOperations = redisTemplate.opsForList();
        var key = userChatId;
        ObjectMapper mapper = new ObjectMapper();
        TwitchClip[] twitchClipsArray;

        try {
            twitchClipsArray = mapper.readValue(listOperations.range(key, 0, -1).toString(), TwitchClip[].class);
        } catch (JsonProcessingException e) {
            log.error("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }

        List<TwitchClip> clipList = Arrays.asList(twitchClipsArray);

        return clipList;
    }

    @Override
    public void deleteAllClipsByUserChatId(String userChatId) {
        redisTemplate.delete(userChatId);
    }

}

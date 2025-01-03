package com.alexlatkin.twitchclipstgbot.service.serviceImpl;

import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.alexlatkin.twitchclipstgbot.model.repository.CacheBroadcasterRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CacheBroadcasterImpl implements CacheBroadcasterRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private ValueOperations valueOperations;
    @Override
    public void cacheCaster(String key, Broadcaster broadcaster) {
        valueOperations = redisTemplate.opsForValue();
        ObjectMapper mapper = new ObjectMapper();
        String casterAsString;

        try {
            casterAsString = mapper.writeValueAsString(broadcaster);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        valueOperations.set(key, casterAsString);
    }

    @Override
    public Broadcaster getCacheCaster(String key) {
        valueOperations = redisTemplate.opsForValue();
        ObjectMapper mapper = new ObjectMapper();
        Broadcaster caster;

        try {
            caster = mapper.readValue(valueOperations.get(key).toString(), Broadcaster.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return caster;
    }
}
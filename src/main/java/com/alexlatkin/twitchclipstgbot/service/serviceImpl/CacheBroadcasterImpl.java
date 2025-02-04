package com.alexlatkin.twitchclipstgbot.service.serviceImpl;

import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.alexlatkin.twitchclipstgbot.model.repository.CacheBroadcasterRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CacheBroadcasterImpl implements CacheBroadcasterRepository {
    private final StringRedisTemplate stringRedisTemplate;
    @Override
    public void cacheCaster(String key, Broadcaster broadcaster) {

        ObjectMapper mapper = new ObjectMapper();
        String casterAsString;

        try {
            casterAsString = mapper.writeValueAsString(broadcaster);
        } catch (JsonProcessingException e) {
            log.error("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }

        stringRedisTemplate.opsForValue().set(key, casterAsString, Duration.ofHours(1));
    }

    @Override
    public Optional<Broadcaster> getCacheCaster(String key) {
        ObjectMapper mapper = new ObjectMapper();
        var casterAsString = stringRedisTemplate.opsForValue().get(key);

        if (casterAsString == null) {
            return Optional.empty();
        }

        try {
            return Optional.ofNullable(mapper.readValue(casterAsString, Broadcaster.class));
        } catch (JsonProcessingException e){
            log.error("Error: " + e.getMessage());
            return Optional.empty();
        }

    }
}
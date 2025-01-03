package com.alexlatkin.twitchclipstgbot.service.serviceImpl;

import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CacheBroadcasterImplTest {
    @InjectMocks
    CacheBroadcasterImpl cacheBroadcaster;
    @Mock
    RedisTemplate<String, Object> redisTemplate;
    @Mock
    ValueOperations<String, Object> valueOperations;

    @Test
    void cacheCaster() throws JsonProcessingException {
        Broadcaster broadcaster = new Broadcaster(1, "firstBc");
        ObjectMapper mapper = new ObjectMapper();
        String key = "1P";

        String casterAsString = mapper.writeValueAsString(broadcaster);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        cacheBroadcaster.cacheCaster(key, broadcaster);

        verify(valueOperations, times(1)).set(key, casterAsString);
    }

    @Test
    void cacheCaster_JsonProcessingException() {
        String key = "1P";
        Broadcaster broadcaster = new Broadcaster(1, "firstBc");

        CacheBroadcasterImpl cacheBroadcasterSpy = spy(cacheBroadcaster);

        doThrow(new RuntimeException()).when(cacheBroadcasterSpy).cacheCaster(key, broadcaster);

        assertThrows(RuntimeException.class, () -> cacheBroadcasterSpy.cacheCaster(key, broadcaster));
    }


    @Test
    void getCacheCaster() throws JsonProcessingException {
        Broadcaster broadcaster = new Broadcaster(1, "firstBc");
        ObjectMapper mapper = new ObjectMapper();
        String key = "1P";

        String casterAsString = mapper.writeValueAsString(broadcaster);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenReturn(casterAsString);

        cacheBroadcaster.getCacheCaster(key);

        verify(valueOperations, times(1)).get(key);
    }

    @Test
    void getCacheCaster_JsonProcessingException() {
        String key = "1P";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> cacheBroadcaster.getCacheCaster(key));
    }
}
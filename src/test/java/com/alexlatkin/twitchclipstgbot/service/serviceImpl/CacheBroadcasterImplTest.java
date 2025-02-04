package com.alexlatkin.twitchclipstgbot.service.serviceImpl;

import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CacheBroadcasterImplTest {
    @InjectMocks
    CacheBroadcasterImpl cacheBroadcaster;
    @Mock
    StringRedisTemplate stringRedisTemplate;
    @Mock
    ValueOperations<String, String> valueOperations;

    @Test
    void cacheCaster() throws JsonProcessingException {
        Broadcaster broadcaster = new Broadcaster(1, "firstBc");
        ObjectMapper mapper = new ObjectMapper();
        String key = "1P";

        String casterAsString = mapper.writeValueAsString(broadcaster);

        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);

        cacheBroadcaster.cacheCaster(key, broadcaster);

        verify(valueOperations, times(1)).set(key, casterAsString, Duration.ofHours(1));
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

        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenReturn(casterAsString);

        cacheBroadcaster.getCacheCaster(key);

        verify(valueOperations, times(1)).get(key);
    }

    @Test
    void getCacheCaster_No_Bc() {
        String key = "1P";

        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenReturn(null);

        var result = cacheBroadcaster.getCacheCaster(key);

        assertEquals(Optional.empty(), result);
        verify(valueOperations, times(1)).get(key);
    }

    @Test
    void getCacheCaster_JsonProcessingException() {
        String key = "1P";

        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> cacheBroadcaster.getCacheCaster(key));
    }
}
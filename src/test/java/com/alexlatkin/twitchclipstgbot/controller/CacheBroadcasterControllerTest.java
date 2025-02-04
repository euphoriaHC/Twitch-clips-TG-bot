package com.alexlatkin.twitchclipstgbot.controller;

import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.alexlatkin.twitchclipstgbot.model.repository.CacheBroadcasterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CacheBroadcasterControllerTest {
    @InjectMocks
    CacheBroadcasterController cacheBroadcasterController;
    @Mock
    CacheBroadcasterRepository cacheBroadcaster;
    @Test
    void cacheCaster() {
        Broadcaster broadcasterMock = mock(Broadcaster.class);
        String key = "1P";

        cacheBroadcasterController.cacheCaster(key, broadcasterMock);

        verify(cacheBroadcaster, times(1)).cacheCaster(key, broadcasterMock);
    }

    @Test
    void getCacheCaster() {
        Optional<Broadcaster> broadcasterMock = mock(Optional.class);
        String key = "1P";

        when(cacheBroadcaster.getCacheCaster(key)).thenReturn(broadcasterMock);

        var result = cacheBroadcasterController.getCacheCaster(key);

        assertEquals(broadcasterMock, result);
        verify(cacheBroadcaster, times(1)).getCacheCaster(key);
    }
}
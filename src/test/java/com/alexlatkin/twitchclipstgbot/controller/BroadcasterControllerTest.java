package com.alexlatkin.twitchclipstgbot.controller;

import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.alexlatkin.twitchclipstgbot.service.BroadcasterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BroadcasterControllerTest {
    @InjectMocks
    BroadcasterController broadcasterController;
    @Mock
    BroadcasterService broadcasterService;

    @Test
    void existsBroadcasterByBroadcasterName_ShouldCallBroadcasterService() {
        String broadcasterName = "pudge";

        when(broadcasterService.existsBroadcasterByBroadcasterName(broadcasterName)).thenReturn(true);

        var result = broadcasterController.existsBroadcasterByBroadcasterName(broadcasterName);

        assertTrue(result);
        verify(broadcasterService, times(1)).existsBroadcasterByBroadcasterName(broadcasterName);
    }

    @Test
    void getBroadcasterByBroadcasterName_ShouldCallBroadcasterService() {
        Broadcaster broadcasterMock = mock(Broadcaster.class);
        String broadcasterName = "pudge";

        when(broadcasterService.getBroadcasterByBroadcasterName(broadcasterName)).thenReturn(broadcasterMock);

        var result = broadcasterController.getBroadcasterByBroadcasterName(broadcasterName);

        assertEquals(broadcasterMock, result);
        verify(broadcasterService, times(1)).getBroadcasterByBroadcasterName(broadcasterName);
    }

    @Test
    void addBroadcaster_ShouldCallBroadcasterService() {
        Broadcaster broadcasterMock = mock(Broadcaster.class);

        broadcasterController.addBroadcaster(broadcasterMock);

        verify(broadcasterService, times(1)).addBroadcaster(broadcasterMock);
    }
}
package com.alexlatkin.twitchclipstgbot.service.serviceImpl;

import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.alexlatkin.twitchclipstgbot.model.repository.BroadcasterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BroadcasterServiceImplTest {
    @InjectMocks
    private BroadcasterServiceImpl broadcasterService;
    @Mock
    private BroadcasterRepository broadcasterRepository;
    @Test
    void getBroadcasterByBroadcasterName_shouldCallRepository() {
        Broadcaster broadcasterMock = mock(Broadcaster.class);
        String broadcasterName = "pudge";

        when(broadcasterRepository.findBroadcasterByBroadcasterName(broadcasterName)).thenReturn(broadcasterMock);

        var result = broadcasterService.getBroadcasterByBroadcasterName(broadcasterName);

        assertEquals(broadcasterMock, result);
        verify(broadcasterRepository, times(1)).findBroadcasterByBroadcasterName(broadcasterName);
    }

    @Test
    void existsBroadcasterByBroadcasterName_shouldCallRepository() {
        String bcName = "pudge";

        when(broadcasterRepository.existsBroadcasterByBroadcasterName(bcName)).thenReturn(true);

        var result = broadcasterRepository.existsBroadcasterByBroadcasterName(bcName);

        assertTrue(result);
        verify(broadcasterRepository, times(1)).existsBroadcasterByBroadcasterName(bcName);
    }


    @Test
    void addBroadcaster_shouldCallRepository() {
        Broadcaster broadcasterMock = mock(Broadcaster.class);

        broadcasterService.addBroadcaster(broadcasterMock);

        verify(broadcasterRepository, times(1)).save(broadcasterMock);
    }
}

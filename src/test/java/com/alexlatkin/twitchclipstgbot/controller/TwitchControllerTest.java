package com.alexlatkin.twitchclipstgbot.controller;

import com.alexlatkin.twitchclipstgbot.service.dto.TwitchGameDto;
import com.alexlatkin.twitchclipstgbot.service.dto.TwitchUser;
import com.alexlatkin.twitchclipstgbot.service.TwitchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TwitchControllerTest {

    @InjectMocks
    TwitchController twitchController;
    @Mock
    TwitchService twitchService;

    @Test
    void getCasterByCasterName() {
        var casterName = "qsnake";
        List<TwitchUser> expectedList = List.of(new TwitchUser(71558231, casterName));

        when(twitchService.getBroadcaster(casterName)).thenReturn(expectedList);

        var result = twitchController.getCasterByCasterName(casterName);

        assertEquals(expectedList, result);
        verify(twitchService, times(1)).getBroadcaster(casterName);
    }

    @Test
    void getGameByGameName() {
        var gameName = "Path of exile";
        List<TwitchGameDto> expectedList = List.of(new TwitchGameDto(1, "path of exile"));

        when(twitchService.getGame(gameName)).thenReturn(expectedList);

        var result = twitchController.getGameByGameName(gameName);

        assertEquals(expectedList, result);
        verify(twitchService, times(1)).getGame(gameName);
    }
}
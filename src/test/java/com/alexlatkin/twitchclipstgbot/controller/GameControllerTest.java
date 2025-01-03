package com.alexlatkin.twitchclipstgbot.controller;

import com.alexlatkin.twitchclipstgbot.model.entity.Game;
import com.alexlatkin.twitchclipstgbot.service.GameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {
    @InjectMocks
    GameController gameController;
    @Mock
    GameService gameService;

    @Test
    void addGame_ShouldCallGameService() {
        Game gameMock = mock(Game.class);

        gameController.addGame(gameMock);

        verify(gameService, times(1)).addGame(gameMock);
    }

    @Test
    void getGameByGameName_ShouldCallGameService() {
        Game gameMock = mock(Game.class);
        String gameName = "Dota 2";

        when(gameService.getGameByGameName(gameName)).thenReturn(gameMock);

        var result = gameController.getGameByGameName(gameName);

        assertEquals(gameMock, result);
        verify(gameService, times(1)).getGameByGameName(gameName);
    }

    @Test
    void existsGameByGameName_ShouldCallGameService() {
        String gameName = "Path of Exile";

        when(gameService.existsGameByGameName(gameName)).thenReturn(true);

        var result = gameController.existsGameByGameName(gameName);

        assertTrue(result);
        verify(gameService, times(1)).existsGameByGameName(gameName);
    }

    @Test
    void findGameByMisprintGameName_ShouldCallGameService() {
        String gameName = "Path of exile";
        List<Game> expectedList = List.of(new Game(1, "Path of exile"));

        when(gameService.findGameByMisprintGameName(gameName)).thenReturn(expectedList);

        var result = gameController.findGameByMisprintGameName(gameName);

        assertEquals(expectedList, result);
        verify(gameService, times(1)).findGameByMisprintGameName(gameName);
    }

    @Test
    void existsGameByMisprintGameName_ShouldCallGameService() {
        String gameName = "Path of Exile";

        when(gameService.existsGameByMisprintGameName(gameName)).thenReturn(true);

        var result = gameController.existsGameByMisprintGameName(gameName);

        assertTrue(result);
        verify(gameService, times(1)).existsGameByMisprintGameName(gameName);
    }
}
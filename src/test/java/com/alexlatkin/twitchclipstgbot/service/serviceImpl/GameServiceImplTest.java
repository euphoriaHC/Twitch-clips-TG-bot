package com.alexlatkin.twitchclipstgbot.service.serviceImpl;

import com.alexlatkin.twitchclipstgbot.model.entity.Game;
import com.alexlatkin.twitchclipstgbot.model.repository.GameRepository;
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
class GameServiceImplTest {
    @InjectMocks
    GameServiceImpl gameService;
    @Mock
    GameRepository gameRepository;

    @Test
    void existsGameByGameName_shouldCallRepository() {
        String gameName = "Path of exile";

        when(gameRepository.existsGameByGameName(gameName)).thenReturn(true);

        var result = gameService.existsGameByGameName(gameName);

        assertTrue(result);
        verify(gameRepository, times(1)).existsGameByGameName(gameName);
    }

    @Test
    void getGameByGameName_shouldCallRepository() {
        Game gameMock = mock(Game.class);
        String gameName = "Path of exile";

        when(gameRepository.findGameByGameName(gameName)).thenReturn(gameMock);

        var result = gameService.getGameByGameName(gameName);

        assertEquals(gameMock, result);
        verify(gameRepository, times(1)).findGameByGameName(gameName);
    }

    @Test
    void addGame_shouldCallRepository() {
        Game gameMock = mock(Game.class);

        gameService.addGame(gameMock);

        verify(gameRepository, times(1)).save(gameMock);
    }

    @Test
    void findGameByMisprintGameName_shouldCallRepository() {
        String gameName = "CS";
        List<Game> gameList = List.of(new Game(1, "CS"));

        when(gameRepository.findGameByMisprintGameName(gameName)).thenReturn(gameList);

        var result = gameService.findGameByMisprintGameName(gameName);

        assertEquals(gameList, result);
        verify(gameRepository, times(1)).findGameByMisprintGameName(gameName);
    }

    @Test
    void existsGameByMisprintGameName_shouldCallRepository() {
        String gameName = "Path of exile";

        when(gameRepository.existsGameByMisprintGameName(gameName)).thenReturn(true);

        var result = gameService.existsGameByMisprintGameName(gameName);

        assertTrue(result);
        verify(gameRepository, times(1)).existsGameByMisprintGameName(gameName);
    }
}
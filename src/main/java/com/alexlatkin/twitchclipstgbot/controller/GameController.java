package com.alexlatkin.twitchclipstgbot.controller;

import com.alexlatkin.twitchclipstgbot.model.entity.Game;
import com.alexlatkin.twitchclipstgbot.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;
    public void addGame(Game game) {
        gameService.addGame(game);
    }
    public Game getGameByGameName(String gameName) {
        return gameService.getGameByGameName(gameName);
    }
    public boolean existsGameByGameName(String gameName) {
        return gameService.existsGameByGameName(gameName);
    }
    public List<Game> findGameByMisprintGameName(String misprintGameName) {
        return gameService.findGameByMisprintGameName(misprintGameName);
    }
    public boolean existsGameByMisprintGameName(String misprintGameName) {
        return gameService.existsGameByMisprintGameName(misprintGameName);
    }
}

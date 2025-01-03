package com.alexlatkin.twitchclipstgbot.service;

import com.alexlatkin.twitchclipstgbot.model.entity.Game;

import java.util.List;

public interface GameService {
    Game getGameByGameName(String gameName);
    void addGame(Game game);
    boolean existsGameByGameName(String gameName);
    List<Game> findGameByMisprintGameName(String misprintGameName);
    boolean existsGameByMisprintGameName(String misprintGameName);
}

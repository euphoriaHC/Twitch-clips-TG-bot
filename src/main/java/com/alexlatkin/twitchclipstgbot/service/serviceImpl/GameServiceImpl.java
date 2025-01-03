package com.alexlatkin.twitchclipstgbot.service.serviceImpl;

import com.alexlatkin.twitchclipstgbot.model.entity.Game;
import com.alexlatkin.twitchclipstgbot.model.repository.GameRepository;
import com.alexlatkin.twitchclipstgbot.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    @Transactional
    @Override
    public boolean existsGameByGameName(String gameName) {
        return gameRepository.existsGameByGameName(gameName);
    }
    @Transactional
    @Override
    public Game getGameByGameName(String gameName) {
        return gameRepository.findGameByGameName(gameName);
    }
    @Transactional
    @Override
    public void addGame(Game game) {
        gameRepository.save(game);
    }
    @Transactional
    @Override
    public List<Game> findGameByMisprintGameName(String misprintGameName) {
        return gameRepository.findGameByMisprintGameName(misprintGameName);
    }
    @Transactional
    @Override
    public boolean existsGameByMisprintGameName(String misprintGameName) {
        return gameRepository.existsGameByMisprintGameName(misprintGameName);
    }
}

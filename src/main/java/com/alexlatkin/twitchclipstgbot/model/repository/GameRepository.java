package com.alexlatkin.twitchclipstgbot.model.repository;

import com.alexlatkin.twitchclipstgbot.model.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface GameRepository extends JpaRepository<Game, Integer> {
    Game findGameByGameName(String gameName);
    boolean existsGameByGameName(String gameName);
    @Query(value = """
    SELECT * FROM game g WHERE SIMILARITY(:misprintGameName, g.game_name) > 0.3
    """, nativeQuery = true)
    List<Game> findGameByMisprintGameName(@Param("misprintGameName") String misprintGameName);
    @Query(value = """
    SELECT EXISTS (SELECT * FROM game g WHERE SIMILARITY(:misprintGameName, g.game_name) > 0.3)
    """, nativeQuery = true)
    boolean existsGameByMisprintGameName(@Param("misprintGameName") String misprintGameName);
}

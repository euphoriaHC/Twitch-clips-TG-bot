package com.alexlatkin.twitchclipstgbot.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "game")
public class Game {
    @Id
    @Column(name = "game_id")
    private Integer gameId;
    @Column(name = "game_name")
    private String gameName;

    public Game(Integer gameId, String gameName) {
        this.gameId = gameId;
        this.gameName = gameName;
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameId=" + gameId +
                ", gameName='" + gameName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return gameId.equals(game.gameId) && gameName.equals(game.gameName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, gameName);
    }
}

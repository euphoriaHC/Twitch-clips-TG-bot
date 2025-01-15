package com.alexlatkin.twitchclipstgbot.controller;

import com.alexlatkin.twitchclipstgbot.service.dto.TwitchGameDto;
import com.alexlatkin.twitchclipstgbot.service.dto.TwitchUser;
import com.alexlatkin.twitchclipstgbot.service.TwitchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TwitchController {
    private final TwitchService twitchService;

    @GetMapping("/broadcaster/{casterName}")
    public List<TwitchUser> getCasterByCasterName(@PathVariable String casterName) {
        return twitchService.getBroadcaster(casterName);
    }


    @GetMapping("/game/{gameName}")
    public List<TwitchGameDto> getGameByGameName(@PathVariable String gameName) {
        return twitchService.getGame(gameName);
    }
}

package com.alexlatkin.twitchclipstgbot.controller;

import com.alexlatkin.twitchclipstgbot.model.dto.TwitchGameDto;
import com.alexlatkin.twitchclipstgbot.model.dto.TwitchUser;
import com.alexlatkin.twitchclipstgbot.service.TwitchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TwitchController {
    private final TwitchService twitchService;
    public List<TwitchUser> getCasterByCasterName(String casterName) {
        return twitchService.getBroadcaster(casterName);
    }

    public List<TwitchGameDto> getGameByGameName(String gameName) {
        return twitchService.getGame(gameName);
    }
}

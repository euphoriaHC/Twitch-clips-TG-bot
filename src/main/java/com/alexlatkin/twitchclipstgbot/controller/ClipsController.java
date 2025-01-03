package com.alexlatkin.twitchclipstgbot.controller;

import com.alexlatkin.twitchclipstgbot.model.dto.TwitchClipsDto;
import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.alexlatkin.twitchclipstgbot.model.entity.Game;
import com.alexlatkin.twitchclipstgbot.service.ClipService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@RequiredArgsConstructor
public class ClipsController {
    private final ClipService clipService;

    public TwitchClipsDto getClipsByGame(Game game) {
        return clipService.getClipsByGame(game);
    }

    public TwitchClipsDto getClipsByBroadcaster(Broadcaster broadcaster) {
        return clipService.getClipsByBroadcaster(broadcaster);
    }

    public List<CompletableFuture<TwitchClipsDto>> getClipsByUserFollowList(List<Broadcaster> userFollowList)  {
        return clipService.getClipsByUserFollowList(userFollowList);
    }

}

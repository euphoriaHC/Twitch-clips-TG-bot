package com.alexlatkin.twitchclipstgbot.controller;

import com.alexlatkin.twitchclipstgbot.service.dto.TwitchClipsDto;
import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.alexlatkin.twitchclipstgbot.model.entity.Game;
import com.alexlatkin.twitchclipstgbot.service.ClipService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clips")
public class ClipsController {
    private final ClipService clipService;

    @PostMapping("/getClipsByGame")
    public TwitchClipsDto getClipsByGame(@RequestBody Game game) {
        return clipService.getClipsByGame(game);
    }

    @PostMapping("/getClipsByBroadcaster")
    public TwitchClipsDto getClipsByBroadcaster(@RequestBody Broadcaster broadcaster) {
        return clipService.getClipsByBroadcaster(broadcaster);
    }

    @PostMapping("/getClipsByUserFollowList")
    public List<CompletableFuture<TwitchClipsDto>> getClipsByUserFollowList(@RequestBody List<Broadcaster> userFollowList)  {
        return clipService.getClipsByUserFollowList(userFollowList);
    }

}

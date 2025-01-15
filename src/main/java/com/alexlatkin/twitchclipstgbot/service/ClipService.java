package com.alexlatkin.twitchclipstgbot.service;

import com.alexlatkin.twitchclipstgbot.service.dto.TwitchClipsDto;
import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.alexlatkin.twitchclipstgbot.model.entity.Game;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public interface ClipService {
    TwitchClipsDto getClipsByGame(Game game);
    List<CompletableFuture<TwitchClipsDto>> getClipsByUserFollowList(List<Broadcaster> userFollowList);
    TwitchClipsDto getClipsByBroadcaster(Broadcaster broadcaster);
}

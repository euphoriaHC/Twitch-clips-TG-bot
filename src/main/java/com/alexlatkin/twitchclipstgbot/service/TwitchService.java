package com.alexlatkin.twitchclipstgbot.service;

import com.alexlatkin.twitchclipstgbot.model.dto.TwitchClipsDto;
import com.alexlatkin.twitchclipstgbot.model.dto.TwitchGameDto;
import com.alexlatkin.twitchclipstgbot.model.dto.TwitchUser;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TwitchService {
    List<TwitchGameDto> getGame(String gameName);
    List<TwitchUser> getBroadcaster(String broadcasterName);
    TwitchClipsDto getClipsByGameId(int gameId, String date);
    CompletableFuture<TwitchClipsDto> getClipsByBroadcastersId(int broadcasterId, String date);
    TwitchClipsDto getClipsByBroadcasterId(int broadcasterId, String date);
}

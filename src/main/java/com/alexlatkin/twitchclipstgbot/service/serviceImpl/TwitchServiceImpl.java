package com.alexlatkin.twitchclipstgbot.service.serviceImpl;

import com.alexlatkin.twitchclipstgbot.config.TwitchConfig;
import com.alexlatkin.twitchclipstgbot.exception.BroadcasterNotFoundException;
import com.alexlatkin.twitchclipstgbot.model.dto.*;
import com.alexlatkin.twitchclipstgbot.service.TwitchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class TwitchServiceImpl implements TwitchService {
    private final TwitchConfig twitchConfig;
    private final ObjectMapper mapper = new ObjectMapper();
    @Override
    public List<TwitchGameDto> getGame(String gameName) {
        StringBuilder uriSb = new StringBuilder(twitchConfig.getUrl());

        var encodeGameName = URLEncoder.encode(gameName, StandardCharsets.UTF_8).replace("+", "%20");

        uriSb.append("games?name=").append(encodeGameName);

        var client = HttpClient.newHttpClient();

        RootTwitchGame rootTwitchGame;

        try {

            var response = client.send(twitchConfig.twitchRequest(uriSb.toString()), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            rootTwitchGame = mapper.readValue(response.body(), RootTwitchGame.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return rootTwitchGame.getData();
    }

    @Override
    public List<TwitchUser> getBroadcaster(String broadcasterName) {
        StringBuilder uriSb = new StringBuilder(twitchConfig.getUrl());

        var encodeBroadcasterName = URLEncoder.encode(broadcasterName, StandardCharsets.UTF_8).replace("+", "%20");

        uriSb.append("users?login=").append(encodeBroadcasterName);

        var client = HttpClient.newHttpClient();

        RootTwitchUser rootTwitchUser;

        try {

            var response = client.send(twitchConfig.twitchRequest(uriSb.toString()), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            rootTwitchUser = mapper.readValue(response.body(), RootTwitchUser.class);

        } catch (IOException e){
            throw new BroadcasterNotFoundException("Bc not found");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return rootTwitchUser.getData();
    }

    @Override
    public TwitchClipsDto getClipsByGameId(int gameId, String date) {
        StringBuilder uriSb = new StringBuilder(twitchConfig.getUrl());
           uriSb.append("clips?game_id=")
                .append(gameId)
                .append("&started_at=")
                .append(date)
                .append("T00:00:00%2B03:00")
                .append("&first=100");

        var client = HttpClient.newHttpClient();

        TwitchClipsDto twitchClipsDto;

        try {

            var response = client.send(twitchConfig.twitchRequest(uriSb.toString()), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            twitchClipsDto = mapper.readValue(response.body(), TwitchClipsDto.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return twitchClipsDto;
    }

    @Override
    @Async
    public CompletableFuture<TwitchClipsDto> getClipsByBroadcastersId(int broadcasterId, String date) {
        StringBuffer uriSb = new StringBuffer(twitchConfig.getUrl());
           uriSb.append("clips?broadcaster_id=")
                .append(broadcasterId)
                .append("&started_at=")
                .append(date)
                .append("T00:00:00%2B03:00");

        var client = HttpClient.newHttpClient();

        var response = client.sendAsync(twitchConfig.twitchRequest(uriSb.toString()), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))
                .thenApply(HttpResponse::body);

        var twitchClipsDtoCompletableFuture = response.thenApply(data -> {
            var twitchClipsDto = new TwitchClipsDto();
            try {
                twitchClipsDto = mapper.readValue(data, TwitchClipsDto.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return twitchClipsDto;
        });

        return twitchClipsDtoCompletableFuture;
    }

    @Override
    public TwitchClipsDto getClipsByBroadcasterId(int broadcasterId, String date) {
        StringBuilder uriSb = new StringBuilder(twitchConfig.getUrl());
           uriSb.append("clips?broadcaster_id=")
                .append(broadcasterId)
                .append("&started_at=")
                .append(date)
                .append("T00:00:00%2B03:00");

        var client = HttpClient.newHttpClient();

        TwitchClipsDto twitchClipsDto;

        try {

            var response = client.send(twitchConfig.twitchRequest(uriSb.toString()), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            twitchClipsDto = mapper.readValue(response.body(), TwitchClipsDto.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return twitchClipsDto;
    }
}

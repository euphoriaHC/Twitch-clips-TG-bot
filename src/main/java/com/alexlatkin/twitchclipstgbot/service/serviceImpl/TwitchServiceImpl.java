package com.alexlatkin.twitchclipstgbot.service.serviceImpl;

import com.alexlatkin.twitchclipstgbot.config.TwitchConfig;
import com.alexlatkin.twitchclipstgbot.exception.BroadcasterNotFoundException;
import com.alexlatkin.twitchclipstgbot.service.TwitchService;
import com.alexlatkin.twitchclipstgbot.service.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/*
  Класс имеет методы для запросов к твичу...
  Начальная дата, используемая для фильтрации клипов 00:00 по Мск в день запроса
  Максимум клипов за запрос 100
  Больше тут https://dev.twitch.tv/docs/api/reference/
*/
@Slf4j
@RequiredArgsConstructor
@Service
public class TwitchServiceImpl implements TwitchService {
    private final TwitchConfig twitchConfig;
    private final ObjectMapper mapper = new ObjectMapper();

    /*
      Метод получения игры по названию игры
      Клип игры нельзя получить по названию игры, поэтому с помощью этого метода мы получаем айди игры, которое уже используем для получения клипов...
    */
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
            log.error("Error: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            log.error("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return rootTwitchGame.getData();
    }

    /*
      Метод получения стримера по нику
      Клип стримера нельзя получить по нику, поэтому с помощью этого метода мы получаем айди стримера, которое уже используем для получения клипов...
    */
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
            log.error("Error: " + e.getMessage());
            throw new BroadcasterNotFoundException("Bc not found");
        } catch (InterruptedException e) {
            log.error("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return rootTwitchUser.getData();
    }

    // Запрос на клипы по айди игры, ничего интересного
    @Override
    public TwitchClipsDto getClipsByGameId(int gameId, String date) {
        StringBuilder uriSb = new StringBuilder(twitchConfig.getUrl());
           uriSb.append("clips?game_id=")
                .append(gameId)
                .append("&started_at=")
                .append(date)
                .append("&first=100");

        var client = HttpClient.newHttpClient();

        TwitchClipsDto twitchClipsDto;

        try {

            var response = client.send(twitchConfig.twitchRequest(uriSb.toString()), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            twitchClipsDto = mapper.readValue(response.body(), TwitchClipsDto.class);

        } catch (IOException e) {
            log.error("Error: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            log.error("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return twitchClipsDto;
    }

    /*
      Метод используется, когда нужно получить клипы по нескольким стримерам (Команда /follow_list_clips)
      Асинхронный, так как твич не позволяет получить клипы по нескольким стримерам за один запрос
    */
    @Override
    @Async
    public CompletableFuture<TwitchClipsDto> getClipsByBroadcastersId(int broadcasterId, String date) {
        StringBuffer uriSb = new StringBuffer(twitchConfig.getUrl());
           uriSb.append("clips?broadcaster_id=")
                .append(broadcasterId)
                .append("&started_at=")
                .append(date);

        var client = HttpClient.newHttpClient();

        var response = client.sendAsync(twitchConfig.twitchRequest(uriSb.toString()), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))
                .thenApply(HttpResponse::body)
                .thenApply(data -> {
                    var twitchClipsDto = new TwitchClipsDto();
                    try {
                        twitchClipsDto = mapper.readValue(data, TwitchClipsDto.class);
                    } catch (JsonProcessingException e) {
                        log.error("Error: " + e.getMessage());
                        throw new RuntimeException(e);
                    }
                    return twitchClipsDto;
                });

        return response;
    }

    // Запрос на клипы по айди стримера, ничего интересного
    @Override
    public TwitchClipsDto getClipsByBroadcasterId(int broadcasterId, String date) {
        StringBuilder uriSb = new StringBuilder(twitchConfig.getUrl());
           uriSb.append("clips?broadcaster_id=")
                .append(broadcasterId)
                .append("&started_at=")
                .append(date);

        var client = HttpClient.newHttpClient();

        TwitchClipsDto twitchClipsDto;

        try {

            var response = client.send(twitchConfig.twitchRequest(uriSb.toString()), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            twitchClipsDto = mapper.readValue(response.body(), TwitchClipsDto.class);

        } catch (IOException e) {
            log.error("Error: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            log.error("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return twitchClipsDto;
    }
}

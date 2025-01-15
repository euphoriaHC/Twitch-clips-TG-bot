package com.alexlatkin.twitchclipstgbot.service.serviceImpl;

import com.alexlatkin.twitchclipstgbot.config.TwitchConfig;
import com.alexlatkin.twitchclipstgbot.exception.BroadcasterNotFoundException;
import com.alexlatkin.twitchclipstgbot.service.dto.RootTwitchGame;
import com.alexlatkin.twitchclipstgbot.service.dto.TwitchClipsDto;
import com.alexlatkin.twitchclipstgbot.service.dto.TwitchGameDto;
import com.alexlatkin.twitchclipstgbot.service.dto.TwitchUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TwitchServiceImplTest {
    @InjectMocks
    TwitchServiceImpl twitchService;
    @Mock
    TwitchConfig twitchConfig;

    HttpRequest twitchRequest(String uriAsString){

        URI uri;

        try {
            uri = new URI(uriAsString);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        var request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Authorization","Bearer " + "b")
                .header("Client-Id", "cId")
                .timeout(Duration.ofSeconds(20))
                .build();

        return request;

    }

    @Test
    void getGame() {
        var gameName = "Dota 2";

        RootTwitchGame rootTwitchGame = new RootTwitchGame();
        rootTwitchGame.setData(List.of(new TwitchGameDto(29595, gameName)));

        var encodedGameName = URLEncoder.encode(gameName, StandardCharsets.UTF_8).replace("+", "%20");

        var request = twitchRequest("https://api.twitch.tv/helix/games?name=" + encodedGameName);

        when(twitchConfig.twitchRequest(any(String.class))).thenReturn(request);
        when(twitchConfig.getUrl()).thenReturn("https://api.twitch.tv/helix/");

        var result = twitchService.getGame(gameName);

        assertEquals(rootTwitchGame.getData(), result);
        verify(twitchConfig, times(1)).getUrl();
        verify(twitchConfig, times(1)).twitchRequest(any(String.class));
    }

    @Test
    void getGame_IncorrectGameName() {
        var incorrectGameName = "incorrectGameName";
        ArrayList<Object> expected = new ArrayList<>();

        var request = twitchRequest("https://api.twitch.tv/helix/games?name=" + incorrectGameName);

        when(twitchConfig.twitchRequest(any(String.class))).thenReturn(request);
        when(twitchConfig.getUrl()).thenReturn("https://api.twitch.tv/helix/");

        var result = twitchService.getGame(incorrectGameName);

        assertEquals(expected, result);
        verify(twitchConfig, times(1)).getUrl();
        verify(twitchConfig, times(1)).twitchRequest(any(String.class));
    }

    @Test
    void getBroadcaster() {
        var broadcasterName = "qsnake";

        List<TwitchUser> rootTwitchGame = List.of(new TwitchUser(71558231, broadcasterName));

        var request = twitchRequest("https://api.twitch.tv/helix/users?login=" + broadcasterName);

        when(twitchConfig.twitchRequest(any(String.class))).thenReturn(request);
        when(twitchConfig.getUrl()).thenReturn("https://api.twitch.tv/helix/");

        var result = twitchService.getBroadcaster(broadcasterName);

        assertEquals(rootTwitchGame, result);
        verify(twitchConfig, times(1)).getUrl();
        verify(twitchConfig, times(1)).twitchRequest(any(String.class));
    }

    @Test
    void getBroadcaster_IncorrectBroadcasterName() {
        var incorrectBroadcasterName = "}{?|>/";

        var encodeBroadcasterName = URLEncoder.encode(incorrectBroadcasterName, StandardCharsets.UTF_8).replace("+", "%20");

        var request = twitchRequest("https://api.twitch.tv/helix/users?login=" + encodeBroadcasterName);

        when(twitchConfig.twitchRequest(any(String.class))).thenReturn(request);
        when(twitchConfig.getUrl()).thenReturn("https://api.twitch.tv/helix/");

        assertThrows(BroadcasterNotFoundException.class, () -> twitchService.getBroadcaster(incorrectBroadcasterName));
        verify(twitchConfig, times(1)).getUrl();
        verify(twitchConfig, times(1)).twitchRequest(any(String.class));

    }

    @Test
    void getClipsByGameId() {
        int gameId = 29595;
        LocalDate localDate = LocalDate.now();
        String date = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        var request = twitchRequest("https://api.twitch.tv/helix/clips?game_id=29595&started_at=" + date + "T00:00:00%2B03:00" + "&first=100");

        when(twitchConfig.twitchRequest(any(String.class))).thenReturn(request);
        when(twitchConfig.getUrl()).thenReturn("https://api.twitch.tv/helix/");

        var result = twitchService.getClipsByGameId(gameId, date);

        assertInstanceOf(TwitchClipsDto.class, result);
        verify(twitchConfig, times(1)).getUrl();
        verify(twitchConfig, times(1)).twitchRequest(any(String.class));
    }

    @Test
    void getClipsByBroadcastersId() {
        int broadcasterId = 71558231;
        LocalDate localDate = LocalDate.now();
        String date = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        CompletableFuture<TwitchClipsDto> expected = new CompletableFuture<>();

        var request = twitchRequest("https://api.twitch.tv/helix/clips?broadcaster_id=71558231&started_at=" + date + "T00:00:00%2B03:00");

        when(twitchConfig.twitchRequest(any(String.class))).thenReturn(request);
        when(twitchConfig.getUrl()).thenReturn("https://api.twitch.tv/helix/");

        var result = twitchService.getClipsByBroadcastersId(broadcasterId, date);

        assertInstanceOf(expected.getClass(), result);
        verify(twitchConfig, times(1)).getUrl();
        verify(twitchConfig, times(1)).twitchRequest(any(String.class));
    }

    @Test
    void getClipsByBroadcasterId() {
        int broadcasterId = 71558231;
        LocalDate localDate = LocalDate.now();
        String date = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        var request = twitchRequest("https://api.twitch.tv/helix/clips?broadcaster_id=71558231&started_at=" + date + "T00:00:00%2B03:00");

        when(twitchConfig.twitchRequest(any(String.class))).thenReturn(request);
        when(twitchConfig.getUrl()).thenReturn("https://api.twitch.tv/helix/");

        var result = twitchService.getClipsByBroadcasterId(broadcasterId, date);

        assertInstanceOf(TwitchClipsDto.class, result);
        verify(twitchConfig, times(1)).getUrl();
        verify(twitchConfig, times(1)).twitchRequest(any(String.class));
    }
}
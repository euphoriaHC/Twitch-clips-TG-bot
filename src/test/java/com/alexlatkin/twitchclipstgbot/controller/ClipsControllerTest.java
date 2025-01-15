package com.alexlatkin.twitchclipstgbot.controller;

import com.alexlatkin.twitchclipstgbot.service.dto.TwitchClip;
import com.alexlatkin.twitchclipstgbot.service.dto.TwitchClipsDto;
import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.alexlatkin.twitchclipstgbot.model.entity.Game;
import com.alexlatkin.twitchclipstgbot.service.ClipService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClipsControllerTest {
    @InjectMocks
    ClipsController clipsController;
    @Mock
    ClipService clipService;

    @Test
    void getClipsByGame_ShouldCallClipService() {
        Game gameMock = mock(Game.class);
        List<TwitchClip> clipList = List.of(new TwitchClip("url", 1, "firstClip", 100)
                                            , new TwitchClip("url", 2, "secondClip", 200));
        TwitchClipsDto twitchClipsDto = new TwitchClipsDto(clipList);

        when(clipService.getClipsByGame(gameMock)).thenReturn(twitchClipsDto);

        var result = clipsController.getClipsByGame(gameMock);

        assertEquals(twitchClipsDto, result);
        verify(clipService, times(1)).getClipsByGame(gameMock);
    }

    @Test
    void getClipsByBroadcaster_ShouldCallClipService() {
        Broadcaster broadcasterMock = mock(Broadcaster.class);
        List<TwitchClip> clipList = List.of(new TwitchClip("url", 1, "firstClip", 100)
                , new TwitchClip("url", 2, "secondClip", 200));
        TwitchClipsDto twitchClipsDto = new TwitchClipsDto(clipList);

        when(clipService.getClipsByBroadcaster(broadcasterMock)).thenReturn(twitchClipsDto);

        var result = clipsController.getClipsByBroadcaster(broadcasterMock);

        assertEquals(twitchClipsDto, result);
        verify(clipService, times(1)).getClipsByBroadcaster(broadcasterMock);
    }

    @Test
    void getClipsByUserFollowList_ShouldCallClipService() {
        List<Broadcaster> userFollowList = List.of(new Broadcaster(1, "firstBc")
                                                , new Broadcaster(2, "secondBc"));

        List<CompletableFuture<TwitchClipsDto>> expectedClips = List.of(
                CompletableFuture.completedFuture(new TwitchClipsDto(List.of(new TwitchClip("url", 1, "firstClip", 100)))),
                CompletableFuture.completedFuture(new TwitchClipsDto(List.of(new TwitchClip("url", 2, "secondClip", 200)))));


        when(clipService.getClipsByUserFollowList(userFollowList)).thenReturn(expectedClips);

        var result = clipsController.getClipsByUserFollowList(userFollowList);

        assertEquals(expectedClips, result);
        verify(clipService, times(1)).getClipsByUserFollowList(userFollowList);
    }
}
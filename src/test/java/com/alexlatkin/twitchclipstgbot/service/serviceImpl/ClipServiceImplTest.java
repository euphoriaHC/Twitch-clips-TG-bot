package com.alexlatkin.twitchclipstgbot.service.serviceImpl;

import com.alexlatkin.twitchclipstgbot.config.TwitchConfig;
import com.alexlatkin.twitchclipstgbot.service.dto.TwitchClip;
import com.alexlatkin.twitchclipstgbot.service.dto.TwitchClipsDto;
import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.alexlatkin.twitchclipstgbot.model.entity.Game;
import com.alexlatkin.twitchclipstgbot.service.TwitchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClipServiceImplTest {
    @InjectMocks
    ClipServiceImpl clipService;
    @Mock
    TwitchService twitchService;
    @Mock
    TwitchConfig twitchConfig;

    @Test
    void getClipsByGame_ShouldCallTwitchService() {
        Game gameMock = mock(Game.class);
        List<TwitchClip> clipList = List.of(new TwitchClip("url", 1, "firstClip", 100)
                                        , new TwitchClip("url", 2, "secondClip", 200));
        TwitchClipsDto twitchClipsDto = new TwitchClipsDto(clipList);

        LocalDate localDate = LocalDate.now();
        String date = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "T00:00:00%2B03:00";

        when(gameMock.getGameId()).thenReturn(1);
        when(twitchConfig.getDate()).thenReturn(date);
        when(twitchService.getClipsByGameId(gameMock.getGameId(), date)).thenReturn(twitchClipsDto);

        var result = clipService.getClipsByGame(gameMock);

        assertEquals(twitchClipsDto, result);
        verify(twitchService, times(1)).getClipsByGameId(gameMock.getGameId(), twitchConfig.getDate());
    }

    @Test
    void getClipsByUserFollowList_ShouldCallTwitchService() {
        List<Broadcaster> userFollowList = List.of(new Broadcaster(1, "firstBc")
                                                , new Broadcaster(2, "secondBc"));

        List<CompletableFuture<TwitchClipsDto>> expectedClips = List.of(
                CompletableFuture.completedFuture(new TwitchClipsDto(List.of(new TwitchClip("url", 1, "firstClip", 100)))),
                CompletableFuture.completedFuture(new TwitchClipsDto(List.of(new TwitchClip("url", 2, "secondClip", 200)))));

        LocalDate localDate = LocalDate.now();
        String date = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "T00:00:00%2B03:00";

        when(twitchConfig.getDate()).thenReturn(date);
        when(twitchService.getClipsByBroadcastersId(userFollowList.get(0).getBroadcasterId(), date)).thenReturn(expectedClips.get(0));
        when(twitchService.getClipsByBroadcastersId(userFollowList.get(1).getBroadcasterId(), date)).thenReturn(expectedClips.get(1));

        var result = clipService.getClipsByUserFollowList(userFollowList);

        assertEquals(expectedClips.get(0), result.get(0));
        assertEquals(expectedClips.get(1), result.get(1));

        verify(twitchService, times(1)).getClipsByBroadcastersId(userFollowList.get(0).getBroadcasterId(), twitchConfig.getDate());
        verify(twitchService, times(1)).getClipsByBroadcastersId(userFollowList.get(1).getBroadcasterId(), twitchConfig.getDate());
    }

    @Test
    void getClipsByUserFollowList_Exception() {
        List<Broadcaster> userFollowList = List.of(new Broadcaster(1, "firstBc")
                                                , new Broadcaster(2, "secondBc"));

        CompletableFuture<TwitchClipsDto> futureWithError = new CompletableFuture<>();
        futureWithError.completeExceptionally(new RuntimeException());

        LocalDate localDate = LocalDate.now();
        String date = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "T00:00:00%2B03:00";

        when(twitchConfig.getDate()).thenReturn(date);
        when(twitchService.getClipsByBroadcastersId(userFollowList.get(0).getBroadcasterId(), date)).thenReturn(futureWithError);

        assertThrows(RuntimeException.class, () -> clipService.getClipsByUserFollowList(userFollowList));
        verify(twitchService, times(1)).getClipsByBroadcastersId(userFollowList.get(0).getBroadcasterId(), twitchConfig.getDate());
    }

    @Test
    void getClipsByBroadcaster_ShouldCallTwitchService() {
        Broadcaster broadcasterMock = mock(Broadcaster.class);
        List<TwitchClip> clipList = List.of(new TwitchClip("url", 1, "firstClip", 100)
                                        , new TwitchClip("url", 2, "secondClip", 200));
        TwitchClipsDto twitchClipsDto = new TwitchClipsDto(clipList);

        LocalDate localDate = LocalDate.now();
        String date = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "T00:00:00%2B03:00";

        when(twitchConfig.getDate()).thenReturn(date);
        when(broadcasterMock.getBroadcasterId()).thenReturn(1);
        when(twitchService.getClipsByBroadcasterId(broadcasterMock.getBroadcasterId(),date)).thenReturn(twitchClipsDto);

        var result = clipService.getClipsByBroadcaster(broadcasterMock);

        assertEquals(twitchClipsDto, result);
        verify(twitchService, times(1)).getClipsByBroadcasterId(broadcasterMock.getBroadcasterId(), twitchConfig.getDate());
    }
}
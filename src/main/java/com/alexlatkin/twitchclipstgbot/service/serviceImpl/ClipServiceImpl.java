package com.alexlatkin.twitchclipstgbot.service.serviceImpl;

import com.alexlatkin.twitchclipstgbot.model.dto.TwitchClipsDto;
import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.alexlatkin.twitchclipstgbot.model.entity.Game;
import com.alexlatkin.twitchclipstgbot.service.ClipService;
import com.alexlatkin.twitchclipstgbot.service.TwitchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Service
public class ClipServiceImpl implements ClipService {
    private final TwitchService twitchService;

    @Override
    public TwitchClipsDto getClipsByGame(Game game) {
        LocalDate localDate = LocalDate.now();
        String date = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return twitchService.getClipsByGameId(game.getGameId(), date);
    }

    @Override
    public List<CompletableFuture<TwitchClipsDto>> getClipsByUserFollowList(List<Broadcaster> userFollowList) {
        LocalDate localDate = LocalDate.now();
        String date = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        List<CompletableFuture<TwitchClipsDto>> allClips = new ArrayList<>();

        List<Integer> broadcastersId = userFollowList.stream().map(Broadcaster::getBroadcasterId).toList();

        broadcastersId.forEach(bcId -> allClips.add(twitchService.getClipsByBroadcastersId(bcId, date)));

        try {
            CompletableFuture.allOf(allClips.toArray(new CompletableFuture[0])).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return allClips;
    }

    @Override
    public TwitchClipsDto getClipsByBroadcaster(Broadcaster broadcaster) {
        LocalDate localDate = LocalDate.now();
        String date = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return twitchService.getClipsByBroadcasterId(broadcaster.getBroadcasterId(), date);
    }

}

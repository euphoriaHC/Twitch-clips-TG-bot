package com.alexlatkin.twitchclipstgbot.service.serviceImpl;

import com.alexlatkin.twitchclipstgbot.service.dto.TwitchClipsDto;
import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.alexlatkin.twitchclipstgbot.model.entity.Game;
import com.alexlatkin.twitchclipstgbot.service.ClipService;
import com.alexlatkin.twitchclipstgbot.service.TwitchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClipServiceImpl implements ClipService {
    private final TwitchService twitchService;

    @Override
    public TwitchClipsDto getClipsByGame(Game game) {
        return twitchService.getClipsByGameId(game.getGameId(), getDate());
    }

    @Override
    public List<CompletableFuture<TwitchClipsDto>> getClipsByUserFollowList(List<Broadcaster> userFollowList) {
        List<CompletableFuture<TwitchClipsDto>> allClips = new ArrayList<>();

        List<Integer> broadcastersId = userFollowList.stream().map(Broadcaster::getBroadcasterId).toList();

        broadcastersId.forEach(bcId -> allClips.add(twitchService.getClipsByBroadcastersId(bcId, getDate())));

        try {
            CompletableFuture.allOf(allClips.toArray(new CompletableFuture[0])).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return allClips;
    }

    @Override
    public TwitchClipsDto getClipsByBroadcaster(Broadcaster broadcaster) {
        return twitchService.getClipsByBroadcasterId(broadcaster.getBroadcasterId(), getDate());
    }

    /*
      Используется только внутри класса
      Установка начальной даты, используемой для фильтрации клипов (Мск 00:00 в день запроса)
      Формат RFC3339
    */
    private String getDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "T00:00:00%2B03:00";
    }

}

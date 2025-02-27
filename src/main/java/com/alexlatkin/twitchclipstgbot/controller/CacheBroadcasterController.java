package com.alexlatkin.twitchclipstgbot.controller;

import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.alexlatkin.twitchclipstgbot.model.repository.CacheBroadcasterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CacheBroadcasterController {
    private final CacheBroadcasterRepository cacheBroadcaster;

    public void cacheCaster(String key, Broadcaster broadcaster) {
        cacheBroadcaster.cacheCaster(key, broadcaster);
    }

    public Optional<Broadcaster> getCacheCaster(String key) {
        return cacheBroadcaster.getCacheCaster(key);
    }
}

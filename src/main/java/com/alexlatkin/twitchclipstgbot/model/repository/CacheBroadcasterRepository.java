package com.alexlatkin.twitchclipstgbot.model.repository;

import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;

import java.util.Optional;

public interface CacheBroadcasterRepository {
    void cacheCaster(String key, Broadcaster broadcaster);
    Optional<Broadcaster> getCacheCaster(String key);
}

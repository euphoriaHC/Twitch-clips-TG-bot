package com.alexlatkin.twitchclipstgbot.model.repository;

import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;

public interface CacheBroadcasterRepository {
    void cacheCaster(String key, Broadcaster broadcaster);
    Broadcaster getCacheCaster(String key);
}

package com.alexlatkin.twitchclipstgbot.service;

import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;


public interface BroadcasterService {
    Broadcaster getBroadcasterByBroadcasterName(String broadcasterName);
    void addBroadcaster(Broadcaster broadcaster);
    boolean existsBroadcasterByBroadcasterName(String broadcasterName);
}

package com.alexlatkin.twitchclipstgbot.model.repository;

import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BroadcasterRepository extends JpaRepository<Broadcaster, Integer> {
    Broadcaster findBroadcasterByBroadcasterName(String broadcasterName);
    boolean existsBroadcasterByBroadcasterName(String broadcasterName);
}

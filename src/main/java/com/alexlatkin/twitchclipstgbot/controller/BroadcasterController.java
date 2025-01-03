package com.alexlatkin.twitchclipstgbot.controller;

import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.alexlatkin.twitchclipstgbot.service.BroadcasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class BroadcasterController {
    final BroadcasterService broadcasterService;

    public boolean existsBroadcasterByBroadcasterName(String broadcasterName) {
        return broadcasterService.existsBroadcasterByBroadcasterName(broadcasterName);
    }

    public Broadcaster getBroadcasterByBroadcasterName(String broadcasterName) {
        return broadcasterService.getBroadcasterByBroadcasterName(broadcasterName);
    }

    public void addBroadcaster(Broadcaster broadcaster) {
        broadcasterService.addBroadcaster(broadcaster);
    }

}

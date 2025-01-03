package com.alexlatkin.twitchclipstgbot.service.serviceImpl;

import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.alexlatkin.twitchclipstgbot.model.repository.BroadcasterRepository;
import com.alexlatkin.twitchclipstgbot.service.BroadcasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class BroadcasterServiceImpl implements BroadcasterService {
    private final BroadcasterRepository broadcasterRepository;
    @Transactional
    @Override
    public boolean existsBroadcasterByBroadcasterName(String broadcasterName) {
        return broadcasterRepository.existsBroadcasterByBroadcasterName(broadcasterName);
    }
    @Transactional
    @Override
    public Broadcaster getBroadcasterByBroadcasterName(String broadcasterName) {
        return broadcasterRepository.findBroadcasterByBroadcasterName(broadcasterName);
    }
    @Transactional
    @Override
    public void addBroadcaster(Broadcaster broadcaster) {
        broadcasterRepository.save(broadcaster);
    }
}

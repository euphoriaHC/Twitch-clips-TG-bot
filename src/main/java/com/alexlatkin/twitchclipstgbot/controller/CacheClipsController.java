package com.alexlatkin.twitchclipstgbot.controller;

import com.alexlatkin.twitchclipstgbot.service.dto.TwitchClip;
import com.alexlatkin.twitchclipstgbot.model.repository.CacheClipsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CacheClipsController {
    private final CacheClipsRepository cacheClips;

    public void addClipListByUserChatId(String userChatId, List<TwitchClip> twitchClipList) {
       cacheClips.addClipListByUserChatId(userChatId, twitchClipList);
    }

    public TwitchClip getClipByUserChatId(String userChatId) {
        return cacheClips.getClipByUserChatId(userChatId);
    }

    public List<TwitchClip> getClipListByUserChatId(String userChatId) {
        return cacheClips.getClipListByUserChatId(userChatId);
    }

    public void deleteAllClipsByUserChatId(String userChatId) {
        cacheClips.deleteAllClipsByUserChatId(userChatId);
    }

}

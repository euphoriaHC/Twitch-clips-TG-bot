package com.alexlatkin.twitchclipstgbot.model.repository;

import com.alexlatkin.twitchclipstgbot.service.dto.TwitchClip;

import java.util.List;

public interface CacheClipsRepository {
    void addClipByUserChatId(String userChatId, TwitchClip twitchClip);
    void addClipListByUserChatId(String userChatId, List<TwitchClip> twitchClipList);
    TwitchClip getClipByUserChatId(String userChatId);
    List<TwitchClip> getClipListByUserChatId(String userChatId);
    void deleteAllClipsByUserChatId(String userChatId);

}

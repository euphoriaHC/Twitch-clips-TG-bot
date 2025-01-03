package com.alexlatkin.twitchclipstgbot.service;

import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.alexlatkin.twitchclipstgbot.model.entity.User;

import java.util.List;

public interface UserService {
    User getUserByUserChatId(Long chatId);
    boolean existsUserByChatId(Long chatId);
    void addUser(User user);
    void addBroadcasterInUserFollowList(Long chatId, Broadcaster broadcaster);
    void addBroadcasterInUserBlackList(Long chatId, Broadcaster broadcaster);
    List<Broadcaster> getUserFollowListByUserChatId(Long chatId);
    List<Broadcaster> getUserBlackListByUserChatId(Long chatId);
    void deleteBroadcasterFromUserFollowList(Long chatId, Broadcaster broadcaster);
    void deleteBroadcasterFromUserBlackList(Long chatId, Broadcaster broadcaster);
    void clearUserFollowList(Long chatId);
    void clearUserBlackList(Long chatId);
}

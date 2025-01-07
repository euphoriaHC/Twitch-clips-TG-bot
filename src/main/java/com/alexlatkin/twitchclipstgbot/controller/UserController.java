package com.alexlatkin.twitchclipstgbot.controller;

import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.alexlatkin.twitchclipstgbot.model.entity.User;
import com.alexlatkin.twitchclipstgbot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    final UserService userService;

    public boolean existsUserByChatId(Long chatId) {
       return userService.existsUserByChatId(chatId);
    }

    public void addUser(User user) {
        userService.addUser(user);
    }

    public void addBroadcasterInUserFollowList(Long userId, Broadcaster broadcaster) {
        userService.addBroadcasterInUserFollowList(userId, broadcaster);
    }

    public void addBroadcasterInUserBlackList(Long userId, Broadcaster broadcaster) {
        userService.addBroadcasterInUserBlackList(userId, broadcaster);
    }

    public List<Broadcaster> getUserFollowListByUserChatId(Long chatId) {
        return userService.getUserFollowListByUserChatId(chatId);
    }

    public List<Broadcaster> getUserBlackListByUserChatId(Long chatId) {
        return userService.getUserBlackListByUserChatId(chatId);
    }

    public void deleteBroadcasterFromUserFollowList(Long chatId, Broadcaster broadcaster) {
        userService.deleteBroadcasterFromUserFollowList(chatId, broadcaster);
    }

    public void deleteBroadcasterFromUserBlackList(Long chatId, Broadcaster broadcaster) {
        userService.deleteBroadcasterFromUserBlackList(chatId, broadcaster);
    }

    public void clearUserFollowList(Long chatId) {
        userService.clearUserFollowList(chatId);
    }

    public void clearUserBlackList(Long chatId) {
        userService.clearUserBlackList(chatId);
    }
}

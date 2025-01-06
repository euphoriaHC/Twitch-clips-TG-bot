package com.alexlatkin.twitchclipstgbot.service.serviceImpl;

import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import com.alexlatkin.twitchclipstgbot.model.entity.User;
import com.alexlatkin.twitchclipstgbot.model.repository.UserRepository;
import com.alexlatkin.twitchclipstgbot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Transactional
    @Override
    public User getUserByUserChatId(Long chatId) {
        return userRepository.findById(chatId).orElseThrow();
    }

    @Transactional
    @Override
    public boolean existsUserByChatId(Long chatId) {
        return userRepository.existsUserByChatId(chatId);
    }
    @Transactional
    @Override
    public void addUser(User user) {
        userRepository.save(user);
        log.info(user + " пользователь записан в дб");
    }

    @Transactional
    @Override
    public void addBroadcasterInUserFollowList(Long chatId, Broadcaster broadcaster) {
        var user = getUserByUserChatId(chatId);
        user.getFollowList().add(broadcaster);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void addBroadcasterInUserBlackList(Long chatId, Broadcaster broadcaster) {
        var user = getUserByUserChatId(chatId);
        user.getBlackList().add(broadcaster);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public List<Broadcaster> getUserFollowListByUserChatId(Long chatId) {
        return getUserByUserChatId(chatId).getFollowList();
    }

    @Transactional
    @Override
    public List<Broadcaster> getUserBlackListByUserChatId(Long chatId) {
        return getUserByUserChatId(chatId).getBlackList();
    }
    @Transactional
    @Override
    public void deleteBroadcasterFromUserFollowList(Long chatId, Broadcaster broadcaster) {
        var user = getUserByUserChatId(chatId);
        user.getFollowList().remove(broadcaster);
        userRepository.save(user);
    }
    @Transactional
    @Override
    public void deleteBroadcasterFromUserBlackList(Long chatId, Broadcaster broadcaster) {
        var user = getUserByUserChatId(chatId);
        user.getBlackList().remove(broadcaster);
        userRepository.save(user);
    }
    @Transactional
    @Override
    public void clearUserFollowList(Long chatId) {
        var user = getUserByUserChatId(chatId);
        user.getFollowList().clear();
        userRepository.save(user);
    }
    @Transactional
    @Override
    public void clearUserBlackList(Long chatId) {
        var user = getUserByUserChatId(chatId);
        user.getBlackList().clear();
        userRepository.save(user);
    }
}

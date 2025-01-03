package com.alexlatkin.twitchclipstgbot.model.repository;

import com.alexlatkin.twitchclipstgbot.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByChatId(Long chatId);
}

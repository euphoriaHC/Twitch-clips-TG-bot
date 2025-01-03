package com.alexlatkin.twitchclipstgbot.telegramBotCommands.buttonCommands;

import com.alexlatkin.twitchclipstgbot.controller.BroadcasterController;
import com.alexlatkin.twitchclipstgbot.controller.CacheClipsController;
import com.alexlatkin.twitchclipstgbot.controller.UserController;
import com.alexlatkin.twitchclipstgbot.keyboard.KeyboardCasterClipsCommand;
import com.alexlatkin.twitchclipstgbot.keyboard.KeyboardGameClipsCommand;
import com.alexlatkin.twitchclipstgbot.model.dto.TwitchClip;
import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@RequiredArgsConstructor
@Component
public class BlockButtonCommand implements ButtonCommands {
    final UserController userController;
    final BroadcasterController broadcasterController;
    final CacheClipsController cacheClipsController;
    @Override
    public EditMessageText actionButtonInCurrentMessage(Update update, Broadcaster broadcaster) {
        var chatId = update.getCallbackQuery().getMessage().getChatId();
        var chatIdString = chatId.toString();
        var messageId = update.getCallbackQuery().getMessage().getMessageId();
        var buttonKey = update.getCallbackQuery().getData();

        var bcName = broadcaster.getBroadcasterName();

        if (broadcasterController.existsBroadcasterByBroadcasterName(bcName)) {
            userController.addBroadcasterInUserBlackList(chatId, broadcasterController.getBroadcasterByBroadcasterName(bcName));
        } else {
            broadcasterController.addBroadcaster(broadcaster);
            userController.addBroadcasterInUserBlackList(chatId, broadcaster);
        }

        List<TwitchClip> clipList = cacheClipsController.getClipListByUserChatId(chatIdString);
        var filterClipList = new ArrayList<>(clipList.stream().filter(clip -> !userController.getUserBlackListByUserChatId(chatId).contains(
                new Broadcaster(clip.getBroadcasterId(), clip.getBroadcasterName()))).toList());
        filterClipList.sort(Comparator.comparing(TwitchClip::getViewCount));
        cacheClipsController.deleteAllClipsByUserChatId(chatIdString);
        cacheClipsController.addClipListByUserChatId(chatIdString, filterClipList);

        EditMessageText response = new EditMessageText();
        response.setChatId(chatId);
        response.setMessageId(messageId);
        response.setText(String.format("\n %S добавлен в чёрный список", bcName));

        switch (buttonKey) {
            case "GAME_CLIPS_BLOCK" -> response.setReplyMarkup(KeyboardGameClipsCommand.createKeyboardWithNextButton());
            case "CASTER_CLIPS_BLOCK" -> response.setReplyMarkup(KeyboardCasterClipsCommand.createKeyboardWithNextButton());
        }

        return response;
    }

}

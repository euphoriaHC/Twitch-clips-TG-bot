package com.alexlatkin.twitchclipstgbot.telegramBotCommands.buttonCommands;

import com.alexlatkin.twitchclipstgbot.controller.BroadcasterController;
import com.alexlatkin.twitchclipstgbot.controller.UserController;
import com.alexlatkin.twitchclipstgbot.keyboard.KeyboardCasterClipsCommand;
import com.alexlatkin.twitchclipstgbot.keyboard.KeyboardGameClipsCommand;
import com.alexlatkin.twitchclipstgbot.model.entity.Broadcaster;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class FollowButtonCommand implements ButtonCommands {
    final UserController userController;
    final BroadcasterController broadcasterController;
    @Override
    public EditMessageText actionButtonInCurrentMessage(Update update, Broadcaster broadcaster) {
        var chatId = update.getCallbackQuery().getMessage().getChatId();
        var messageId = update.getCallbackQuery().getMessage().getMessageId();
        var buttonKey = update.getCallbackQuery().getData();

        var bcName = broadcaster.getBroadcasterName();

        if (broadcasterController.existsBroadcasterByBroadcasterName(bcName)) {
            userController.addBroadcasterInUserFollowList(chatId, broadcasterController.getBroadcasterByBroadcasterName(bcName));
        } else {
            broadcasterController.addBroadcaster(broadcaster);
            userController.addBroadcasterInUserFollowList(chatId, broadcaster);
        }

        EditMessageText response = new EditMessageText();
        response.setChatId(chatId);
        response.setMessageId(messageId);
        response.setText(" \n Вы подписались на " + bcName);

        switch (buttonKey) {
            case "GAME_CLIPS_FOLLOW" -> response.setReplyMarkup(KeyboardGameClipsCommand.createKeyboardWithNextButton());
            case "CASTER_CLIPS_FOLLOW" -> response.setReplyMarkup(KeyboardCasterClipsCommand.createKeyboardWithNextButton());
        }

        return response;
    }
}

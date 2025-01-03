package com.alexlatkin.twitchclipstgbot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;


public class KeyboardFollowListClipsCommand {
    public static InlineKeyboardMarkup createKeyboardWithNextButton() {
        MessageKeyboardBuilder builder = new MessageKeyboardBuilder();
        InlineKeyboardMarkup keyboard = builder
                .addButton("Следующий клип", "FOLLOW_LIST_CLIPS_NEXT")
                .build();

        return keyboard;
    }

}

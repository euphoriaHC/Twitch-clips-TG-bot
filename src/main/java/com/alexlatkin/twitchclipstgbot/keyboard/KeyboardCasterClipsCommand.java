package com.alexlatkin.twitchclipstgbot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;


public class KeyboardCasterClipsCommand {
    public static InlineKeyboardMarkup createFullKeyboard(String broadcasterName) {
        MessageKeyboardBuilder builder = new MessageKeyboardBuilder();
        InlineKeyboardMarkup keyboard = builder
                .addButton("Подписаться на " + broadcasterName, "CASTER_CLIPS_FOLLOW")
                .addButton("Скрыть " + broadcasterName, "CASTER_CLIPS_BLOCK")
                .addButton("Следующий клип", "CASTER_CLIPS_NEXT")
                .build();

        return keyboard;
    }

    public static InlineKeyboardMarkup createKeyboardWithNextButton() {
        MessageKeyboardBuilder builder = new MessageKeyboardBuilder();
        InlineKeyboardMarkup keyboard = builder
                .addButton("Следующий клип", "CASTER_CLIPS_NEXT")
                .build();

        return keyboard;
    }
}

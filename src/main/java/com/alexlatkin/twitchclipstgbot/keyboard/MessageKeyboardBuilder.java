package com.alexlatkin.twitchclipstgbot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class MessageKeyboardBuilder {
    private final List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
    public MessageKeyboardBuilder addButton(String buttonText, String buttonKey) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonText);
        button.setCallbackData(buttonKey);

        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        buttonRow.add(button);
        keyboard.add(buttonRow);

        return this;
    }

    public InlineKeyboardMarkup build() {
        return new InlineKeyboardMarkup(keyboard);
    }
}

package com.alexlatkin.twitchclipstgbot.service.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class RootTwitchUser {
    private ArrayList<TwitchUser> data;
}

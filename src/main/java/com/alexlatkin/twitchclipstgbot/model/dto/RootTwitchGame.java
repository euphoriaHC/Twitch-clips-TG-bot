package com.alexlatkin.twitchclipstgbot.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RootTwitchGame {
    private List<TwitchGameDto> data;
}

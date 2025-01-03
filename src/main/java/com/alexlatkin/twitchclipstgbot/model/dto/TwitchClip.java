package com.alexlatkin.twitchclipstgbot.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitchClip {
    private String url;
    @JsonProperty("broadcaster_id")
    private int broadcasterId;
    @JsonProperty("broadcaster_name")
    private String broadcasterName;
    @JsonProperty("view_count")
    private int viewCount;
}

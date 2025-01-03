package com.alexlatkin.twitchclipstgbot.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitchClipsDto {
    private List<TwitchClip> data;

    @Override
    public String toString() {
        return "TwitchClipsDto{" +
                "data=" + data +
                '}';
    }
}

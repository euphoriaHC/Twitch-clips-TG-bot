package com.alexlatkin.twitchclipstgbot.exception;


import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ErrorDto {
    private String error;
}

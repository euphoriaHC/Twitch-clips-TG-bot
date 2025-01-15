package com.alexlatkin.twitchclipstgbot.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.time.Duration;

@Slf4j
@Data
@Configuration
@PropertySource("classpath:application.properties")
public class TwitchConfig {
    @Value("${twitchConfig.url}")
    private String url;
    @Value("${twitchConfig.firstHeaderName}")
    private String firstHeaderName;
    @Value("${twitchConfig.firstHeaderValue}")
    private String firstHeaderValue;
    @Value("${twitchConfig.secondHeaderName}")
    private String secondHeaderName;
    @Value("${twitchConfig.secondHeaderValue}")
    private String secondHeaderValue;
    public HttpRequest twitchRequest(String uriAsString){

        URI uri;

        try {
            uri = new URI(uriAsString);
        } catch (URISyntaxException e) {
            log.error("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }

        var request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header(firstHeaderName,"Bearer " + firstHeaderValue)
                .header(secondHeaderName, secondHeaderValue)
                .timeout(Duration.ofSeconds(20))
                .build();

        return request;

    }
}

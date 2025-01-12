package com.alexlatkin.twitchclipstgbot.config;


import com.alexlatkin.twitchclipstgbot.controller.*;
import com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BotInitializer {
    final TelegramBot telegramBot;
    final BotConfig botConfig;
    final TelegramCommands telegramCommands;
    final StartCommand startCommand;
    final HelpCommand helpCommand;
    final GameClipsCommand gameClipsCommand;
    final CasterClipsCommand casterClipsCommand;
    final FollowListClipsCommand followListClipsCommand;
    final FollowListCommand followListCommand;
    final BlackListCommand blackListCommand;
    final DeleteCommand deleteCommand;
    final ClearFollowListCommand clearFollowListCommand;
    final ClearBlackListCommand clearBlackListCommand;
    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

        Map<String, BotCommands> textCommandsFirstMessage = Map.of("/start", startCommand
                                                                ,"/help", helpCommand
                                                                ,"/game_clips", gameClipsCommand
                                                                ,"/caster_clips", casterClipsCommand
                                                                ,"/follow_list_clips", followListClipsCommand
                                                                ,"/follow_list", followListCommand
                                                                ,"/black_list", blackListCommand
                                                                ,"/delete", deleteCommand
                                                                ,"/clear_follow_list", clearFollowListCommand
                                                                ,"/clear_black_list", clearBlackListCommand);

        Map<String, BotCommandsWithSecondMessage> textCommandsWithSecondMessage = Map.of("/game_clips", gameClipsCommand
                                                                                        ,"/caster_clips", casterClipsCommand
                                                                                        ,"/delete", deleteCommand);

        Map<String, BotButtonCommands> buttonCommands = Map.of("GAME_CLIPS_FOLLOW", gameClipsCommand
                                                              ,"GAME_CLIPS_BLOCK", gameClipsCommand
                                                              ,"GAME_CLIPS_NEXT", gameClipsCommand
                                                              ,"CASTER_CLIPS_FOLLOW", casterClipsCommand
                                                              ,"CASTER_CLIPS_BLOCK", casterClipsCommand
                                                              ,"CASTER_CLIPS_NEXT", casterClipsCommand
                                                              ,"FOLLOW_LIST_CLIPS_NEXT", followListClipsCommand);


        // Кнопка меню рядом с полем для ввода текста
        List<BotCommand> commandsMenu = List.of(new BotCommand("/help", "Описание команд")
                                            , new BotCommand("/game_clips", "Клипы по названию игры")
                                            , new BotCommand("/caster_clips", "Клипы по нику стримера")
                                            , new BotCommand("/follow_list_clips", "Клипы по стримерам из ваших подписок")
                                            , new BotCommand("/follow_list", "Ваши подписки")
                                            , new BotCommand("/black_list", "Ваш чёрный лист")
                                            , new BotCommand("/delete", "Удаление из чёрного списка или подписок")
                                            , new BotCommand("/clear_follow_list", "Очистить ваши подписки")
                                            , new BotCommand("/clear_black_list", "Очистить чёрный лист"));
        telegramBot.execute(new SetMyCommands(commandsMenu, new BotCommandScopeDefault(), null));

        telegramCommands.setTextCommandsFirstMessage(textCommandsFirstMessage);
        telegramCommands.setTextCommandWithSecondMessage(textCommandsWithSecondMessage);
        telegramCommands.setButtonCommands(buttonCommands);
        botConfig.setTelegramCommands(telegramCommands);
        telegramBotsApi.registerBot(telegramBot);
    }

}

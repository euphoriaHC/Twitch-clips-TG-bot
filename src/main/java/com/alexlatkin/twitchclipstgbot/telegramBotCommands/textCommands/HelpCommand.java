package com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class HelpCommand implements BotCommands {
    @Override
    public SendMessage firstMessage(Update update) {
        var chatId = update.getMessage().getChatId().toString();
        var answerText = """
                Добро пожаловать в Twitch Clips bot!
                
                С помощью бота ты можешь получать самые популярные клипы игр или стримеров за день, а так же отслеживать стримеров и блокировать их.
                
                Вот полный список команд:
                
                /help - Описание всех команд
                
                /game_clips - Получить клипы по названию игры или категории
                
                /caster_clips - Получить клипы по никнейму стримера
                
                /follow_list_clips - Получить клипы стримеров, которых вы отслеживаете
                
                /follow_list - Ваш список подписок стримеров
                
                /black_list - Ваш чёрный лист стримеров
                
                /delete - Удалить стримера из фоллоу листа или чёрного списка
                
                /clear_follow_list - Очищает ваш список подписок
                
                /clear_black_list - Очищает ваш чёрный лист
                """;

        return new SendMessage(chatId, answerText);
    }
}

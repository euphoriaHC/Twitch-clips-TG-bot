package com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands;

import com.alexlatkin.twitchclipstgbot.controller.CacheClipsController;
import com.alexlatkin.twitchclipstgbot.controller.ClipsController;
import com.alexlatkin.twitchclipstgbot.controller.UserController;
import com.alexlatkin.twitchclipstgbot.keyboard.KeyboardFollowListClipsCommand;
import com.alexlatkin.twitchclipstgbot.service.dto.TwitchClip;
import com.alexlatkin.twitchclipstgbot.service.dto.TwitchClipsDto;
import com.alexlatkin.twitchclipstgbot.telegramBotCommands.buttonCommands.commandsWIthAnswer.NextClipButtonCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/*
 Класс команды /follow_list_clips
 Относится к командам, которые обрабатывают одно сообщение пользователя, сообщения этой команды поддерживают клавиатуру
 Отправляет пользователю клипы стримеров из подписок, созданные за сутки
*/
@Slf4j
@RequiredArgsConstructor
@Component
public class FollowListClipsCommand implements BotButtonCommands, BotCommands {
    final UserController userController;
    final ClipsController clipsController;
    final NextClipButtonCommand nextClipButtonCommand;
    final CacheClipsController cacheClipsController;

    /*
      Метод обрабатывает вызов команды (Сообщение /follow_list_clips)
      Делает запрос через ClipsController, получает клипы, сортирует их, после кэширует их в Redis
    */
    @Override
    public SendMessage firstMessage(Update update) {
        var chatId = update.getMessage().getChatId();
        var chatIdString = chatId.toString();

        var userFollowList = userController.getUserFollowListByUserChatId(chatId);
        List<CompletableFuture<TwitchClipsDto>> clips;
        List<TwitchClip> clipList = new ArrayList<>();

        if (userFollowList.isEmpty()) {
            return new SendMessage(chatIdString, "У вас нет отслеживаемых стримеров");
        } else {

                clips = clipsController.getClipsByUserFollowList(userFollowList);

                for (CompletableFuture<TwitchClipsDto> clip : clips) {

                    try {
                        clipList.addAll(clip.get().getData());
                    } catch (InterruptedException e) {
                        log.error("Error: " + e.getMessage());
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        log.error("Error: " + e.getMessage());
                        throw new RuntimeException(e);
                    }

                }


            clipList.sort(Comparator.comparing(TwitchClip::getViewCount));

            cacheClipsController.addClipListByUserChatId(chatIdString, clipList);

            var clipUrl = cacheClipsController.getClipByUserChatId(chatIdString).getUrl();

            var msg = new SendMessage(chatIdString, clipUrl);

            msg.setReplyMarkup(KeyboardFollowListClipsCommand.createKeyboardWithNextButton());

            return msg;
        }

    }

    /*
      Метод срабатывает при нажатии пользователем на кнопку клавиатуры одного из сообщений принадлежащих команде /follow_list_clips
    */
    @Override
    public SendMessage clickButton(Update update) {
        var buttonKey = update.getCallbackQuery().getData();
        var chatIdString = update.getCallbackQuery().getMessage().getChatId().toString();
        List<TwitchClip> twitchClipList = cacheClipsController.getClipListByUserChatId(chatIdString);
        var answer = new SendMessage();

        if (twitchClipList.isEmpty()) {
            return new SendMessage(chatIdString, "Клипы отслеживаемых стримеров закончились");
        }

        if (buttonKey.equals("FOLLOW_LIST_CLIPS_NEXT")) {
            var clip = cacheClipsController.getClipByUserChatId(chatIdString);
            answer = nextClipButtonCommand.actionWithMessage(update, clip);
        }

        return answer;
    }
}

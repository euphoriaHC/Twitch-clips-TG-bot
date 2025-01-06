package com.alexlatkin.twitchclipstgbot.telegramBotCommands.textCommands;

import com.alexlatkin.twitchclipstgbot.controller.CacheClipsController;
import com.alexlatkin.twitchclipstgbot.controller.ClipsController;
import com.alexlatkin.twitchclipstgbot.controller.UserController;
import com.alexlatkin.twitchclipstgbot.keyboard.KeyboardFollowListClipsCommand;
import com.alexlatkin.twitchclipstgbot.model.dto.TwitchClip;
import com.alexlatkin.twitchclipstgbot.model.dto.TwitchClipsDto;
import com.alexlatkin.twitchclipstgbot.telegramBotCommands.buttonCommands.commandsWIthAnswer.NextClipButtonCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
public class FollowListClipsCommand implements BotButtonCommands, BotCommands {
    final UserController userController;
    final ClipsController clipsController;
    final NextClipButtonCommand nextClipButtonCommand;
    final CacheClipsController cacheClipsController;

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

    @Override
    public SendMessage clickButton(Update update) {
        var buttonKey = update.getCallbackQuery().getData();
        var chatIdString = update.getCallbackQuery().getMessage().getChatId().toString();
        List<TwitchClip> twitchClipList = cacheClipsController.getClipListByUserChatId(chatIdString);
        var answer = new SendMessage();

        if (twitchClipList.isEmpty()) {
            return new SendMessage(chatIdString, "Клипы из фоллоу листа законичилсь");
        }

        if (buttonKey.equals("FOLLOW_LIST_CLIPS_NEXT")) {
            var clip = cacheClipsController.getClipByUserChatId(chatIdString);
            answer = nextClipButtonCommand.actionWithMessage(update, clip);
        }

        return answer;
    }
}

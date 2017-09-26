package io.github.nucleuspowered.phonon.modules.chat.listener;

import com.google.inject.Inject;
import io.github.nucleuspowered.phonon.Phonon;
import io.github.nucleuspowered.phonon.discord.DiscordBot;
import io.github.nucleuspowered.phonon.internal.listener.ConditionalListener;
import io.github.nucleuspowered.phonon.internal.listener.ListenerBase;
import io.github.nucleuspowered.phonon.modules.chat.ChatModule;
import io.github.nucleuspowered.phonon.modules.chat.config.ChannelConfig;
import io.github.nucleuspowered.phonon.modules.chat.config.ChatConfig;
import io.github.nucleuspowered.phonon.modules.chat.config.ChatConfigAdapter;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.achievement.GrantAchievementEvent;

import java.util.function.Predicate;

@ConditionalListener(AchievementListener.Condition.class)
public class AchievementListener extends ListenerBase {

    @Inject DiscordBot bot;

    @Listener
    public void onGrantAchievement(GrantAchievementEvent.TargetPlayer event) {

        if (event.isMessageCancelled() || StringUtils.isBlank(event.getMessage().toPlain())) {
            return;
        }

        plugin.getConfigValue(ChatModule.ID, ChatConfigAdapter.class, ChatConfig::getChannels)
            .ifPresent(channelConfigs -> channelConfigs
                .stream()
                .filter(ChannelConfig::enableAchievementMessages)
                .forEach(channelConfig -> {
                    String message = channelConfig.getAchievementMessageTemplate()
                        .replace("%a", event.getAchievement().getName())
                        .replace("%p", event.getTargetEntity().getName());
                    bot.getChannelById(channelConfig.getDiscordChannelId())
                        .ifPresent(c -> bot.sendMessageToDiscord(message, c));
                })
            );
    }

    public static class Condition implements Predicate<Phonon> {

        @Override public boolean test(Phonon phonon) {
            return phonon.getConfigValue(ChatModule.ID, ChatConfigAdapter.class, ChatConfig::isEnableAchievementMessages).orElse(false);
        }
    }
}

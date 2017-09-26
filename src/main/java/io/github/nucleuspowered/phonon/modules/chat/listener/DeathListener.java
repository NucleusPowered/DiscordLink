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
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;

import java.util.function.Predicate;

@ConditionalListener(DeathListener.Condition.class)
public class DeathListener extends ListenerBase {

    @Inject DiscordBot bot;

    @Listener
    public void onPlayerDeath(DestructEntityEvent.Death event) {

        if (!(event.getTargetEntity() instanceof Player) || event.isMessageCancelled() || StringUtils.isBlank(event.getMessage().toPlain())) {
            return;
        }
        Player player = (Player) event.getTargetEntity();

        plugin.getConfigValue(ChatModule.ID, ChatConfigAdapter.class, ChatConfig::getChannels)
            .ifPresent(channelConfigs -> channelConfigs
                .stream()
                .filter(ChannelConfig::enableDeathMessages)
                .forEach(channelConfig -> {
                    String message = channelConfig.getDeathMessageTemplate()
                        .replace("%s", event.getMessage().toPlain())
                        .replace("%p", player.getName());
                    bot.getChannelById(channelConfig.getDiscordChannelId())
                        .ifPresent(channel -> bot.sendMessageToDiscord(message, channel));
                })
            );
    }

    public static class Condition implements Predicate<Phonon> {

        @Override public boolean test(Phonon phonon) {
            return phonon.getConfigValue(ChatModule.ID, ChatConfigAdapter.class, ChatConfig::isEnableDeathMessages).orElse(false);
        }
    }
}

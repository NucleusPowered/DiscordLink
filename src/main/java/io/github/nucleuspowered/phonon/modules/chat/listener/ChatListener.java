package io.github.nucleuspowered.phonon.modules.chat.listener;

import com.google.inject.Inject;
import io.github.nucleuspowered.phonon.Phonon;
import io.github.nucleuspowered.phonon.discord.DiscordBot;
import io.github.nucleuspowered.phonon.internal.listener.ListenerBase;
import io.github.nucleuspowered.phonon.modules.chat.ChatModule;
import io.github.nucleuspowered.phonon.modules.chat.PhotonChannel;
import io.github.nucleuspowered.phonon.modules.chat.config.ChatConfig;
import io.github.nucleuspowered.phonon.modules.chat.config.ChatConfigAdapter;
import io.github.nucleuspowered.phonon.modules.core.CoreModule;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.message.MessageChannelEvent;

public class ChatListener extends ListenerBase {

    @Inject DiscordBot bot;

    @Listener(order = Order.LAST)
    public void onMinecraftChat(MessageChannelEvent.Chat event) {

        if (event.isMessageCancelled() || !event.getChannel().isPresent()) {
            return;
        }

        plugin.getConfigValue(ChatModule.ID, ChatConfigAdapter.class, ChatConfig::getChannels).ifPresent(channelConfigs -> {
            if (event.getChannel().get() instanceof PhotonChannel) {
                PhotonChannel channel = (PhotonChannel) event.getChannel().get();
                channelConfigs.stream()
                    .filter(channelConfig -> channelConfig.getMinecraftChannelName().equals(channel.getName().toPlain()))
                    .forEach(x -> bot.sendMessageToDiscord(event.getMessage().toPlain(), channel.getChannel()));
            } else {
                channelConfigs.stream()
                    .filter(channelConfig -> channelConfig.getMinecraftChannelName().equalsIgnoreCase("global"))
                    .forEach(channelConfig -> bot.getChannelById(channelConfig.getDiscordChannelId())
                        .ifPresent(c -> bot.sendMessageToDiscord(event.getMessage().toPlain(), c)));
            }
        });
    }
}

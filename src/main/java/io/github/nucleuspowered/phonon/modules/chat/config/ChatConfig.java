package io.github.nucleuspowered.phonon.modules.chat.config;

import com.google.common.collect.Lists;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class ChatConfig {

    @Setting List<ChannelConfig> channels = Lists.newArrayList();
    @Setting private boolean enableAchievementMessages = false;
    @Setting private boolean enableDeathMessages = false;

    public List<ChannelConfig> getChannels() {
        return channels;
    }

    public boolean isEnableAchievementMessages() {
        return enableAchievementMessages;
    }

    public boolean isEnableDeathMessages() {
        return enableDeathMessages;
    }
}

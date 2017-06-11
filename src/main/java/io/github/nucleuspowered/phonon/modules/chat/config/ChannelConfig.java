package io.github.nucleuspowered.phonon.modules.chat.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.apache.commons.lang3.StringUtils;

@ConfigSerializable
public class ChannelConfig {

    @Setting private String discordChannelId = "";
    @Setting private String minecraftChannelName = "";
    @Setting private String achievementMessageTemplate;
    @Setting private String deathMessageTemplate;

    public String getDiscordChannelId() {
        return discordChannelId;
    }

    public String getMinecraftChannelName() {
        return minecraftChannelName;
    }

    public boolean enableAchievementMessages() {
        return StringUtils.isNotBlank(achievementMessageTemplate);
    }

    public String getAchievementMessageTemplate() {
        return achievementMessageTemplate;
    }

    public boolean enableDeathMessages() {
        return StringUtils.isNotBlank(deathMessageTemplate);
    }

    public String getDeathMessageTemplate() {
        return deathMessageTemplate;
    }
}

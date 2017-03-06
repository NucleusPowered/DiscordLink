package io.github.nucleuspowered.phonon.modules.core.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

/**
 * The core section to the main config file.
 */
@ConfigSerializable
public class CoreConfig {

    @Setting("bot-name")
    private String name = "Phonon";

    @Setting("discord-token")
    private String discordToken = "";

    public String getName() {
        return name;
    }

    public String getDiscordToken() {
        return discordToken;
    }
}

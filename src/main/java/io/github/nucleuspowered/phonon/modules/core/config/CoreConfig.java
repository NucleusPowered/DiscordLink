package io.github.nucleuspowered.phonon.modules.core.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

/**
 * The core section to the main config file.
 */
@ConfigSerializable
public class CoreConfig {

    @Setting("bot-game")
    private String game = "";

    @Setting("bot-token")
    private String token = "";

    @Setting("bot-command-prefix")
    private String prefix = "!";

    public String getGame() {
        return game;
    }

    public String getToken() {
        return token;
    }

    public String getPrefix() {
        return prefix;
    }
}

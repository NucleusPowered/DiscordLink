package io.github.nucleuspowered.phonon.modules.command.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class CommandConfig {

    @Setting
    public String commandChannel = "";

}

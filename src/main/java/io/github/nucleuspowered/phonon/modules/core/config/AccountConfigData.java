package io.github.nucleuspowered.phonon.modules.core.config;

import io.github.nucleuspowered.phonon.internal.configurate.AbstractConfig;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ConfigSerializable
public class AccountConfigData extends AbstractConfig {

    @Setting
    public Map<String, UUID> accounts = new HashMap<>();

}

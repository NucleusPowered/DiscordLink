package io.github.nucleuspowered.phonon.modules.core.config;

import io.github.nucleuspowered.phonon.internal.configurate.BaseConfig;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ConfigSerializable
public class AccountConfigData extends BaseConfig {

    @Setting
    private Map<String, UUID> accounts = new HashMap<>();

    public Map<String, UUID> getAccounts() {
        return this.accounts;
    }
}

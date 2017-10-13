package io.github.nucleuspowered.phonon.internal.configurate;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;

public class BaseConfig<T extends ConfigurationLoader<K>, K extends ConfigurationNode> {

    private T loader;
    private K node;
    private TypeToken<BaseConfig> type;

    public void init(T loader, K node, TypeToken<BaseConfig> token) {
        this.loader = loader;
        this.node = node;
        this.type = token;
    }

    public void save() {
        try {
            this.node.setValue(this.type, this);
            this.loader.save(this.node);
        } catch (IOException | ObjectMappingException e) {
            e.printStackTrace();
        }
    }

    public T getLoader() {
        return this.loader;
    }

    public K getNode() {
        return this.node;
    }

    public TypeToken<BaseConfig> getType() {
        return this.type;
    }
}

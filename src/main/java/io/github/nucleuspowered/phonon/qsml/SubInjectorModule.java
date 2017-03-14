package io.github.nucleuspowered.phonon.qsml;

import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;

import java.util.Map;
import java.util.function.Supplier;

/**
 * This {@link SubInjectorModule} allows for types to be registered dynamically.
 */
public class SubInjectorModule extends AbstractModule {

    private final Map<Class<?>, Supplier<?>> bindings = Maps.newHashMap();

    public <T> boolean addBinding(Class<T> clazz, Supplier<T> supplier) {
        if (!bindings.containsKey(clazz)) {
            bindings.put(clazz, supplier);
            return true;
        }

        return false;
    }

    public void reset() {
        bindings.clear();
    }

    @Override protected void configure() {
        bindings.keySet().forEach(this::get);
    }

    // We know it's of the casted type.
    @SuppressWarnings("unchecked")
    private <T> void get(Class<T> key) {
        bind(key).toProvider(() -> (T)bindings.get(key));
    }
}

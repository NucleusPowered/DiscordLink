package io.github.nucleuspowered.phonon.qsml;

import com.google.common.collect.Maps;
import io.github.nucleuspowered.phonon.Phonon;

import java.util.Map;
import java.util.function.Supplier;

/**
 * This {@link SubInjectorModule} allows for types to be registered dynamically.
 */
public class SubInjectorModule extends InjectorModule {

    private final Map<Class<?>, Supplier<?>> bindings = Maps.newHashMap();

    public SubInjectorModule(Phonon phonon) {
        super(phonon);
    }

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
        super.configure();

        bindings.keySet().forEach(this::get);
    }

    // We know it's of the casted type.
    @SuppressWarnings("unchecked")
    private <T> void get(Class<T> key) {
        bind(key).toProvider(() -> (T)bindings.get(key));
    }
}

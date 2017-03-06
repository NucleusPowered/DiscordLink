package io.github.nucleuspowered.phonon.qsml;

import com.google.inject.AbstractModule;
import io.github.nucleuspowered.phonon.Phonon;

public class InjectorModule extends AbstractModule {

    private final Phonon phonon;

    public InjectorModule(Phonon phonon) {
        this.phonon = phonon;
    }

    @Override protected void configure() {
        bind(Phonon.class).toInstance(this.phonon);
    }
}

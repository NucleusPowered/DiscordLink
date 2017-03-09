package io.github.nucleuspowered.phonon.qsml;

import com.google.inject.AbstractModule;
import io.github.nucleuspowered.phonon.Phonon;
import io.github.nucleuspowered.phonon.internal.command.PhononCommand;

public class InjectorModule extends AbstractModule {

    private final Phonon phonon;
    private final PhononCommand phononCommand;

    public InjectorModule(Phonon phonon, PhononCommand command) {
        this.phonon = phonon;
        this.phononCommand = command;
    }

    @Override protected void configure() {
        bind(Phonon.class).toInstance(this.phonon);
        bind(PhononCommand.class).toInstance(this.phononCommand);
    }
}

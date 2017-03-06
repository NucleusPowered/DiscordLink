package io.github.nucleuspowered.phonon.qsml.modulespec;

import com.google.inject.Inject;
import io.github.nucleuspowered.phonon.Phonon;
import uk.co.drnaylor.quickstart.Module;

/**
 * A module that has no configuration.
 */
public abstract class StandardModule implements Module {

    @Inject private Phonon phononPlugin;

    protected Phonon getPhononPlugin() {
        return this.phononPlugin;
    }

    @Override public final void onEnable() {
        // Any classes can make use of the injector.
        // This section will be used to do any common tasks, such as scan for commands/listeners/hooks/whatever
        // getPhononPlugin().getPhononInjector().getInstance(Class)
        // getPhononPlugin().getPhononInjector().injectMembers(InstantiatedObject);
        phononEnable();
    }

    public void phononEnable() {}
}

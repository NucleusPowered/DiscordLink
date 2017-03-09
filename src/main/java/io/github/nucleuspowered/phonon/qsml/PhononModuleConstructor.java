package io.github.nucleuspowered.phonon.qsml;

import io.github.nucleuspowered.phonon.Phonon;
import uk.co.drnaylor.quickstart.Module;
import uk.co.drnaylor.quickstart.exceptions.QuickStartModuleLoaderException;
import uk.co.drnaylor.quickstart.loaders.ModuleConstructor;

/**
 * Constructs {@link Module}s
 */
public class PhononModuleConstructor implements ModuleConstructor {

    private final Phonon phonon;

    public PhononModuleConstructor(Phonon phonon) {
        this.phonon = phonon;
    }

    /**
     * Instantiates modules
     *
     * @param moduleClass The {@link Class} of the module to load.
     * @return The module
     * @throws QuickStartModuleLoaderException.Construction if there was a failure in instantiation.
     */
    @Override
    public Module constructModule(Class<? extends Module> moduleClass) throws QuickStartModuleLoaderException.Construction {
        return phonon.getPhononInjector().getInstance(moduleClass);
    }
}

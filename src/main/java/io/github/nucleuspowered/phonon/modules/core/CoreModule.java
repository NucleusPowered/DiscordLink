package io.github.nucleuspowered.phonon.modules.core;

import io.github.nucleuspowered.phonon.modules.core.config.CoreConfigAdapter;
import io.github.nucleuspowered.phonon.qsml.modulespec.ConfigurableModule;
import uk.co.drnaylor.quickstart.annotations.ModuleData;

@ModuleData(id = CoreModule.ID, name = "Core", isRequired = true)
public class CoreModule extends ConfigurableModule<CoreConfigAdapter> {

    public static final String ID = "core";

    @Override protected CoreConfigAdapter createConfigAdapter() {
        return new CoreConfigAdapter();
    }

    @Override public void phononEnable() {
        super.phononEnable();
        this.getBot().onEnable(getPhononPlugin());
    }
}

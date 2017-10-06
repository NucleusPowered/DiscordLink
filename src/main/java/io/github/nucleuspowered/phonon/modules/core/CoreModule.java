package io.github.nucleuspowered.phonon.modules.core;

import io.github.nucleuspowered.phonon.modules.core.config.AccountConfigData;
import io.github.nucleuspowered.phonon.modules.core.config.CoreConfigAdapter;
import io.github.nucleuspowered.phonon.qsml.modulespec.ConfigurableModule;
import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import uk.co.drnaylor.quickstart.annotations.ModuleData;

import java.nio.file.Path;

@ModuleData(id = CoreModule.ID, name = "Core", isRequired = true)
public class CoreModule extends ConfigurableModule<CoreConfigAdapter> {

    public static final String ID = "core";

    @Override protected CoreConfigAdapter createConfigAdapter() {
        return new CoreConfigAdapter();
    }

    @Override public void phononEnable() {
        super.phononEnable();
        this.getBot().onEnable(getPhononPlugin());
        Path path = this.getPhononPlugin().getConfigDir().resolve("account-data.json");
        AccountConfigData data = this.getPhononPlugin().getConfig(path, AccountConfigData.class,
                GsonConfigurationLoader.builder().setPath(path).build());
        this.getPhononPlugin().getAllConfigs().put(AccountConfigData.class, data);
    }
}

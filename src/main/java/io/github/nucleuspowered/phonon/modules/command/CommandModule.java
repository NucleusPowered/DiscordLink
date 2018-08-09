package io.github.nucleuspowered.phonon.modules.command;

import io.github.nucleuspowered.phonon.modules.command.config.CommandConfigAdapter;
import io.github.nucleuspowered.phonon.qsml.modulespec.ConfigurableModule;
import uk.co.drnaylor.quickstart.annotations.ModuleData;

@ModuleData(id = CommandModule.ID, name = "Command")
public class CommandModule extends ConfigurableModule<CommandConfigAdapter> {

    public static final String ID = "command";

    @Override
    protected CommandConfigAdapter createConfigAdapter() {
        return new CommandConfigAdapter();
    }

    @Override
    public void phononEnable() {
        super.phononEnable();
    }
}

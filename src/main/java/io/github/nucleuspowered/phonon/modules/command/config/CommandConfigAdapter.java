package io.github.nucleuspowered.phonon.modules.command.config;

import uk.co.drnaylor.quickstart.config.TypedAbstractConfigAdapter;

public class CommandConfigAdapter extends TypedAbstractConfigAdapter.StandardWithSimpleDefault<CommandConfig> {

    public CommandConfigAdapter() {
        super(CommandConfig.class);
    }
}

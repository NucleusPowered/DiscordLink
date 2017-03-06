package io.github.nucleuspowered.phonon.modules.core.config;

import uk.co.drnaylor.quickstart.config.TypedAbstractConfigAdapter;

/**
 * Connects the main config file with the {@link CoreConfig} file.
 */
public class CoreConfigAdapter extends TypedAbstractConfigAdapter.StandardWithSimpleDefault<CoreConfig> {

    public CoreConfigAdapter() {
        // Required thanks to type erasure.
        super(CoreConfig.class);
    }
}

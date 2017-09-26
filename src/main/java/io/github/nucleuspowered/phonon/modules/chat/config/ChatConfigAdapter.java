package io.github.nucleuspowered.phonon.modules.chat.config;

import uk.co.drnaylor.quickstart.config.TypedAbstractConfigAdapter;

public class ChatConfigAdapter extends TypedAbstractConfigAdapter.StandardWithSimpleDefault<ChatConfig> {

    public ChatConfigAdapter() {
        super(ChatConfig.class);
    }
}

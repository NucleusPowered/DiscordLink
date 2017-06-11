package io.github.nucleuspowered.phonon.modules.chat;

import io.github.nucleuspowered.phonon.modules.chat.config.ChatConfigAdapter;
import io.github.nucleuspowered.phonon.qsml.modulespec.ConfigurableModule;
import uk.co.drnaylor.quickstart.annotations.ModuleData;

@ModuleData(id = ChatModule.ID, name = "Chat")
public class ChatModule extends ConfigurableModule<ChatConfigAdapter> {

    public static final String ID = "chat";

    @Override protected ChatConfigAdapter createConfigAdapter() {
        return new ChatConfigAdapter();
    }

    @Override public void phononEnable() {
        super.phononEnable();
    }
}

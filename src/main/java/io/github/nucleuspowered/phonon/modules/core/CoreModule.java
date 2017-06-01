package io.github.nucleuspowered.phonon.modules.core;

import io.github.nucleuspowered.phonon.discord.DiscordBot;
import io.github.nucleuspowered.phonon.modules.core.config.CoreConfig;
import io.github.nucleuspowered.phonon.modules.core.config.CoreConfigAdapter;
import io.github.nucleuspowered.phonon.qsml.modulespec.ConfigurableModule;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Icon;
import uk.co.drnaylor.quickstart.annotations.ModuleData;

import java.util.Optional;

@ModuleData(id = CoreModule.ID, name = "Core", isRequired = true)
public class CoreModule extends ConfigurableModule<CoreConfigAdapter> {

    public static final String ID = "core";

    @Override protected CoreConfigAdapter createConfigAdapter() {
        return new CoreConfigAdapter();
    }

    @Override public void phononEnable() {
        super.phononEnable();

        CoreConfig config = this.getAdapter().getNodeOrDefault();
        String token = config.getToken();
        DiscordBot bot = new DiscordBot(token);
        if (!config.getGame().isEmpty()) {
            bot.getJda().getPresence().setGame(Game.of(config.getGame()));
        }
    }
}

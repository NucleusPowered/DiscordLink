package io.github.nucleuspowered.phonon.modules.command.command.discord;

import com.google.inject.Inject;
import io.github.nucleuspowered.phonon.Phonon;
import io.github.nucleuspowered.phonon.discord.DiscordCommandSource;
import io.github.nucleuspowered.phonon.discord.command.BotCommand;
import io.github.nucleuspowered.phonon.discord.command.IDiscordCommand;
import io.github.nucleuspowered.phonon.modules.command.CommandModule;
import io.github.nucleuspowered.phonon.modules.command.config.CommandConfig;
import io.github.nucleuspowered.phonon.modules.command.config.CommandConfigAdapter;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.text.Text;

@BotCommand("execute")
public class ExecuteCommand implements IDiscordCommand {

    @Inject private Phonon plugin;

    @Override
    public CommandElement[] getArgs() {
        return new CommandElement[] {
                GenericArguments.remainingJoinedStrings(Text.of("arguments"))
        };
    }

    @Override
    public void execute(User user, CommandContext args, MessageChannel channel) {
        CommandConfig config = this.plugin.getConfigAdapter(CommandModule.ID, CommandConfigAdapter.class).get().getNodeOrDefault();
        if (channel.getId().equals(config.commandChannel)) {
            DiscordCommandSource source = new DiscordCommandSource(plugin, user, channel);
            String arguments = (String) args.getOne("arguments").get();
            Sponge.getCommandManager().process(source, arguments);
        }
    }
}

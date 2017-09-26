package io.github.nucleuspowered.phonon.discord.command;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;

public interface IDiscordCommand {

    CommandElement[] getArgs();

    void execute(User user, CommandContext args, MessageChannel channel);
}

package io.github.nucleuspowered.phonon.internal.command;

import com.google.inject.Inject;
import io.github.nucleuspowered.phonon.Phonon;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.spec.CommandExecutor;

public abstract class PhononSubcommand implements CommandExecutor {

    private static CommandElement[] empty = new CommandElement[0];
    @Inject private Phonon plugin;

    protected Phonon getPlugin() {
        return this.plugin;
    }

    public CommandElement[] getArguments() {
        return empty;
    }
}

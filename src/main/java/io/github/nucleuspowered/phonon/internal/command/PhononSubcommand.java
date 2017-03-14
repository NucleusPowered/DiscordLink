package io.github.nucleuspowered.phonon.internal.command;

import com.google.inject.Inject;
import io.github.nucleuspowered.phonon.Phonon;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.spec.CommandExecutor;

import java.util.Optional;

public abstract class PhononSubcommand implements CommandExecutor {

    private static CommandElement[] empty = new CommandElement[0];
    @Inject private Phonon plugin;

    protected abstract String[] getAliases();

    protected abstract Optional<String> getPermission();

    protected final Phonon getPlugin() {
        return this.plugin;
    }

    public CommandElement[] getArguments() {
        return empty;
    }
}

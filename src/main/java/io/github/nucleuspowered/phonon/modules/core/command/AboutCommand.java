package io.github.nucleuspowered.phonon.modules.core.command;

import io.github.nucleuspowered.phonon.internal.command.PhononSubcommand;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.annotation.NonnullByDefault;

import java.util.Optional;

@NonnullByDefault
public class AboutCommand extends PhononSubcommand {

    private final Text nameKey = Text.of("name");

    @Override protected String[] getAliases() {
        return new String[] { "about" };
    }

    @Override protected Optional<String> getPermission() {
        return Optional.of("phonon.about.base");
    }

    @Override public CommandElement[] getArguments() {
        return new CommandElement[] {
                GenericArguments.optionalWeak(GenericArguments.string(nameKey))
        };
    }

    @Override public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        src.sendMessage(Text.of("Hi ", args.<String>getOne(nameKey).orElse("Unknown")));
        return CommandResult.success();
    }
}

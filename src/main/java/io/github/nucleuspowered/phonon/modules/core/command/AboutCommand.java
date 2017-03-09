package io.github.nucleuspowered.phonon.modules.core.command;

import io.github.nucleuspowered.phonon.internal.command.Command;
import io.github.nucleuspowered.phonon.internal.command.PhononSubcommand;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.annotation.NonnullByDefault;

@NonnullByDefault
@Command(value = "about", permission = "phonon.about.base")
public class AboutCommand extends PhononSubcommand {

    private final Text nameKey = Text.of("name");

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

package io.github.nucleuspowered.phonon.internal.command;

import com.google.common.collect.Maps;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Tuple;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

public class PhononCommand implements CommandCallable {

    /**
     * The subcommands that this command will handle.
     */
    private final Map<String, CommandSpec> subCommands = Maps.newHashMap();

    public boolean registerSubCommand(PhononSubcommand subcommandToRegister) {
        // Work out how the sub command system will work - probably annotation based, but not sure yet.
        // By definition, all subcommands will be lower case
        return false;
    }

    /**
     * Removes the specified sub command type from the mapping.
     *
     * <p>
     *     This is a class, so we don't have to hold a reference to the actual command.
     * </p>
     *
     * @param subcommand The class of the subcommand to remove.
     * @return Whether a subcommand was removed.
     */
    public boolean removeSubCommand(Class<? extends PhononSubcommand> subcommand) {
        List<String> keysToRemove = subCommands.entrySet().stream()
                .filter(x -> x.getValue().getExecutor() instanceof PhononSubcommand)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (keysToRemove.isEmpty()) {
            return false;
        }

        keysToRemove.forEach(subCommands::remove);
        return true;
    }

    @Override public CommandResult process(CommandSource source, String arguments) throws CommandException {
        return CommandResult.empty();
    }

    @Override public List<String> getSuggestions(CommandSource source, String arguments, @Nullable Location<World> targetPosition)
            throws CommandException {
        // Get the last argument, use it for the subcommands
        final String arg = arguments.substring(arguments.lastIndexOf(" ")).toLowerCase();
        return subCommands.entrySet().stream().filter(x -> x.getKey().startsWith(arg))
                .filter(x -> x.getValue().testPermission(source))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override public boolean testPermission(CommandSource source) {
        // This is for the root command only. Should probably be changed.
        return source.hasPermission("phonon.command.base");
    }

    @Override public Optional<Text> getShortDescription(CommandSource source) {
        return Optional.empty();
    }

    @Override public Optional<Text> getHelp(CommandSource source) {
        return Optional.empty();
    }

    @Override public Text getUsage(CommandSource source) {
        return Text.EMPTY;
    }

    private Optional<Tuple<CommandSpec, String>> getSpec(String arg) {
        String[] args = arg.split(" ", 2);
        String commands = args[0];
        if (subCommands.containsKey(commands.toLowerCase())) {
            String remainingArgs = args.length == 2 ? args[1] : "";
            return Optional.of(Tuple.of(subCommands.get(commands.toLowerCase()), remainingArgs));
        }

        return Optional.empty();
    }
}

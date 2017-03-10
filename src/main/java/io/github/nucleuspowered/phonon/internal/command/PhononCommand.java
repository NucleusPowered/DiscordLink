package io.github.nucleuspowered.phonon.internal.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandPermissionException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.parsing.InputTokenizer;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

@NonnullByDefault
public final class PhononCommand implements CommandCallable {

    private InputTokenizer tokenizer = InputTokenizer.quotedStrings(false);

    /**
     * The subcommands that this command will handle.
     */
    private final Map<String, CommandSpec> subCommands = Maps.newHashMap();

    /**
     * Registers a {@link PhononSubcommand}
     *
     * @param subcommandToRegister The subcommand to register.
     * @return <code>true</code> if successful.
     */
    public boolean registerSubCommand(PhononSubcommand subcommandToRegister) {
        // Work out how the sub command system will work - probably annotation based, but not sure yet.
        // By definition, all subcommands will be lower case

        // Register the command.
        Collection<String> sc = Arrays.asList(subcommandToRegister.getAliases());
        if (subCommands.keySet().stream().map(String::toLowerCase).noneMatch(sc::contains)) {
            // We can register the aliases. Create the CommandSpec
            // We might want to add descriptions in.
            CommandSpec.Builder specbuilder = CommandSpec.builder();
            subcommandToRegister.getPermission().ifPresent(specbuilder::permission);
            CommandSpec spec = specbuilder
                .arguments(subcommandToRegister.getArguments())
                .executor(subcommandToRegister)
                .build();

            sc.forEach(x -> subCommands.put(x.toLowerCase(), spec));
            return true;
        }

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
        Collection<String> keysToRemove = subCommands.entrySet().stream()
                .filter(x -> x.getValue().getExecutor() instanceof PhononSubcommand)
                .map(Map.Entry::getKey).collect(Collectors.toList());

        if (keysToRemove.isEmpty()) {
            return false;
        }

        keysToRemove.forEach(subCommands::remove);
        return true;
    }

    @Override public CommandResult process(CommandSource source, String arguments) throws CommandException {
        // Get the first argument, is it a child?
        final CommandArgs args = new CommandArgs(arguments, tokenizer.tokenize(arguments, false));

        Optional<CommandSpec> optionalSpec = getSpec(args);
        if (optionalSpec.isPresent()) {
            CommandSpec spec = optionalSpec.get();
            CommandContext context = new CommandContext();
            spec.checkPermission(source);
            spec.populateContext(source, args, context);
            return spec.getExecutor().execute(source, context);
        }

        if (testPermission(source)) {
            // Else, what do we want to do here?
            source.sendMessage(Text.of("Phonon from Nucleus."));
            return CommandResult.success();
        }

        throw new CommandPermissionException();
    }

    private String getFirst(CommandArgs a) {
        try {
            return a.peek().toLowerCase();
        } catch (Exception e) {
            return "";
        }
    }

    @Override public List<String> getSuggestions(CommandSource source, String arguments, @Nullable Location<World> targetPosition)
            throws CommandException {
        final CommandArgs args = new CommandArgs(arguments, tokenizer.tokenize(arguments, false));
        final String firstArg;

        try {
            firstArg = args.peek().toLowerCase();
        } catch (Exception e) {
            return Lists.newArrayList(subCommands.keySet());
        }

        try {
            Optional<CommandSpec> optionalSpec = getSpec(args);
            if (optionalSpec.isPresent()) {
                CommandSpec spec = optionalSpec.get();
                CommandContext context = new CommandContext();
                spec.checkPermission(source);
                return spec.complete(source, args, context);
            }
        } catch (Exception e) {
            // ignored - most likely not a sub command.
        }

        // Only if this is the first arg.
        if (args.getAll().size() == 1) {
            return subCommands.entrySet().stream().filter(x -> x.getKey().startsWith(firstArg))
                    .filter(x -> x.getValue().testPermission(source))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }

        return Lists.newArrayList();
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

    private Optional<CommandSpec> getSpec(CommandArgs args) throws CommandException {
        if (args.hasNext()) {
            String child = args.next().toLowerCase();
            if (subCommands.containsKey(child)) {
                // Try to execute it.
                final CommandContext context = new CommandContext();
                return Optional.of(subCommands.get(child));
            }

            // Just temporary.
            throw new CommandException(Text.of(TextColors.RED, child, " is not a valid subcommand!"), true);
        }

        return Optional.empty();
    }
}

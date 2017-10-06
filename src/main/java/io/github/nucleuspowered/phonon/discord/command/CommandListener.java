package io.github.nucleuspowered.phonon.discord.command;

import static org.spongepowered.api.util.SpongeApiTranslationHelper.t;

import io.github.nucleuspowered.phonon.Phonon;
import io.github.nucleuspowered.phonon.discord.DiscordCommandSource;
import io.github.nucleuspowered.phonon.modules.core.CoreModule;
import io.github.nucleuspowered.phonon.modules.core.config.CoreConfig;
import io.github.nucleuspowered.phonon.modules.core.config.CoreConfigAdapter;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.args.parsing.InputTokenizer;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandListener extends ListenerAdapter {

    private final Phonon phononPlugin;
    private Map<String, IDiscordCommand> aliases;

    @SuppressWarnings("unchecked")
    public CommandListener(Phonon phononPlugin) {
        this.phononPlugin = phononPlugin;
        List<Class<? extends IDiscordCommand>> commands = this.phononPlugin.getModuleContainer().getLoadedClasses().stream()
                .filter(IDiscordCommand.class::isAssignableFrom)
                .filter(x -> !Modifier.isAbstract(x.getModifiers()) && !Modifier.isInterface(x.getModifiers()))
                .map(x -> (Class<? extends IDiscordCommand>) x)
                .collect(Collectors.toList());

        this.aliases = new HashMap<>();
        commands.forEach(commandClass -> {
            IDiscordCommand command = this.phononPlugin.getPhononInjector().getInstance(commandClass);
            BotCommand annotation = commandClass.getAnnotation(BotCommand.class);

            if (annotation == null) {
                return;
            }
            for (String alias : annotation.value()) {
                aliases.put(alias, command);
            }
        });
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        CoreConfig config = this.phononPlugin.getConfigAdapter(CoreModule.ID, CoreConfigAdapter.class).get().getNodeOrDefault();

        if (event.getMessage().getRawContent().startsWith(config.getPrefix()) && !event.getMessage().getAuthor().isBot()) {
            DiscordCommandSource source = new DiscordCommandSource(event.getAuthor(), event.getChannel());

            int end = event.getMessage().getRawContent().indexOf(" ");
            String commandName;
            String arguments;
            if (end == -1) {
                commandName = event.getMessage().getRawContent().substring(1);
                arguments = "";
            } else {
                commandName = event.getMessage().getRawContent().substring(1, end);
                arguments = event.getMessage().getRawContent().substring(end + 1);
            }


            if (aliases.containsKey(commandName)) {
                IDiscordCommand command = aliases.get(commandName);
                CommandElement args= null;
                try {
                    CommandArgs commandArgs = new CommandArgs(arguments,
                            InputTokenizer.quotedStrings(false).tokenize(arguments, true));
                    CommandContext context = new CommandContext();
                    args = GenericArguments.seq(command.getArgs());
                    args.parse(source, commandArgs, context);
                    if (commandArgs.hasNext()) {
                        commandArgs.next();
                        throw commandArgs.createError(t("Invalid arguments!"));
                    }
                    command.execute(event.getAuthor(), context, event.getChannel());
                } catch (ArgumentParseException e) {
                    String message = "Invalid Arguments";
                    if (e.getMessage() != null) {
                        message = e.getMessage();
                    }

                    if (args == null) {
                        event.getChannel().sendMessage(message).queue();
                    } else {
                        event.getChannel().sendMessage(message + System.lineSeparator() +
                                config.getPrefix() + commandName + args.getUsage(source).toPlain()).queue();
                    }
                }

                if (!(event.getChannel() instanceof PrivateChannel)) {
                    event.getMessage().delete().queue();
                }
            }
        }
    }

}

package io.github.nucleuspowered.phonon.discord.command;

import static org.spongepowered.api.util.SpongeApiTranslationHelper.t;

import io.github.nucleuspowered.phonon.Phonon;
import io.github.nucleuspowered.phonon.discord.DiscordCommandSource;
import io.github.nucleuspowered.phonon.modules.core.CoreModule;
import io.github.nucleuspowered.phonon.modules.core.config.CoreConfig;
import io.github.nucleuspowered.phonon.modules.core.config.CoreConfigAdapter;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.args.parsing.InputTokenizer;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;

public class CommandListener extends ListenerAdapter {

    private Phonon phononPlugin;
    private CommandElement args;

    public CommandListener(Phonon phononPlugin) {
        this.phononPlugin = phononPlugin;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        CoreConfig config = this.phononPlugin.getConfigAdapter(CoreModule.ID, CoreConfigAdapter.class).get().getNodeOrDefault();
        if (event.getMessage().getRawContent().startsWith(config.getPrefix()) && !event.getMessage().getAuthor().isBot() &&
                event.getTextChannel() != null) {
            DiscordCommandSource source = new DiscordCommandSource(event.getAuthor(), event.getTextChannel());

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

            @SuppressWarnings("unchecked")
            List<Class<? extends IDiscordCommand>> commands = this.phononPlugin.getModuleContainer().getLoadedClasses().stream()

                    .filter(IDiscordCommand.class::isAssignableFrom)
                    .filter(x -> !Modifier.isAbstract(x.getModifiers()) && !Modifier.isInterface(x.getModifiers()))
                    .map(x -> (Class<? extends IDiscordCommand>)x)
                    .collect(Collectors.toList());

            commands.forEach(commandClass -> {
                IDiscordCommand command = this.phononPlugin.getPhononInjector().getInstance(commandClass);
                BotCommand annotation = commandClass.getAnnotation(BotCommand.class);

                if (annotation == null) {
                    return;
                }

                for (String alias : annotation.value()) {
                    if (alias.equals(commandName)) {
                        try {
                            CommandArgs commandArgs = new CommandArgs(arguments,
                                    InputTokenizer.quotedStrings(false).tokenize(arguments, true));
                            CommandContext context = new CommandContext();
                            this.args = GenericArguments.seq(command.getArgs());
                            this.args.parse(source, commandArgs, context);
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

                            event.getChannel().sendMessage(message + System.lineSeparator() +
                                    config.getPrefix() + commandName + this.args.getUsage(source).toPlain()).queue();
                        }
                    }
                }
            });
        }
    }

}

package io.github.nucleuspowered.phonon.discord.command.arguments;

import io.github.nucleuspowered.phonon.discord.DiscordBot;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class UserArgument extends CommandElement {

    private DiscordBot bot;

    public UserArgument(@Nullable Text key, DiscordBot bot) {
        super(key);
        this.bot = bot;
    }

    @Override
    protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        if (args.hasNext()) {
            String arg = args.next();

            if (arg.matches("<@!?([0-9]+)>")) {
                String id = arg.replaceAll("[@!<>]", "");
                if (id.contains("!")) {
                    id = id.replace("!", "");
                }
                return bot.getJda().getUserById(id);
            }

            if (bot.getJda().getUsersByName(arg, true).size() == 1) {
                return bot.getJda().getUsersByName(arg, true).get(0);
            } else {
                String name = arg.substring(0, arg.length() - 5);
                String discriminator = arg.substring(arg.length() - 5);
                String symbol = discriminator.substring(0, 1);

                if (symbol.equals("#")) {
                    return bot.getUserByDiscriminator(name, discriminator.substring(1))
                            .orElseThrow(() -> args.createError(Text.of(TextColors.RED, "User not found!")));
                }
            }
        }
        throw args.createError(Text.of(TextColors.RED, "User not found!"));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        return new ArrayList<>();
    }
}

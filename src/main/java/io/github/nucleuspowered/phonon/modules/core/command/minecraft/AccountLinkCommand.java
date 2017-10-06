package io.github.nucleuspowered.phonon.modules.core.command.minecraft;

import com.google.inject.Inject;
import io.github.nucleuspowered.phonon.discord.DiscordBot;
import io.github.nucleuspowered.phonon.internal.command.PhononSubcommand;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.security.SecureRandom;
import java.util.Optional;

public class AccountLinkCommand extends PhononSubcommand {

    @Inject private DiscordBot bot;

    @Override
    protected String[] getAliases() {
        return new String[] {"accountlink"};
    }

    @Override
    protected Optional<String> getPermission() {
        return Optional.of("phonon.accountlink.base");
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            throw new CommandException(Text.of("You must be a player to use this command!"));
        }

        Player player = (Player) src;

        String code = generateCode(5);
        while (bot.getCodes().containsKey(code)) {
            code = generateCode(5);
        }
        bot.getCodes().put(code, player.getUniqueId());
        player.sendMessage(Text.of("Your code is: ", code, ". Send the code in a message to Phonon in discord in order to confirm the link."));

        return CommandResult.success();
    }

    private String generateCode(int codeLength) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < codeLength; i++) {
            sb.append(random.nextInt(9));
        }

        return sb.toString();
    }

}

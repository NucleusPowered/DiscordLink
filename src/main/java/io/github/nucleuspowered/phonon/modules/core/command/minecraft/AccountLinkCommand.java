package io.github.nucleuspowered.phonon.modules.core.command.minecraft;

import com.google.inject.Inject;
import io.github.nucleuspowered.phonon.Phonon;
import io.github.nucleuspowered.phonon.discord.DiscordBot;
import io.github.nucleuspowered.phonon.internal.command.PhononSubcommand;
import io.github.nucleuspowered.phonon.modules.core.config.AccountConfigData;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.security.SecureRandom;
import java.util.Optional;

public class AccountLinkCommand extends PhononSubcommand {

    @Inject private DiscordBot bot;
    @Inject private Phonon plugin;

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

        AccountConfigData data = (AccountConfigData) this.plugin.getAllConfigs().get(AccountConfigData.class);

        if (player.getSubjectData().getOptions(SubjectData.GLOBAL_CONTEXT).containsKey("discord-user")) {
            String id = player.getSubjectData().getOptions(SubjectData.GLOBAL_CONTEXT).get("discord-user");
            if (data.getAccounts().containsKey(id)) {
                throw new CommandException(Text.of("Your accounts are already linked!"));
            }
        }

        String code = generateCode(5);
        while (this.bot.getCodes().containsKey(code)) {
            code = generateCode(5);
        }
        this.bot.getCodes().put(code, player.getUniqueId());
        player.sendMessage(Text.of(
                TextColors.GREEN, "Your code is: ", TextColors.GOLD, code, TextColors.GREEN, ".", Text.NEW_LINE,
                TextColors.AQUA, "Use the command !confirmaccount <code> in a private message to the Phonon bot in Discord "
                        + "using your code in order to confirm the link."));

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

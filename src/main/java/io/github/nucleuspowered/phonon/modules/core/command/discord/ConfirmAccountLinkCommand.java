package io.github.nucleuspowered.phonon.modules.core.command.discord;

import com.google.inject.Inject;
import io.github.nucleuspowered.phonon.Phonon;
import io.github.nucleuspowered.phonon.discord.DiscordBot;
import io.github.nucleuspowered.phonon.discord.command.BotCommand;
import io.github.nucleuspowered.phonon.discord.command.IDiscordCommand;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;

@BotCommand("confirmaccount")
public class ConfirmAccountLinkCommand implements IDiscordCommand {

    @Inject private DiscordBot bot;
    @Inject private Phonon phonon;

    @Override
    public CommandElement[] getArgs() {
        return new CommandElement[] {
                GenericArguments.string(Text.of("code"))
        };
    }

    @Override
    public void execute(User user, CommandContext args, MessageChannel channel) {
        String code = (String) args.getOne("code").get();
        //link successful
        if (bot.getCodes().containsKey(code)) {
            org.spongepowered.api.entity.living.player.User spongeUser = Sponge.getServiceManager().provideUnchecked(UserStorageService.class)
                    .get(bot.getCodes().get(code)).get();
            spongeUser.getSubjectData().setOption(SubjectData.GLOBAL_CONTEXT, "discord-user", user.getId());

            //TODO Still need to provide a way to get the Minecraft player from Discord

            channel.sendMessage("You have successfully linked your account to " + spongeUser.getName()).queue();
        } else {
            channel.sendMessage("That is not a valid code!").queue();
        }

    }
}

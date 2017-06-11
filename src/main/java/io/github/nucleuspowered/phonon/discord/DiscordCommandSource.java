package io.github.nucleuspowered.phonon.discord;

import com.google.inject.Inject;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.util.Tristate;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class DiscordCommandSource implements CommandSource {

    @Inject DiscordBot bot;

    private final User user;
    private final TextChannel channel;

    public DiscordCommandSource(User user, TextChannel channel) {
        this.user = user;
        this.channel = channel;
    }

    @Override
    public String getName() {
        return channel.getGuild().getMember(user).getEffectiveName();
    }

    @Override
    public Optional<CommandSource> getCommandSource() {
        return Optional.of(this);
    }

    @Override
    public SubjectCollection getContainingCollection() {
        return Sponge.getGame().getServiceManager().provideUnchecked(PermissionService.class).getGroupSubjects();
    }

    @Override
    public String getIdentifier() {
        return user.getName();
    }

    @Override
    public void sendMessage(Text message) {
        bot.sendMessageToDiscord(message.toPlain(), channel);
    }

    @Override
    public MessageChannel getMessageChannel() {
        return MessageChannel.fixed(this);
    }

    @Override
    public void setMessageChannel(MessageChannel channel) {
        //NO OP
    }

    @Override
    public Tristate getPermissionValue(Set<Context> contexts, String permission) {
        return Tristate.TRUE;
    }

    @Override
    public SubjectData getSubjectData() {
        return null;
    }

    @Override
    public SubjectData getTransientSubjectData() {
        return null;
    }

    @Override
    public boolean isChildOf(Set<Context> contexts, Subject parent) {
        return false;
    }

    @Override
    public List<Subject> getParents(Set<Context> contexts) {
        return null;
    }

    @Override
    public Optional<String> getOption(Set<Context> contexts, String key) {
        return null;
    }

    @Override
    public Set<Context> getActiveContexts() {
        return null;
    }
}

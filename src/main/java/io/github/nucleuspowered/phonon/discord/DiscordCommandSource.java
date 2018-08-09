package io.github.nucleuspowered.phonon.discord;

import com.google.inject.Inject;
import io.github.nucleuspowered.phonon.Phonon;
import io.github.nucleuspowered.phonon.modules.core.config.AccountConfigData;
import net.dv8tion.jda.core.entities.User;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.service.permission.SubjectReference;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.util.Tristate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class DiscordCommandSource implements CommandSource {

    @Inject private DiscordBot bot;
    private Phonon plugin;

    private final User user;
    private final org.spongepowered.api.entity.living.player.User spongeUser;
    private final net.dv8tion.jda.core.entities.MessageChannel channel;

    public DiscordCommandSource(Phonon plugin, User user, net.dv8tion.jda.core.entities.MessageChannel channel) {
        this.plugin = plugin;
        this.user = user;
        AccountConfigData data = (AccountConfigData) this
                .plugin
                .getAllConfigs()
                .get(AccountConfigData.class);
        UUID uuid = data.getAccounts().get(this.user.getId());
        this.spongeUser = Sponge.getServiceManager().provideUnchecked(UserStorageService.class).get(uuid).get();
        this.channel = channel;
    }

    @Override
    public String getName() {
        return user.getName();
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
    public SubjectReference asSubjectReference() {
        return this.getContainingCollection().newSubjectReference(this.spongeUser.getIdentifier());
    }

    @Override
    public boolean isSubjectDataPersisted() {
        return false;
    }

    @Override
    public String getIdentifier() {
        return user.getId();
    }

    @Override
    public void sendMessage(Text message) {
        channel.sendMessage(message.toPlain()).queue();
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
        return this.spongeUser.getPermissionValue(contexts, permission);
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
    public boolean isChildOf(Set<Context> contexts, SubjectReference parent) {
        return false;
    }

    @Override
    public List<SubjectReference> getParents(Set<Context> contexts) {
        return new ArrayList<>();
    }

    @Override
    public Optional<String> getOption(Set<Context> contexts, String key) {
        return this.spongeUser.getOption(contexts, key);
    }

    @Override
    public Set<Context> getActiveContexts() {
        return this.spongeUser.getActiveContexts();
    }
}

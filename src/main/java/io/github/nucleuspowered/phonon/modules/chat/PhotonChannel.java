package io.github.nucleuspowered.phonon.modules.chat;

import com.google.common.collect.Sets;
import net.dv8tion.jda.core.entities.TextChannel;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MessageReceiver;

import java.util.Collection;
import java.util.Set;

public class PhotonChannel implements MessageChannel {

    private Text name;
    private TextChannel channel;
    private Set<MessageReceiver> members;

    public PhotonChannel(Text name, TextChannel channel) {
        this.name = name;
        this.channel = channel;
        this.members = Sets.newHashSet();
    }

    public Text getName() {
        return name;
    }

    public void setName(Text name) {
        this.name = name;
    }

    public TextChannel getChannel() {
        return channel;
    }

    @Override public Collection<MessageReceiver> getMembers() {
        return members;
    }
}

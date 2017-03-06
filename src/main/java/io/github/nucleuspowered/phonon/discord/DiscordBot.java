package io.github.nucleuspowered.phonon.discord;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class DiscordBot {
    private static DiscordBot instance;

    private JDA jda;

    public DiscordBot(String token) {
        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(token)
                    .setAudioEnabled(false)
                    .setAutoReconnect(true)
                    .setEnableShutdownHook(true)
                    .buildAsync();
        } catch (LoginException | RateLimitedException e) {
            e.printStackTrace();
        }

        instance = this;
    }

    public JDA getJda() {
        return jda;
    }

    public Optional<TextChannel> getChannelById(String id) {
        return Optional.ofNullable(getJda().getTextChannelById(id));
    }

    public Optional<List<TextChannel>> getChannelsByGuildId(String guildID) {
        return Optional.ofNullable(getJda().getGuildById(guildID).getTextChannels());
    }

    public MessageEmbed.Field getEmbedField(String title, String description, boolean inline) {
        return new MessageEmbed.Field(title, description, inline);
    }

    public MessageEmbed getEmbed(Color color, String title, String url, CharSequence description, MessageEmbed.Field... fields) {
        EmbedBuilder e = new EmbedBuilder();
        e.setColor(color);
        for (MessageEmbed.Field field : fields) {
            e.addField(field);
        }
        e.setTitle(title, url);
        e.setDescription(description);
        return e.build();
    }

    public MessageEmbed getEmbedWithThumbnail(Color color, String title, String url, CharSequence description, String thumbnailUrl, MessageEmbed.Field... fields) {
        MessageEmbed e = getEmbed(color, title, url, description, fields);
        EmbedBuilder ee = new EmbedBuilder(e);
        ee.setThumbnail(thumbnailUrl);
        return ee.build();
    }

    public MessageEmbed getEmbedWithThumbnailAndFooter(Color color, String title, String url, CharSequence description, String thumbnailUrl, String footerUrl, String footerText, MessageEmbed.Field... fields) {
        MessageEmbed e = getEmbedWithThumbnail(color, title, url, description, thumbnailUrl, fields);
        EmbedBuilder ee = new EmbedBuilder(e);
        ee.setFooter(footerText, footerUrl);
        return ee.build();
    }

    public MessageEmbed addImage(MessageEmbed embed, String url) {
        return new EmbedBuilder(embed).setImage(url).build();
    }

    public void sendEmbedToDiscord(MessageEmbed embed, TextChannel channel) {
        channel.sendMessage(embed).submit();
    }

    public void sendMessageToDiscord(String message, TextChannel channel) {
        channel.sendMessage(message).submit();
    }

    /**
     * Used to get an active discord invite from a specific guild.
     * @param guildID the guild id
     */
    public Optional<Object> getActiveDiscordInvite(String guildID) {
        List<Invite> invites = getJda().getGuildById(guildID).getInvites().complete();
        for (Invite invite : invites) {
            if (!invite.isTemporary() && invite.getUses() < invite.getMaxUses()) return Optional.of(invite);
        }
        return Optional.empty();
    }

    public Optional<Guild> getGuild(String id) {
        return Optional.ofNullable(getJda().getGuildById(id));
    }

    public static DiscordBot getInstance() {
        return instance;
    }
}

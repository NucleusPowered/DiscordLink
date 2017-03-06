package io.github.nucleuspowered.phonon.discord;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.requests.RestAction;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class DiscordBot {
    static JDA jda;

    //currently just doing static for easy access can be changed later
    //we can also do
    public static void init(String token) {
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
    }

    public static JDA getJda() {
        return jda;
    }

    public static TextChannel getChannelById(String id) {
        return getJda().getTextChannelById(id);
    }

    public static List<TextChannel> getChannelsByGuildId(String guildID) {
        return getJda().getGuildById(guildID).getTextChannels();
    }

    public static MessageEmbed.Field getEmbedField(String title, String description, boolean inline) {
        return new MessageEmbed.Field(title, description, inline);
    }

    public static MessageEmbed getEmbed(Color color, String title, String url, CharSequence description, MessageEmbed.Field... fields) {
        EmbedBuilder e = new EmbedBuilder();
        e.setColor(color);
        for (MessageEmbed.Field field : fields) {
            e.addField(field);
        }
        e.setTitle(title, url);
        e.setDescription(description);
        return e.build();
    }

    public static MessageEmbed getEmbedWithThumbnail(Color color, String title, String url, CharSequence description, String thumbnailUrl, MessageEmbed.Field... fields) {
        MessageEmbed e = getEmbed(color, title, url, description, fields);
        EmbedBuilder ee = new EmbedBuilder(e);
        ee.setThumbnail(thumbnailUrl);
        return ee.build();
    }

    public static MessageEmbed getEmbedWithThumbnailAndFooter(Color color, String title, String url, CharSequence description, String thumbnailUrl, String footerUrl, String footerText, MessageEmbed.Field... fields) {
        MessageEmbed e = getEmbedWithThumbnail(color, title, url, description, thumbnailUrl, fields);
        EmbedBuilder ee = new EmbedBuilder(e);
        ee.setFooter(footerText, footerUrl);
        return ee.build();
    }

    public static MessageEmbed addImage(MessageEmbed embed, String url) {
        return new EmbedBuilder(embed).setImage(url).build();
    }

    public static void sendEmbedToDiscord(MessageEmbed embed, TextChannel channel) {
        channel.sendMessage(embed).submit();
    }

    public static void sendMessageToDiscord(String message, TextChannel channel) {
        channel.sendMessage(message).submit();
    }

    public static RestAction<Message> prepareToSendEmbed(MessageEmbed embed, TextChannel channel) {
        return channel.sendMessage(embed);
    }

    /**
     * Used to get an active discord invite from a specific guild.
     * @param guildID the guild id
     */
    public static Optional<Object> getActiveDiscordInvite(String guildID) {
        List<Invite> invites = getJda().getGuildById(guildID).getInvites().complete();
        for (Invite invite : invites) {
            if (!invite.isTemporary() && invite.getUses() < invite.getMaxUses()) return Optional.of(invite);
        }
        return Optional.empty();
    }

    public static Guild getGuild(String id) {
        return getJda().getGuildById(id);
    }

}

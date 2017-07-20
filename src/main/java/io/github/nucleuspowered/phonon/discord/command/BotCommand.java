package io.github.nucleuspowered.phonon.discord.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  Classes with this annotation are to be registered as a discord bot command
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BotCommand {

    /**
     * Aliases for this command
     *
     * @return The command aliases
     */
    String[] value();

}

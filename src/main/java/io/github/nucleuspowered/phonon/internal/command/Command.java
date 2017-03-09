package io.github.nucleuspowered.phonon.internal.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that this is a command to be registered under /phonon.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {

    /**
     * The aliases to register.
     *
     * @return The command aliases.
     */
    String[] value();

    /**
     * The required permission for this subcommand.
     *
     * @return The permission.
     */
    String permission() default "";
}

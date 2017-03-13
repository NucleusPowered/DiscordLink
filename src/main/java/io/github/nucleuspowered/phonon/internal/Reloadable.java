package io.github.nucleuspowered.phonon.internal;

/**
 * Marks a command or listener as reloadable - the {@link #onReload()} method will be called whenever the plugin config is reloaded.
 */
public interface Reloadable {

    /**
     * Fired when the Phonon plugin configuration is reloaded.
     */
    void onReload();
}

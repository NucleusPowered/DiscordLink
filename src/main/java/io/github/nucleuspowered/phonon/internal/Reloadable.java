package io.github.nucleuspowered.phonon.internal;

/**
 * Marks a command or listener are reloadable.
 */
public interface Reloadable {

    /**
     * Fired when Phonon is reloaded.
     */
    void onReload();
}

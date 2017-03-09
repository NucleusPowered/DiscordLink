package io.github.nucleuspowered.phonon.internal.listener;

import io.github.nucleuspowered.phonon.Phonon;

import java.util.function.Predicate;

public @interface ConditionalListener {

    Class<? extends Predicate<Phonon>> value();
}

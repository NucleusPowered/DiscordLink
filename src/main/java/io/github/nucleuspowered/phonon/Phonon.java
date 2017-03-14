package io.github.nucleuspowered.phonon;

import static io.github.nucleuspowered.phonon.PluginInfo.DESCRIPTION;
import static io.github.nucleuspowered.phonon.PluginInfo.ID;
import static io.github.nucleuspowered.phonon.PluginInfo.NAME;
import static io.github.nucleuspowered.phonon.PluginInfo.URL;
import static io.github.nucleuspowered.phonon.PluginInfo.VERSION;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.github.nucleuspowered.phonon.internal.command.PhononCommand;
import io.github.nucleuspowered.phonon.qsml.InjectorModule;
import io.github.nucleuspowered.phonon.qsml.PhononLoggerProxy;
import io.github.nucleuspowered.phonon.qsml.PhononModuleConstructor;
import io.github.nucleuspowered.phonon.qsml.SubInjectorModule;
import io.github.nucleuspowered.phonon.util.Action;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import uk.co.drnaylor.quickstart.config.AbstractConfigAdapter;
import uk.co.drnaylor.quickstart.config.TypedAbstractConfigAdapter;
import uk.co.drnaylor.quickstart.exceptions.IncorrectAdapterTypeException;
import uk.co.drnaylor.quickstart.exceptions.NoModuleException;
import uk.co.drnaylor.quickstart.exceptions.QuickStartModuleLoaderException;
import uk.co.drnaylor.quickstart.modulecontainers.DiscoveryModuleContainer;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.inject.Inject;

@Plugin(
        id = ID,
        name = NAME,
        version = VERSION,
        description = DESCRIPTION,
        url = URL
)
public class Phonon {

    private final Logger logger;
    private final ConfigurationLoader<CommentedConfigurationNode> loader;
    private final SubInjectorModule subInjectorModule = new SubInjectorModule();

    private final PhononCommand phononCommand;
    private Injector phononInjector;
    private DiscoveryModuleContainer container;

    // Using a map for later implementation of reloadable modules.
    private Multimap<String, Action> reloadables = HashMultimap.create();

    @Inject
    public Phonon(Logger logger, @DefaultConfig(sharedRoot = false) ConfigurationLoader<CommentedConfigurationNode> loader) {
        this.logger = logger;
        this.loader = loader;
        this.phononCommand = new PhononCommand();
        this.phononInjector = Guice.createInjector(new InjectorModule(this, this.phononCommand));
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        try {
            this.container = DiscoveryModuleContainer.builder()
                    .setPackageToScan("io.github.nucleuspowered.phonon.modules") // All modules will be in here.
                    .setLoggerProxy(new PhononLoggerProxy(this.logger))
                    .setConstructor(new PhononModuleConstructor(this)) // How modules are constructed
                    .setConfigurationLoader(loader)
                    .setOnEnable(this::updateInjector) // Before the enable phase, update the Guice injector.
                    .build(true);
        } catch (Exception e) {
            e.printStackTrace();
            onError();
        }
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        try {
            this.container.loadModules(true);
        } catch (QuickStartModuleLoaderException.Construction | QuickStartModuleLoaderException.Enabling construction) {
            construction.printStackTrace();
            onError();
        }

        Sponge.getCommandManager().register(this, this.phononCommand, "phonon");
    }

    @Listener
    public void onReloadEvent(GameReloadEvent event) {
        try {
            reload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds actions to perform on reload.
     *
     * @param module The module to add the actions for.
     * @param reloadable The {@link Action} to take.
     */
    public void addReloadable(String module, Action reloadable) {
        this.reloadables.put(module.toLowerCase(), reloadable);
    }

    /**
     * Removes all reloadables for a module.
     *
     * @param module The module.
     */
    public void removeReloadablesForModule(String module) {
        this.reloadables.removeAll(module.toLowerCase());
    }

    /**
     * Reloads the config, and anything else that is registered to be reloaded.
     *
     * @throws IOException if the config could not be read.
     */
    public void reload() throws IOException {
        this.container.reloadSystemConfig();
        reloadables.values().forEach(Action::action);
    }

    /**
     * Gets the Phonon injector modules, for injecting this plugin instance into classes.
     *
     * @return The injector.
     */
    public Injector getPhononInjector() {
        return this.phononInjector;
    }

    /**
     * Stages a class for addition to the Guice injector
     *
     * @param key The {@link Class} to add.
     * @param getter The {@link Supplier} that gets the class.
     * @param <T> The type.
     */
    public <T> void addToSubInjectorModule(Class<T> key, Supplier<T> getter) {
        this.subInjectorModule.addBinding(key, getter);
    }

    /**
     * Updates the injector with the latest bindings.
     */
    private void updateInjector() {
        this.phononInjector = this.phononInjector.createChildInjector(this.subInjectorModule);
        this.subInjectorModule.reset();
    }

    /**
     * Unregisters all listeners in case of error.
     */
    private void onError() {
        Sponge.getEventManager().unregisterPluginListeners(this);
    }

    /**
     * Gets the {@link AbstractConfigAdapter}
     * @param id The ID of the module that the adapter is registered to.
     * @param configAdapterClass The {@link Class} of the adapter
     * @param <T> The type of the adapter
     * @return An {@link Optional} that will contain the adapter.
     */
    public <T extends AbstractConfigAdapter<?>> Optional<T> getConfigAdapter(String id, Class<T> configAdapterClass) {
        try {
            return Optional.of(this.container.getConfigAdapterForModule(id, configAdapterClass));
        } catch (NoModuleException | IncorrectAdapterTypeException e) {
            return Optional.empty();
        }
    }

    /**
     * Allows for quick inspection of a config value.
     *
     * @param id The ID of the module
     * @param configAdapterClass The {@link Class} of the adapter
     * @param fnToGetValue The {@link Function} that takes the config object and gets the result out.
     * @param <R> The result type.
     * @param <C> The type of the config object that the {@link TypedAbstractConfigAdapter} provides
     * @param <T> The type of the adapter
     * @return The result, wrapped in an {@link Optional}.
     */
    public <R, C, T extends TypedAbstractConfigAdapter<C>> Optional<R> getConfigValue(String id, Class<T> configAdapterClass, Function<C, R> fnToGetValue) {
        Optional<T> tOptional = getConfigAdapter(id, configAdapterClass);
        if (tOptional.isPresent()) {
            return Optional.of(fnToGetValue.apply(tOptional.get().getNodeOrDefault()));
        }

        return Optional.empty();
    }

    /**
     * Gets the {@link DiscoveryModuleContainer}
     *
     * @return The {@link DiscoveryModuleContainer}
     */
    public DiscoveryModuleContainer getModuleContainer() {
        return this.container;
    }
}

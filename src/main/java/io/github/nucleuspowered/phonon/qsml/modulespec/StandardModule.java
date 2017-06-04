package io.github.nucleuspowered.phonon.qsml.modulespec;

import com.google.inject.Inject;
import com.google.inject.Injector;
import io.github.nucleuspowered.phonon.Phonon;
import io.github.nucleuspowered.phonon.discord.DiscordBot;
import io.github.nucleuspowered.phonon.internal.Reloadable;
import io.github.nucleuspowered.phonon.internal.command.PhononCommand;
import io.github.nucleuspowered.phonon.internal.command.PhononSubcommand;
import io.github.nucleuspowered.phonon.internal.listener.ConditionalListener;
import io.github.nucleuspowered.phonon.internal.listener.ListenerBase;
import io.github.nucleuspowered.phonon.util.Action;
import org.spongepowered.api.Sponge;
import uk.co.drnaylor.quickstart.Module;
import uk.co.drnaylor.quickstart.annotations.ModuleData;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A module that has no configuration.
 */
public abstract class StandardModule implements Module {

    private final String moduleId;
    private final String moduleName;
    @Inject private Phonon phononPlugin;
    @Inject private PhononCommand phononCommand;
    @Inject private DiscordBot discordBot;

    private final String packageName;

    public StandardModule() {
        ModuleData md = this.getClass().getAnnotation(ModuleData.class);
        this.moduleId = md.id();
        this.moduleName = md.name();
        this.packageName = this.getClass().getPackage().getName() + ".";
    }

    protected String getModuleId() {
        return this.moduleId;
    }

    protected String getModuleName() {
        return this.moduleName;
    }

    protected Phonon getPhononPlugin() {
        return this.phononPlugin;
    }

    protected DiscordBot getBot() {
        return this.discordBot;
    }

    @Override public final void onEnable() {
        // Any classes can make use of the injector.
        // This section will be used to do any common tasks, such as scan for commands/listeners/hooks/whatever
        // getPhononPlugin().getPhononInjector().getInstance(Class)
        // getPhononPlugin().getPhononInjector().injectMembers(InstantiatedObject);

        // Get all the subcommands to register.
        List<Class<? extends PhononSubcommand>> subcommandList = getStreamForModule(PhononSubcommand.class)
                .collect(Collectors.toList());

        // For each command, instantiate and register.
        final Injector injector = this.phononPlugin.getPhononInjector();

        // Add the subcommands.
        subcommandList.forEach(x -> {
            PhononSubcommand subcommand = injector.getInstance(x);
            if (subcommand instanceof Reloadable) {
                Reloadable r = (Reloadable)subcommand;
                this.phononPlugin.addReloadable(this.moduleId, r::onReload);

                // Reload it now
                r.onReload();
            }

            this.phononCommand.registerSubCommand(subcommand);
        });

        // Now, listeners.
        registerListeners(this.phononPlugin, injector);

        phononEnable();
    }

     // Separate method for if we use a disableable module in the future.
    protected void registerListeners(Phonon plugin, Injector injector) {
        List<Class<? extends ListenerBase>> listenerClass = getStreamForModule(ListenerBase.class).collect(Collectors.toList());

        // Instantiate them all.
        listenerClass.forEach(x -> {
            ConditionalListener cl = x.getAnnotation(ConditionalListener.class);
            ListenerBase base = injector.getInstance(x);
            if (base instanceof Reloadable) {
                Reloadable r = (Reloadable)base;
                this.phononPlugin.addReloadable(this.moduleId, r::onReload);

                // Reload it now
                r.onReload();
            }

            if (cl != null) {
                // Create the reloadable.
                try {
                    Predicate<Phonon> p = cl.value().newInstance();
                    Action a = () -> {
                        Sponge.getEventManager().unregisterListeners(base);
                        if (p.test(plugin)) {
                            Sponge.getEventManager().registerListeners(plugin, base);
                        }
                    };

                    this.phononPlugin.addReloadable(moduleId, a);
                    a.action();
                } catch (Exception e) {
                    // Developers - this should never happen!
                    e.printStackTrace();
                }
            } else {
                Sponge.getEventManager().registerListeners(plugin, base);
            }
        });
    }

    public void phononEnable() {}

    /*
     * This is where the magic happens, folks!
     */
    @SuppressWarnings("unchecked")
    private <T> Stream<Class<? extends T>> getStreamForModule(Class<T> assignableClass) {
        return phononPlugin.getModuleContainer().getLoadedClasses().stream()
                .filter(assignableClass::isAssignableFrom)
                .filter(x -> x.getPackage().getName().startsWith(packageName))
                .filter(x -> !Modifier.isAbstract(x.getModifiers()) && !Modifier.isInterface(x.getModifiers()))
                .map(x -> (Class<? extends T>)x);
    }
}

package com.gungens.misc.libs;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class SetupAnnotations {
    private JavaPlugin plugin;
    public static List<Listener> listeners = new ArrayList<>();
    public SetupAnnotations(JavaPlugin plugin, String commandsPathName, String listenersPathName) {
        this.plugin = plugin;
        registerListeners(listenersPathName);
        registerCommands(commandsPathName);

    }
    private void registerListeners(String packageToScan) {
        Reflections reflections = new Reflections(packageToScan);
        Set<Class<?>> listenerClasses = reflections.getTypesAnnotatedWith(Register.class);

        for (Class<?> clazz : listenerClasses) {
            if (Listener.class.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers())) {
                try {
                    Listener listener = (Listener) clazz.getDeclaredConstructor().newInstance();


                    plugin.getServer().getPluginManager().registerEvents(listener, plugin);
                    listeners.add(listener);
                } catch (Exception e) {
                    Bukkit.getLogger().log(Level.SEVERE, "Failed to register listener " + clazz.getName(), e);
                }
            }

        }
    }
    private void registerCommands(String packageToScan) {
        Reflections reflections = new Reflections(packageToScan);
        Set<Class<?>> commandClasses = reflections.getTypesAnnotatedWith(RegisterCommand.class);

        for (Class<?> clazz : commandClasses) {
            if (CommandExecutor.class.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers())) {
                try {
                    CommandExecutor commandExecutor = (CommandExecutor) clazz.getDeclaredConstructor().newInstance();
                    RegisterCommand annotation = clazz.getAnnotation(RegisterCommand.class);
                    String commandName = annotation.name();
                    PluginCommand command = plugin.getCommand(commandName);
                    command.setExecutor(commandExecutor);
                    if (commandExecutor instanceof TabCompleter completer) {
                        command.setTabCompleter(completer);
                    }
                } catch (Exception e) {
                    Bukkit.getLogger().log(Level.SEVERE, "Failed to register command " + clazz.getName(), e);
                }
            }
        }
    }
}

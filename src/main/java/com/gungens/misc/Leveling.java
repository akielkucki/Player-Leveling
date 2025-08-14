package com.gungens.misc;

import com.gungens.misc.libs.SetupAnnotations;
import org.bukkit.plugin.java.JavaPlugin;

public final class Leveling extends JavaPlugin {

    public static Leveling instance;
    public static String databasePath = "";
    @Override
    public void onEnable() {
        instance = this;

        new SetupAnnotations(this,
                "com.gungens.misc.commands",
                "com.gungens.misc.listeners"
        );
        saveDefaultConfig();
        databasePath = getConfig().getString("database_url");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

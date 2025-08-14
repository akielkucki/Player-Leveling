package com.gungens.misc.libs;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class LevelExpansion extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "gungens";
    }
    
    @Override
    public String getAuthor() {
        return "Alex";
    }
    
    @Override
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.equals("player_deaths")) {
            return null;
        }
        return null;
    }
}
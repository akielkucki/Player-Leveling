package com.gungens.misc.listeners;

import com.gungens.misc.libs.Register;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Register
public class LevelupListeners implements Listener {
    private final Map<EntityType, Float> entityExpValues = new HashMap<>();
    private final Map<UUID, Long> lastExpMessageTime = new HashMap<>();
    private final long EXP_MESSAGE_COOLDOWN = 5000; // 5 seconds cooldown

    public LevelupListeners() {
        initializeEntityExpValues();
    }

    private void initializeEntityExpValues() {
        entityExpValues.put(EntityType.ZOMBIE, 0.15f);
        entityExpValues.put(EntityType.SKELETON, 0.18f);
        entityExpValues.put(EntityType.CREEPER, 0.22f);
        entityExpValues.put(EntityType.SPIDER, 0.15f);
        entityExpValues.put(EntityType.ENDERMAN, 0.35f);
        entityExpValues.put(EntityType.BLAZE, 0.30f);
        entityExpValues.put(EntityType.WITHER, 1.0f);
        entityExpValues.put(EntityType.ENDER_DRAGON, 5.0f);
    }

    @EventHandler
    public void onPlayerKillEntity(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            if (killer != null) {
                EntityType entityType = event.getEntityType();
                float baseExp = entityExpValues.getOrDefault(entityType, 0.1f);
                float randomBonus = new Random().nextFloat(0.1f);
                float expGained = baseExp + randomBonus;

                addExperienceToPlayer(killer, expGained);

                String entityName = formatEntityName(entityType.toString());
                killer.sendMessage(format("&a+" + String.format("%.2f", expGained*100) + " XP &7from killing a " + entityName));
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player.getLevel() > 0) {
            int previousLevel = player.getLevel();
            player.setLevel(Math.max(0, previousLevel - 1));
            player.setExp(0.7f);
            player.sendMessage(format("&c&lOh no! &cYou lost a level from dying!"));
        } else if (player.getExp() > 0.2f) {
            player.setExp(Math.max(0, player.getExp() - 0.2f));
            player.sendMessage(format("&c&lOh no! &cYou lost some experience from dying!"));
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        long currentTime = System.currentTimeMillis();
        UUID playerId = player.getUniqueId();

        if (!shouldSendExpMessage(playerId, currentTime)) {
            float expGained = 0.02f + new Random().nextFloat(0.05f);
            addExperienceToPlayer(player, expGained);

            if (shouldSendExpMessage(playerId, currentTime)) {
                player.sendMessage(format("&a+" + String.format("%.2f", expGained) + " XP &7from collecting items"));
                lastExpMessageTime.put(playerId, currentTime);
            }
        }
    }

    private boolean shouldSendExpMessage(UUID playerId, long currentTime) {
        Long lastTime = lastExpMessageTime.get(playerId);
        return lastTime == null || currentTime - lastTime >= EXP_MESSAGE_COOLDOWN;
    }

    private void addExperienceToPlayer(Player player, float expAmount) {
        float currentExp = player.getExp();
        float totalExp = currentExp + expAmount;

        if (totalExp >= 1.0f) {
            int levelsToAdd = (int) totalExp;
            float remainingExp = totalExp - levelsToAdd;

            int newLevel = player.getLevel() + levelsToAdd;
            player.setLevel(newLevel);
            player.setExp(remainingExp);

            playCelebratoryEffects(player, newLevel);
        } else {
            player.setExp(totalExp);
        }
    }

    private void playCelebratoryEffects(Player player, int newLevel) {
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, null);
        player.sendMessage(format("&6&l⭐ LEVEL UP! &6You reached level " + newLevel + "! &6&l⭐"));

        if (newLevel % 5 == 0) {
            player.sendMessage(format("&d&lMILESTONE ACHIEVED! &dYou've reached level " + newLevel + "!"));
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5f, 1.0f);
        }
    }

    private String formatEntityName(String name) {
        String[] parts = name.toLowerCase().split("_");
        StringBuilder result = new StringBuilder();

        for (String part : parts) {
            if (part.length() > 0) {
                result.append(Character.toUpperCase(part.charAt(0)))
                        .append(part.substring(1))
                        .append(" ");
            }
        }

        return result.toString().trim();
    }

    private String format(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
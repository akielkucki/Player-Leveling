package com.gungens.misc.services;

import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LevelService {
    public static LevelService INSTANCE = new LevelService();
    private final Map<UUID, Integer> zombieHunterLevel = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> zombieHunterExp = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> playerKills = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> playerDeaths = new ConcurrentHashMap<>();

    private Random random = new Random();
    private void awardZombieHunterExp(Player player, int expAmount) {
        UUID playerId = player.getUniqueId();
        int currentExp = zombieHunterExp.getOrDefault(playerId, 0);
        int currentLevel = zombieHunterLevel.getOrDefault(playerId, 1);

        int newExp = currentExp + expAmount;
        int expNeeded = currentLevel * 100; // Each level requires more exp

        // Level up if enough exp
        if (newExp >= expNeeded) {
            int newLevel = currentLevel + 1;
            zombieHunterLevel.put(playerId, newLevel);
            zombieHunterExp.put(playerId, newExp - expNeeded); // Keep leftover exp

            // Level up celebration
            player.sendMessage("");
            player.sendMessage(ChatColor.RED + "✧ ZOMBIE HUNTER LEVEL UP ✧");
            player.sendMessage(ChatColor.YELLOW + "Your zombie hunter level is now " + ChatColor.GOLD + newLevel);
            player.sendMessage(ChatColor.YELLOW + "You'll find better loot in zombie chests!");
            player.sendMessage("");

            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, player.getLocation().add(0, 1, 0), 30, 0.5, 0.5, 0.5, 0.1);

            // Give a reward at milestone levels
            if (newLevel % 5 == 0) {

                if (reward != null) {
                    player.getInventory().addItem(reward);
                    player.sendMessage(ChatColor.GOLD + "You received a special reward: " + reward.getItemMeta().getDisplayName());
                }
            }
        } else {
            zombieHunterExp.put(playerId, newExp);
            // Show progress bar occasionally
            if (random.nextInt(10) == 0) {
                showProgressBar(player, newExp, expNeeded);
            }
        }
    }
    private void showProgressBar(Player player, int current, int max) {
        int bars = 20; // Progress bar length
        int filledBars = (int) Math.ceil((double) current / max * bars);

        StringBuilder progressBar = new StringBuilder(ChatColor.RED + "Zombie Hunter: " + ChatColor.DARK_RED);
        for (int i = 0; i < bars; i++) {
            if (i < filledBars) {
                progressBar.append("■");
            } else {
                progressBar.append(ChatColor.GRAY).append("■");
            }
        }

        progressBar.append(" ").append(ChatColor.YELLOW).append(current).append("/").append(max);
        player.sendMessage(progressBar.toString());
    }
}

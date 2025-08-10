package com.gungens.misc.services;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MissionService {
    public interface MissionStore {
        // Optional persistence hooks. Implement if you want DB or file saving.
        default Map<String, Mission> load(UUID playerId) { return new HashMap<>(); }
        default void save(UUID playerId, Mission mission) {}
        default void delete(UUID playerId, String missionId) {}
    }

    public static final class Mission {
        private final String id;
        private int target;
        private int progress;
        private boolean completed;
        private final long assignedAt;

        public Mission(String id, int target) {
            this.id = Objects.requireNonNull(id);
            this.target = Math.max(1, target);
            this.progress = 0;
            this.completed = false;
            this.assignedAt = System.currentTimeMillis();
        }
        public String id() { return id; }
        public int target() { return target; }
        public int progress() { return progress; }
        public boolean completed() { return completed; }
    }

    private final JavaPlugin plugin;
    private final MissionStore store;

    // missions[playerId][missionId] -> Mission
    private final Map<UUID, Map<String, Mission>> missions = new ConcurrentHashMap<>();
    // completionHandlers[playerId][missionId] -> List<Runnable>
    private final Map<UUID, Map<String, List<Runnable>>> completionHandlers = new ConcurrentHashMap<>();

    public MissionService(JavaPlugin plugin) {
        this(plugin, new MissionStore(){});
    }
    public MissionService(JavaPlugin plugin, MissionStore store) {
        this.plugin = Objects.requireNonNull(plugin);
        this.store = Objects.requireNonNull(store);
    }

    /* -------- Public API -------- */

    public void assign(UUID playerId, String missionId, int target) {
        missions.computeIfAbsent(playerId, id -> new ConcurrentHashMap<>());
        missions.get(playerId).compute(missionId, (k, existing) -> {
            Mission m = (existing == null) ? new Mission(missionId, target) : existing;
            if (existing == null) store.save(playerId, m);
            return m;
        });
    }

    public boolean isActive(UUID playerId, String missionId) {
        Mission m = get(playerId, missionId);
        return m != null && !m.completed;
    }

    /** Increments progress; returns new progress (clamped to target). */
    public int increment(UUID playerId, String missionId, int amount) {
        if (amount <= 0) return getProgress(playerId, missionId);
        Mission m = get(playerId, missionId);
        if (m == null || m.completed) return (m == null) ? 0 : m.progress;

        synchronized (m) {
            if (m.completed) return m.progress;
            m.progress = Math.min(m.progress + amount, m.target);
            store.save(playerId, m);
            if (m.progress >= m.target) {
                m.completed = true;
                store.save(playerId, m);
                fireCompletion(playerId, missionId);
            }
            return m.progress;
        }
    }

    public void complete(UUID playerId, String missionId) {
        Mission m = get(playerId, missionId);
        if (m == null || m.completed) return;
        synchronized (m) {
            m.progress = m.target;
            m.completed = true;
            store.save(playerId, m);
        }
        fireCompletion(playerId, missionId);
    }

    public int getProgress(UUID playerId, String missionId) {
        Mission m = get(playerId, missionId);
        return (m == null) ? 0 : m.progress;
    }

    public int getTarget(UUID playerId, String missionId) {
        Mission m = get(playerId, missionId);
        return (m == null) ? 0 : m.target;
    }

    public Optional<Mission> getMission(UUID playerId, String missionId) {
        return Optional.ofNullable(get(playerId, missionId));
    }

    /** Register a one-shot callback for this player's mission; runs on main thread on completion. */
    public void onComplete(UUID playerId, String missionId, Runnable handler) {
        Mission m = get(playerId, missionId);
        if (m != null && m.completed) {
            runMain(handler);
            return;
        }
        completionHandlers
            .computeIfAbsent(playerId, id -> new ConcurrentHashMap<>())
            .computeIfAbsent(missionId, id -> Collections.synchronizedList(new ArrayList<>()))
            .add(Objects.requireNonNull(handler));
    }

    /** Clear a mission (e.g., after payout), removing state & handlers. */
    public void clear(UUID playerId, String missionId) {
        Map<String, Mission> byId = missions.get(playerId);
        if (byId != null) byId.remove(missionId);
        Map<String, List<Runnable>> byIdHandlers = completionHandlers.get(playerId);
        if (byIdHandlers != null) byIdHandlers.remove(missionId);
        store.delete(playerId, missionId);
    }

    /** Lazy load missions for a player (call on login if you persist). */
    public void ensureLoaded(UUID playerId) {
        missions.computeIfAbsent(playerId, id -> new ConcurrentHashMap<>(store.load(playerId)));
    }

    /* -------- Internal -------- */

    private Mission get(UUID playerId, String missionId) {
        Map<String, Mission> byId = missions.get(playerId);
        return (byId == null) ? null : byId.get(missionId);
    }

    private void fireCompletion(UUID playerId, String missionId) {
        List<Runnable> toRun;
        Map<String, List<Runnable>> byMission = completionHandlers.get(playerId);
        if (byMission == null) return;
        synchronized (byMission) {
            toRun = byMission.remove(missionId);
        }
        if (toRun != null) {
            for (Runnable r : toRun) runMain(r);
        }
    }

    private void runMain(Runnable r) {
        if (Bukkit.isPrimaryThread()) r.run();
        else Bukkit.getScheduler().runTask(plugin, r);
    }
}

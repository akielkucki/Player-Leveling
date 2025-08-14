package com.gungens.misc.models;

public class User {
    private String id;
    private String username;
    private String discordId;
    private int level;
    private int zLevel;
    private int kills;
    private int deaths;
    private long totalPlaytime;
    private transient long joinTime;
    private double balance;

    public User(String id, String username, int level, int zLevel, int kills, int deaths,long joinTime, long totalPlaytime, double balance) {
        this.id = id;
        this.username = username;
        this.discordId = "";
        this.level = level;
        this.zLevel = zLevel;
        this.kills = kills;
        this.deaths = deaths;
        this.totalPlaytime = totalPlaytime;
        this.balance = balance;
        this.joinTime = joinTime;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDiscordId() {
        return discordId;
    }

    public void setDiscordId(String discordId) {
        this.discordId = discordId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getzLevel() {
        return zLevel;
    }

    public void setzLevel(int zLevel) {
        this.zLevel = zLevel;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public long getTotalPlaytime() {
        return totalPlaytime;
    }

    public void setTotalPlaytime(long totalPlaytime) {
        this.totalPlaytime = totalPlaytime;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public long getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(long joinTime) {
        this.joinTime = joinTime;
    }

    public void setTotalPlaytime() {
        this.totalPlaytime = (System.currentTimeMillis() - joinTime)*1000;
    }
}

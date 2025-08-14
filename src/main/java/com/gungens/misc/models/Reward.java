package com.gungens.misc.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.bukkit.inventory.ItemStack;

@DatabaseTable(tableName = "rewards")
public class Reward {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "base64_string", dataType = DataType.STRING)
    private String b64string;
    @DatabaseField(columnName = "chance", dataType = DataType.DOUBLE)
    private double chance;
    @DatabaseField(columnName = "min_level",dataType = DataType.INTEGER)
    private int minLevel;
    private transient ItemStack itemStack;

    public Reward(int id, String b64string, double chance, int minLevel) {
        this.id = id;
        this.b64string = b64string;
        this.chance = chance;
        this.minLevel = minLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getB64string() {
        return b64string;
    }

    public void setB64string(String b64string) {
        this.b64string = b64string;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(int minLevel) {
        this.minLevel = minLevel;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}

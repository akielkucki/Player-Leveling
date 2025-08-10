package com.gungens.misc.models;

import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "rewards")
public class Reward {
    private int id;
    private String b64string;
}

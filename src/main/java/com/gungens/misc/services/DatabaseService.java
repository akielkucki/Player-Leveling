package com.gungens.misc.services;


import com.gungens.misc.Leveling;
import com.gungens.misc.models.Reward;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.bukkit.Bukkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

public class DatabaseService  {
    private static final Logger log = LoggerFactory.getLogger(DatabaseService.class);
    private Dao<Reward, String> rewardDao;
    @SuppressWarnings("all")
    public DatabaseService() {
        String PSQL_DATABASE_URL = "jdbc:postgresql://147.135.31.124:5432/gungens?user=gungens&password=Buildcr33k*";
        File dbFile = new File(Leveling.instance.getDataFolder().getAbsolutePath()+"/database.db");

        try {

            dbFile.getParentFile().mkdirs();
            dbFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (ConnectionSource psqlConnectionSource = new JdbcConnectionSource(PSQL_DATABASE_URL)) {



            Bukkit.getLogger().log(Level.INFO, "Database created/connected with url: " + PSQL_DATABASE_URL);
        } catch (SQLException | IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error while inserting data", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadLeveledPlayers() throws SQLException {

    }

    public void flushDirtyPlayers() throws SQLException {

    }
}

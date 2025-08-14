package com.gungens.misc.services;

import com.gungens.misc.Leveling;
import com.gungens.misc.models.Reward;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class SqliteService {
    private Dao<Reward, String> rewardDao;
    public SqliteService() {
        File dbFile = new File(Leveling.instance.getDataFolder().getAbsolutePath()+"/database.db");

        try {
            dbFile.getParentFile().mkdirs();
            dbFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:sqlite:" + dbFile.getAbsolutePath());
            rewardDao = DaoManager.createDao(connectionSource, Reward.class);
            TableUtils.createTableIfNotExists(connectionSource, Reward.class);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void saveReward(Reward reward) {
        try {
            rewardDao.createOrUpdate(reward);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadAllRewards() {
        try {
            LevelService.INSTANCE.loadAllRewards(rewardDao.queryForAll());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

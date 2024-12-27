package com.motus.analytics.model.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.motus.analytics.model.dao.UserDao;
import com.motus.analytics.model.entity.User;

@Database(entities = {User.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}

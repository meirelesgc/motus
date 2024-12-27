package com.motus.analytics.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.motus.analytics.model.entity.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    public void addUser(User user);
    @Update
    public void updateUser(User user);
    @Delete
    public void deleteUser(User user);
    @Query("SELECT name, phone, email FROM users")
    public List<User> getAllUsers();
    @Query("SELECT name, phone, email, password FROM users WHERE email = :email")
    public User getUserByEmail(String email);
}
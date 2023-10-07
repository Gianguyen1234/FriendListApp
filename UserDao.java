package com.example.friendlistapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.friendlistapp.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insertUser (User user);

    @Query("SELECT * FROM user")
    List<User> getListUser();

    @Query("SELECT * FROM user where userName= :username ")
    List<User> checkUser (String username);

    @Update
    void updateUser (User user);

    @Delete
    void deleteUser (User user);


}

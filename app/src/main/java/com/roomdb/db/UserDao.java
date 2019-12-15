package com.roomdb.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.roomdb.db.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("Select * from user")
    List<User> getAllUser();

    @Query("Select * from user where userId LIKE :uId AND password LIKE :pwd LIMIT 1")
    User getUser(String uId,String pwd);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    long insertUser(User user);

    @Update
    int updateUser(User user);

    @Delete
    int deleteUser(User user);
}

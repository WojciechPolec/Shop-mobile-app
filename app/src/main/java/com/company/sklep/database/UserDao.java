package com.company.sklep.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createNewUser(UserEntity user);

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    List<UserEntity> getUser(String email, String password);

    @Query("SELECT * FROM users WHERE email = :email")
    List<UserEntity> getUser(String email);

    @Query("SELECT * FROM users WHERE id = :id")
    List<UserEntity> getUser(Long id);
}

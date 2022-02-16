package com.company.sklep.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "users")
public class UserEntity implements Serializable {
    @PrimaryKey(autoGenerate = true) public Long id;
    @ColumnInfo(name = "name") public String name = "";
    @ColumnInfo(name = "surrname") public String surname = "";
    @ColumnInfo(name = "email") public String email = "";
    @ColumnInfo(name = "password") public String password = "";

    public UserEntity(){}

    public UserEntity(String name, String surname, String email, String password){
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }
 }

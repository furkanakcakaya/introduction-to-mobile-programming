package com.example.mobile30_03.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RUser {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    public String username;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "phone_number")
    public String phoneNumber;

    @ColumnInfo(name = "profile_pic")
    public String profilePic;

}
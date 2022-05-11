package com.example.mobile30_03.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private static int nextId = 1;
    public String username;
    public String password;
    public String email;
    public String phoneNumber;
    public int id;

    public User(String username,String password,String email,String phoneNumber) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        id = nextId++;
    }

    protected User(Parcel in) {
        username = in.readString();
        password = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        id = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(password);
        parcel.writeString(email);
        parcel.writeString(phoneNumber);
        parcel.writeInt(id);
    }
}


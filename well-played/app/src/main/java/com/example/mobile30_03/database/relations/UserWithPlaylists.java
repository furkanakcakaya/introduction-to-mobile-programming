package com.example.mobile30_03.database.relations;


import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.mobile30_03.database.RPlaylist;
import com.example.mobile30_03.database.RUser;

import java.util.List;

public class UserWithPlaylists {
    @Embedded
    public RUser rUser;
    @Relation(
            parentColumn = "username",
            entityColumn = "username"
    )
    public List<RPlaylist> rPlaylists;

}

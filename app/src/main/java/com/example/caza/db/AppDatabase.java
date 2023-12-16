package com.example.caza.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.caza.dao.DataCollectionItemDao;
import com.example.caza.dao.MessageDao;
import com.example.caza.dao.RecordingDao;
import com.example.caza.entities.DataCollectionItem;
import com.example.caza.entities.Message;
import com.example.caza.entities.Recording;

@Database(entities = {Recording.class, Message.class, DataCollectionItem.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract RecordingDao recordingDao();
    public abstract MessageDao messageDao();
    public abstract DataCollectionItemDao dataCollectionItemDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "DataCollectorDB") // Use your chosen database name
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}


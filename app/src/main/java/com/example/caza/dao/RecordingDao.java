package com.example.caza.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.caza.entities.Recording;
import java.util.List;

@Dao
public interface RecordingDao {
    @Query("SELECT * FROM recording") // Ensure 'recording' is your table name
    List<Recording> getAll();

    @Insert
    long insert(Recording recording); // Method to insert a single recording and return its ID

    @Delete
    void delete(Recording recording); // Method to delete a recording
}

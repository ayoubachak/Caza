package com.example.caza.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.caza.entities.Message;
import java.util.List;

@Dao
public interface MessageDao {
    @Query("SELECT * FROM message") // Assuming your table is named 'message'
    List<Message> getAll();

    @Insert
    long insert(Message message); // Insert a single message and return its ID

    @Delete
    void delete(Message message);

    @Query("SELECT * FROM message WHERE id = :messageId")
    Message getMessageById(int messageId);
}

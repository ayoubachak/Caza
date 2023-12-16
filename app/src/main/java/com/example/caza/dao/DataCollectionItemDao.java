package com.example.caza.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.caza.entities.DataCollectionItem;
import java.util.List;

@Dao
public interface DataCollectionItemDao {
    @Query("SELECT * FROM data_collection_item") // Replace with the correct table name
    List<DataCollectionItem> getAll();

    @Insert
    long insert(DataCollectionItem dataCollectionItem); // Insert method for DataCollectionItem

    @Delete
    void delete(DataCollectionItem dataCollectionItem);

}

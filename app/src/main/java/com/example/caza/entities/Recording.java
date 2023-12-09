package com.example.caza.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Recording {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "file_path")
    public String filePath;

    // Constructor, getters and setters...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}

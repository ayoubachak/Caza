package com.example.caza.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "data_collection_item")
public class DataCollectionItem {
    private Recording recording;
    private Message message;

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "recording_id")
    public int recordingId;

    @ColumnInfo(name = "message_id")
    public int messageId;

    // Constructor, getters and setters...

    public Recording getRecording() {
        return recording;
    }

    public void setRecording(Recording recording) {
        this.recording = recording;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecordingId() {
        return recordingId;
    }

    public void setRecordingId(int recordingId) {
        this.recordingId = recordingId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
}
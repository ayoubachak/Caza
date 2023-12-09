package com.example.caza.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Message {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "type") // "command", "text", "audio"
    public String type;

    @ColumnInfo(name = "content") // Command or text content
    public String content;

    // For command messages
    @ColumnInfo(name = "command_name")
    public String commandName;

    @ColumnInfo(name = "command_args")
    public String commandArgs;

    // Constructor, getters and setters...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandArgs() {
        return commandArgs;
    }

    public void setCommandArgs(String commandArgs) {
        this.commandArgs = commandArgs;
    }
}

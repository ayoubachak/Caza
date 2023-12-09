package com.example.caza.db;
import androidx.room.TypeConverter;
import com.example.caza.entities.Recording;
import com.example.caza.entities.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class Converters {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static Recording recordingFromString(String value) {
        Type type = new TypeToken<Recording>() {}.getType();
        return gson.fromJson(value, type);
    }

    @TypeConverter
    public static String recordingToString(Recording recording) {
        return gson.toJson(recording);
    }

    @TypeConverter
    public static Message messageFromString(String value) {
        Type type = new TypeToken<Message>() {}.getType();
        return gson.fromJson(value, type);
    }

    @TypeConverter
    public static String messageToString(Message message) {
        return gson.toJson(message);
    }
}

package com.example.caza;

import android.app.Activity;

import com.example.caza.callbacks.GPSResultCallback;
import com.example.caza.callbacks.TemperatureResultCallback;
import com.example.caza.handlers.CommandHandler;
import com.example.caza.handlers.EchoCommandHandler;
import com.example.caza.handlers.GPSCommandHandler;
import com.example.caza.handlers.HelpCommandHandler;
import com.example.caza.handlers.SMSCommandHandler;
import com.example.caza.handlers.TemperatureCommandHandler;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {
    private Map<String, CommandHandler> commandMap;

    public CommandExecutor(Activity activity) {
        commandMap = new HashMap<>();
        commandMap.put("/sms", new SMSCommandHandler());
        commandMap.put("/temp", new TemperatureCommandHandler((TemperatureResultCallback) activity));
        commandMap.put("/gps", new GPSCommandHandler((GPSResultCallback) activity));
        commandMap.put("/echo", new EchoCommandHandler());
        commandMap.put("/help", new HelpCommandHandler(commandMap));
    }

    public String executeCommand(String userInput, Activity activity) {
        String[] parts = userInput.split(" ", 2);
        String command = parts[0];
        String[] args = parts.length > 1 ? parts[1].split(" ") : new String[0];

        CommandHandler handler = commandMap.get(command);
        if (handler != null) {
            return handler.execute(args, activity);
        } else {
            return "Unknown command. Type '/help' for a list of commands.";
        }
    }
}


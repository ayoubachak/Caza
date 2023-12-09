package com.example.caza.handlers;


import android.app.Activity;

import java.util.Map;

public class HelpCommandHandler implements CommandHandler {
    private Map<String, CommandHandler> commandHandlers;
    public  HelpCommandHandler(Map<String, CommandHandler> commandHandlers){
        this.commandHandlers = commandHandlers;
    }
    @Override
    public String execute(String[] args, Activity activity) {
        StringBuilder helpMessage = new StringBuilder("All Commands:\n");
        for (Map.Entry<String, CommandHandler> entry : commandHandlers.entrySet()) {
            helpMessage.append(entry.getKey()).append(": ").append(entry.getValue().help()).append("\n\n");
        }
        return helpMessage.toString();
    }

    @Override
    public String help() {
        return "/help:\n"
                + "Lists all available commands and their usage instructions.";
    }
}
package com.bookface.Search.Commands;

import lombok.Singular;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class CommandTranslator {

    private CommandTranslator c;
    private final HashMap<String, String> commands;

    public CommandTranslator(){
        commands = new HashMap<>();
        populateCommands();
    }

    private void populateCommands(){
        commands.put("SearchUserByName", "com.bookface.Search.Commands.SearchUserByName");
        //.....
    }

    public String getName(String name){
        return commands.get(name);
    }

//    public CommandTranslator getCommandTranslator(){
//        if(c == null){
//            c = new CommandTranslator();
//        }
//        return c;
//    }
}

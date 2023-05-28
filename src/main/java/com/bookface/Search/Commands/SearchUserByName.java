package com.bookface.Search.Commands;

import com.bookface.Search.ElasticHandlers.UserElasticHandler;
import com.bookface.Search.Models.User;
import com.bookface.Search.Records.PageSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;

public class SearchUserByName implements Command<List<User>, HashMap<String, Object>>{

    @Autowired
    UserElasticHandler userElasticHandler;

    public List<User> execute(HashMap<String, Object> input) {

        String pageNum = (String) input.get("pageNum");
        String username = (String) input.get("username");
        String pageSize = (String) input.get("pageSize");

        if (username.contains(" "))
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "username cannot contain spaces");

        return userElasticHandler.getAll(new PageSettings(username, pageNum, pageSize));
    }


}

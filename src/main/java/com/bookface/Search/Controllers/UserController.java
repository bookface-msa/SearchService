package com.bookface.Search.Controllers;

import com.bookface.Search.ElasticHandlers.UserElasticHandler;
import com.bookface.Search.Models.User;
import com.bookface.Search.Records.OptionalUser;
import com.bookface.Search.Records.PageSettings;
import com.bookface.Search.Repos.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;


@SuppressWarnings("unchecked")
@Slf4j
@RestController
@RequestMapping("/search/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private TopicExchange exchangeUser;
    @Autowired
    private UserElasticHandler userElasticHandler;

    @Cacheable(value = "userCache")
    public User getUserById(String id) {
        log.info("Fetching user with id {}", id);
        OptionalUser user = userElasticHandler.getUserWithId(id);

        if (user != null && !user.isNull())
            return user.user();
        else
            return null;
    }

    @GetMapping
    @Cacheable(value = "userCache")
    List<User> getAll(@RequestParam String query, @RequestParam(defaultValue = "0") String pageNum,
                      @RequestParam(defaultValue = "10") String pageSize) {

        log.info("Searching for users with query {}", query);

        return userElasticHandler.getAll(new PageSettings(query, pageNum, pageSize));
    }

    @RabbitListener(queues = "elastic.users.create")
    void addUser(User user) {
        log.info(user.getUsername() + " " + user.getId());
        userElasticHandler.addUser(user);
    }

    @RabbitListener(queues = "elastic.users.update")
    void editUser(User user) {

        User old = getUserById(user.getId());

        if (old != null) {
            old.setBio(user.getBio() == null ? old.getBio() : user.getBio());
            old.setUsername(user.getUsername() == null ? old.getUsername() : user.getUsername());
            old.setImageurl(user.getImageurl() == null ? old.getImageurl() : user.getImageurl());

            log.info("Requesting to update user " + old);
            User result = userElasticHandler.addUser(old);
        } else {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "User with id " + user.getId() + " is not found");
        }
    }

    @RabbitListener(queues = "elastic.users.delete")
    void deleteUser(String id) {
        if (id.contains(" "))
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "id cannot contain spaces");
        userElasticHandler.delUser(id);
        log.info("Delete request successful for " + id);
    }


}

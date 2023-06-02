package com.bookface.Search.Controllers;

import com.bookface.Search.ElasticHandlers.UserElasticHandler;
import com.bookface.Search.Models.User;
import com.bookface.Search.Records.OptionalUser;
import com.bookface.Search.Records.PageSettings;
import com.bookface.Search.Repos.UserRepository;
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
@RestController
@RequestMapping("/user")
public class UserController {

    UserRepository userRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private TopicExchange exchangeUser;
    @Autowired
    private UserElasticHandler userElasticHandler;

    public UserController(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Cacheable(value = "userCache")
    public User getUserById(String id) {
        OptionalUser user = userElasticHandler.getUserWithId(id);

        if (user != null && !user.isNull())
            return user.user();
        else
            return null;
    }

    @GetMapping
    @Cacheable(value = "userCache")
    List<User> getAll(@RequestParam String username, @RequestParam(defaultValue = "0") String pageNum,
                      @RequestParam(defaultValue = "10") String pageSize) {
        if (username.contains(" "))
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "username cannot contain spaces");

        return userElasticHandler.getAll(new PageSettings(username, pageNum, pageSize));
    }

//    @PostMapping
    @RabbitListener(queues = "elastic.users.create")
    void addUser(@RequestBody User user) {
        System.out.println(user.getUsername() + " " + user.getId());
        userElasticHandler.addUser(user);
    }


//    @PutMapping
    @RabbitListener(queues = "elastic.users.update")
    void editUser(@RequestBody User user) {

        User old = getUserById(user.getId());

        if (old != null) {
            old.setBio(user.getBio() == null ? old.getBio() : user.getBio());
            old.setUsername(user.getUsername() == null ? old.getUsername() : user.getUsername());
            old.setImageurl(user.getImageurl() == null ? old.getImageurl() : user.getImageurl());

            System.out.println(" [x] Requesting to update user " + old);
            User result = userElasticHandler.addUser(old);
        } else {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "User with id " + user.getId() + " is not found");
        }
    }

//    @DeleteMapping
    @RabbitListener(queues = "elastic.users.delete")
    void deleteUser(@RequestParam String id) {
        if (id.contains(" "))
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "id cannot contain spaces");
        userElasticHandler.delUser(id);
        System.out.println(" [.] Delete request successful for " + id);
    }


}

package com.bookface.Search.Controllers;

import com.bookface.Search.Models.User;
import com.bookface.Search.Repos.UserRepository;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    UserRepository userRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DirectExchange exchange;

    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping
    List<User> getAll(@RequestParam String username){
        System.out.println(" [x] Requesting " + username);
        @SuppressWarnings("unchecked")
        List<User> response = (List<User>) rabbitTemplate.convertSendAndReceive(exchange.getName(), "reqs", username);
        System.out.println(" [.] Got '" + response + "'");
        return response;
    }

    @PostMapping
    User addUser(@RequestBody User user){
//        userRepository.save(user);
        System.out.println(" [x] Requesting to add " + user);
        User response = (User) rabbitTemplate.convertSendAndReceive(exchange.getName(), "reqs", user);
        System.out.println(" [.] Got '" + response + "'");
        return response;
    }

}

package com.bookface.Search.Controllers;

import com.bookface.Search.Models.User;
import com.bookface.Search.Repos.UserRepository;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@SuppressWarnings("unchecked")
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
        List<User> response = (List<User>) rabbitTemplate.convertSendAndReceive(exchange.getName(), "getAll", username);
        System.out.println(" [.] Got '" + response + "'");
        return response;
    }

    @PostMapping
    User addUser(@RequestBody User user){
        System.out.println(" [x] Requesting to add " + user);
        User response = (User) rabbitTemplate.convertSendAndReceive(exchange.getName(), "addUser", user);
        System.out.println(" [.] Got '" + response + "'");
        return response;
    }

    @PutMapping
    User editUser(@RequestBody User user){

        System.out.println(" [x] Requesting to update " + user);
        User old = (User) rabbitTemplate.convertSendAndReceive(exchange.getName(), "getById", user.getId());
        System.out.println(" [.] Got " + old);

        if(old != null){
            old.setBio(user.getBio() == null? old.getBio() : user.getBio());
            old.setUsername(user.getUsername() == null? old.getUsername() : user.getUsername());
            old.setImageurl(user.getImageurl() == null? old.getImageurl() : user.getImageurl());

            System.out.println(" [x] Requesting to set " + old);
            User result = (User) rabbitTemplate.convertSendAndReceive(exchange.getName(), "addUser", old);
            System.out.println(" [.] Got " + result);
            return result;
        }else{
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "User with id " + user.getId() + " is not found");
        }
    }


}

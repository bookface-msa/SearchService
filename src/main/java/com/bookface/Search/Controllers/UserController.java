package com.bookface.Search.Controllers;

import com.bookface.Search.Models.User;
import com.bookface.Search.Repos.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
class UserController {

    UserRepository userRepository;

    UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping
    List<User> getAll(){
        List<User> list = new ArrayList<User>();
//        Iterator<User> iterator = userRepository.findAll().iterator();
//        while (iterator.hasNext()){
//            list.add(iterator.next());
//        }
        return userRepository.findByUsername("John");
    }

    @PostMapping
    User addUser(@RequestBody User user){
//        System.out.println(user.getUsername());
        userRepository.save(user);
        return user;
    }

}

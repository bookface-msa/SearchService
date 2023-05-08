package com.bookface.Search.Controllers;

import com.bookface.Search.Models.User;
import com.bookface.Search.Repos.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Component
public class ElasticHandler {

    @Autowired
    UserRepository userRepository;


    @RabbitListener(queues = "elastic.users.getAll")
        List<User> getAll(String username) {
            System.out.println(" [x] Received request for " + username);
            List<User> result = userRepository.findByUsernameContaining(username);
        System.out.println(" [.] Returned " + result);
        return result;
    }

    @RabbitListener(queues = "elastic.users.getById")
    User getUserWithId(String id) {
        System.out.println(" [x] Received request for user with id " + id);
        Optional<User> result = userRepository.findById(id);

        if(result.isPresent()) {
            System.out.println(" [.] Returned " + result);
            return result.get();
        }

        System.out.println(" [.] Returned " + result);
        return null;
    }

    @RabbitListener(queues = "elastic.users.addUser")
    User addUser(User user){
        System.out.println(" [x] Received request to add " + user);
        User saved = userRepository.save(user);
        System.out.println(" [.] Returned " + saved);
        return saved;
    }


}

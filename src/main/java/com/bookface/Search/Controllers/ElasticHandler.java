package com.bookface.Search.Controllers;

import com.bookface.Search.Models.User;
import com.bookface.Search.Repos.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Component
public class ElasticHandler {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @RabbitListener(queues = "elastic.reqs")
        List<User> getAll(String username) {
            System.out.println(" [x] Received request for " + username);
            List<User> result = userRepository.findByUsernameContaining(username);
        System.out.println(" [.] Returned " + result);
        return result;
    }

    @RabbitListener(queues = "elastic.reqs")
    User addUser(User user){
        System.out.println(" [x] Received request to add " + user);
        User saved = userRepository.save(user);
        System.out.println(" [.] Returned " + saved);
        return saved;
    }

}

package com.bookface.Search.Controllers;

import com.bookface.Search.Models.User;
import com.bookface.Search.Records.PageSettings;
import com.bookface.Search.Repos.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserElasticHandler {

    @Autowired
    UserRepository userRepository;


    @RabbitListener(queues = "elastic.users.getAll")
        List<User> getAll(PageSettings pageSettings) {
        String username = pageSettings.content();
        int pageNum = Integer.parseInt(pageSettings.pageNum());
        int pageSize = Integer.parseInt(pageSettings.pageSize());
        System.out.println(" [x] Received request for " + username);
        Page<User> result = userRepository.findByUsernameContaining(username, PageRequest.of(pageNum,pageSize));
        System.out.println(" [.] Returned " + result);
        return result.getContent();
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

    @RabbitListener(queues = "elastic.users.delUser")
    String delUser(String id){
        System.out.println(" [x] Received request to delete " + id);
        userRepository.deleteById(id);
        System.out.println(" [.] Deleted " + id);
        return "success";
    }


}

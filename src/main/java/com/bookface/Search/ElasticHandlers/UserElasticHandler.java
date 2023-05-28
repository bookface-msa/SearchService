package com.bookface.Search.ElasticHandlers;

import com.bookface.Search.Models.User;
import com.bookface.Search.Records.OptionalUser;
import com.bookface.Search.Records.PageSettings;
import com.bookface.Search.Repos.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<User> getAll(PageSettings pageSettings) {
        String username = pageSettings.content();
        int pageNum = Integer.parseInt(pageSettings.pageNum());
        int pageSize = Integer.parseInt(pageSettings.pageSize());

        System.out.println(" [x] Received request for " + username);
        Page<User> result = userRepository.findByUsernameContaining(username, PageRequest.of(pageNum,pageSize));
        System.out.println(" [.] Returned " + result);
        return result.getContent();
    }

    @RabbitListener(queues = "elastic.users.getById")
    public OptionalUser getUserWithId(String id) {
        System.out.println(" [x] Received request for user with id " + id);
        Optional<User> result = userRepository.findById(id);

        if(result.isPresent()) {
            System.out.println(" [.] Returned " + result);
            return OptionalUser.builder().isNull(false).user(result.get()).build();
        }

        System.out.println(" [.] Returned " + result);
        return OptionalUser.builder().isNull(true).build();
    }

    @RabbitListener(queues = "elastic.users.addUser")
    public User addUser(User user){
        System.out.println(" [x] Received request to add " + user);
        User saved = userRepository.save(user);
        System.out.println(" [.] Returned " + saved);
        return saved;
    }

    @RabbitListener(queues = "elastic.users.delUser")
    public String delUser(String id){
        System.out.println(" [x] Received request to delete " + id);
        userRepository.deleteById(id);
        System.out.println(" [.] Deleted " + id);
        return "success";
    }


}

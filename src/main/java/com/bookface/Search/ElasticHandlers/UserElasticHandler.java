package com.bookface.Search.ElasticHandlers;

import com.bookface.Search.Models.User;
import com.bookface.Search.Records.OptionalUser;
import com.bookface.Search.Records.PageSettings;
import com.bookface.Search.Repos.UserRepository;
import com.fasterxml.jackson.datatype.jdk8.OptionalIntDeserializer;
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

    public List<User> getAll(PageSettings pageSettings) {
        String username = pageSettings.content();
        int pageNum = Integer.parseInt(pageSettings.pageNum());
        int pageSize = Integer.parseInt(pageSettings.pageSize());

        Page<User> result = userRepository.findByUsernameContaining(username, PageRequest.of(pageNum,pageSize));
        return result.getContent();
    }

    public OptionalUser getUserWithId(String id) {
        Optional<User> result = userRepository.findById(id);

        if(result.isPresent()) {
            return OptionalUser.builder().isNull(false).user(result.get()).build();
        }

        return OptionalUser.builder().isNull(true).build();
    }

    public OptionalUser getUserWithUsername(String username){
        List<User> results = userRepository.findByUsername(username);

        if(results.isEmpty()){
            return new OptionalUser(true, null);
        }
        return new OptionalUser(false, results.get(0));
    }


    public User addUser(User user){
        User saved = userRepository.save(user);
        return saved;
    }


    public void delUser(String id){
        userRepository.deleteById(id);
    }


}

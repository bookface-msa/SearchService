package com.bookface.Search;

import com.bookface.Search.Models.User;
import com.bookface.Search.Repos.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {
    private UserRepository userRepository;

    public void createUserIndexBulk(final List<User> users) {
        userRepository.saveAll(users);
    }

    public void createProductIndex(final User user) {
        userRepository.save(user);
    }
}

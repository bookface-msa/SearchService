package com.bookface.Search.Repos;

import com.bookface.Search.Models.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface UserRepository extends ElasticsearchRepository<User, String> {
    List<User> findByUsername(String username);
    List<User> findByUsernameContaining(String username);

}

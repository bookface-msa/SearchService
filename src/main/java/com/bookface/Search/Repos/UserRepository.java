package com.bookface.Search.Repos;

import com.bookface.Search.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface UserRepository extends ElasticsearchRepository<User, String> {

    Page<User> findByUsernameContaining(String username, Pageable pageable);

}

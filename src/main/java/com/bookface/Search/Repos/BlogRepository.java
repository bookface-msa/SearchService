package com.bookface.Search.Repos;

import com.bookface.Search.Models.Blog;
import com.bookface.Search.Models.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BlogRepository extends ElasticsearchRepository<Blog, String> {

    List<Blog> findByContent(String content);
    List<Blog> findByTagsContaining(String tags);
}

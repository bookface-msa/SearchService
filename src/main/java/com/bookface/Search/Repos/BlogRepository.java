package com.bookface.Search.Repos;
import com.bookface.Search.Models.Blog;
import com.bookface.Search.Models.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BlogRepository extends ElasticsearchRepository<Blog, String> {

    Page<Blog> findByBodyOrTitle(String content, String title, Pageable pageable);
//    List<Blog> findByTagsContaining(Tag tags);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"tags\": \"?0\"}}]}}")
    Page<Blog> findByTagUsingDeclaredQuery(String tag, Pageable pageable);


}

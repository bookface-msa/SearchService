package com.bookface.Search.Repos;
import com.bookface.Search.Models.Blog;
import com.bookface.Search.Models.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BlogRepository extends ElasticsearchRepository<Blog, String> {

    List<Blog> findByContentOrTitle(String content, String title);
//    List<Blog> findByTagsContaining(Tag tags);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"tags\": \"?0\"}}]}}")
    List<Blog> findByTagUsingDeclaredQuery(String tag);

}

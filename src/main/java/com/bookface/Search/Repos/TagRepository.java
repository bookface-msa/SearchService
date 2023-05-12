package com.bookface.Search.Repos;

import com.bookface.Search.Models.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;



public interface TagRepository extends ElasticsearchRepository<Tag, String> {

 //   Page<Tag> findByTagStartingWithOrderByRelevancyDesc(String tag, Pageable pageable);

    @Query("{\"match_phrase_prefix\": {\"tag\": { \"query\": \"?0\" }}}")
    Page<Tag> findByTagUsingDeclaredQuery(String tag, Pageable pageable);


}

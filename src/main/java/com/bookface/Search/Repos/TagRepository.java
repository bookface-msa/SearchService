package com.bookface.Search.Repos;

import com.bookface.Search.Models.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface TagRepository extends ElasticsearchRepository<Tag, String> {

    Page<Tag> findByTagStartingWithOrderByRelevancyDesc(String tag, Pageable pageable);



}

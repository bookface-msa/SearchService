package com.bookface.Search.ElasticHandlers;

import com.bookface.Search.Models.Tag;
import com.bookface.Search.Repos.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class TagElasticHandler {

    @Autowired
    TagRepository tagRepository;

    public List<String> search(String tag, String pageNum, String pageSize) {
        Sort sort = Sort.by("relevancy").descending();
        return tagRepository.findByTagOrderByRelevancyDesc(tag,
                        PageRequest.of(Integer.parseInt(pageNum), Integer.parseInt(pageSize)).withSort(sort))
                .stream().map(Tag::getTag).toList();
    }

    @RabbitListener(queues = "elastic.tags.save")
    public void addTag(String tag) {

        Tag newTag;
        Optional<Tag> t;
        try {
             t = tagRepository.findById(tag);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            t = Optional.empty();
        }
        if (t.isPresent()) {
            newTag = t.get();
            newTag.setRelevancy(newTag.getRelevancy() + 1);
        } else {
            newTag = Tag.builder().tag(tag).relevancy(1).build();
        }
        Tag result = tagRepository.save(newTag);
        log.info("Saved tag " + result.getTag() + " with relevancy " + result.getRelevancy());
    }


}

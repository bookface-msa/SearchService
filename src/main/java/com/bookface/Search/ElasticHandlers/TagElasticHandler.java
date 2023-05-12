package com.bookface.Search.ElasticHandlers;

import com.bookface.Search.Models.Tag;
import com.bookface.Search.Repos.TagRepository;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TagElasticHandler {

    @Autowired
    TagRepository tagRepository;

    @RabbitListener(queues = "elastic.tags.search")
    List<String> search(String tag) {

//        tag = tag.replace(" ", "\\\\ ");
        System.out.println(" [x] Received request for " + tag);
        List<String> result =
                tagRepository.findByTagUsingDeclaredQuery(tag, PageRequest.of(0, 10))
                        .stream()
                        .sorted((Tag a, Tag b) -> b.getRelevancy() - a.getRelevancy())
                        .map(Tag::getTag)
                        .toList();
        System.out.println(" [.] Returned " + result);
        return result;
    }

    @RabbitListener(queues = "elastic.tags.save")
    @RabbitHandler
    void addTag(String tag) {
        System.out.println(" [x] Received request for " + tag);
        Tag newTag;
        Optional<Tag> t = tagRepository.findById(tag);
        if (t.isPresent()) {
            newTag = t.get();
            newTag.setRelevancy(newTag.getRelevancy() + 1);
        } else {
            newTag = Tag.builder().tag(tag).relevancy(1).build();
        }
        Tag result = tagRepository.save(newTag);
        System.out.println(" [.] Saved tag " + result.getTag() + " with relevancy " + result.getRelevancy());
    }


}

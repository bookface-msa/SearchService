package com.bookface.Search.Controllers;


import com.bookface.Search.Repos.TagRepository;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    TagRepository tagRepository;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    DirectExchange exchangeTag;


    @GetMapping
    @Cacheable(value = "tagCache")
    List<String> get(@RequestParam String tag) {
        System.out.println(" [x] Requesting tag search for " + tag);
        List<String> res = (List<String>) rabbitTemplate.convertSendAndReceive(exchangeTag.getName(), "search"
                , tag);
        System.out.println(" [.] Received response for " + res);
        return res;
    }

//    @PostMapping
//    public Tag add(@RequestParam String tag) {
//        System.out.println(" [x] Requesting to add tag " + tag);
//        Tag res = (Tag) rabbitTemplate.convertSendAndReceive(exchangeTag.getName(), "save", tag);
//        System.out.println(" [.] Received response for " + res);
//        return res;
//    }

}

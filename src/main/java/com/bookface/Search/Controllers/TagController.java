package com.bookface.Search.Controllers;

import com.bookface.Search.ElasticHandlers.TagElasticHandler;
import com.bookface.Search.Models.Tag;
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
    RabbitTemplate rabbitTemplate;
    @Autowired
    DirectExchange exchangeTag;
    @Autowired
    TagElasticHandler tagElasticHandler;


    @GetMapping
    @Cacheable(value = "tagCache")
    List<String> get(@RequestParam String tag, @RequestParam(defaultValue = "0") String pageNum,
                     @RequestParam(defaultValue = "10") String pageSize) {
        System.out.println(" [x] Requesting tag search for " + tag);
        return tagElasticHandler.search(tag, pageNum, pageSize);
    }

//    @PostMapping
//    public Tag add(@RequestParam String tag) {
//        System.out.println(" [x] Requesting to add tag " + tag);
//        Tag res = (Tag) rabbitTemplate.convertSendAndReceive(exchangeTag.getName(), "save", tag);
//        System.out.println(" [.] Received response for " + res);
//        return res;
//    }

}

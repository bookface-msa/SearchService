package com.bookface.Search.Controllers;

import com.bookface.Search.ElasticHandlers.TagElasticHandler;
import com.bookface.Search.Models.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@SuppressWarnings("unchecked")
@Slf4j
@RestController
@RequestMapping("/search/tags")
public class TagController {


    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    DirectExchange exchangeTag;
    @Autowired
    TagElasticHandler tagElasticHandler;


    @GetMapping
    @Cacheable(value = "tagCache")
    Object[] get(@RequestParam String tag, @RequestParam(defaultValue = "0") String pageNum,
                     @RequestParam(defaultValue = "10") String pageSize) {

        log.info("Requesting tag search for {}", tag);
        return tagElasticHandler.search(tag, pageNum, pageSize).toArray();
    }

}

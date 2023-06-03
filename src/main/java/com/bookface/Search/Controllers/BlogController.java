package com.bookface.Search.Controllers;

import com.bookface.Search.ElasticHandlers.BlogElasticHandler;
import com.bookface.Search.Models.Blog;
import com.bookface.Search.Records.BlognUser;
import com.bookface.Search.Records.PageSettings;
import com.bookface.Search.Repos.BlogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@SuppressWarnings("unchecked")
@Slf4j
@RestController
@RequestMapping("/search/blog")
public class BlogController {
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private TopicExchange exchangeBlog;
    @Autowired
    private DirectExchange exchangeTag;
    @Autowired
    private BlogElasticHandler blogElasticHandler;

    @Autowired
    TagController tagController;

    @GetMapping
    List<BlognUser> get(@RequestParam String query, @RequestParam(defaultValue = "0") String pageNum,
                        @RequestParam(defaultValue = "10") String pageSize) {
        log.info("Searching for Blogs with query \"{}\"", query);

        return blogElasticHandler.search(new PageSettings(query, pageNum, pageSize));
    }

    @GetMapping("/tag")
    List<BlognUser> getTag(@RequestParam String tag, @RequestParam(defaultValue = "0") String pageNum,
                           @RequestParam(defaultValue = "10") String pageSize) {
        log.info("Searching for Blogs with tag \"{}\"", tag);

        return blogElasticHandler.searchTags(new PageSettings(tag, pageNum, pageSize));
    }


    @RabbitListener(queues = "elastic.blogs.create")
    void add(Blog blog) {
        log.info("Creating blog \"{}\"", blog.getTitle());

       Blog res = blogElasticHandler.add(blog);

        for (String t : blog.getTags()) {

            rabbitTemplate.convertAndSend(exchangeTag.getName(), "save", t); //no need to wait for response
        }
        log.info("Created blog with id {}", res.getId());


    }


    @RabbitListener(queues = "elastic.blogs.update")
    void editBlog( Blog blog) throws ResponseStatusException {
        Blog result = blogElasticHandler.edit(blog);

        if(result == null)
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Blog with id " + blog.getId() + " was not found");
        else
            log.info("Edited blog with id {}", blog.getId());

    }


    @RabbitListener(queues = "elastic.blogs.delete")
    void delete(String id) {
        blogElasticHandler.delete(id);
        log.info("Deleted blog with id {}", id);

    }

}

package com.bookface.Search.Controllers;

import com.bookface.Search.ElasticHandlers.BlogElasticHandler;
import com.bookface.Search.Models.Blog;
import com.bookface.Search.Records.BlognUser;
import com.bookface.Search.Records.PageSettings;
import com.bookface.Search.Repos.BlogRepository;
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
@RestController
@RequestMapping("/blog")
public class BlogController {
    BlogRepository blogRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private TopicExchange exchangeBlog;
    @Autowired
    private DirectExchange exchangeTag;
    @Autowired
    private BlogElasticHandler blogElasticHandler;

    public BlogController(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @Autowired
    TagController tagController;

    @GetMapping
    List<BlognUser> get(@RequestParam String content, @RequestParam(defaultValue = "0") String pageNum,
                        @RequestParam(defaultValue = "10") String pageSize) {

        return blogElasticHandler.search(new PageSettings(content, pageNum, pageSize));
    }

    @GetMapping("/tag")
    List<BlognUser> getTag(@RequestParam String content, @RequestParam(defaultValue = "0") String pageNum,
                           @RequestParam(defaultValue = "10") String pageSize) {

        return blogElasticHandler.searchTags(new PageSettings(content, pageNum, pageSize));
    }

//    @PostMapping
    @RabbitListener(queues = "elastic.blogs.create")
    void add(Blog blog) {

       Blog res = blogElasticHandler.add(blog);

        for (String t : blog.getTags()) {
            //tagController.add(t);
            rabbitTemplate.convertAndSend(exchangeTag.getName(), "save", t); //no need to wait for response
        }
//        return res;
    }

//    @PutMapping
    @RabbitListener(queues = "elastic.blogs.update")
    void editBlog( Blog blog) throws ResponseStatusException {
        Blog result = blogElasticHandler.edit(blog);
        System.out.println(result);
//        if (result != null) return (Blog) result;
        if(result == null)
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Blog with id " + blog.getId() + " was not found");
    }

//    @DeleteMapping
    @RabbitListener(queues = "elastic.blogs.delete")
    void delete(String id) {
        blogElasticHandler.delete(id);
    }

}

package com.bookface.Search.Controllers;

import com.bookface.Search.Models.Blog;
import com.bookface.Search.Models.Tag;
import com.bookface.Search.Models.User;
import com.bookface.Search.Records.BlognUser;
import com.bookface.Search.Records.PageSettings;
import com.bookface.Search.Repos.BlogRepository;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/blog")
public class BlogController {
    BlogRepository blogRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DirectExchange exchangeBlog;
    @Autowired
    private DirectExchange exchangeTag;

    public BlogController(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @Autowired
    TagController tagController;

    @GetMapping
    @Cacheable(value = "blogCache")
    List<BlognUser> get(@RequestParam String content, @RequestParam(defaultValue = "0") String pageNum,
                        @RequestParam(defaultValue = "10") String pageSize) {

        System.out.println(" [x] Requesting blog search for " + content);
        List<BlognUser> res = (List<BlognUser>) rabbitTemplate.convertSendAndReceive(exchangeBlog.getName(), "search"
                , new PageSettings(content, pageNum, pageSize));
        System.out.println(" [.] Received response for " + content);

        return res;
    }

    @GetMapping("/tag")
    @Cacheable(value = "blogCache")
    List<BlognUser> getTag(@RequestParam String content, @RequestParam(defaultValue = "0") String pageNum,
                           @RequestParam(defaultValue = "10") String pageSize) {
        System.out.println(" [x] Requesting blog search for tag " + content);
        List<BlognUser> res = (List<BlognUser>) rabbitTemplate.convertSendAndReceive(exchangeBlog.getName(),
                "searchTags", new PageSettings(content, pageNum, pageSize));
        System.out.println(" [.] Received response for tag " + content);
        return res;
    }

    @PostMapping
    Blog add(@RequestBody Blog blog) {
        //System.out.println(blog.getDate());
        Blog res = (Blog) rabbitTemplate.convertSendAndReceive(exchangeBlog.getName(), "create", blog);

        for (String t : blog.getTags()) {
//            tagController.add(t);
            rabbitTemplate.convertAndSend(exchangeTag.getName(), "save", t); //no need to wait for response
        }

        return res;
    }

    @PutMapping
    Blog editBlog(@RequestBody Blog blog) throws ResponseStatusException {
        Object result = rabbitTemplate.convertSendAndReceive(exchangeBlog.getName(), "update", blog);
        System.out.println(result);
        if (result != null) return (Blog) result;
        throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Blog with id " + blog.getId() + " was not found");
    }

    @DeleteMapping
    void delete(@RequestParam String id) {
        rabbitTemplate.convertSendAndReceive(exchangeBlog.getName(), "delete", id);
    }

}

package com.bookface.Search.Controllers;

import com.bookface.Search.Models.Blog;
import com.bookface.Search.Models.Tag;
import com.bookface.Search.Models.User;
import com.bookface.Search.Repos.BlogRepository;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/blog")
public class BlogController {

    BlogRepository blogRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DirectExchange exchange;

    public BlogController(BlogRepository blogRepository){
        this.blogRepository = blogRepository;
    }

    @GetMapping
    List<BlognUser> get(@RequestParam String content){
        System.out.println(content);
        List<Blog> result = blogRepository.findByContentOrTitle(content, content);

        List<BlognUser> res = new ArrayList<>();

        for(Blog b: result){
            System.out.println(" [x] Requesting to update " + b.getAuthorId());
            User user = (User) rabbitTemplate.convertSendAndReceive(exchange.getName(), "getById", b.getAuthorId());
            System.out.println(" [.] Got " + user);
            BlognUser bu = new BlognUser(b, user);
            res.add(bu);
        }
        return res;
    }

    record BlognUser(Blog blog, User user){};


    @RequestMapping("Tag") //blogTag
    @GetMapping
    List<Blog> getTag(@RequestParam String tag){
        List<Blog> result = blogRepository.findByTagUsingDeclaredQuery(tag);
        return result;
    }


    @PostMapping
    Blog add(@RequestBody Blog blog){
      //  System.out.println(blog.getTags()[0]);
        Blog result = blogRepository.save(blog);
        return result;
    }

    @PutMapping
    Blog editBlog(@RequestBody Blog blog){

        Optional<Blog> oldBlog = blogRepository.findById(blog.getId());

        if(oldBlog.isPresent()){
            Blog updated = oldBlog.get();
            updated.setDate(blog.getDate() == null? updated.getDate() : blog.getDate());
            updated.setTags(blog.getTags() == null? updated.getTags() : blog.getTags());
            updated.setContent(blog.getContent() == null? updated.getContent() : blog.getContent());
            updated.setTitle(blog.getTitle() == null? updated.getTitle() : blog.getTitle());
            updated.setAuthorId(blog.getAuthorId() == null? updated.getAuthorId() : blog.getAuthorId());
            Blog result = blogRepository.save(updated);
            return result;
        }else{
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Blog with id " + blog.getId() + " was not found");
        }
    }
    @DeleteMapping
    void delete(@RequestParam String id){
        blogRepository.deleteById(id);

        System.out.println("Deleted blog " + id);
    }

}

package com.bookface.Search.Controllers;

import com.bookface.Search.Models.Blog;
import com.bookface.Search.Repos.BlogRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    BlogRepository blogRepository;

    @GetMapping
    List<Blog> get(@RequestParam String content){
        List<Blog> result = blogRepository.findByContent(content);
        return result;
    }


    @PostMapping
    Blog add(@RequestBody Blog blog){
        Blog result = blogRepository.save(blog);

        System.out.println(result.getTags());
        return result;
    }

    @DeleteMapping
    void delete(@RequestParam String id){
        blogRepository.deleteById(id);

        System.out.println("Deleted blog " + id);
    }

}

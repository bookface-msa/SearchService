package com.bookface.Search.ElasticHandlers;

import com.bookface.Search.Controllers.UserController;
import com.bookface.Search.Models.Blog;
import com.bookface.Search.Models.User;
import com.bookface.Search.Records.BlognUser;
import com.bookface.Search.Records.PageSettings;
import com.bookface.Search.Repos.BlogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class BlogElasticHandler {
    @Autowired
    BlogRepository blogRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DirectExchange exchangeUser;

    @Autowired
    private UserController userController;

    @Autowired
    private UserElasticHandler userElasticHandler;

    public List<BlognUser> getUsersForBlogs(List<Blog> blogs) {
        List<BlognUser> res = new ArrayList<BlognUser>();

        for (Blog b : blogs) {
            User user = b.getAuthorId() == null? null :userElasticHandler.getUserWithId(b.getAuthorId()).user();
            BlognUser bu = new BlognUser(b, user);
            res.add(bu);
        }

        return res;
    }

    @Cacheable(value = "blogCache")
    public List<BlognUser> search(PageSettings pageSettings) {

        String content = pageSettings.content();
        int pageNum = Integer.parseInt(pageSettings.pageNum());
        int pageSize = Integer.parseInt(pageSettings.pageSize());
        Page<Blog> blogs = blogRepository.findByBodyOrTitle(content, content, PageRequest.of(pageNum, pageSize));

        return getUsersForBlogs(blogs.getContent());
    }


    @Cacheable(value = "blogCache")
    public List<BlognUser> searchTags(PageSettings pageSettings) {
        String content = pageSettings.content();
        int pageNum = Integer.parseInt(pageSettings.pageNum());
        int pageSize = Integer.parseInt(pageSettings.pageSize());
        Page<Blog> blogs = blogRepository.findByTagUsingDeclaredQuery(content, PageRequest.of(pageNum, pageSize));

        return getUsersForBlogs(blogs.getContent());
    }


    public Blog add(Blog blog) {
        return blogRepository.save(blog);
    }

    public Blog edit(Blog blog) {
        Optional<Blog> oldBlog = blogRepository.findById(blog.getId());

        if (oldBlog.isPresent()) {
            Blog updated = oldBlog.get();
            updated.setUpdatedAt(blog.getUpdatedAt() == null ? updated.getUpdatedAt() : blog.getUpdatedAt());
            updated.setCreatedAt(blog.getCreatedAt() == null ? updated.getCreatedAt() : blog.getCreatedAt());
            updated.setTags(blog.getTags() == null ? updated.getTags() : blog.getTags());
            updated.setBody(blog.getBody() == null ? updated.getBody() : blog.getBody());
            updated.setTitle(blog.getTitle() == null ? updated.getTitle() : blog.getTitle());
            updated.setAuthorId(blog.getAuthorId() == null ? updated.getAuthorId() : blog.getAuthorId());

            return blogRepository.save(updated);
        }
        return null;
    }


    public void delete(String id) {
        blogRepository.deleteById(id);
    }


}

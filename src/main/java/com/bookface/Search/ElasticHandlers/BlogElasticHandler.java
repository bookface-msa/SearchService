package com.bookface.Search.ElasticHandlers;

import com.bookface.Search.Controllers.UserController;
import com.bookface.Search.Models.Blog;
import com.bookface.Search.Models.User;
import com.bookface.Search.Records.BlognUser;
import com.bookface.Search.Records.PageSettings;
import com.bookface.Search.Repos.BlogRepository;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            User user = userElasticHandler.getUserWithId(b.getAuthorId()).user();
//            User user = userController.getUserById(b.getAuthorId());
            BlognUser bu = new BlognUser(b, user);
            res.add(bu);
        }

        return res;
    }

    @RabbitListener(queues = "elastic.blogs.search")
    public List<BlognUser> search(PageSettings pageSettings) {

        System.out.println("------Blog Search for: " + pageSettings.content());
        String content = pageSettings.content();
        int pageNum = Integer.parseInt(pageSettings.pageNum());
        int pageSize = Integer.parseInt(pageSettings.pageSize());
        Page<Blog> blogs = blogRepository.findByBodyOrTitle(content, content, PageRequest.of(pageNum, pageSize));
        List<BlognUser> res = getUsersForBlogs(blogs.getContent());
        return res;
    }

    @RabbitListener(queues = "elastic.blogs.searchTags")
    public List<BlognUser> searchTags(PageSettings pageSettings) {
        String content = pageSettings.content();
        int pageNum = Integer.parseInt(pageSettings.pageNum());
        int pageSize = Integer.parseInt(pageSettings.pageSize());
        Page<Blog> blogs = blogRepository.findByTagUsingDeclaredQuery(content, PageRequest.of(pageNum, pageSize));
        List<BlognUser> res = getUsersForBlogs(blogs.getContent());
        return res;
    }

//    @RabbitListener(queues = "elastic.blogs.create")
    public Blog add(Blog blog) {
        Blog result = blogRepository.save(blog);
        System.out.println("Blog with id " + blog.getId() + " added to elasticsearch");
        return result;
    }

//    @RabbitListener(queues = "elastic.blogs.update")
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
            Blog result = blogRepository.save(updated);
            return result;
        }
        return null;
    }

//    @RabbitListener(queues = "elastic.blogs.delete")
    public void delete(String id) {
        blogRepository.deleteById(id);
    }


}

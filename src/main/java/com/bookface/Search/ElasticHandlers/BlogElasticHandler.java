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

    List<BlognUser> getUsersForBlogs(List<Blog> blogs) {
        List<BlognUser> res = new ArrayList<BlognUser>();

        for (Blog b : blogs) {
            User user = userController.getUserById(b.getAuthorId());
            BlognUser bu = new BlognUser(b, user);
            res.add(bu);
        }

        return res;
    }

    @RabbitListener(queues = "elastic.blogs.search")
    List<BlognUser> search(PageSettings pageSettings) {
        String content = pageSettings.content();
        int pageNum = Integer.parseInt(pageSettings.pageNum());
        int pageSize = Integer.parseInt(pageSettings.pageSize());
        Page<Blog> blogs = blogRepository.findByContentOrTitle(content, content, PageRequest.of(pageNum, pageSize));
        List<BlognUser> res = getUsersForBlogs(blogs.getContent());
        return res;
    }

    @RabbitListener(queues = "elastic.blogs.searchTags")
    List<BlognUser> searchTags(PageSettings pageSettings) {
        String content = pageSettings.content();
        int pageNum = Integer.parseInt(pageSettings.pageNum());
        int pageSize = Integer.parseInt(pageSettings.pageSize());
        Page<Blog> blogs = blogRepository.findByTagUsingDeclaredQuery(content, PageRequest.of(pageNum, pageSize));
        List<BlognUser> res = getUsersForBlogs(blogs.getContent());
        return res;
    }

    @RabbitListener(queues = "elastic.blogs.create")
    Blog add(Blog blog) {
        Blog result = blogRepository.save(blog);
        return result;
    }

    @RabbitListener(queues = "elastic.blogs.update")
    Blog edit(Blog blog) {
        Optional<Blog> oldBlog = blogRepository.findById(blog.getId());

        if (oldBlog.isPresent()) {
            Blog updated = oldBlog.get();
            updated.setDate(blog.getDate() == null ? updated.getDate() : blog.getDate());
            updated.setTags(blog.getTags() == null ? updated.getTags() : blog.getTags());
            updated.setContent(blog.getContent() == null ? updated.getContent() : blog.getContent());
            updated.setTitle(blog.getTitle() == null ? updated.getTitle() : blog.getTitle());
            updated.setAuthorId(blog.getAuthorId() == null ? updated.getAuthorId() : blog.getAuthorId());
            Blog result = blogRepository.save(updated);
            return result;
        }
        return null;
    }

    @RabbitListener(queues = "elastic.blogs.delete")
    void delete(String id) {
        blogRepository.deleteById(id);
    }


}

package com.bookface.Search.Messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@EnableRabbit
@Configuration
public class RabbitConfig {

    @Bean
    public MessageConverter converter() {
        ObjectMapper mapper = JsonMapper.builder() // or different mapper for other format
                .addModule(new Jdk8Module())
                .addModule(new JavaTimeModule())
                .build();

        return new Jackson2JsonMessageConverter(mapper);
    }

//  ==========================USERS===========================

    @Bean
    DirectExchange exchangeUser() {
        return new DirectExchange("elastic.users");
    }
    @Bean
    public Queue queueFindUser() {
        return new Queue("elastic.users.getAll");
    }

    @Bean
    public Queue queueCreateUser() {
        return new Queue("elastic.users.addUser");
    }

    @Bean
    public Queue queueFindUserById() {
        return new Queue("elastic.users.getById");
    }

    @Bean
    public Queue queueDelUser(){ return new Queue("elastic.users.delUser"); }


    @Bean
    Binding bindingFindUser(DirectExchange exchangeUser, Queue queueFindUser) {
        return BindingBuilder.bind(queueFindUser).to(exchangeUser).with("getAll");
    }
    @Bean
    Binding bindingCreateUser(DirectExchange exchangeUser, Queue queueCreateUser) {
        return BindingBuilder.bind(queueCreateUser).to(exchangeUser).with("addUser");
    }
    @Bean
    Binding bindingFindUserById(DirectExchange exchangeUser, Queue queueFindUserById) {
        return BindingBuilder.bind(queueFindUserById).to(exchangeUser).with("getById");
    }

    @Bean
    Binding bindingDelUser(DirectExchange exchangeUser, Queue queueDelUser){
        return BindingBuilder.bind(queueDelUser).to(exchangeUser).with("delUser");
    }

//  ==========================BLOGS===========================
    @Bean
    TopicExchange exchangeBlog() { return new TopicExchange("elastic.blogs");}

    @Bean
    public Queue queueFindBlog() { return new Queue("elastic.blogs.search");}

    @Bean
    public Queue queueFindTags(){ return new Queue("elastic.blogs.searchTags");}

    @Bean
    public Queue queueCreateBlog(){return new Queue("elastic.blogs.create");}

    @Bean
    public Queue queueUpdateBlog(){return new Queue("elastic.blogs.update");}

    @Bean
    public Queue queueDeleteBlog(){return new Queue("elastic.blogs.delete");}
    @Bean
    Binding bindingFindBlog(TopicExchange exchangeBlog, Queue queueFindBlog){
        return BindingBuilder.bind(queueFindBlog).to(exchangeBlog).with("search");
    }
    @Bean
    Binding bindingFindTags(TopicExchange exchangeBlog, Queue queueFindTags){
        return BindingBuilder.bind(queueFindTags).to(exchangeBlog).with("searchTags");
    }
    @Bean
    Binding bindingCreateBlog(TopicExchange exchangeBlog, Queue queueCreateBlog){
        return BindingBuilder.bind(queueCreateBlog).to(exchangeBlog).with("create");
    }
    @Bean
    Binding bindingUpdateBlog(TopicExchange exchangeBlog, Queue queueUpdateBlog){
        return BindingBuilder.bind(queueUpdateBlog).to(exchangeBlog).with("update");
    }
    @Bean
    Binding bindingDeleteBlog(TopicExchange exchangeBlog, Queue queueDeleteBlog){
        return BindingBuilder.bind(queueDeleteBlog).to(exchangeBlog).with("delete");
    }

//  ==========================TAGS===========================

    @Bean
    DirectExchange exchangeTag() { return new DirectExchange("elastic.tags");}

    @Bean
    public Queue queueSearchTags() { return new Queue("elastic.tags.search");}

    @Bean
    public Queue queueSaveTags(){ return new Queue("elastic.tags.save");}

    @Bean
    Binding bindingSearchTags(DirectExchange exchangeTag, Queue queueSearchTags){
        return BindingBuilder.bind(queueSearchTags).to(exchangeTag).with("search");
    }
    @Bean
    Binding bindingSaveTag(DirectExchange exchangeTag, Queue queueSaveTags) {
        return BindingBuilder.bind(queueSaveTags).to(exchangeTag).with("save");
    }

}
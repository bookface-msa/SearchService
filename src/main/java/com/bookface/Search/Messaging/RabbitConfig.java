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
    TopicExchange exchangeUser() {
        return new TopicExchange("elastic.users");
    }
    @Bean
    public Queue queueFindUser() {
        return new Queue("elastic.users.getAll");
    }


    @Bean
    public Queue queueFindUserById() {
        return new Queue("elastic.users.getById");
    }

    @Bean
    public Queue queueCreateUser() {
        return new Queue("elastic.users.create");
    }
    @Bean
    public Queue queueDelUser(){ return new Queue("elastic.users.delete"); }

    @Bean
    public Queue queueUpdateUser(){ return new Queue("elastic.users.update"); }


    @Bean
    Binding bindingFindUser(TopicExchange exchangeUser, Queue queueFindUser) {
        return BindingBuilder.bind(queueFindUser).to(exchangeUser).with("getAll");
    }
    @Bean
    Binding bindingCreateUser(TopicExchange exchangeUser, Queue queueCreateUser) {
        return BindingBuilder.bind(queueCreateUser).to(exchangeUser).with("create");
    }
    @Bean
    Binding bindingFindUserById(TopicExchange exchangeUser, Queue queueFindUserById) {
        return BindingBuilder.bind(queueFindUserById).to(exchangeUser).with("getById");
    }

    @Bean
    Binding bindingDelUser(TopicExchange exchangeUser, Queue queueDelUser){
        return BindingBuilder.bind(queueDelUser).to(exchangeUser).with("delete");
    }

    @Bean
    Binding bindingUpdateUser(TopicExchange exchangeUser, Queue queueUpdateUser){
        return BindingBuilder.bind(queueUpdateUser).to(exchangeUser).with("delete");
    }

//  ==========================BLOGS===========================
    @Bean
    TopicExchange exchangeBlog() { return new TopicExchange("elastic.blogs");}

    @Bean
    public Queue queueCreateBlog(){return new Queue("elastic.blogs.create");}

    @Bean
    public Queue queueUpdateBlog(){return new Queue("elastic.blogs.update");}

    @Bean
    public Queue queueDeleteBlog(){return new Queue("elastic.blogs.delete");}

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
    public Queue queueSaveTags(){ return new Queue("elastic.tags.save");}

    @Bean
    Binding bindingSaveTag(DirectExchange exchangeTag, Queue queueSaveTags) {
        return BindingBuilder.bind(queueSaveTags).to(exchangeTag).with("save");
    }

}
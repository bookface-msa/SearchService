package com.bookface.Search.Messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
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
        return new Jackson2JsonMessageConverter();
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
    DirectExchange exchangeBlog() { return new DirectExchange("elastic.blogs");}

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
    Binding bindingFindBlog(DirectExchange exchangeBlog, Queue queueFindBlog){
        return BindingBuilder.bind(queueFindBlog).to(exchangeBlog).with("search");
    }
    @Bean
    Binding bindingFindTags(DirectExchange exchangeBlog, Queue queueFindTags){
        return BindingBuilder.bind(queueFindTags).to(exchangeBlog).with("searchTags");
    }
    @Bean
    Binding bindingCreateBlog(DirectExchange exchangeBlog, Queue queueCreateBlog){
        return BindingBuilder.bind(queueCreateBlog).to(exchangeBlog).with("create");
    }
    @Bean
    Binding bindingUpdateBlog(DirectExchange exchangeBlog, Queue queueUpdateBlog){
        return BindingBuilder.bind(queueUpdateBlog).to(exchangeBlog).with("update");
    }
    @Bean
    Binding bindingDeleteBlog(DirectExchange exchangeBlog, Queue queueDeleteBlog){
        return BindingBuilder.bind(queueDeleteBlog).to(exchangeBlog).with("delete");
    }

}
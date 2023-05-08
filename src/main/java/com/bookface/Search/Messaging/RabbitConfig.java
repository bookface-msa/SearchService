package com.bookface.Search.Messaging;

import com.bookface.Search.Controllers.UserController;
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
    DirectExchange exchange() {
        return new DirectExchange("elastic.users");
    }
    @Bean
    public Queue queue0() {
        return new Queue("elastic.users.getAll");
    }

    @Bean
    public Queue queue1() {
        return new Queue("elastic.users.addUser");
    }

    @Bean
    public Queue queue2() {
        return new Queue("elastic.users.getById");
    }


    @Bean
    Binding binding0(DirectExchange exchange, Queue queue0) {
        return BindingBuilder.bind(queue0).to(exchange).with("getAll");
    }
    @Bean
    Binding binding1(DirectExchange exchange, Queue queue1) {
        return BindingBuilder.bind(queue1).to(exchange).with("addUser");
    }
    @Bean
    Binding binding2(DirectExchange exchange, Queue queue2) {
        return BindingBuilder.bind(queue2).to(exchange).with("getById");
    }

    @Bean
        public MessageConverter converter() {
            return new Jackson2JsonMessageConverter();
    }
}
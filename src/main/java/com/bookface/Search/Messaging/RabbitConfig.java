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
import org.springframework.context.annotation.Profile;


@EnableRabbit
@Configuration
public class RabbitConfig {

    @Bean
    public Queue queue() {
        return new Queue("elastic.reqs");
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange("elastic");
    }

    @Bean
    Binding testeBinding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("reqs");
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
}
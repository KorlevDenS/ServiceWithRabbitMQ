package com.korolev.dens.micro;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttDataRabbitConfig {

    //Name the queue
    final static String YIHON_WQ_M = "topic.mqtt.data.yihon.wq.m";


    // Create a queue
    @Bean
    public Queue yihonWqmQueue() {
        return new Queue(MqttDataRabbitConfig.YIHON_WQ_M);
    }

    //Direct Exchange Name: directExchange
    @Bean
    DirectExchange directExchange() {
        return new DirectExchange("directExchange");
    }

    //Binding Bind the queue to the exchange, and set the matching key: directExchange
    @Bean
    Binding bindingYihonWqmMessage(Queue yihonWqmQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(yihonWqmQueue).to(directExchange).with("yihonWqm");
    }

}

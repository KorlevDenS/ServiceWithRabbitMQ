package com.korolev.dens.micro;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * mqtt client message processing class
 * **/
@Slf4j
@Component
public class MqttReceiveHandle {

    //Use RabbitTemplate, which provides methods for receiving/sending, etc.
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void handle(Message<?> message) {
        log.info("Subject: {}, QOS:{}, message received data: {}", message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC),
                message.getHeaders().get(MqttHeaders.RECEIVED_QOS), message.getPayload());
        //Send to the rabbit message queue
        rabbitTemplate.convertAndSend("directExchange", "yihonWqm", (String) message.getPayload());

    }
}
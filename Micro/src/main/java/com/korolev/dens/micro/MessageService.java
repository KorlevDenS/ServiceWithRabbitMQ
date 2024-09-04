package com.korolev.dens.micro;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.rabbitmq.jms.admin.RMQConnectionFactory;
import com.rabbitmq.jms.admin.RMQDestination;
import com.rabbitmq.jms.client.message.RMQBytesMessage;
import jakarta.jms.*;

@Service
public class MessageService {

    private final String host;

    private final Integer port;

    private final String userName;

    private final String password;

    private final String queueName;

    public MessageService(
            @Value("${spring.rabbitmq.host}") String host,
            @Value("${spring.rabbitmq.port}") Integer port,
            @Value("${spring.rabbitmq.username}") String userName,
            @Value("${spring.rabbitmq.password}") String password,
            @Value("${spring.rabbitmq.template.exchange}") String queueName
    ) {
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.queueName = queueName;

    }

    @Async
    public void startListener() {
        RMQConnectionFactory factory = new RMQConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(userName);
        factory.setPassword(password);

        RMQDestination jmsDestination = new RMQDestination();
        jmsDestination.setAmqp(true);
        jmsDestination.setAmqpQueueName(queueName);

        try (Connection connection = factory.createConnection()) {
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(jmsDestination);

            while (true) {
                Message message = consumer.receive();
                if (message instanceof RMQBytesMessage) {
                    byte[] bytes = new byte[(int) ((RMQBytesMessage) message).getBodyLength()];
                    ((RMQBytesMessage) message).readBytes(bytes);

                    System.out.println("ПолуЧено сообщение: " + new String(bytes));
                }
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}

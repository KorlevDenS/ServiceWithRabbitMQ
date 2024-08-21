package com.korolev.dens.micro;


import com.rabbitmq.jms.admin.RMQConnectionFactory;
import com.rabbitmq.jms.admin.RMQDestination;
import com.rabbitmq.jms.client.message.RMQBytesMessage;
import jakarta.jms.*;

public class Main {

    public static void main(String[] args) {
        RMQConnectionFactory factory = new RMQConnectionFactory();
        factory.setHost("localhost"); // Укажите хост RabbitMQ
        factory.setPort(5672); // Порт по умолчанию
        factory.setUsername("user"); // Имя пользователя
        factory.setPassword("password"); // Пароль


        RMQDestination jmsDestination = new RMQDestination();
        jmsDestination.setAmqp(true);
        jmsDestination.setAmqpQueueName("exampleQueue");


        try (Connection connection = factory.createConnection()) {
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(jmsDestination);


            while (true) {
                Message message = consumer.receive();

                if (message instanceof RMQBytesMessage) {
                    byte[] bytes = new byte[(int) ((RMQBytesMessage) message).getBodyLength()];
                    ((RMQBytesMessage) message).readBytes(bytes);

                    System.out.println("Получено сообщение: " + new String(bytes));
                }

            }

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
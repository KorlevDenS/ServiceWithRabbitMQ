package com.korolev.dens.micro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MicroApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MicroApplication.class, args);
        MessageService messageService = context.getBean(MessageService.class);
        messageService.startListener();
    }

}

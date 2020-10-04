package com.example.demo.consumers.optional.lab;

import com.example.demo.consumers.optional.expelling.ExpellingStatement;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
        "com.example.demo.consumers.optional.lab",
        "com.example.demo.models",
        "com.example.demo.utils"
})
public class LabStatement implements CommandLineRunner {

    private static final String LAB_QUEUE_NAME = "lab_queue";

    public static void main(String[] args) {
        SpringApplication.run(LabStatement.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.basicConsume(LAB_QUEUE_NAME, true, (tag, message) -> {}, tag -> {});
    }
}

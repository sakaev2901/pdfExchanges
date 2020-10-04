package com.example.demo.consumers.optional.expelling;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
        "com.example.demo.consumers.optional.expelling",
        "com.example.demo.models",
        "com.example.demo.utils"
})
public class ExpellingStatement implements CommandLineRunner {

    private static final String EXPELLING_QUEUE_NAME = "expelling_queue";

    public static void main(String[] args) {
        SpringApplication.run(ExpellingStatement.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.basicConsume(EXPELLING_QUEUE_NAME, true, (tag, message) -> {
            System.out.println(message.getBody());
        }, tag -> {});
    }
}

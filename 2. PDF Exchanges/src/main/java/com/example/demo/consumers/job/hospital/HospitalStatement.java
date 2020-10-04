package com.example.demo.consumers.job.hospital;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
        "com.example.demo.consumers.job.hospital",
        "com.example.demo.models",
        "com.example.demo.utils"
})
public class HospitalStatement implements CommandLineRunner {

    private static final String HOSPITAL_QUEUE_NAME = "file.job.hospital";
    private static final String EXCHANGE_NAME = "fanout_ex";


    public static void main(String[] args) {
        SpringApplication.run(HospitalStatement.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue, EXCHANGE_NAME, "");
        channel.basicConsume(queue, true, (tag, message) -> {
            System.out.println(new String(message.getBody()));
        }, tag -> {});
    }
}

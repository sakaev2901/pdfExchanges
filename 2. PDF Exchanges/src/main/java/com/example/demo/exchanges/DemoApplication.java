package com.example.demo.exchanges;

import com.example.demo.models.Statement;
import com.example.demo.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Date;

@SpringBootApplication
@ComponentScan({
        "com.example.demo.exchanges",
        "com.example.demo.config",
        "com.example.demo.controllers"
})
public class DemoApplication implements CommandLineRunner {

    @Autowired
    private ObjectMapper objectMapper;

    private static final String EXPELLING_QUEUE_NAME = "expelling_queue";
    private static final String LAB_QUEUE_NAME = "lab_queue";

    private static final String EXPELLING_QUEUE_BINDING_KEY = "file.optional.expelling";
    private static final String LAB_QUEUE_BINDING_KEY = "file.optional.lab";
    private static final String CAR_SALE_QUEUE_BINDING_KEY = "file.car.sale";
    private static final String CAR_LOSS_QUEUE_BINDING_KEY = "file.car.loss";

    private static final String EXCHANGE_NAME = "docs_topic_ex";
    private static final String OPTIONAL_EXCHANGE_NAME = "docs_direct_ex";
    private static final String JOB_EXCHANGE_NAME = "docs_fanout_ex";

    private static final String EXCHANGE_TYPE = "topic";

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String userData = "Эльдар Сакаев Мунирович 29.01.2000 8800-32234";
        User user = User.builder()
                .name("Артур")
                .surname("Артур")
                .birthday("2222")
                .patronymic("sdf")
                .passwordId("324243")
                .build();

        Statement statement = Statement.builder()
                .user(user)
                .date(new Date())
                .build();

        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);

        channel.basicPublish(EXCHANGE_NAME, CAR_SALE_QUEUE_BINDING_KEY, null, objectMapper.writeValueAsBytes(statement));
        channel.basicPublish(EXCHANGE_NAME, CAR_LOSS_QUEUE_BINDING_KEY, null, userData.getBytes());

        channel.exchangeDeclare(OPTIONAL_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueBind(EXPELLING_QUEUE_NAME, OPTIONAL_EXCHANGE_NAME, EXPELLING_QUEUE_BINDING_KEY);
        channel.queueBind(LAB_QUEUE_NAME, OPTIONAL_EXCHANGE_NAME, LAB_QUEUE_BINDING_KEY);

        channel.basicPublish(OPTIONAL_EXCHANGE_NAME, LAB_QUEUE_BINDING_KEY, null, "hellos".getBytes());
        channel.basicPublish(OPTIONAL_EXCHANGE_NAME, EXPELLING_QUEUE_BINDING_KEY , null, "hellos".getBytes());


    }
}

package com.example.demo.consumers.job.fire;

import com.example.demo.models.User;
import com.example.demo.utils.PdfGenerator;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
        "com.example.demo.consumers.job.fire",
        "com.example.demo.models",
        "com.example.demo.utils"
})
public class FireStatement  implements CommandLineRunner {

    @Autowired
    private PdfGenerator pdfGenerator;

    private static final String FIRE_QUEUE_NAME = "file.job.fire";
    private static final String EXCHANGE_NAME = "fanout_ex";

    public static void main(String[] args) {
        SpringApplication.run(FireStatement.class, args);
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
//            pdfGenerator.generatePdfDocs(User.from(new String(message.getBody())), message.getEnvelope().getRoutingKey());

        }, tag -> {});
    }
}

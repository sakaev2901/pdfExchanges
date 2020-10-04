package com.example.demo.consumers.car.main;


import com.example.demo.models.Statement;
import com.example.demo.models.User;
import com.example.demo.utils.PdfGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        "com.example.demo.consumers.car.main",
        "com.example.demo.models",
        "com.example.demo.utils",
        "com.example.demo.config"
})
public class CarStatements implements CommandLineRunner {

    @Autowired
    private PdfGenerator pdfGenerator;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String LOSS_ROUTING_KEY= "file.car.*";
    private static final String DOCS_EXCHANGE = "docs_topic_ex";

    public static void main(String[] args) {
        SpringApplication.run(CarStatements.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, DOCS_EXCHANGE, LOSS_ROUTING_KEY);
        channel.basicConsume(queueName, true, (tag, message) -> {
            Statement statement = objectMapper.readValue(message.getBody(), Statement.class);
            pdfGenerator.generatePdfDocs(statement, message.getEnvelope().getRoutingKey());
        }, tag -> {});
    }
}

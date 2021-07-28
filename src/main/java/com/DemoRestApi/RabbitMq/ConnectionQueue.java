package com.DemoRestApi.RabbitMq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Data;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeoutException;

@Data
public class ConnectionQueue {
    public final static String QUEUE_NAME = "queue";
    private static ConnectionFactory factory;

    public static Channel getChannel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setVirtualHost("/");
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        return channel;
    }
}

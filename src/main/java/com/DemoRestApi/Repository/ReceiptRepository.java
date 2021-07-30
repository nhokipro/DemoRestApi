package com.DemoRestApi.Repository;

import com.DemoRestApi.ConnectRedis.ConnectionPoolRedis;
import com.DemoRestApi.Entity.Receipt;
import com.DemoRestApi.RabbitMq.ConnectionQueue;
import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import static com.DemoRestApi.RabbitMq.ConnectionQueue.QUEUE_NAME;

@Data
@Slf4j
public class ReceiptRepository {

    ConnectionQueue connectionQueue;
    Gson gson;
    String getReply;

    public ReceiptRepository() {
        connectionQueue = new ConnectionQueue();
        gson = new Gson();
    }

    public String pushReceipt(Receipt receipt) throws IOException, TimeoutException, InterruptedException {
        log.info("start pushReceipt");
        Channel channel = ConnectionQueue.getChannel();
        String replyQueueName = channel.queueDeclare().getQueue();
        final String corrId = UUID.randomUUID().toString();
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", QUEUE_NAME, props, gson.toJson(receipt).getBytes());

        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

        String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response.offer(new String(delivery.getBody(), StandardCharsets.UTF_8));
            }
        }, consumerTag -> {
        });
        getReply = response.take();
        return getReply;
    }

    public Boolean existByTokenAndCreateDate(String token, Date createDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Jedis jedis = ConnectionPoolRedis.getJedisPool().getResource();

            List<String> listCreateDateInRedis = jedis.lrange("createDates", 0, jedis.llen("createDates"));
            for (int i = 0; i < listCreateDateInRedis.size(); i++) {
                if (createDate.getDay() != simpleDateFormat.parse(listCreateDateInRedis.get(i)).getDay() &&
                        createDate.getMonth() != simpleDateFormat.parse(listCreateDateInRedis.get(i)).getMonth() &&
                        createDate.getYear() != simpleDateFormat.parse(listCreateDateInRedis.get(i)).getYear()) {
                    jedis.del("createDates");
                    jedis.del("tokens");
                }
            }

            List<String> listTokenInRedis = jedis.lrange("tokens", 0, jedis.llen("tokens"));
            for (int i = 0; i < listTokenInRedis.size(); i++) {
                if (token.equals(listTokenInRedis.get(i))) {
                    return true;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }
}
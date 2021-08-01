package com.DemoRestApi.Repository;

import com.DemoRestApi.Config.ConvertToLocalDateTime;
import com.DemoRestApi.ConnectRedis.ConnectionPoolRedis;
import com.DemoRestApi.Entity.Receipt;
import com.DemoRestApi.Entity.RestResponseHandle;
import com.DemoRestApi.RabbitMq.ConnectionQueue;
import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public ReceiptRepository() {
        connectionQueue = new ConnectionQueue();
        gson = new Gson();
    }

    public RestResponseHandle pushReceipt(Receipt receipt) throws IOException, TimeoutException, InterruptedException {
        log.info("start pushReceipt");
        Channel channel = ConnectionQueue.getChannel();
        String replyQueueName = channel.queueDeclare().getQueue();
        final String corrId = UUID.randomUUID().toString();
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();
        log.info("send receipt to queue");
        channel.basicPublish("", QUEUE_NAME, props, gson.toJson(receipt).getBytes());
        log.info("create block queue");
        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

        String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                log.info("get Reply of consumer");
                response.offer(new String(delivery.getBody(), StandardCharsets.UTF_8));
            }
        }, consumerTag -> {
        });
        String getReply = response.take();
        log.info("end pushReceipt");
        return gson.fromJson(getReply, RestResponseHandle.class);
    }

    public Boolean existByTokenAndCreateDate(String token, Date createDate) {
        log.info("start existByTokenAndCreateDate");
        LocalDateTime result = ConvertToLocalDateTime.convertToLocalDateTimeViaInstant(createDate);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            log.info("get list of createDates in redis");
            Jedis jedis = ConnectionPoolRedis.getJedisPool().getResource();
            List<String> listCreateDateInRedis = jedis.lrange("createDates", 0, jedis.llen("createDates"));
            for (int i = 0; i < listCreateDateInRedis.size(); i++) {
                if (result.getDayOfWeek() != LocalDate.parse(listCreateDateInRedis.get(i), dateTimeFormatter).getDayOfWeek() &&
                        result.getMonth() != LocalDate.parse(listCreateDateInRedis.get(i), dateTimeFormatter).getMonth() &&
                        result.getYear() != LocalDate.parse(listCreateDateInRedis.get(i), dateTimeFormatter).getYear()) {
                    log.info("if not exist by createDate else del values of keys");
                    jedis.del("createDates");
                    jedis.del("tokens");
                }
            }

            List<String> listTokenInRedis = jedis.lrange("tokens", 0, jedis.llen("tokens"));
            log.info("begin check exist token");
            for (int i = 0; i < listTokenInRedis.size(); i++) {
                if (token.equals(listTokenInRedis.get(i))) {
                    return true;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        log.info("end existByTokenAndCreateDate");
        return false;
    }
}
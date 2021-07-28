package com.DemoRestApi.Repository;

import com.DemoRestApi.ConnectDB.ConnectionPool;
import com.DemoRestApi.Entity.Receipt;
import com.DemoRestApi.Exception.DateFormatException;
import com.DemoRestApi.Exception.OrderCodeNullException;
import com.DemoRestApi.Exception.PromotionCodeException;
import com.DemoRestApi.Exception.RealAmountException;
import com.DemoRestApi.RabbitMq.ConnectionQueue;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.sql.*;
import java.util.concurrent.TimeoutException;

import static com.DemoRestApi.RabbitMq.ConnectionQueue.QUEUE_NAME;

public class ReceiptRepository {

    ConnectionPool connectionPool;
    ConnectionQueue connectionQueue;
    Gson gson;

    public ReceiptRepository() {
        connectionPool = new ConnectionPool();
        connectionQueue = new ConnectionQueue();
        gson = new Gson();
    }

    Connection connection = null;

    public Receipt pushReceipt(Receipt receipt) throws IOException, TimeoutException {
        Channel channel = ConnectionQueue.getChannel();
        channel.basicPublish("", QUEUE_NAME, null, gson.toJson(receipt).getBytes());
        return receipt;
    }

    public Boolean existByTokenAndCreateDate(String token, Date createDate) {
        try {
            connection = connectionPool.getConnection();

            String sql = "SELECT 1 FROM `Receipt` WHERE `token_key` = ? AND create_date = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, token);
            preparedStatement.setDate(2, createDate);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

}
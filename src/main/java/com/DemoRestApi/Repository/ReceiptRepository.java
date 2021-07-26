package com.DemoRestApi.Repository;

import com.DemoRestApi.ConnectDB.ConnectionPool;
import com.DemoRestApi.Entity.Receipt;
import com.DemoRestApi.DTO.ReceiptCreateRequest;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReceiptRepository {

    ConnectionPool connectionPool;

    public ReceiptRepository() {
        connectionPool = new ConnectionPool();
    }

    Connection connection = null;

    public List<Receipt> getListReceipts() {
        List<Receipt> receipts = new ArrayList<>();
        try {
            connection = connectionPool.getConnection();
            String sql = "SELECT * FROM Receipt";
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Receipt receipt = new Receipt(resultSet.getLong("id"),
                        resultSet.getString("token_key"),
                        resultSet.getString("user_name"),
                        resultSet.getString("mobile"),
                        resultSet.getString("bank_code"),
                        resultSet.getString("accountNo"),
                        resultSet.getDate("pay_date"),
                        resultSet.getString("additional_data"),
                        resultSet.getDouble("debit_amount"),
                        resultSet.getString("order_code"),
                        resultSet.getDouble("real_amount"),
                        resultSet.getString("promotion_code"),
                        resultSet.getDate("create_date"));
                receipts.add(receipt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return receipts;
    }

    @SneakyThrows
    public Receipt findById(Long id) {
        Connection connection = connectionPool.getConnection();

        String sql = "SELECT * FROM Receipt " +
                "WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Receipt receipt = Receipt.builder()
                    .id(resultSet.getLong(1))
                    .tokenKey(resultSet.getString(2))
                    .username(resultSet.getString(3))
                    .mobile(resultSet.getString(4))
                    .bankCode(resultSet.getString(5))
                    .accountNo(resultSet.getString(6))
                    .payDate(resultSet.getDate(7))
                    .additionalData(resultSet.getString(8))
                    .debitAmount(resultSet.getDouble(9))
                    .orderCode(resultSet.getString(10))
                    .realAmount(resultSet.getDouble(11))
                    .promotionCode(resultSet.getString(12))
                    .createDate(resultSet.getDate(13))
                    .build();
            return receipt;
        }
        return null;
    }

    public Receipt createReceipt(ReceiptCreateRequest receiptCreateRequest) throws SQLException {
        Connection connection = connectionPool.getConnection();

        String sql = "INSERT INTO `Receipt` (token_key,	user_name, mobile, bank_code, accountNo, debit_amount, order_code, real_amount, promotion_code)"
                + "VALUE(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setString(1, receiptCreateRequest.getTokenKey());
        preparedStatement.setString(2, receiptCreateRequest.getUsername());
        preparedStatement.setString(3, receiptCreateRequest.getMobile());
        preparedStatement.setString(4, receiptCreateRequest.getBankCode());
        preparedStatement.setString(5, receiptCreateRequest.getAccountNo());
        preparedStatement.setDouble(6, receiptCreateRequest.getDebitAmount());
        preparedStatement.setString(7, receiptCreateRequest.getOrderCode());
        preparedStatement.setDouble(8, receiptCreateRequest.getRealAmount());
        preparedStatement.setString(9, receiptCreateRequest.getPromotionCode());
        preparedStatement.executeUpdate();
        ResultSet rs = preparedStatement.getGeneratedKeys();
        Long generatedKey = 0L;
        if (rs.next()) {
            generatedKey = rs.getLong(1);
        }
        return findById(generatedKey);
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
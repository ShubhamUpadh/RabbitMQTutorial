package org.Prac2;

import com.rabbitmq.client.*;

public class ReceiveLogs {
    private static final String EXCHANGE_NAME="logs";

    public static void main(String[] args) throws Exception{
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        String queueName = channel.queueDeclare().getQueue(); // autodeclare karo queue
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) ->{
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Recieved '" + message + "' ");
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});

    }
}

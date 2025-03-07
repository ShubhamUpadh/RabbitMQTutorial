package org.Prac2;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class EmitLog {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args){
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        try (Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel()){
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

            String message = args.length < 1 ? "info: Hello World ! ":
                    String.join(" ", args);

            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "' ");
        }
        catch (IOException | TimeoutException e){
            System.out.println("Exception occurred " + e.getMessage());
            e.printStackTrace();
        }
    }

}

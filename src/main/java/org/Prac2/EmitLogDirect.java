package org.Prac2;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class EmitLogDirect {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args){
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()){
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            String severity = getSev(args);
            String message = getMsg(args);

            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "' ");
        }
        catch (IOException | TimeoutException e){
            System.out.println("Exception occurred " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String getSev(String[] strings){
        if (strings.length < 1) return "info";
        return strings[0];
    }

    private static String getMsg(String[] strings){
        if (strings.length < 2) return "Hello World";
        return joinStrings(strings," ", 1);
    }

    private static String joinStrings(String[] strings, String delimiter, int startIndex){
        int len = strings.length;
        if (len == 0) return "";
        if (len <= startIndex) return "";
        StringBuilder words = new StringBuilder(strings[startIndex]);
        for (int i = startIndex+1; i < len; i++){
            words.append(delimiter).append(strings[i]);
        }

        return words.toString();
    }
}

package com.cjm.shiro.boot_shiro_v1.mq.rabbit.v1.ack;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author chenjm
 * @version V1.0
 * @Title: ConfirmConsumer
 * @Package: com.cjm.shiro.boot_shiro_v1.mq.rabbit.v1
 * @Description: TOTO
 * @date 2019 2019/8/21 21:36
 **/
public class ConfirmConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setVirtualHost("/");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "test_confirm_exchange";
        String queueName = "test_confirm_queue";
        String routingKey = "item.#";
        channel.exchangeDeclare(exchangeName, "topic", true, false, null);
        channel.queueDeclare(queueName, false, false, false, null);

        channel.queueBind(queueName, exchangeName, routingKey);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.err.println("[X] Received '"+msg+"'");
            }
        };

        channel.basicConsume(queueName, true, consumer);
    }
}

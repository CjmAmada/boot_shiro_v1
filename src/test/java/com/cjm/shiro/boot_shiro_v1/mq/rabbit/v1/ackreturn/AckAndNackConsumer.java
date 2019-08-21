package com.cjm.shiro.boot_shiro_v1.mq.rabbit.v1.ackreturn;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class AckAndNackConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setVirtualHost("/");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        String exchangeName = "test_ack_exchange";
        String queueName = "test_ack_queue";
        String routingKey = "item.#";
        channel.exchangeDeclare(exchangeName, "topic", true, false, null);
        channel.queueDeclare(queueName, false, false, false, null);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.err.println("[x] Received '" + msg + "'");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (0 == properties.getHeaders().get("num")) {
                    channel.basicNack(envelope.getDeliveryTag(), false, true);
                } else {
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };

        channel.basicConsume(queueName, false, consumer);
    }
}

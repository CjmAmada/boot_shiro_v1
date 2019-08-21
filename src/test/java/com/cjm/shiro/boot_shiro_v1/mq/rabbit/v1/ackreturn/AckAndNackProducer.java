package com.cjm.shiro.boot_shiro_v1.mq.rabbit.v1.ackreturn;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 1、首先我们发送五条消息，将每条消息对应的循环下标i放入消息的properties中
 * 作为标记，以便于我们在后面的回调方法中识别。
 * 2、其次，我们将消费端的channel.basicConsue(queueName, false, consumer
 * );中的autoAck属性设置为false，
 * 如果设置为true的话，将会正常输出五条消息。
 * 3、我们通过Thread.sleep(2000)来延迟一秒，用以看清结果，我们获取到properties
 * 中的num之后，通过channel.basicNack(envelope.getDeliveryTag(),false, true);
 * 将num为0的消息设置为nack，即消费失败，并且requeue属性设置为true，
 * 即消费失败的消息重回队列末端.
 */
public class AckAndNackProducer {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setVirtualHost("/");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "test_ack_exchange";
        String routingKey = "item.update";

        String msg = "this is ack msg ";
        for (int i = 0; i < 5; i++) {
            Map<String, Object> headers = new HashMap<String, Object>();
            headers.put("num", i);

            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                    .deliveryMode(2)
                    .headers(headers)
                    .build();

            String tem = msg + ":" + i;
            channel.basicPublish(exchangeName, routingKey, true, properties, tem.getBytes());
            System.err.println("send msg :" + msg);
        }

        channel.close();
        connection.close();
    }
}

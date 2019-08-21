package com.cjm.shiro.boot_shiro_v1.mq.rabbit.v1.ack;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 1、生产者投递的消息和消费者确认消息
 * 2、分为两种模式：同步模式与异步模式
 * 1）同步模式分为（单条消息确认）与（批量确认）
 * a）单条消息确认：channel.waitForConfirms()普通发送方确认模式；
 * 消息到达交换器，就会返回true;
 * b）批量消息确认：channel.waitForConfirmsOrDie()批量确认模式，
 * 使用同步方式等所有的消息发送之后才会执行后面代码，只要有一个
 * 消息未到达交换器就会抛出IOException异常
 * <p>
 * 2）异步模式为生产者，异步监听消息确认
 * 异步监听消息确认：channel.addConfirmListener()异步监听发送方
 * 确认模式。
 * 3、其次说下消费者对RabbitMQ消息确认。总共分为两种方式，手动确认
 * 和自动确认
 * 消费者收到的每一条消息都必须进行确认。消息确认后，RabbitMQ才
 * 会从队列删除这条消息，RabbitMQ不会为未确认消息设置超时时间，
 * 它判断此消息是否需要重新投递给消费者的唯一依据是消费该消息的消费
 * 者连接是否已经断开。
 * <p>
 * 这么设计的原因是Rabbitmq允许消费者消费一条消息的时间可以很久很久。
 */

public class ConfirmProducer {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setVirtualHost("/");
        factory.setUsername("guest");
        factory.setPassword("guest");
        // 连接
        Connection connection = factory.newConnection();
        // 通道
        Channel channel = connection.createChannel();

        String exchangeName = "test_confirm_exchange";
        String routingKey = "item.update";

        // 指定消息的投递模式：confirm确认模式
        channel.confirmSelect();

        final long start = System.nanoTime();

        for (int i = 0; i < 5; i++) {
            String msg = "this is confirm msg " + i;
            channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());
            System.err.println("send message : " + msg);
        }

        // 添加一个确认监听，这里就不关闭连接了，为了能保证能收到监听到消息
        channel.addConfirmListener(new ConfirmListener() {
            /**
             * 返回成功的回调函数
             * @param deliveryTag
             * @param multiple
             * @throws IOException
             */
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.err.println("success ack");
                System.err.println(multiple);
                System.err.println("耗时： " + (System.nanoTime() - start) / 1000 + " ms");
            }

            /**
             * 返回失败的回调函数
             * @param deliveryTag
             * @param multiple
             * @throws IOException
             */
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.err.println("default ack");
                System.err.println("耗时： " + (System.nanoTime() - start) / 1000 + " ms");
            }
        });
    }
}

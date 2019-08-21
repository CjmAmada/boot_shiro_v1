package com.cjm.shiro.boot_shiro_v1.mq.rabbit.v1.retu;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Return listener用于处理一些不可路由的消息
 * 消息生产者，通过指定一个Exchagne和Routingkey，把消息送到到某一个
 * 队列中去，然后我们的消费者监听队列，进行消费处理操作！
 *
 * 但是在某些情况下，如果我们在发送消息的时候，当前的exchange不存在或者指定
 * 的路由key路由不到，这个时候如果我们需要监听这种不可达的消息，就要使用
 * Return Listener!
 *
 * 在基础api中有一个关键的配置荐：Mandatory：如果为true，则监听器会接收到路由
 * 不可达的消息，然后进行后续处理，如果为false，那么broker端自动删除该消息！
 */
public class ReturnListeningProducer {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setVirtualHost("/");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "test_return_exchange";
        String routingKey = "item.update";
        String errorRoutingKey = "error.update";

        channel.confirmSelect();

        for (int i = 0; i < 3; i++) {
            String msg = "this is return == listening msg ";
            if (i == 0) {
                channel.basicPublish(exchangeName, errorRoutingKey, true, null, msg.getBytes());
            }else{
                channel.basicPublish(exchangeName, routingKey, true, null, msg.getBytes());
            }
            System.err.println("send msg  " + msg );
        }

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
            }
        });

        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.err.println("return relyCode "+replyCode);
                System.err.println("return replyText "+replyText);
                System.err.println("return exchange "+exchange);
                System.err.println("return routingKey "+routingKey);
                System.err.println("return properties "+properties);
                System.err.println("return body "+new String(body));
            }
        });
    }
}

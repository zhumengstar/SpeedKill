package com.xupt.zhumeng.speedkill.rabbitmq;

import com.xupt.zhumeng.speedkill.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {

    private static Logger log=LoggerFactory.getLogger(MQSender.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    public void send(Object message){
        String msg=RedisService.beanToString(message);
        log.info("send message:"+msg);
        amqpTemplate.convertAndSend(MQConfig.QUEUE,msg);
    }

    public void sendTopic(Object message){
        String msg=RedisService.beanToString(message);
        log.info("send topic message:"+msg);
        /**
         * 先发送到TOPIC_EXCHANGE交换机，再发送到QUEUE中，服务器指定消息接受器接受消息
         *
         * 只有匹配topic.key1的消息队列可以接受到消息
         *
         * message+"1" ： 发送的消息
         */
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key1",message+"1");
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key2",message+"2");
    }


    public void sendFanout(Object message){
        String msg=RedisService.beanToString(message);
        log.info("send fanout message:"+msg);
        /**
         * 先发送到TOPIC_EXCHANGE交换机，再发送到QUEUE中，服务器指定消息接受器接受消息
         *
         * 只有匹配topic.key1的消息队列可以接受到消息
         *
         * message+"1" ： 发送的消息
         */
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",message+"1");
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",message+"2");
    }

    public void sendHeader(Object message){
        String msg=RedisService.beanToString(message);
        log.info("send header message:"+msg);

        MessageProperties properties=new MessageProperties();
        properties.setHeader("header1","value1");
        properties.setHeader("header2","value2");

        Message obj=new Message(msg.getBytes(),properties);

        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE,MQConfig.HEADER_QUEUE,obj);
    }


    public void sendMsMessage(MsMessage mm) {
        String msg = RedisService.beanToString(mm);
        log.info("send message:" + msg);
        amqpTemplate.convertAndSend(MQConfig.MS_QUEUE, msg);

    }

}

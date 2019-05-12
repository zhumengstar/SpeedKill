package com.xupt.zhumeng.speedkill.rabbitmq;

import com.xupt.zhumeng.speedkill.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


//接受消息的服务端
@Service
public class MQReceiver {


   private static Logger log=LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    AmqpTemplate amqpTemplate;



   @RabbitListener(queues = MQConfig.QUEUE)
    public void receive1(String message){
       log.info("receive message:"+message);
        String msg=RedisService.beanToString(message);
    }


   @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void receiveTopic1(String message){
       log.info("receive Topic1 message:"+message);
       String msg=RedisService.stringToBean(message,String.class);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void receiveTopic2(String message){
        log.info("receive Topic2 message:"+message);
        String msg=RedisService.stringToBean(message,String.class);
    }


    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
    public void receiveHeaderQueue(byte[] message){
        log.info("receive header message:"+new String(message));
        String msg=RedisService.stringToBean(new String(message),String.class);
    }






}

package com.xupt.zhumeng.speedkill.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class MQConfig {

    public static final String QUEUE="queue";
    public static final String TOPIC_QUEUE1="topic.queue1";
    public static final String TOPIC_QUEUE2="topic.queue2";
    public static final String HEADER_QUEUE="headerqueue";
    public static final String TOPIC_EXCHANGE="topic.exchange";
    public static final String ROUTING_KEY1="topic.key1";
    public static final String ROUTING_KEY2="topic.#";
    public static final String FANOUT_EXCHANGE="fanout.exchange";
    public static final String HEADERS_EXCHANGE="headers.exchange";
    public static final String MS_QUEUE ="ms.queue";

    @Bean
    public Queue msQueue() {
        return new Queue(MS_QUEUE, true);
    }

    /**
     * direct模式 交换机Exchange
     */
    @Bean
    public Queue queue(){

        return new Queue(QUEUE,true);
    }



    /**
     * Topic模式 交换机Exchange
     * @return
     */
    @Bean
    public Queue topicQueue1(){

        return new Queue(TOPIC_QUEUE1,true);
    }
    /**
     * Topic模式 交换机Exchange
     * @return
     */
    @Bean
    public Queue topicQueue2(){
        return new Queue(TOPIC_QUEUE2,true);
    }
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }
    @Bean
    public Binding topicBinding1(){
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with(ROUTING_KEY1);
    }@Bean
    public Binding topicBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with(ROUTING_KEY2);
    }

    /**
     * Fanout模式 交换机Exchange
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE);
    }
    @Bean
    public Binding fanoutBinding1(){
        return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());
    }
    @Bean
    public Binding fanoutBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
    }
    @Bean
    public Binding fanoutBinding3(){
        return BindingBuilder.bind(queue()).to(fanoutExchange());
    }

    /**
     * Header模式 交换机Exchange
     */
    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(HEADERS_EXCHANGE);
    }
    @Bean
    public Queue headerQueue1(){
        return new Queue(HEADER_QUEUE,true);
    }
    @Bean
    public Binding headerBinding(){
        Map<String,Object> map=new HashMap<String ,Object>();
        map.put("header1","value1");
        map.put("header2","value2");
        return BindingBuilder.bind(headerQueue1()).to(headersExchange()).whereAll(map).match();
    }

}

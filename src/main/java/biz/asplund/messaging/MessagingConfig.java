package biz.asplund.messaging;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {
    public final static String DIRECT_QUEUE_NAME = "amqp.direct.queue";
    public final static String DIRECT_ROUTING_KEY = "direct";
    public final static String DIRECT_EXCHANGE_NAME = "amqp.direct.exchange";
    public final static String FANOUT_QUEUE_NAME = "amqp.fanout.queue";
    public final static String FANOUT_EXCHANGE_NAME = "amqp.fanout.exchange";
    public final static String TOPIC_QUEUE_NAME = "amqp.topic.queue";
    public final static String TOPIC_EXCHANGE_NAME = "amqp.topic.exchange";
    public static final String BINDING_PATTERN_ERROR = "#.error";
    private static final boolean NON_DURABLE = false;
    private static final boolean DO_NOT_AUTO_DELETE = false;

    @Bean
    public Declarables directBindings() {
        var directQueue = new Queue(DIRECT_QUEUE_NAME, NON_DURABLE);

        var directExchange = new DirectExchange(
                DIRECT_EXCHANGE_NAME,
                NON_DURABLE,
                DO_NOT_AUTO_DELETE);

        return new Declarables(directQueue,
                               directExchange,
                               BindingBuilder
                                       .bind(directQueue)
                                       .to(directExchange)
                                       .with(DIRECT_ROUTING_KEY));
    }

    @Bean
    public Declarables topicBindings() {
        var topicQueue = new Queue(TOPIC_QUEUE_NAME, NON_DURABLE);

        var topicExchange = new TopicExchange(
                TOPIC_EXCHANGE_NAME,
                NON_DURABLE,
                DO_NOT_AUTO_DELETE);

        return new Declarables(
                topicQueue,
                topicExchange,
                BindingBuilder
                        .bind(topicQueue)
                        .to(topicExchange)
                        .with(BINDING_PATTERN_ERROR));
    }

    @Bean
    public Declarables fanoutBindings() {
        var fanoutQueue = new Queue(FANOUT_QUEUE_NAME, NON_DURABLE);

        var fanoutExchange = new FanoutExchange(
                FANOUT_EXCHANGE_NAME,
                NON_DURABLE,
                DO_NOT_AUTO_DELETE);

        return new Declarables(
                fanoutQueue,
                fanoutExchange,
                BindingBuilder
                        .bind(fanoutQueue)
                        .to(fanoutExchange));
    }

}
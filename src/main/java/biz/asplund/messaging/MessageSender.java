package biz.asplund.messaging;

import static biz.asplund.messaging.MessagingConfig.DIRECT_EXCHANGE_NAME;
import static biz.asplund.messaging.MessagingConfig.DIRECT_ROUTING_KEY;
import static biz.asplund.messaging.MessagingConfig.FANOUT_EXCHANGE_NAME;
import static biz.asplund.messaging.MessagingConfig.TOPIC_EXCHANGE_NAME;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {
    private final RabbitTemplate rabbitTemplate;

    public MessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void broadcast(String message) {
        this.rabbitTemplate.convertAndSend(FANOUT_EXCHANGE_NAME, "", message);
    }

    public void sendError(String message) {
        this.rabbitTemplate.convertAndSend(TOPIC_EXCHANGE_NAME, "this.is.an.error", message);
    }

    public void send(String message) {
        this.rabbitTemplate.convertAndSend(DIRECT_EXCHANGE_NAME, DIRECT_ROUTING_KEY, message);
    }

}
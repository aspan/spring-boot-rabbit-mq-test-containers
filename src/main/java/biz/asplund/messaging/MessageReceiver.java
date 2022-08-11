package biz.asplund.messaging;

import static biz.asplund.messaging.MessagingConfig.BINDING_PATTERN_ERROR;
import static biz.asplund.messaging.MessagingConfig.DIRECT_QUEUE_NAME;
import static biz.asplund.messaging.MessagingConfig.FANOUT_QUEUE_NAME;
import static biz.asplund.messaging.MessagingConfig.TOPIC_QUEUE_NAME;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {
    @RabbitListener(queues = {FANOUT_QUEUE_NAME})
    public void receiveMessageFromFanout(String message) {
        System.out.println("Received fanout message: " + message);
    }

    @RabbitListener(queues = {DIRECT_QUEUE_NAME})
    public void receiveMessageFromDirect(String message) {
        System.out.println("Received direct message: " + message);
    }

    @RabbitListener(queues = {TOPIC_QUEUE_NAME})
    public void receiveMessageFromTopic(String message) {
        System.out.println("Received topic (" + BINDING_PATTERN_ERROR + ") message: " + message);
    }
}
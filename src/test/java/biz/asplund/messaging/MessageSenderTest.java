package biz.asplund.messaging;


import static biz.asplund.messaging.MessagingConfig.DIRECT_EXCHANGE_NAME;
import static biz.asplund.messaging.MessagingConfig.DIRECT_ROUTING_KEY;
import static biz.asplund.messaging.MessagingConfig.FANOUT_EXCHANGE_NAME;
import static biz.asplund.messaging.MessagingConfig.TOPIC_EXCHANGE_NAME;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class MessageSenderTest {
    public static final String TEST_BROADCAST_MESSAGE = "Test";
    public static final String TEST_DIRECT_MESSAGE = "Test Direct";
    public static final String TEST_ERROR_MESSAGE = "Test Error";
    private MessageSender subject;
    private RabbitTemplate rabbitTemplateMock;

    @BeforeEach
    void setUp() {
        this.rabbitTemplateMock = mock(RabbitTemplate.class);
        this.subject = new MessageSender(this.rabbitTemplateMock);
    }

    @Test
    void testDirect() {
        assertThatCode(() -> this.subject.send(TEST_DIRECT_MESSAGE))
                .doesNotThrowAnyException();

        verify(this.rabbitTemplateMock)
                .convertAndSend(
                        eq(DIRECT_EXCHANGE_NAME),
                        eq(DIRECT_ROUTING_KEY),
                        eq(TEST_DIRECT_MESSAGE)
                );
    }

    @Test
    void testBroadcast() {
        assertThatCode(() -> this.subject.broadcast(TEST_BROADCAST_MESSAGE))
                .doesNotThrowAnyException();

        verify(this.rabbitTemplateMock)
                .convertAndSend(
                        eq(FANOUT_EXCHANGE_NAME),
                        eq(""),
                        eq(TEST_BROADCAST_MESSAGE));
    }

    @Test
    void testSendError() {
        assertThatCode(() -> this.subject.sendError(TEST_ERROR_MESSAGE))
                .doesNotThrowAnyException();

        verify(this.rabbitTemplateMock)
                .convertAndSend(
                        eq(TOPIC_EXCHANGE_NAME),
                        endsWith("error"),
                        eq(TEST_ERROR_MESSAGE));
    }
}
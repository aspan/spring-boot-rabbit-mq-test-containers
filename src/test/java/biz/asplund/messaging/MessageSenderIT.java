package biz.asplund.messaging;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import biz.asplund.messaging.MessageSenderIT.RabbitMQContainerPropertiesInitializer;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@ContextConfiguration(initializers = RabbitMQContainerPropertiesInitializer.class)
@Testcontainers
public class MessageSenderIT {
    private static final String BROADCAST_MESSAGE = "Broadcast Message \uD83D\uDC33";
    private static final String DIRECT_MESSAGE = "Direct Message \uD83D\uDC33";
    private static final String TOPIC_MESSAGE = "Topic Message \uD83D\uDC33";
    @Container
    final static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:management");

    @Autowired
    private MessageSender messageSender;

    @SpyBean
    private MessageReceiver messageReceiver;

    @Test
    void testBroadcast() {
        messageSender.broadcast(BROADCAST_MESSAGE);
        await().atMost(5, SECONDS)
               .untilAsserted(
                       () -> verify(messageReceiver, atMostOnce())
                               .receiveMessageFromFanout(eq(BROADCAST_MESSAGE)));
    }

    @Test
    void testDirect() {
        messageSender.send(DIRECT_MESSAGE);
        await().atMost(5, SECONDS)
               .untilAsserted(
                       () -> verify(messageReceiver, atMostOnce())
                               .receiveMessageFromDirect(eq(DIRECT_MESSAGE)));
    }

    @Test
    void testTopic() {
        messageSender.sendError(TOPIC_MESSAGE);
        await().atMost(5, SECONDS)
               .untilAsserted(
                       () -> verify(messageReceiver, atMostOnce())
                               .receiveMessageFromTopic(eq(TOPIC_MESSAGE)));

    }

//    @AfterAll
//    static void sleepAfter() throws Exception{
//        Thread.sleep(60000L);
//    }

    @Slf4j
    public static class RabbitMQContainerPropertiesInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public static final int DEFAULT_AMQP_PORT = 5672;
        public static final int DEFAULT_HTTP_PORT = 15672;
        public static final String SPRING_RABBITMQ_HOST_PROPERTY = "spring.rabbitmq.host";
        public static final String SPRING_RABBITMQ_PORT_PROPERTY = "spring.rabbitmq.port";

        @Override
        public void initialize(@NonNull ConfigurableApplicationContext configurableApplicationContext) {
            LOGGER.info("RabbitMQ Admin: {}", getAdminUrl());
            TestPropertyValues.of(
                    Map.of(
                            SPRING_RABBITMQ_HOST_PROPERTY, getHost(),
                            SPRING_RABBITMQ_PORT_PROPERTY, getPort()
                    )
            ).applyTo(configurableApplicationContext);
        }

        private String getHost() {
            return rabbitMQContainer.getHost();
        }

        private String getPort() {
            return "" + rabbitMQContainer.getMappedPort(DEFAULT_AMQP_PORT);
        }

        private String getAdminUrl() {
            return "http://" + rabbitMQContainer.getHost() + ":" + rabbitMQContainer.getMappedPort(DEFAULT_HTTP_PORT);
        }
    }
}
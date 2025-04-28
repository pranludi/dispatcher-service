package com.polarbookshop.dispatcherservice;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class FunctionsStreamIntegrationTests {

    @Autowired
    InputDestination input;

    @Autowired
    OutputDestination output;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void whenOrderAcceptedThenDispatched() throws Exception {
        long orderId = 121;
        Message<OrderAcceptedMessage> inputMessage = MessageBuilder.withPayload(new OrderAcceptedMessage(orderId)).build();
        Message<OrderDispatchedMessage> expectedOutputMessage = MessageBuilder.withPayload(new OrderDispatchedMessage(orderId)).build();

        this.input.send(inputMessage);
        assertThat(objectMapper.readValue(output.receive().getPayload(), OrderDispatchedMessage.class))
            .isEqualTo(expectedOutputMessage.getPayload());
    }
}
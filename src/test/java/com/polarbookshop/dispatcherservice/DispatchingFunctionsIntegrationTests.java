package com.polarbookshop.dispatcherservice;

import java.util.function.Function;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@FunctionalSpringBootTest
@Disabled("These tests are only necessary when using the functions alone (no bindings)")
class DispatchingFunctionsIntegrationTests {

    @Autowired
    FunctionCatalog catalog;

    @Test
    void packAndLabelOrder() {
        Function<OrderAcceptedMessage, Flux<OrderDispatchedMessage>> packAndLabel =
            catalog.lookup(
                Function.class,
                "pack|label"
            );
        long orderId = 121;

        StepVerifier
            .create(packAndLabel.apply(new OrderAcceptedMessage(orderId)))
            .expectNextMatches(
                dispatchedOrder ->
                    dispatchedOrder.equals(new OrderDispatchedMessage(orderId))
            )
            .verifyComplete();
    }
}
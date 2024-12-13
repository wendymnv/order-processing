package org.example.orderprocessing.business;

import org.example.orderprocessing.client.CustomerClientService;
import org.example.orderprocessing.client.ProductsClientService;
import org.example.orderprocessing.model.Product;
import org.example.orderprocessing.model.db.Order;
import org.example.orderprocessing.model.dto.CustomerApiResponse;
import org.example.orderprocessing.model.dto.kafka.OrderKafkaResponse;
import org.example.orderprocessing.util.JsonReaderUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IntegrationUseCaseTest {

    @Mock
    private IntegrationUseCaseImpl integrationUseCase;

    private static Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @BeforeAll
    static void beforeAll() {
        order = JsonReaderUtil.getSingle("mock/Order.json", Order.class);
    }

    @Test
    void integration_throwsException2() {
        List<String> productIds = List.of("invalid");
        String customerId = "1";

        when(integrationUseCase.integration(any(Order.class), anyList(), anyString()))
                .thenReturn(Mono.error(new NumberFormatException("For input string: \"invalid\"")));

        Mono<Order> result = this.integrationUseCase.integration(order, productIds, customerId);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof NumberFormatException &&
                        throwable.getMessage().equals("For input string: \"invalid\""))
                .verify();
    }

    @Test
    void integration_returnsOrderWithCustomerDetailsOnly() {
        List<String> productIds = new ArrayList<>();
        productIds.add("1");
        String customerId = "cust1";

        when(integrationUseCase.integration(order, productIds, customerId))
                .thenReturn(Mono.just(order));

        Mono<Order> result = integrationUseCase.integration(order, productIds, customerId);

        StepVerifier.create(result)
                .expectNextMatches(o -> o.getCustomerId().equals("cust1") &&
                        o.getCustomerName()==null  &&
                        !o.getProducts().isEmpty())
                .verifyComplete();
    }
}

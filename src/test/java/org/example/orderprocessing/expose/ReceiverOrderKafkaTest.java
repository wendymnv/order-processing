package org.example.orderprocessing.expose;

import org.example.orderprocessing.business.IntegrationUseCase;
import org.example.orderprocessing.model.db.Order;

import org.example.orderprocessing.model.dto.kafka.OrderKafkaResponse;
import org.example.orderprocessing.repository.OrderRepository;
import org.example.orderprocessing.util.JsonReaderUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReceiverOrderKafkaTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private IntegrationUseCase integrationUseCase;

    @InjectMocks
    private ReceiverOrdersKafka receiverOrdersKafka;

    private static OrderKafkaResponse orderKafkaResponse;
    private static Order order;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @BeforeAll
    static void beforeAll() {
        orderKafkaResponse = JsonReaderUtil.getSingle("mock/OrderKafkaResponse.json", OrderKafkaResponse.class);
        order = JsonReaderUtil.getSingle("mock/Order.json", Order.class);
    }

    @Test
    void listen_validMessage() {
        String orderJson = "{ \"event\": { \"troubleOrder\": { \"id\": \"22\", \"productIds\": [\"prod1\"], " +
                "\"customerId\": \"cust1\", \"type\": \"CU-Donation\" } } }";
        when(integrationUseCase.integration(any(Order.class), anyList(), anyString())).thenReturn(Mono.just(order));
        receiverOrdersKafka.listen(orderJson);
        verify(orderRepository).saveDocument(order);
        assertEquals("22", order.getId());
    }

}

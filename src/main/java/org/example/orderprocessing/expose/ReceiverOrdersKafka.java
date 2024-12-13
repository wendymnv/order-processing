package org.example.orderprocessing.expose;

import org.example.orderprocessing.business.IntegrationUseCase;
import org.example.orderprocessing.model.db.Order;
import org.example.orderprocessing.model.dto.kafka.OrderKafkaResponse;
import org.example.orderprocessing.pattern.IOrderAdapter;
import org.example.orderprocessing.pattern.OrderAdapterFactory;
import org.example.orderprocessing.repository.OrderRepository;
import org.example.orderprocessing.util.JsonReaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import static org.example.orderprocessing.model.constants.Constants.TXT_EXCEPTION;
/**
 * Implementation of the IntegrationUseCase interface.
 * This service integrates customer and product details into an order.
 */

@EnableAsync
@Component
@ComponentScan
public class ReceiverOrdersKafka {

    private static final Logger log = LoggerFactory.getLogger(ReceiverOrdersKafka.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private IntegrationUseCase integrationUseCase;

    @Transactional
    @KafkaListener(topics = "${order-processing.topic.consumer.foo}")
    public void listen(@Payload String message) {
        log.info("Starting process Orders From Kafka: {}", LocalDateTime.now());
        var idOrder = 0;

        try {
            var orderKafkaResponse = JsonReaderUtil.getObjectFromJson(message, OrderKafkaResponse.class);
            idOrder = Integer.parseInt(orderKafkaResponse.getEvent().getTroubleOrder().getId());
            log.info("Kafka idOrder: {}", idOrder);
            Order order = Order.convertToOrderResult(orderKafkaResponse);
            order = modifyOrderUsingAdapter(order);
            Mono<Order> orderMono = this.integrationUseCase.integration(order, orderKafkaResponse.getEvent().getTroubleOrder().getProductIds(), orderKafkaResponse.getEvent().getTroubleOrder().getCustomerId());
            orderMono.subscribe(integratedOrder -> {
                orderRepository.saveDocument(integratedOrder);
                log.info("Order save: {}", LocalDateTime.now());
            });
            log.info("Finally idOrder: {}", idOrder);
        } catch (Exception e) {
            log.error(this.getClass().getName().concat(TXT_EXCEPTION).concat(e.getLocalizedMessage()));

        } finally {
            log.info("Ending Process Orders From Kafka: {} - {}", idOrder, LocalDateTime.now());
        }
    }

    private Order modifyOrderUsingAdapter(Order order) {
        IOrderAdapter adapter = OrderAdapterFactory.getCreateAdapter(order);
        return adapter.modifyOrder(order);
    }
}

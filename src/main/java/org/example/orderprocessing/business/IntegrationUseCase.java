package org.example.orderprocessing.business;

import org.example.orderprocessing.model.db.Order;
import reactor.core.publisher.Mono;
import java.util.List;

public interface IntegrationUseCase  {
    Mono<Order> integration(Order order, List<String> listProductIds, String customerId);
}

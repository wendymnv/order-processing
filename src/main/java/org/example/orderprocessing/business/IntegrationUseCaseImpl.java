package org.example.orderprocessing.business;

import lombok.extern.slf4j.Slf4j;
import org.example.orderprocessing.client.CustomerClientService;
import org.example.orderprocessing.client.ProductsClientService;
import org.example.orderprocessing.model.db.Order;
import org.example.orderprocessing.model.Product;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the IntegrationUseCase interface.
 * This service integrates customer and product details into an order.
 */

@Slf4j
@Service
@ComponentScan
public class IntegrationUseCaseImpl implements IntegrationUseCase {

    private final CustomerClientService customerClientService;
    private final ProductsClientService productsClientService;

    public IntegrationUseCaseImpl(CustomerClientService customerClientService,
                                  ProductsClientService productsClientService) {
        this.customerClientService = customerClientService;
        this.productsClientService = productsClientService;
    }

    public Mono<Order> integration(Order order, List<String> listProductIds, String customerId) {
        try {
            List<Product> list = new ArrayList<>();
            return Mono.when(
                    listProductIds.stream()
                            .map(ids -> productsClientService.getProductDetail(Integer.parseInt(ids))
                                    .map(productApiResponse -> {
                                        list.add(Product.getProduct(productApiResponse));
                                        return productApiResponse;
                                    }))
                            .toArray(Mono[]::new)
            ).then(
                    customerClientService.getCustomerDetail(Integer.parseInt(customerId))
                            .map(customerApiResponse -> {
                                order.setCustomerId(String.valueOf(
                                        customerApiResponse.getPokemonItems().getFirst().getId()));
                                order.setCustomerName(customerApiResponse.getPokemonItems().getFirst().getName());
                                order.setProducts(list);
                                return order;
                            })
            );
        } catch (Exception e) {
            log.error("Integration Error: {}", e.getMessage());
            return Mono.error(new RuntimeException("Integration Error", e));
        }
    }

}

package org.example.orderprocessing.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.orderprocessing.client.CustomerClientService;
import org.example.orderprocessing.config.PropertiesConfig;
import org.example.orderprocessing.exception.ExternalServerException;
import org.example.orderprocessing.model.dto.CustomerApiResponse;
import org.example.orderprocessing.model.enums.ErrorCategory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import java.util.List;

/**
 * Implementation of the IntegrationUseCase interface.
 * This service integrates customer and product details into an order.
 */
@Slf4j
@Component
@ComponentScan
public class CustomerClientServiceImpl implements CustomerClientService {
    private final PropertiesConfig config;

    public CustomerClientServiceImpl(PropertiesConfig config) {
        this.config = config;
    }

    public Mono<CustomerApiResponse> getCustomerDetail(int id) {
        //String query = "query getItem($id: Int!) { pokemon_v2_item(where: {id: {_eq: $id}}) { name, id } }";
        String query = "query getItems { pokemon_v2_item { name, id } }";
        return HttpGraphQlClient.builder()
                .url(config.getUrlCustomer())
                .header("content-type", "application/json")
                .header("accept", "*/*")
                .build()
                .document(query)
                .operationName("getItems")
                .retrieve("data.pokemon_v2_item")
                .toEntityList(CustomerApiResponse.PokemonItem.class)
                .map(items -> {
                    if (items.isEmpty()) {
                        items = List.of(new CustomerApiResponse.PokemonItem("name", 1));
                    }
                    CustomerApiResponse response = new CustomerApiResponse();
                    response.setPokemonItems(items);
                    return response;
                })
                .switchIfEmpty(Mono.error(new RuntimeException("No items found")))
                .onErrorResume(throwable -> {
                    log.error("Error fetching items: {}", throwable.getMessage());
                    return Mono.error(new RuntimeException("Error fetching items", throwable));
                });
    }


    public Mono<CustomerApiResponse> handleService(Throwable throwable) {
        if (Exceptions.isFatal(throwable)) {
            return Mono.error(new ExternalServerException(ErrorCategory.EXTERNAL_ERROR, "Api - Error fatal"));
        } else {
            log.error(throwable.getMessage(), throwable);
            return Mono.error(new ExternalServerException(ErrorCategory.EXTERNAL_ERROR, "Api - Error " + throwable.getMessage()));
        }
    }
}

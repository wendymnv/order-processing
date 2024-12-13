package org.example.orderprocessing.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.orderprocessing.client.ProductsClientService;
import org.example.orderprocessing.config.PropertiesConfig;
import org.example.orderprocessing.exception.ExternalServerException;
import org.example.orderprocessing.model.dto.ProductsApiResponse;
import org.example.orderprocessing.model.enums.ErrorCategory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import java.time.Duration;
/**
 * Implementation of the IntegrationUseCase interface.
 * This service integrates customer and product details into an order.
 */

@Slf4j
@Component
@ComponentScan
public class ProductsClientServiceImpl implements ProductsClientService {

    private final PropertiesConfig config;
    private final WebClient.Builder webClient;

    public ProductsClientServiceImpl(PropertiesConfig config,  WebClient.Builder webClient) {
        this.config = config;
        this.webClient = webClient;
    }

    public Mono<ProductsApiResponse> getProductDetail(int id) {
        return this.webClient.baseUrl(config.getUrlProducts().concat(String.valueOf(id)))
                .filter(ExchangeFilterFunction.ofResponseProcessor(
                        ProductsClientServiceImpl::exchangeFilterResponseProcessor)
                )
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .responseTimeout(Duration.ofSeconds(config.getTimeCloseProducts()))))
                .build()
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ProductsApiResponse.class)
                .switchIfEmpty(Mono.error(new ExternalServerException(ErrorCategory.EXTERNAL_ERROR, "Api - " +
                        "Bad response error")))
                .onErrorResume(this::handleService);
    }


    public Mono<ProductsApiResponse> handleService(
             Throwable throwable
    ) {
        if (Exceptions.isFatal(throwable)) {
            return Mono.error(new ExternalServerException(ErrorCategory.EXTERNAL_ERROR, "Api - Error fatal"));
        }else if (throwable instanceof WebClientRequestException) {
            return Mono.error(new ExternalServerException(ErrorCategory.EXTERNAL_ERROR,
                    "Api - Error WebClientRequestException - "+ throwable.getCause().toString()));
        }else {
            log.error(throwable.getMessage(), throwable);
            return Mono.error(throwable);
        }
    }

    public static Mono<ClientResponse> exchangeFilterResponseProcessor(ClientResponse response) {
        if (response.statusCode().is5xxServerError()) {
            return response.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(
                            new ExternalServerException(ErrorCategory.EXTERNAL_ERROR, "Api - Server Response Error")
                    ));
        }

        if (response.statusCode().is4xxClientError()) {
            return response.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(
                            new ExternalServerException(ErrorCategory.EXTERNAL_ERROR, "Api - Bad Request Error")
                    ));
        }
        return Mono.just(response);
    }
}

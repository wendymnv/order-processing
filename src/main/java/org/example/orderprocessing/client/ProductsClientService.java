package org.example.orderprocessing.client;

import org.example.orderprocessing.model.dto.ProductsApiResponse;
import reactor.core.publisher.Mono;

public interface ProductsClientService {
     Mono<ProductsApiResponse> getProductDetail(int id);
}

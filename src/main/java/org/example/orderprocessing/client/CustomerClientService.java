package org.example.orderprocessing.client;

import org.example.orderprocessing.model.dto.CustomerApiResponse;
import reactor.core.publisher.Mono;

public interface CustomerClientService {
    Mono<CustomerApiResponse> getCustomerDetail(int id);
}

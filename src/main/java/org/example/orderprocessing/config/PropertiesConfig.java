package org.example.orderprocessing.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Data
@Component
@ComponentScan
@NoArgsConstructor
public class PropertiesConfig {
    @Value("${order-processing.products.url}")
    String urlProducts;

    @Value("${order-processing.products.time-connection}")
    int timeCloseProducts;

    @Value("${order-processing.customers.url}")
    String urlCustomer;

}

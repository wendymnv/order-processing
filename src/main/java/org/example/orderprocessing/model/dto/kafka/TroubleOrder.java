package org.example.orderprocessing.model.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TroubleOrder {

    private String id;
    private String correlationId;
    private String type;
    private String status;
    private Date orderDate;
    private Date deliveryDate;
    private String customerId;
    private String shippingAddress;
    private double totalAmount;
    private String paymentMethod;
    private String trackingNumber;
    private List<String> productIds;

}

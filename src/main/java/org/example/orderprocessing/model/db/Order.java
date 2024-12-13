package org.example.orderprocessing.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.orderprocessing.model.Product;
import org.example.orderprocessing.model.dto.kafka.OrderKafkaResponse;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String customerId;
    private String customerName;
    private List<Product> products;
    private String status;
    private Date orderDate;
    private Date deliveryDate;
    private String shippingAddress;
    private double totalAmount;
    private String paymentMethod;
    private String trackingNumber;
    private String type;

  public static Order convertToOrderResult(OrderKafkaResponse orderKafkaResponse) {
    Order order = new Order();
    order.setId(orderKafkaResponse.getEvent().getTroubleOrder().getCorrelationId());
      order.setCustomerId(null);
      order.setCustomerName(null);
      order.setProducts(null);
      order.setStatus("CREATED");
      order.setType(orderKafkaResponse.getEvent().getTroubleOrder().getType());
      order.setOrderDate(orderKafkaResponse.getEvent().getTroubleOrder().getOrderDate());
      order.setDeliveryDate(orderKafkaResponse.getEvent().getTroubleOrder().getDeliveryDate());
      order.setShippingAddress(orderKafkaResponse.getEvent().getTroubleOrder().getShippingAddress());
      order.setTotalAmount(orderKafkaResponse.getEvent().getTroubleOrder().getTotalAmount());
      order.setPaymentMethod(orderKafkaResponse.getEvent().getTroubleOrder().getPaymentMethod());
      order.setTrackingNumber(orderKafkaResponse.getEvent().getTroubleOrder().getTrackingNumber());
      return  order;
  }

}
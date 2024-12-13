package org.example.orderprocessing.repository;

import com.mongodb.client.MongoCollection;
import org.example.orderprocessing.exception.GenericDomainException;
import org.example.orderprocessing.expose.ReceiverOrdersKafka;
import org.example.orderprocessing.model.db.Order;
import org.example.orderprocessing.model.enums.ErrorCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Repository implementation for managing Order documents in MongoDB.
 */
@Service
public class OrderRepositoryImpl  {

    private static final Logger log = LoggerFactory.getLogger(ReceiverOrdersKafka.class);

    @Autowired
    private MongoTemplate mongoTemplate;


    public void saveDocument( Order order) {
        try{
            MongoCollection<Document> collection = mongoTemplate.getCollection("orders");
            log.info("Saving order in MongoDB: {}", order);
            List<Document> productDocuments = order.getProducts().stream()
                    .map(product -> new Document()
                            .append("id", product.getId())
                            .append("name", product.getName())
                            .append("description", product.getDescription())
                            .append("price", product.getPrice()))
                    .toList();

            Document document = new Document()
                    .append("id", order.getId())
                    .append("status", order.getStatus())
                    .append("deliveryDate", order.getDeliveryDate())
                    .append("shippingAddress", order.getShippingAddress())
                    .append("totalAmount", order.getTotalAmount())
                    .append("type", order.getType())
                    .append("paymentMethod", order.getPaymentMethod())
                    .append("trackingNumber", order.getTrackingNumber())
                    .append("customerId", order.getCustomerId())
                    .append("customerName", order.getCustomerName())
                    .append("products", productDocuments)
                    .append("orderDate", order.getOrderDate());
            collection.insertOne(document);
        } catch (Exception e) {
            log.error("Error saving order in MongoDB: {}", e.getMessage());
            throw new GenericDomainException(ErrorCategory.EXTERNAL_DATA_REPOSITORY, "Error saving order in MongoDB");
        }finally{
            log.info("Processed orderId: {}", order.getId());
        }
    }

}
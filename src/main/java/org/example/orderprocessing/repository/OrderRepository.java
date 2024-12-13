package org.example.orderprocessing.repository;

import org.example.orderprocessing.model.db.Order;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
/**
 * Repository interface for managing Order documents in MongoDB.
 */
@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    void saveDocument( Order order);
}
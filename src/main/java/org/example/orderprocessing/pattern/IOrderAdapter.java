package org.example.orderprocessing.pattern;

import org.example.orderprocessing.model.db.Order;
/**
 * Interface for order adapters.
 * Provides a method to modify an order.
 */
public interface IOrderAdapter {
    Order modifyOrder(Order order);
}

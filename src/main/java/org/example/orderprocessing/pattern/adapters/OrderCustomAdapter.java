package org.example.orderprocessing.pattern.adapters;

import org.example.orderprocessing.model.db.Order;
import org.example.orderprocessing.pattern.IOrderAdapter;
/**
 * Modifies the given order by applying custom calculations.
 */

public class OrderCustomAdapter implements IOrderAdapter {
    public Order modifyOrder(Order order) {
        double modifiedTotalAmount = order.getTotalAmount() * 0.9;
        order.setTotalAmount(modifiedTotalAmount);
        return order;
    }
}

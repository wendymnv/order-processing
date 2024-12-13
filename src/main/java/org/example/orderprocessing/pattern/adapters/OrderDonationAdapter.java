package org.example.orderprocessing.pattern.adapters;

import org.example.orderprocessing.model.db.Order;
import org.example.orderprocessing.pattern.IOrderAdapter;
/**
 * Modifies the given order by applying donation-specific calculations.
 */
public class OrderDonationAdapter implements IOrderAdapter {

    public Order modifyOrder(Order order) {
        double donationAmount = 5.0;
        double modifiedTotalAmount = order.getTotalAmount() + donationAmount;
        order.setTotalAmount(modifiedTotalAmount);
        return order;
    }
}

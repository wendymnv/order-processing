package org.example.orderprocessing.pattern;

import org.example.orderprocessing.model.db.Order;
import org.example.orderprocessing.pattern.adapters.OrderCustomAdapter;
import org.example.orderprocessing.pattern.adapters.OrderDonationAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.example.orderprocessing.model.constants.Constants.USE_CASE_CUSTOM_ORDER;
import static org.example.orderprocessing.model.constants.Constants.USE_CASE_DONATION_ORDER;
/**
 * Retrieves the appropriate adapter for the given order and modifies the order using that adapter.
 */
public class OrderAdapterFactory {
    private static final Map<String, Supplier<IOrderAdapter>> ADAPTER_MAP = new HashMap<>();

    private OrderAdapterFactory() {
        throw new IllegalStateException("Utility class");
    }

    static {
        ADAPTER_MAP.put(USE_CASE_CUSTOM_ORDER, OrderCustomAdapter::new);
        ADAPTER_MAP.put(USE_CASE_DONATION_ORDER, OrderDonationAdapter::new);
    }

    public static IOrderAdapter getAdapter(String useCaseId) {
        Supplier<IOrderAdapter> adapterSupplier = ADAPTER_MAP.get(useCaseId);
        if (adapterSupplier == null) {
            throw new IllegalArgumentException("Invalid adapter type");
        }
        return adapterSupplier.get();
    }
    public static IOrderAdapter getCreateAdapter(Order order) {
        return getAdapter(order.getType());
    }
}

package com.epam.esm.entity.audit;

import com.epam.esm.entity.Order;

import javax.persistence.PrePersist;
import java.time.LocalDateTime;

/**
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
public class OrderAudit {

    /**
     * Performed before the persist operation and sets the order creation date
     *
     * @param order the order in which we set creation date
     */
    @PrePersist
    public void beforeCreateOrder(Order order) {
        order.setPurchaseTime(LocalDateTime.now());
    }
}

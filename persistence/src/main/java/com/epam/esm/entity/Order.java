package com.epam.esm.entity;

import com.epam.esm.entity.audit.OrderAudit;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Audited
@EntityListeners(OrderAudit.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(name = "purchase_time", nullable = false)
    private LocalDateTime purchaseTime;
    @Column(name = "gift_certificate_id", nullable = false)
    private Long giftCertificateId;
    @Column(name = "user_id", nullable = false)
    private Long userId;
}

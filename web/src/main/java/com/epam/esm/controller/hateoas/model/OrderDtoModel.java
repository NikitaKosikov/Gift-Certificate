package com.epam.esm.controller.hateoas.model;

import com.epam.esm.controller.hateoas.assembler.GiftCertificateDtoAssembler;
import com.epam.esm.controller.hateoas.assembler.UserDtoAssembler;
import com.epam.esm.dto.impl.GiftCertificateDto;
import com.epam.esm.dto.impl.OrderDto;
import com.epam.esm.dto.impl.UserDto;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@Relation(value = "order", collectionRelation = "orders")
public class OrderDtoModel extends RepresentationModel<OrderDtoModel> {

    private final GiftCertificateDtoAssembler giftCertificateDtoAssembler = new GiftCertificateDtoAssembler();
    private final UserDtoAssembler userDtoAssembler = new UserDtoAssembler();

    @Getter
    private final Long id;
    @Getter
    private final BigDecimal price;
    @Getter
    private final LocalDateTime purchaseTime;
    @Getter
    private final Long giftCertificateId;
    @Getter
    private final Long userId;

    public OrderDtoModel(OrderDto orderDto) {
        this.id = orderDto.getId();
        this.price = orderDto.getPrice();
        this.purchaseTime = orderDto.getPurchaseTime();
        this.giftCertificateId = orderDto.getId();
        this.userId = orderDto.getUserId();
    }
}

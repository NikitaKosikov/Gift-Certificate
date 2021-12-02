package com.epam.esm.controller.hateoas.link.impl;

import com.epam.esm.controller.hateoas.assembler.OrderDtoAssembler;
import com.epam.esm.controller.hateoas.model.OrderDtoModel;
import com.epam.esm.controller.hateoas.link.CustomLink;
import com.epam.esm.dto.impl.OrderDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Class is implementation of interface {@link CustomLink} and
 * intended to work with order links
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@Component
public class OrderLink implements CustomLink<OrderDtoModel, OrderDto> {

    @Override
    public OrderDtoModel toLink(OrderDto entity) {
        return new OrderDtoAssembler().toModel(entity);
    }

    @Override
    public CollectionModel<OrderDtoModel> toLink(List<OrderDto> entities) {
        if (entities!=null && !entities.isEmpty()){
            CollectionModel<OrderDtoModel> orderDtoModels = new OrderDtoAssembler().toCollectionModel(entities);
            return CollectionModel.of(orderDtoModels);
        }
        return CollectionModel.empty();
    }
}

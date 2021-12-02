package com.epam.esm.controller.hateoas.assembler;

import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.hateoas.model.OrderDtoModel;
import com.epam.esm.dto.impl.OrderDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Class that sets a links to our OrderDtoModel
 *
 * @author Nikita Kosikov
 * @version 1.0
 * @see org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
 */
public class OrderDtoAssembler extends RepresentationModelAssemblerSupport<OrderDto, OrderDtoModel> {

    public OrderDtoAssembler() {
        super(OrderController.class, OrderDtoModel.class);
    }

    @Override
    protected OrderDtoModel instantiateModel(OrderDto entity) {
        return new OrderDtoModel(entity);
    }

    /**
     * Sets a links to our OrderDtoModel.
     *
     * @param orderDto the object in which will be added links.
     * @return the OrderDtoModel this contains links.
     */
    @Override
    public OrderDtoModel toModel(OrderDto orderDto) {
        OrderDtoModel orderDtoModel = createModelWithId(orderDto.getId(), orderDto);
        orderDtoModel.add(linkTo(methodOn(OrderController.class).save(orderDto)).withRel("save"));
        return orderDtoModel;
    }
}

package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.model.OrderDtoModel;
import com.epam.esm.controller.hateoas.link.CustomLink;
import com.epam.esm.converter.ParametersToDtoConverter;
import com.epam.esm.dto.impl.OrderDto;
import com.epam.esm.dto.impl.SearchByParametersDto;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Class is an endpoint for user authentication and registration
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@RestController
@RequestMapping("/orders")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class OrderController {

    private final OrderService orderService;
    private final CustomLink<OrderDtoModel, OrderDto> orderDtoCustomLink;
    private final ParametersToDtoConverter parametersToDtoConverter;

    @Autowired
    public OrderController(OrderService orderService, CustomLink<OrderDtoModel, OrderDto> orderDtoCustomLink, ParametersToDtoConverter parametersToDtoConverter){
        this.orderService = orderService;
        this.orderDtoCustomLink = orderDtoCustomLink;
        this.parametersToDtoConverter = parametersToDtoConverter;
    }

    /**
     * Find orders by parameters, processes GET requests at
     * /orders
     *
     * @param pageable the contains information about the data selection.
     * @param parameters the order search parameters.
     * @return the responseEntity object with contains HTTP status (OK)
     * and found orders as CollectionModel<OrderDtoModel>.
     */
    @GetMapping
    public ResponseEntity<CollectionModel<OrderDtoModel>> findAll(Pageable pageable,
                                                             @RequestParam(required = false) Map<String, String> parameters){
        SearchByParametersDto searchByParametersDto = parametersToDtoConverter.convert(parameters);
        List<OrderDto> orders = orderService.findAll(pageable, searchByParametersDto);
        CollectionModel<OrderDtoModel> orderDtoModels = orderDtoCustomLink.toLink(orders);
        return new ResponseEntity<>(orderDtoModels, HttpStatus.OK);
    }

    /**
     * Find order by id, processes GET requests at /orders/{id}.
     *
     * @param id the order id which will be found.
     * @return the responseEntity object with contains HTTP status (OK)
     * and the found order as OrderDtoModel.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDtoModel> findById(@PathVariable Long id){
        OrderDto order = orderService.findById(id);
        OrderDtoModel orderDtoModel = orderDtoCustomLink.toLink(order);
        return new ResponseEntity<>(orderDtoModel, HttpStatus.OK);
    }

    /**
     * Create new order, processes POST requests at /orders.
     *
     * @param orderDto the new order which will be created.
     * @return the responseEntity object with contains HTTP status (CREATED)
     * and the saved order as OrderDtoModel.
     */
    @PostMapping
    public ResponseEntity<OrderDtoModel> save(@RequestBody OrderDto orderDto){
        OrderDto order = orderService.save(orderDto);
        OrderDtoModel orderDtoModel = orderDtoCustomLink.toLink(order);
        return new ResponseEntity<>(orderDtoModel, HttpStatus.CREATED);
    }
}


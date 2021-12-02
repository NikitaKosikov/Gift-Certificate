package com.epam.esm.service.impl;

import com.epam.esm.specification.model.SearchByParameters;
import com.epam.esm.dto.impl.OrderDto;
import com.epam.esm.dto.impl.SearchByParametersDto;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.CustomResourceNotFoundException;
import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.IncorrectParameterValueException;
import com.epam.esm.specification.SearchByParametersSpecification;
import com.epam.esm.service.OrderService;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.util.MessageKey;
import com.epam.esm.validator.OrderValidator;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Class is implementation of interface {@link OrderService} and intended to
 * work with order
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final String ORDER_COLUMN_PRICE_DB="price";

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final OrderValidator orderValidator;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper, OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.orderValidator = orderValidator;
    }

    @Override
    public List<OrderDto> findAll(Pageable pageable, SearchByParametersDto searchByParametersDto) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), searchByParametersDto.getSort());
        SearchByParameters searchByParameters = modelMapper.map(searchByParametersDto, SearchByParameters.class);
        Page<Order> orders = orderRepository.findAll(new SearchByParametersSpecification<>(searchByParameters), pageable);
        return modelMapper.map(orders.getContent(), new TypeToken<List<OrderDto>>() {}.getType());
    }

    @Override
    public OrderDto findById(Long id) throws CustomResourceNotFoundException {
        orderValidator.validateId(id);
        Optional<Order> orderOptional = orderRepository.findById(id);
        return orderOptional.map(order -> modelMapper.map(order, OrderDto.class))
                .orElseThrow(() -> new CustomResourceNotFoundException("no order by id",
                        MessageKey.ORDER_NOT_FOUND_BY_ID,String.valueOf(id), ErrorCode.ORDER.getCode()));
    }

    @Override
    public OrderDto save(OrderDto orderDto) throws IncorrectParameterValueException {
        orderValidator.validate(orderDto);
        Order order = modelMapper.map(orderDto, Order.class);
        order = orderRepository.save(order);
        return modelMapper.map(order, OrderDto.class);
    }
}

package com.epam.esm.service.impl;

import com.epam.esm.config.ServiceConfiguration;
import com.epam.esm.dto.impl.*;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.CustomResourceNotFoundException;
import com.epam.esm.exception.IncorrectParameterValueException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.specification.SearchByParametersSpecification;
import com.epam.esm.specification.model.SearchByParameters;
import com.epam.esm.validator.OrderValidator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = ServiceConfiguration.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderValidator orderValidator;
    private OrderService orderService;
    @Mock
    private ModelMapper modelMapper;

    private static Order order1;
    private static Order order2;
    private static Order order3;
    private static OrderDto orderDto1;
    private static OrderDto orderDto2;
    private static OrderDto orderDto3;
    private static Pageable pageable;
    private static Page<Order> orderPage;
    private static List<Order> orders;
    private static List<OrderDto> ordersDto;
    private static SearchByParametersDto searchByParametersDto;
    private static SearchByParameters searchByParameters;


    @BeforeEach
    public void initUseCase(){
        orderService = new OrderServiceImpl(orderRepository, modelMapper, orderValidator);
    }

    @BeforeAll
    public static void setUp(){
        order1 = new Order(1L, new BigDecimal("100.00"),
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), 1L,1L);
        order2 = new Order(null, new BigDecimal("120.00"),
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), 1L,1L);
        order3 = new Order(2L, new BigDecimal("140.00"),
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), 1L,1L);
        orderDto1 = new OrderDto(1L, new BigDecimal("100.00"),
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), 1L,1L);
        orderDto2 = new OrderDto(null, new BigDecimal("100.00"),
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), 1L,1L);
        orderDto3 = new OrderDto(2L, new BigDecimal("100.00"),
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), 1L,1L);
        pageable = PageRequest.of(1,5, Sort.unsorted());
        orders = List.of(order1, order2);
        ordersDto = List.of(orderDto1, orderDto2);
        searchByParametersDto = new SearchByParametersDto(new HashMap<>(), new ArrayList<>(), Sort.unsorted());
        searchByParameters = new SearchByParameters();
        orderPage = new PageImpl<>(List.of(order1, order2));
    }

    @AfterAll
    public static void tearDown(){
        order1 = null;
        order2 = null;
        order3 = null;
        orderDto1 = null;
        orderDto2 = null;
        orderDto3 = null;
        pageable = null;
        orders = null;
        ordersDto = null;
    }

    @Test
    void findAll() {
        Type type = new TypeToken<List<OrderDto>>() {}.getType();
        when(modelMapper.map(searchByParametersDto, SearchByParameters.class)).thenReturn(searchByParameters);
        when(orderRepository.findAll(isA(SearchByParametersSpecification.class), isA(Pageable.class))).thenReturn(orderPage);
        when(modelMapper.map(orders, type)).thenReturn(ordersDto);

        List<OrderDto> actualList = orderService.findAll(pageable, searchByParametersDto);

        assertEquals(actualList, ordersDto);
    }

    @Test
    void findByIdPositiveResult() {
        final Long id = 1L;
        doNothing().when(orderValidator).validateId(anyLong());
        when(orderRepository.findById(id)).thenReturn(Optional.of(order1));
        when(modelMapper.map(order1, OrderDto.class)).thenReturn(orderDto1);

        OrderDto actualOrderDto = orderService.findById(id);

        assertEquals(orderDto1, actualOrderDto);
    }

    @Test
    void findByIdNotFoundUserException() {
        final Long id = 100000L;
        doNothing().when(orderValidator).validateId(anyLong());
        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomResourceNotFoundException.class, () -> orderService.findById(id));
    }

    @Test
    void findByIdIncorrectIdException() {
        final Long id = -200L;
        doThrow(new IncorrectParameterValueException()).when(orderValidator).validateId(id);
        assertThrows(IncorrectParameterValueException.class, () -> orderService.findById(id));
    }

    @Test
    void savePositiveTest() {
        doNothing().when(orderValidator).validate(orderDto2);
        when(modelMapper.map(orderDto2, Order.class)).thenReturn(order2);
        when(orderRepository.save(order2)).thenReturn(order3);
        when(modelMapper.map(order3, OrderDto.class)).thenReturn(orderDto3);

        OrderDto actualOrderDto = orderService.save(orderDto2);

        assertEquals(orderDto3, actualOrderDto);
    }

    @Test
    void OrderValidatorOrderHasNotGiftCertificateId() {
        OrderDto orderDtoHasNotGiftCertificateId = new OrderDto(null, new BigDecimal("100.00"),
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), null,1L);

        doThrow(new IncorrectParameterValueException()).when(orderValidator).validate(orderDtoHasNotGiftCertificateId);

        assertThrows(IncorrectParameterValueException.class, () -> orderService.save(orderDtoHasNotGiftCertificateId));
    }

    @Test
    void OrderValidatorOrderHasNotUserId() {
        OrderDto orderDtoHasNotGiftCertificateId = new OrderDto(null, new BigDecimal("100.00"),
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), 1L,null);

        doThrow(new IncorrectParameterValueException()).when(orderValidator).validate(orderDtoHasNotGiftCertificateId);

        assertThrows(IncorrectParameterValueException.class, () -> orderService.save(orderDtoHasNotGiftCertificateId));
    }

    @Test
    void OrderValidatorInvalidPrice() {
        OrderDto orderDtoPriceIsNull = new OrderDto(null, new BigDecimal("100.00"),
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), 1L,1L);
        OrderDto orderDtoPriceScaleLessThanTwo = new OrderDto(null, new BigDecimal("100.0"),
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), 1L,1L);
        OrderDto orderDtoPriceScaleMoreThanTwo = new OrderDto(null, new BigDecimal("100.000"),
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), 1L,1L);
        OrderDto orderDtoPriceLessThanRequiredPrice = new OrderDto(null, new BigDecimal("0.00001"),
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), 1L,1L);
        OrderDto orderDtoPriceMoreThanRequiredPrice = new OrderDto(null, new BigDecimal("12321321321321321312"),
                LocalDateTime.of(2020, 12, 12, 12, 0, 0), 1L,1L);

        doThrow(new IncorrectParameterValueException()).when(orderValidator).validate(orderDtoPriceIsNull);
        doThrow(new IncorrectParameterValueException()).when(orderValidator).validate(orderDtoPriceScaleLessThanTwo);
        doThrow(new IncorrectParameterValueException()).when(orderValidator).validate(orderDtoPriceScaleMoreThanTwo);
        doThrow(new IncorrectParameterValueException()).when(orderValidator).validate(orderDtoPriceLessThanRequiredPrice);
        doThrow(new IncorrectParameterValueException()).when(orderValidator).validate(orderDtoPriceMoreThanRequiredPrice);

        assertThrows(IncorrectParameterValueException.class, () -> orderService.save(orderDtoPriceIsNull));
        assertThrows(IncorrectParameterValueException.class, () -> orderService.save(orderDtoPriceScaleLessThanTwo));
        assertThrows(IncorrectParameterValueException.class, () -> orderService.save(orderDtoPriceScaleMoreThanTwo));
        assertThrows(IncorrectParameterValueException.class, () -> orderService.save(orderDtoPriceLessThanRequiredPrice));
        assertThrows(IncorrectParameterValueException.class, () -> orderService.save(orderDtoPriceMoreThanRequiredPrice));
    }



}
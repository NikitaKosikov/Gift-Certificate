package com.epam.esm.service;

import com.epam.esm.dto.impl.OrderDto;
import com.epam.esm.dto.impl.SearchByParametersDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
public interface OrderService {

    /**
     * Looking for a set of orders as dto.
     *
     * @param pageable the contains information about the data selection.
     * @param searchByParametersDto the object that contains sorting and filter by fields.
     * @return list of found orders as dto or List#empty() if none found.
     */
    List<OrderDto> findAll(Pageable pageable, SearchByParametersDto searchByParametersDto);

    /**
     * Looking for the order as dto by specific id.
     *
     * @param id the id of order.
     * @return the found order as dto or throw CustomResourceNotFoundException if none found.
     */
    OrderDto findById(Long id);


    /**
     * Create order.
     *
     * @param orderDto the order as dto by which order will be created.
     * @return the created order as dto.
     */
    OrderDto save(OrderDto orderDto);
}

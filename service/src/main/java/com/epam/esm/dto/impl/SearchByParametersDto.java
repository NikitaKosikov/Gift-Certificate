package com.epam.esm.dto.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.util.*;

/**
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchByParametersDto {
    private Map<String, List<String>> filtration = new HashMap<>();
    private List<String> tagNames = new ArrayList<>();
    private Sort sort;

}

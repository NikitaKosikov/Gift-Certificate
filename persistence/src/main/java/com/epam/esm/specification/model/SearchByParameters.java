package com.epam.esm.specification.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchByParameters {
    private Map<String, List<String>> filters = new HashMap<>();
    private List<String> tagNames = new ArrayList<>();
}

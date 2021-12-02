package com.epam.esm.converter;

import com.epam.esm.dto.impl.SearchByParametersDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class converts parameters to Dto
 *
 * @author Nikita Kosikov
 * @version 1.0
 *
 */
@Component
public class ParametersToDtoConverter {

    private static final String TAG_NAMES_PARAMETER = "tag_names";
    private static final String SORT_PARAMETER = "sort";
    private static final String SEPARATOR_PARAMETERS = ",";
    private static final String DESC_SORT_SYMBOL = "-";
    private static final String ASC_SORT_SYMBOL = "+";

    /**
     * Convert parameters from request to SearchByParametersDto object.
     *
     * @param parameters the sorting, filtering, list of tag names parameters from request.
     * @return the SearchByParametersDto object.
     */
    public SearchByParametersDto convert(Map<String, String> parameters) {
        SearchByParametersDto searchByParametersDto = new SearchByParametersDto();
        List<String> tags = parseAndRemoveTagNamesString(parameters);
        searchByParametersDto.setTagNames(tags);
        addFiltrationTo(parameters, searchByParametersDto);
        addSortingTo(parameters, searchByParametersDto);
        return searchByParametersDto;
    }

    private List<String> parseAndRemoveTagNamesString(Map<String, String> parameters){
        List<String> tags;
        String tagNames = parameters.get(TAG_NAMES_PARAMETER);
        if (StringUtils.isNotBlank(tagNames)){
            tags = Stream.of(tagNames.split(SEPARATOR_PARAMETERS)).map(String::strip).collect(Collectors.toList());
        }else {
            return new ArrayList<>();
        }
        parameters.remove(TAG_NAMES_PARAMETER);
        return tags;
    }

    /**
     * Adding filtering in searchByParametersDto object.
     *
     * @param parameters the filtering parameters that will be added in searchByParametersDto object.
     * @param searchByParametersDto the searchByParametersDto object which will be added filtering.
     */
    public void addFiltrationTo(Map<String, String> parameters, SearchByParametersDto searchByParametersDto){

        Map<String, List<String>> filters = new HashMap<>();
        for (Map.Entry<String, String> parameter : parameters.entrySet()) {
            String[] parameterValues = parameter.getValue().split(SEPARATOR_PARAMETERS);
            List<String> values = new ArrayList<>();
            for (String parameterValue : parameterValues) {
                values.add(parameterValue.strip());
            }
            filters.put(parameter.getKey(), values);
        }

        searchByParametersDto.setFiltration(filters);
    }

    /**
     * Adding soring in searchByParametersDto object.
     *
     * @param parameters the sorting parameters that will be added in searchByParametersDto object.
     * @param searchByParametersDto the searchByParametersDto object which will be added sorting.
     */
    public void addSortingTo(Map<String, String> parameters, SearchByParametersDto searchByParametersDto){
        List<String> sort = new ArrayList<>();
        String sortString = parameters.get(SORT_PARAMETER);
        if (StringUtils.isNotBlank(sortString)){
            sort = Stream.of(sortString.split(SEPARATOR_PARAMETERS)).map(String::strip).collect(Collectors.toList());
        }

        Sort sorting = buildSort(sort);
        searchByParametersDto.setSort(sorting);
    }

    private Sort buildSort(List<String> dataSortList){
        List<Sort.Order> orders = new ArrayList<>();
        if (dataSortList!=null){
            for (String dataSort : dataSortList) {
                String sortByValue = dataSort.substring(1);
                if (dataSort.startsWith(ASC_SORT_SYMBOL)){
                    orders.add(Sort.Order.asc(sortByValue));
                }else if (dataSort.startsWith(DESC_SORT_SYMBOL)){
                    orders.add(Sort.Order.desc(sortByValue));
                }else {
                    orders.add(Sort.Order.asc(dataSort));
                }
            }
        }
        return Sort.by(orders);
    }
}

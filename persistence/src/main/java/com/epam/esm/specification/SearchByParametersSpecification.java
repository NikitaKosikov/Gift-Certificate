package com.epam.esm.specification;

import com.epam.esm.specification.model.SearchByParameters;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
public class SearchByParametersSpecification<T> implements Specification<T> {

    private static final String TAG_NAME_FROM_DB = "name";
    private static final String NAME_OF_TAG_TABLE = "tags";
    private static final String SIGN_PERCENT = "%";

    private final SearchByParameters searchByParameters;

    public SearchByParametersSpecification(SearchByParameters searchByParameters) {
        this.searchByParameters = searchByParameters;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        for (String tagName : searchByParameters.getTagNames()) {
            predicates.add(addTagName(tagName, criteriaBuilder, root));
        }
        predicates.addAll(addGiftCertificateByPartOfValue(searchByParameters.getFilters(), criteriaBuilder, root));

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private Predicate addTagName(String tagName, CriteriaBuilder criteriaBuilder,
                                 Root<T> root) {
        return criteriaBuilder.like(root.join(NAME_OF_TAG_TABLE).get(TAG_NAME_FROM_DB),
                SIGN_PERCENT + tagName.toLowerCase() + SIGN_PERCENT);
    }

    private List<Predicate> addGiftCertificateByPartOfValue(Map<String, List<String>> conditions, CriteriaBuilder criteriaBuilder, Root<T> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (!conditions.isEmpty()){
            for (Map.Entry<String, List<String>> condition : conditions.entrySet()) {
                for (String condi : condition.getValue()) {
                    predicates.add(criteriaBuilder.like(root.get(condition.getKey()),
                            SIGN_PERCENT + condi.toLowerCase() + SIGN_PERCENT));

                }
            }
        }

        return predicates;
    }

}

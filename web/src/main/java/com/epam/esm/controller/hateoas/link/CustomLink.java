package com.epam.esm.controller.hateoas.link;

import com.epam.esm.dto.AbstractDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

/**
 * T is classes which extends RepresentationModel class
 * and V is classes which extends AbstractDto class.
 *
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
public interface CustomLink<T extends RepresentationModel<T>, V extends AbstractDto> {

    /**
     * Added links to one entity.
     *
     * @param entity the entity in which will be added links.
     * @return the V object that will contain links.
     */
    T toLink(V entity);

    /**
     * Added links to all entities.
     *
     * @param entities the list of entities in which will be added links.
     * @return the CollectionModel<T> objects that will contain links.
     */
    CollectionModel<T> toLink(List<V> entities);
}

package com.epam.esm.service;

import com.epam.esm.dto.impl.SearchByParametersDto;
import com.epam.esm.dto.impl.TagDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {

    /**
     * Looking for a set of tags as dto.
     *
     * @param pageable the contains information about the data selection.
     * @param searchByParametersDto the object that contains sorting and filter by fields.
     * @return list of found tags as dto or List#empty() if none found.
     */
    List<TagDto> findAll(Pageable pageable, SearchByParametersDto searchByParametersDto);


    /**
     * Looking for the tag as dto by specific id.
     *
     * @param id the id of tag.
     * @return the found tag as dto or throw CustomResourceNotFoundException if none found.
     */
    TagDto findById(Long id);

    /**
     * Create tag.
     *
     * @param tagDto the tag dto by which order will be created.
     * @return the created tag as dto.
     */
    TagDto save(TagDto tagDto);

    /**
     * Full tag update.
     *
     * @param tagDto the tag as dto that will replace the old one.
     * @param id the id old tag that will be replacement.
     * @return the updated tag as dto.
     */
    TagDto putUpdate(TagDto tagDto, Long id);

    /**
     * Partial tag update.
     *
     * @param newTagDto the tag as dto that will replace the old one.
     * @param id the id old tag that will be replacement.
     * @return the updated tag as dto.
     */
    TagDto patchUpdate(TagDto newTagDto, Long id);

    /**
     * Delete the tag by specific id.
     *
     * @param id the id of the tag.
     */
    void delete(Long id);
}

package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.model.TagDtoModel;
import com.epam.esm.controller.hateoas.link.CustomLink;
import com.epam.esm.converter.ParametersToDtoConverter;
import com.epam.esm.dto.impl.SearchByParametersDto;
import com.epam.esm.dto.impl.TagDto;
import com.epam.esm.service.TagService;
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
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    private final CustomLink<TagDtoModel, TagDto> tagDtoCustomLink;
    private final ParametersToDtoConverter parametersToDtoConverter;

    @Autowired
    public TagController(TagService tagService, CustomLink<TagDtoModel, TagDto> tagDtoCustomLink, ParametersToDtoConverter parametersToDtoConverter){
        this.tagService = tagService;
        this.tagDtoCustomLink = tagDtoCustomLink;
        this.parametersToDtoConverter = parametersToDtoConverter;
    }

    /**
     * Find tags by parameters, processes GET requests at
     * /tags
     *
     * @param pageable the contains information about the data selection.
     * @param parameters the tag search parameters.
     * @return the responseEntity object with contains HTTP status (200)
     * and found tags as CollectionModel<TagDtoModel>.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CollectionModel<TagDtoModel>> findAll(Pageable pageable, @RequestParam(required = false) Map<String, String> parameters){
        SearchByParametersDto searchByParametersDto = parametersToDtoConverter.convert(parameters);
        List<TagDto> tags = tagService.findAll(pageable, searchByParametersDto);
        CollectionModel<TagDtoModel> tagDtoModelCollectionModel = tagDtoCustomLink.toLink(tags);
        return new ResponseEntity<>(tagDtoModelCollectionModel, HttpStatus.OK);
    }

    /**
     * Find tag by id, processes GET requests at /tags/{id}.
     *
     * @param id the tag id which will be found.
     * @return the responseEntity object with contains HTTP status (OK)
     * and the found tag as TagDtoModel.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<TagDtoModel> findById(@PathVariable Long id){
        TagDto tag = tagService.findById(id);
        TagDtoModel tagDtoModel = tagDtoCustomLink.toLink(tag);
        return new ResponseEntity<>(tagDtoModel, HttpStatus.OK);
    }

    /**
     * Create new tag, processes POST requests at /tags.
     *
     * @param tagDto the new tag which will be created.
     * @return the responseEntity object with contains HTTP status (CREATED)
     * and the saved tag as TagDtoModel.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TagDtoModel> save(@RequestBody TagDto tagDto){
        TagDto tag = tagService.save(tagDto);
        TagDtoModel tagDtoModel = tagDtoCustomLink.toLink(tag);
        return new ResponseEntity<>(tagDtoModel, HttpStatus.CREATED);
    }

    /**
     * Full tag update, processes PUT requests at /tags/{id}
     *
     * @param id the tag id which will be updated.
     * @param tagDto the tag dto with updated fields.
     * @return the responseEntity object with contains HTTP status (OK)
     * and the updated tag as TagDtoModel.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TagDtoModel> putUpdate(@PathVariable Long id, @RequestBody TagDto tagDto){
        TagDto tag = tagService.putUpdate(tagDto, id);
        TagDtoModel tagDtoModel = tagDtoCustomLink.toLink(tag);
        return new ResponseEntity<>(tagDtoModel, HttpStatus.OK);
    }

    /**
     * Partial tag update,, processes PUT requests at /tags/{id}
     *
     * @param id the tag id which will be updated.
     * @param tagDto the tag dto with updated fields.
     * @return the responseEntity object with contains HTTP status (OK)
     * and the updated tag as TagDtoModel.
     */
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TagDtoModel> patchUpdate(@PathVariable Long id, @RequestBody TagDto tagDto){
        TagDto tag = tagService.patchUpdate(tagDto, id);
        TagDtoModel tagDtoModel = tagDtoCustomLink.toLink(tag);
        return new ResponseEntity<>(tagDtoModel, HttpStatus.OK);
    }

    /**
     * Delete tag by id, processes DELETE requests at
     * /tags/{id}.
     *
     * @param id the tag id which will be deleted.
     * @return the responseEntity object with contains only HTTP status (NO_CONTENT).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TagDto> delete(@PathVariable Long id){
        tagService.delete(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}

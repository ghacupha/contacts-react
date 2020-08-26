package io.github.contacts.web.rest;

import io.github.contacts.domain.ContactsFileType;
import io.github.contacts.service.ContactsFileTypeService;
import io.github.contacts.web.rest.errors.BadRequestAlertException;
import io.github.contacts.service.dto.ContactsFileTypeCriteria;
import io.github.contacts.service.ContactsFileTypeQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link io.github.contacts.domain.ContactsFileType}.
 */
@RestController
@RequestMapping("/api")
public class ContactsFileTypeResource {

    private final Logger log = LoggerFactory.getLogger(ContactsFileTypeResource.class);

    private static final String ENTITY_NAME = "contactsContactsFileType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactsFileTypeService contactsFileTypeService;

    private final ContactsFileTypeQueryService contactsFileTypeQueryService;

    public ContactsFileTypeResource(ContactsFileTypeService contactsFileTypeService, ContactsFileTypeQueryService contactsFileTypeQueryService) {
        this.contactsFileTypeService = contactsFileTypeService;
        this.contactsFileTypeQueryService = contactsFileTypeQueryService;
    }

    /**
     * {@code POST  /contacts-file-types} : Create a new contactsFileType.
     *
     * @param contactsFileType the contactsFileType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactsFileType, or with status {@code 400 (Bad Request)} if the contactsFileType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contacts-file-types")
    public ResponseEntity<ContactsFileType> createContactsFileType(@Valid @RequestBody ContactsFileType contactsFileType) throws URISyntaxException {
        log.debug("REST request to save ContactsFileType : {}", contactsFileType);
        if (contactsFileType.getId() != null) {
            throw new BadRequestAlertException("A new contactsFileType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactsFileType result = contactsFileTypeService.save(contactsFileType);
        return ResponseEntity.created(new URI("/api/contacts-file-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contacts-file-types} : Updates an existing contactsFileType.
     *
     * @param contactsFileType the contactsFileType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactsFileType,
     * or with status {@code 400 (Bad Request)} if the contactsFileType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactsFileType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contacts-file-types")
    public ResponseEntity<ContactsFileType> updateContactsFileType(@Valid @RequestBody ContactsFileType contactsFileType) throws URISyntaxException {
        log.debug("REST request to update ContactsFileType : {}", contactsFileType);
        if (contactsFileType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ContactsFileType result = contactsFileTypeService.save(contactsFileType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contactsFileType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /contacts-file-types} : get all the contactsFileTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactsFileTypes in body.
     */
    @GetMapping("/contacts-file-types")
    public ResponseEntity<List<ContactsFileType>> getAllContactsFileTypes(ContactsFileTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ContactsFileTypes by criteria: {}", criteria);
        Page<ContactsFileType> page = contactsFileTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contacts-file-types/count} : count all the contactsFileTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/contacts-file-types/count")
    public ResponseEntity<Long> countContactsFileTypes(ContactsFileTypeCriteria criteria) {
        log.debug("REST request to count ContactsFileTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(contactsFileTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /contacts-file-types/:id} : get the "id" contactsFileType.
     *
     * @param id the id of the contactsFileType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactsFileType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contacts-file-types/{id}")
    public ResponseEntity<ContactsFileType> getContactsFileType(@PathVariable Long id) {
        log.debug("REST request to get ContactsFileType : {}", id);
        Optional<ContactsFileType> contactsFileType = contactsFileTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactsFileType);
    }

    /**
     * {@code DELETE  /contacts-file-types/:id} : delete the "id" contactsFileType.
     *
     * @param id the id of the contactsFileType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contacts-file-types/{id}")
    public ResponseEntity<Void> deleteContactsFileType(@PathVariable Long id) {
        log.debug("REST request to delete ContactsFileType : {}", id);
        contactsFileTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/contacts-file-types?query=:query} : search for the contactsFileType corresponding
     * to the query.
     *
     * @param query the query of the contactsFileType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/contacts-file-types")
    public ResponseEntity<List<ContactsFileType>> searchContactsFileTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ContactsFileTypes for query {}", query);
        Page<ContactsFileType> page = contactsFileTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}

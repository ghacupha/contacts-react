package io.github.contacts.web.rest;

import io.github.contacts.service.ContactsFileUploadService;
import io.github.contacts.web.rest.errors.BadRequestAlertException;
import io.github.contacts.service.dto.ContactsFileUploadDTO;
import io.github.contacts.service.dto.ContactsFileUploadCriteria;
import io.github.contacts.service.ContactsFileUploadQueryService;

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
 * REST controller for managing {@link io.github.contacts.domain.ContactsFileUpload}.
 */
@RestController
@RequestMapping("/api")
public class ContactsFileUploadResource {

    private final Logger log = LoggerFactory.getLogger(ContactsFileUploadResource.class);

    private static final String ENTITY_NAME = "contactsContactsFileUpload";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactsFileUploadService contactsFileUploadService;

    private final ContactsFileUploadQueryService contactsFileUploadQueryService;

    public ContactsFileUploadResource(ContactsFileUploadService contactsFileUploadService, ContactsFileUploadQueryService contactsFileUploadQueryService) {
        this.contactsFileUploadService = contactsFileUploadService;
        this.contactsFileUploadQueryService = contactsFileUploadQueryService;
    }

    /**
     * {@code POST  /contacts-file-uploads} : Create a new contactsFileUpload.
     *
     * @param contactsFileUploadDTO the contactsFileUploadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactsFileUploadDTO, or with status {@code 400 (Bad Request)} if the contactsFileUpload has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contacts-file-uploads")
    public ResponseEntity<ContactsFileUploadDTO> createContactsFileUpload(@Valid @RequestBody ContactsFileUploadDTO contactsFileUploadDTO) throws URISyntaxException {
        log.debug("REST request to save ContactsFileUpload : {}", contactsFileUploadDTO);
        if (contactsFileUploadDTO.getId() != null) {
            throw new BadRequestAlertException("A new contactsFileUpload cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactsFileUploadDTO result = contactsFileUploadService.save(contactsFileUploadDTO);
        return ResponseEntity.created(new URI("/api/contacts-file-uploads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contacts-file-uploads} : Updates an existing contactsFileUpload.
     *
     * @param contactsFileUploadDTO the contactsFileUploadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactsFileUploadDTO,
     * or with status {@code 400 (Bad Request)} if the contactsFileUploadDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactsFileUploadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contacts-file-uploads")
    public ResponseEntity<ContactsFileUploadDTO> updateContactsFileUpload(@Valid @RequestBody ContactsFileUploadDTO contactsFileUploadDTO) throws URISyntaxException {
        log.debug("REST request to update ContactsFileUpload : {}", contactsFileUploadDTO);
        if (contactsFileUploadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ContactsFileUploadDTO result = contactsFileUploadService.save(contactsFileUploadDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contactsFileUploadDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /contacts-file-uploads} : get all the contactsFileUploads.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactsFileUploads in body.
     */
    @GetMapping("/contacts-file-uploads")
    public ResponseEntity<List<ContactsFileUploadDTO>> getAllContactsFileUploads(ContactsFileUploadCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ContactsFileUploads by criteria: {}", criteria);
        Page<ContactsFileUploadDTO> page = contactsFileUploadQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contacts-file-uploads/count} : count all the contactsFileUploads.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/contacts-file-uploads/count")
    public ResponseEntity<Long> countContactsFileUploads(ContactsFileUploadCriteria criteria) {
        log.debug("REST request to count ContactsFileUploads by criteria: {}", criteria);
        return ResponseEntity.ok().body(contactsFileUploadQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /contacts-file-uploads/:id} : get the "id" contactsFileUpload.
     *
     * @param id the id of the contactsFileUploadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactsFileUploadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contacts-file-uploads/{id}")
    public ResponseEntity<ContactsFileUploadDTO> getContactsFileUpload(@PathVariable Long id) {
        log.debug("REST request to get ContactsFileUpload : {}", id);
        Optional<ContactsFileUploadDTO> contactsFileUploadDTO = contactsFileUploadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactsFileUploadDTO);
    }

    /**
     * {@code DELETE  /contacts-file-uploads/:id} : delete the "id" contactsFileUpload.
     *
     * @param id the id of the contactsFileUploadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contacts-file-uploads/{id}")
    public ResponseEntity<Void> deleteContactsFileUpload(@PathVariable Long id) {
        log.debug("REST request to delete ContactsFileUpload : {}", id);
        contactsFileUploadService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/contacts-file-uploads?query=:query} : search for the contactsFileUpload corresponding
     * to the query.
     *
     * @param query the query of the contactsFileUpload search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/contacts-file-uploads")
    public ResponseEntity<List<ContactsFileUploadDTO>> searchContactsFileUploads(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ContactsFileUploads for query {}", query);
        Page<ContactsFileUploadDTO> page = contactsFileUploadService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}

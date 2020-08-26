package io.github.contacts.web.rest;

import io.github.contacts.service.ContactsMessageTokenService;
import io.github.contacts.web.rest.errors.BadRequestAlertException;
import io.github.contacts.service.dto.ContactsMessageTokenDTO;
import io.github.contacts.service.dto.ContactsMessageTokenCriteria;
import io.github.contacts.service.ContactsMessageTokenQueryService;

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
 * REST controller for managing {@link io.github.contacts.domain.ContactsMessageToken}.
 */
@RestController
@RequestMapping("/api")
public class ContactsMessageTokenResource {

    private final Logger log = LoggerFactory.getLogger(ContactsMessageTokenResource.class);

    private static final String ENTITY_NAME = "contactsContactsMessageToken";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactsMessageTokenService contactsMessageTokenService;

    private final ContactsMessageTokenQueryService contactsMessageTokenQueryService;

    public ContactsMessageTokenResource(ContactsMessageTokenService contactsMessageTokenService, ContactsMessageTokenQueryService contactsMessageTokenQueryService) {
        this.contactsMessageTokenService = contactsMessageTokenService;
        this.contactsMessageTokenQueryService = contactsMessageTokenQueryService;
    }

    /**
     * {@code POST  /contacts-message-tokens} : Create a new contactsMessageToken.
     *
     * @param contactsMessageTokenDTO the contactsMessageTokenDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactsMessageTokenDTO, or with status {@code 400 (Bad Request)} if the contactsMessageToken has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contacts-message-tokens")
    public ResponseEntity<ContactsMessageTokenDTO> createContactsMessageToken(@Valid @RequestBody ContactsMessageTokenDTO contactsMessageTokenDTO) throws URISyntaxException {
        log.debug("REST request to save ContactsMessageToken : {}", contactsMessageTokenDTO);
        if (contactsMessageTokenDTO.getId() != null) {
            throw new BadRequestAlertException("A new contactsMessageToken cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactsMessageTokenDTO result = contactsMessageTokenService.save(contactsMessageTokenDTO);
        return ResponseEntity.created(new URI("/api/contacts-message-tokens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contacts-message-tokens} : Updates an existing contactsMessageToken.
     *
     * @param contactsMessageTokenDTO the contactsMessageTokenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactsMessageTokenDTO,
     * or with status {@code 400 (Bad Request)} if the contactsMessageTokenDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactsMessageTokenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contacts-message-tokens")
    public ResponseEntity<ContactsMessageTokenDTO> updateContactsMessageToken(@Valid @RequestBody ContactsMessageTokenDTO contactsMessageTokenDTO) throws URISyntaxException {
        log.debug("REST request to update ContactsMessageToken : {}", contactsMessageTokenDTO);
        if (contactsMessageTokenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ContactsMessageTokenDTO result = contactsMessageTokenService.save(contactsMessageTokenDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contactsMessageTokenDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /contacts-message-tokens} : get all the contactsMessageTokens.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactsMessageTokens in body.
     */
    @GetMapping("/contacts-message-tokens")
    public ResponseEntity<List<ContactsMessageTokenDTO>> getAllContactsMessageTokens(ContactsMessageTokenCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ContactsMessageTokens by criteria: {}", criteria);
        Page<ContactsMessageTokenDTO> page = contactsMessageTokenQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contacts-message-tokens/count} : count all the contactsMessageTokens.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/contacts-message-tokens/count")
    public ResponseEntity<Long> countContactsMessageTokens(ContactsMessageTokenCriteria criteria) {
        log.debug("REST request to count ContactsMessageTokens by criteria: {}", criteria);
        return ResponseEntity.ok().body(contactsMessageTokenQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /contacts-message-tokens/:id} : get the "id" contactsMessageToken.
     *
     * @param id the id of the contactsMessageTokenDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactsMessageTokenDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contacts-message-tokens/{id}")
    public ResponseEntity<ContactsMessageTokenDTO> getContactsMessageToken(@PathVariable Long id) {
        log.debug("REST request to get ContactsMessageToken : {}", id);
        Optional<ContactsMessageTokenDTO> contactsMessageTokenDTO = contactsMessageTokenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactsMessageTokenDTO);
    }

    /**
     * {@code DELETE  /contacts-message-tokens/:id} : delete the "id" contactsMessageToken.
     *
     * @param id the id of the contactsMessageTokenDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contacts-message-tokens/{id}")
    public ResponseEntity<Void> deleteContactsMessageToken(@PathVariable Long id) {
        log.debug("REST request to delete ContactsMessageToken : {}", id);
        contactsMessageTokenService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/contacts-message-tokens?query=:query} : search for the contactsMessageToken corresponding
     * to the query.
     *
     * @param query the query of the contactsMessageToken search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/contacts-message-tokens")
    public ResponseEntity<List<ContactsMessageTokenDTO>> searchContactsMessageTokens(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ContactsMessageTokens for query {}", query);
        Page<ContactsMessageTokenDTO> page = contactsMessageTokenService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}

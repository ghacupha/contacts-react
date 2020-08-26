package io.github.contacts.service;

import io.github.contacts.service.dto.ContactsMessageTokenDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.contacts.domain.ContactsMessageToken}.
 */
public interface ContactsMessageTokenService {

    /**
     * Save a contactsMessageToken.
     *
     * @param contactsMessageTokenDTO the entity to save.
     * @return the persisted entity.
     */
    ContactsMessageTokenDTO save(ContactsMessageTokenDTO contactsMessageTokenDTO);

    /**
     * Get all the contactsMessageTokens.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContactsMessageTokenDTO> findAll(Pageable pageable);


    /**
     * Get the "id" contactsMessageToken.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContactsMessageTokenDTO> findOne(Long id);

    /**
     * Delete the "id" contactsMessageToken.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the contactsMessageToken corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContactsMessageTokenDTO> search(String query, Pageable pageable);
}

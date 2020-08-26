package io.github.contacts.service;

import io.github.contacts.service.dto.ContactsFileUploadDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.contacts.domain.ContactsFileUpload}.
 */
public interface ContactsFileUploadService {

    /**
     * Save a contactsFileUpload.
     *
     * @param contactsFileUploadDTO the entity to save.
     * @return the persisted entity.
     */
    ContactsFileUploadDTO save(ContactsFileUploadDTO contactsFileUploadDTO);

    /**
     * Get all the contactsFileUploads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContactsFileUploadDTO> findAll(Pageable pageable);


    /**
     * Get the "id" contactsFileUpload.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContactsFileUploadDTO> findOne(Long id);

    /**
     * Delete the "id" contactsFileUpload.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the contactsFileUpload corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContactsFileUploadDTO> search(String query, Pageable pageable);
}

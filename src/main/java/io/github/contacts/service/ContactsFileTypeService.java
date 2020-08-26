package io.github.contacts.service;

import io.github.contacts.domain.ContactsFileType;
import io.github.contacts.repository.ContactsFileTypeRepository;
import io.github.contacts.repository.search.ContactsFileTypeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link ContactsFileType}.
 */
@Service
@Transactional
public class ContactsFileTypeService {

    private final Logger log = LoggerFactory.getLogger(ContactsFileTypeService.class);

    private final ContactsFileTypeRepository contactsFileTypeRepository;

    private final ContactsFileTypeSearchRepository contactsFileTypeSearchRepository;

    public ContactsFileTypeService(ContactsFileTypeRepository contactsFileTypeRepository, ContactsFileTypeSearchRepository contactsFileTypeSearchRepository) {
        this.contactsFileTypeRepository = contactsFileTypeRepository;
        this.contactsFileTypeSearchRepository = contactsFileTypeSearchRepository;
    }

    /**
     * Save a contactsFileType.
     *
     * @param contactsFileType the entity to save.
     * @return the persisted entity.
     */
    public ContactsFileType save(ContactsFileType contactsFileType) {
        log.debug("Request to save ContactsFileType : {}", contactsFileType);
        ContactsFileType result = contactsFileTypeRepository.save(contactsFileType);
        contactsFileTypeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the contactsFileTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactsFileType> findAll(Pageable pageable) {
        log.debug("Request to get all ContactsFileTypes");
        return contactsFileTypeRepository.findAll(pageable);
    }


    /**
     * Get one contactsFileType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContactsFileType> findOne(Long id) {
        log.debug("Request to get ContactsFileType : {}", id);
        return contactsFileTypeRepository.findById(id);
    }

    /**
     * Delete the contactsFileType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ContactsFileType : {}", id);
        contactsFileTypeRepository.deleteById(id);
        contactsFileTypeSearchRepository.deleteById(id);
    }

    /**
     * Search for the contactsFileType corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactsFileType> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ContactsFileTypes for query {}", query);
        return contactsFileTypeSearchRepository.search(queryStringQuery(query), pageable);    }
}

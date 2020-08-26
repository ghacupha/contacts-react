package io.github.contacts.service.impl;

import io.github.contacts.service.ContactsFileUploadService;
import io.github.contacts.domain.ContactsFileUpload;
import io.github.contacts.repository.ContactsFileUploadRepository;
import io.github.contacts.repository.search.ContactsFileUploadSearchRepository;
import io.github.contacts.service.dto.ContactsFileUploadDTO;
import io.github.contacts.service.mapper.ContactsFileUploadMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link ContactsFileUpload}.
 */
@Service
@Transactional
public class ContactsFileUploadServiceImpl implements ContactsFileUploadService {

    private final Logger log = LoggerFactory.getLogger(ContactsFileUploadServiceImpl.class);

    private final ContactsFileUploadRepository contactsFileUploadRepository;

    private final ContactsFileUploadMapper contactsFileUploadMapper;

    private final ContactsFileUploadSearchRepository contactsFileUploadSearchRepository;

    public ContactsFileUploadServiceImpl(ContactsFileUploadRepository contactsFileUploadRepository, ContactsFileUploadMapper contactsFileUploadMapper, ContactsFileUploadSearchRepository contactsFileUploadSearchRepository) {
        this.contactsFileUploadRepository = contactsFileUploadRepository;
        this.contactsFileUploadMapper = contactsFileUploadMapper;
        this.contactsFileUploadSearchRepository = contactsFileUploadSearchRepository;
    }

    @Override
    public ContactsFileUploadDTO save(ContactsFileUploadDTO contactsFileUploadDTO) {
        log.debug("Request to save ContactsFileUpload : {}", contactsFileUploadDTO);
        ContactsFileUpload contactsFileUpload = contactsFileUploadMapper.toEntity(contactsFileUploadDTO);
        contactsFileUpload = contactsFileUploadRepository.save(contactsFileUpload);
        ContactsFileUploadDTO result = contactsFileUploadMapper.toDto(contactsFileUpload);
        contactsFileUploadSearchRepository.save(contactsFileUpload);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactsFileUploadDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContactsFileUploads");
        return contactsFileUploadRepository.findAll(pageable)
            .map(contactsFileUploadMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ContactsFileUploadDTO> findOne(Long id) {
        log.debug("Request to get ContactsFileUpload : {}", id);
        return contactsFileUploadRepository.findById(id)
            .map(contactsFileUploadMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContactsFileUpload : {}", id);
        contactsFileUploadRepository.deleteById(id);
        contactsFileUploadSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactsFileUploadDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ContactsFileUploads for query {}", query);
        return contactsFileUploadSearchRepository.search(queryStringQuery(query), pageable)
            .map(contactsFileUploadMapper::toDto);
    }
}

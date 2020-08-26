package io.github.contacts.service.impl;

import io.github.contacts.service.ContactsMessageTokenService;
import io.github.contacts.domain.ContactsMessageToken;
import io.github.contacts.repository.ContactsMessageTokenRepository;
import io.github.contacts.repository.search.ContactsMessageTokenSearchRepository;
import io.github.contacts.service.dto.ContactsMessageTokenDTO;
import io.github.contacts.service.mapper.ContactsMessageTokenMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link ContactsMessageToken}.
 */
@Service
@Transactional
public class ContactsMessageTokenServiceImpl implements ContactsMessageTokenService {

    private final Logger log = LoggerFactory.getLogger(ContactsMessageTokenServiceImpl.class);

    private final ContactsMessageTokenRepository contactsMessageTokenRepository;

    private final ContactsMessageTokenMapper contactsMessageTokenMapper;

    private final ContactsMessageTokenSearchRepository contactsMessageTokenSearchRepository;

    public ContactsMessageTokenServiceImpl(ContactsMessageTokenRepository contactsMessageTokenRepository, ContactsMessageTokenMapper contactsMessageTokenMapper, ContactsMessageTokenSearchRepository contactsMessageTokenSearchRepository) {
        this.contactsMessageTokenRepository = contactsMessageTokenRepository;
        this.contactsMessageTokenMapper = contactsMessageTokenMapper;
        this.contactsMessageTokenSearchRepository = contactsMessageTokenSearchRepository;
    }

    @Override
    public ContactsMessageTokenDTO save(ContactsMessageTokenDTO contactsMessageTokenDTO) {
        log.debug("Request to save ContactsMessageToken : {}", contactsMessageTokenDTO);
        ContactsMessageToken contactsMessageToken = contactsMessageTokenMapper.toEntity(contactsMessageTokenDTO);
        contactsMessageToken = contactsMessageTokenRepository.save(contactsMessageToken);
        ContactsMessageTokenDTO result = contactsMessageTokenMapper.toDto(contactsMessageToken);
        contactsMessageTokenSearchRepository.save(contactsMessageToken);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactsMessageTokenDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContactsMessageTokens");
        return contactsMessageTokenRepository.findAll(pageable)
            .map(contactsMessageTokenMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ContactsMessageTokenDTO> findOne(Long id) {
        log.debug("Request to get ContactsMessageToken : {}", id);
        return contactsMessageTokenRepository.findById(id)
            .map(contactsMessageTokenMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContactsMessageToken : {}", id);
        contactsMessageTokenRepository.deleteById(id);
        contactsMessageTokenSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactsMessageTokenDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ContactsMessageTokens for query {}", query);
        return contactsMessageTokenSearchRepository.search(queryStringQuery(query), pageable)
            .map(contactsMessageTokenMapper::toDto);
    }
}

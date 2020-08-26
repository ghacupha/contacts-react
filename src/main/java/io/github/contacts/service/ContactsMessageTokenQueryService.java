package io.github.contacts.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import io.github.contacts.domain.ContactsMessageToken;
import io.github.contacts.domain.*; // for static metamodels
import io.github.contacts.repository.ContactsMessageTokenRepository;
import io.github.contacts.repository.search.ContactsMessageTokenSearchRepository;
import io.github.contacts.service.dto.ContactsMessageTokenCriteria;
import io.github.contacts.service.dto.ContactsMessageTokenDTO;
import io.github.contacts.service.mapper.ContactsMessageTokenMapper;

/**
 * Service for executing complex queries for {@link ContactsMessageToken} entities in the database.
 * The main input is a {@link ContactsMessageTokenCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContactsMessageTokenDTO} or a {@link Page} of {@link ContactsMessageTokenDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContactsMessageTokenQueryService extends QueryService<ContactsMessageToken> {

    private final Logger log = LoggerFactory.getLogger(ContactsMessageTokenQueryService.class);

    private final ContactsMessageTokenRepository contactsMessageTokenRepository;

    private final ContactsMessageTokenMapper contactsMessageTokenMapper;

    private final ContactsMessageTokenSearchRepository contactsMessageTokenSearchRepository;

    public ContactsMessageTokenQueryService(ContactsMessageTokenRepository contactsMessageTokenRepository, ContactsMessageTokenMapper contactsMessageTokenMapper, ContactsMessageTokenSearchRepository contactsMessageTokenSearchRepository) {
        this.contactsMessageTokenRepository = contactsMessageTokenRepository;
        this.contactsMessageTokenMapper = contactsMessageTokenMapper;
        this.contactsMessageTokenSearchRepository = contactsMessageTokenSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ContactsMessageTokenDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContactsMessageTokenDTO> findByCriteria(ContactsMessageTokenCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ContactsMessageToken> specification = createSpecification(criteria);
        return contactsMessageTokenMapper.toDto(contactsMessageTokenRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContactsMessageTokenDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactsMessageTokenDTO> findByCriteria(ContactsMessageTokenCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContactsMessageToken> specification = createSpecification(criteria);
        return contactsMessageTokenRepository.findAll(specification, page)
            .map(contactsMessageTokenMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContactsMessageTokenCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ContactsMessageToken> specification = createSpecification(criteria);
        return contactsMessageTokenRepository.count(specification);
    }

    /**
     * Function to convert {@link ContactsMessageTokenCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ContactsMessageToken> createSpecification(ContactsMessageTokenCriteria criteria) {
        Specification<ContactsMessageToken> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ContactsMessageToken_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ContactsMessageToken_.description));
            }
            if (criteria.getTimeSent() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeSent(), ContactsMessageToken_.timeSent));
            }
            if (criteria.getTokenValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTokenValue(), ContactsMessageToken_.tokenValue));
            }
            if (criteria.getReceived() != null) {
                specification = specification.and(buildSpecification(criteria.getReceived(), ContactsMessageToken_.received));
            }
            if (criteria.getActioned() != null) {
                specification = specification.and(buildSpecification(criteria.getActioned(), ContactsMessageToken_.actioned));
            }
            if (criteria.getContentFullyEnqueued() != null) {
                specification = specification.and(buildSpecification(criteria.getContentFullyEnqueued(), ContactsMessageToken_.contentFullyEnqueued));
            }
        }
        return specification;
    }
}

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

import io.github.contacts.domain.ContactsFileUpload;
import io.github.contacts.domain.*; // for static metamodels
import io.github.contacts.repository.ContactsFileUploadRepository;
import io.github.contacts.repository.search.ContactsFileUploadSearchRepository;
import io.github.contacts.service.dto.ContactsFileUploadCriteria;
import io.github.contacts.service.dto.ContactsFileUploadDTO;
import io.github.contacts.service.mapper.ContactsFileUploadMapper;

/**
 * Service for executing complex queries for {@link ContactsFileUpload} entities in the database.
 * The main input is a {@link ContactsFileUploadCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContactsFileUploadDTO} or a {@link Page} of {@link ContactsFileUploadDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContactsFileUploadQueryService extends QueryService<ContactsFileUpload> {

    private final Logger log = LoggerFactory.getLogger(ContactsFileUploadQueryService.class);

    private final ContactsFileUploadRepository contactsFileUploadRepository;

    private final ContactsFileUploadMapper contactsFileUploadMapper;

    private final ContactsFileUploadSearchRepository contactsFileUploadSearchRepository;

    public ContactsFileUploadQueryService(ContactsFileUploadRepository contactsFileUploadRepository, ContactsFileUploadMapper contactsFileUploadMapper, ContactsFileUploadSearchRepository contactsFileUploadSearchRepository) {
        this.contactsFileUploadRepository = contactsFileUploadRepository;
        this.contactsFileUploadMapper = contactsFileUploadMapper;
        this.contactsFileUploadSearchRepository = contactsFileUploadSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ContactsFileUploadDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContactsFileUploadDTO> findByCriteria(ContactsFileUploadCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ContactsFileUpload> specification = createSpecification(criteria);
        return contactsFileUploadMapper.toDto(contactsFileUploadRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContactsFileUploadDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactsFileUploadDTO> findByCriteria(ContactsFileUploadCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContactsFileUpload> specification = createSpecification(criteria);
        return contactsFileUploadRepository.findAll(specification, page)
            .map(contactsFileUploadMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContactsFileUploadCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ContactsFileUpload> specification = createSpecification(criteria);
        return contactsFileUploadRepository.count(specification);
    }

    /**
     * Function to convert {@link ContactsFileUploadCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ContactsFileUpload> createSpecification(ContactsFileUploadCriteria criteria) {
        Specification<ContactsFileUpload> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ContactsFileUpload_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ContactsFileUpload_.description));
            }
            if (criteria.getFileName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileName(), ContactsFileUpload_.fileName));
            }
            if (criteria.getPeriodFrom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPeriodFrom(), ContactsFileUpload_.periodFrom));
            }
            if (criteria.getPeriodTo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPeriodTo(), ContactsFileUpload_.periodTo));
            }
            if (criteria.getContactsFileTypeId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getContactsFileTypeId(), ContactsFileUpload_.contactsFileTypeId));
            }
            if (criteria.getUploadSuccessful() != null) {
                specification = specification.and(buildSpecification(criteria.getUploadSuccessful(), ContactsFileUpload_.uploadSuccessful));
            }
            if (criteria.getUploadProcessed() != null) {
                specification = specification.and(buildSpecification(criteria.getUploadProcessed(), ContactsFileUpload_.uploadProcessed));
            }
            if (criteria.getUploadToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUploadToken(), ContactsFileUpload_.uploadToken));
            }
        }
        return specification;
    }
}

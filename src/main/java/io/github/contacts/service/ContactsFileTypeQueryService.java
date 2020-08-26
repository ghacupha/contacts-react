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

import io.github.contacts.domain.ContactsFileType;
import io.github.contacts.domain.*; // for static metamodels
import io.github.contacts.repository.ContactsFileTypeRepository;
import io.github.contacts.repository.search.ContactsFileTypeSearchRepository;
import io.github.contacts.service.dto.ContactsFileTypeCriteria;

/**
 * Service for executing complex queries for {@link ContactsFileType} entities in the database.
 * The main input is a {@link ContactsFileTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContactsFileType} or a {@link Page} of {@link ContactsFileType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContactsFileTypeQueryService extends QueryService<ContactsFileType> {

    private final Logger log = LoggerFactory.getLogger(ContactsFileTypeQueryService.class);

    private final ContactsFileTypeRepository contactsFileTypeRepository;

    private final ContactsFileTypeSearchRepository contactsFileTypeSearchRepository;

    public ContactsFileTypeQueryService(ContactsFileTypeRepository contactsFileTypeRepository, ContactsFileTypeSearchRepository contactsFileTypeSearchRepository) {
        this.contactsFileTypeRepository = contactsFileTypeRepository;
        this.contactsFileTypeSearchRepository = contactsFileTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ContactsFileType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContactsFileType> findByCriteria(ContactsFileTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ContactsFileType> specification = createSpecification(criteria);
        return contactsFileTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ContactsFileType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactsFileType> findByCriteria(ContactsFileTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContactsFileType> specification = createSpecification(criteria);
        return contactsFileTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContactsFileTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ContactsFileType> specification = createSpecification(criteria);
        return contactsFileTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ContactsFileTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ContactsFileType> createSpecification(ContactsFileTypeCriteria criteria) {
        Specification<ContactsFileType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ContactsFileType_.id));
            }
            if (criteria.getContactsFileTypeName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactsFileTypeName(), ContactsFileType_.contactsFileTypeName));
            }
            if (criteria.getContactsFileMediumType() != null) {
                specification = specification.and(buildSpecification(criteria.getContactsFileMediumType(), ContactsFileType_.contactsFileMediumType));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ContactsFileType_.description));
            }
            if (criteria.getContactsfileType() != null) {
                specification = specification.and(buildSpecification(criteria.getContactsfileType(), ContactsFileType_.contactsfileType));
            }
        }
        return specification;
    }
}

package io.github.contacts.repository;

import io.github.contacts.domain.ContactsFileType;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ContactsFileType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactsFileTypeRepository extends JpaRepository<ContactsFileType, Long>, JpaSpecificationExecutor<ContactsFileType> {
}

package io.github.contacts.repository;

import io.github.contacts.domain.ContactsFileUpload;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ContactsFileUpload entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactsFileUploadRepository extends JpaRepository<ContactsFileUpload, Long>, JpaSpecificationExecutor<ContactsFileUpload> {
}

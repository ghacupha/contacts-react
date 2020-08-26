package io.github.contacts.repository;

import io.github.contacts.domain.ContactsMessageToken;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ContactsMessageToken entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactsMessageTokenRepository extends JpaRepository<ContactsMessageToken, Long>, JpaSpecificationExecutor<ContactsMessageToken> {
}

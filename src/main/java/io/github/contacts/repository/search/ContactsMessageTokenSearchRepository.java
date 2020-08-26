package io.github.contacts.repository.search;

import io.github.contacts.domain.ContactsMessageToken;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link ContactsMessageToken} entity.
 */
public interface ContactsMessageTokenSearchRepository extends ElasticsearchRepository<ContactsMessageToken, Long> {
}

package io.github.contacts.repository.search;

import io.github.contacts.domain.Contact;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Contact} entity.
 */
public interface ContactSearchRepository extends ElasticsearchRepository<Contact, Long> {
}

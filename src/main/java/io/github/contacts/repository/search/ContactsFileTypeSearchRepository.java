package io.github.contacts.repository.search;

import io.github.contacts.domain.ContactsFileType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link ContactsFileType} entity.
 */
public interface ContactsFileTypeSearchRepository extends ElasticsearchRepository<ContactsFileType, Long> {
}

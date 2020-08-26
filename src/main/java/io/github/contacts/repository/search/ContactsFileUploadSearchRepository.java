package io.github.contacts.repository.search;

import io.github.contacts.domain.ContactsFileUpload;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link ContactsFileUpload} entity.
 */
public interface ContactsFileUploadSearchRepository extends ElasticsearchRepository<ContactsFileUpload, Long> {
}

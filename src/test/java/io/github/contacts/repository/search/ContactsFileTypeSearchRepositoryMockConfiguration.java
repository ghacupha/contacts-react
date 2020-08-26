package io.github.contacts.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ContactsFileTypeSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ContactsFileTypeSearchRepositoryMockConfiguration {

    @MockBean
    private ContactsFileTypeSearchRepository mockContactsFileTypeSearchRepository;

}

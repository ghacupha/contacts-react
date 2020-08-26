package io.github.contacts.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ContactsFileUploadSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ContactsFileUploadSearchRepositoryMockConfiguration {

    @MockBean
    private ContactsFileUploadSearchRepository mockContactsFileUploadSearchRepository;

}

package io.github.contacts.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ContactsFileUploadMapperTest {

    private ContactsFileUploadMapper contactsFileUploadMapper;

    @BeforeEach
    public void setUp() {
        contactsFileUploadMapper = new ContactsFileUploadMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(contactsFileUploadMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(contactsFileUploadMapper.fromId(null)).isNull();
    }
}

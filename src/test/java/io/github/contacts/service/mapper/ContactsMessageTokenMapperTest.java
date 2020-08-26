package io.github.contacts.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ContactsMessageTokenMapperTest {

    private ContactsMessageTokenMapper contactsMessageTokenMapper;

    @BeforeEach
    public void setUp() {
        contactsMessageTokenMapper = new ContactsMessageTokenMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(contactsMessageTokenMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(contactsMessageTokenMapper.fromId(null)).isNull();
    }
}

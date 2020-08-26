package io.github.contacts.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.contacts.web.rest.TestUtil;

public class ContactsMessageTokenDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactsMessageTokenDTO.class);
        ContactsMessageTokenDTO contactsMessageTokenDTO1 = new ContactsMessageTokenDTO();
        contactsMessageTokenDTO1.setId(1L);
        ContactsMessageTokenDTO contactsMessageTokenDTO2 = new ContactsMessageTokenDTO();
        assertThat(contactsMessageTokenDTO1).isNotEqualTo(contactsMessageTokenDTO2);
        contactsMessageTokenDTO2.setId(contactsMessageTokenDTO1.getId());
        assertThat(contactsMessageTokenDTO1).isEqualTo(contactsMessageTokenDTO2);
        contactsMessageTokenDTO2.setId(2L);
        assertThat(contactsMessageTokenDTO1).isNotEqualTo(contactsMessageTokenDTO2);
        contactsMessageTokenDTO1.setId(null);
        assertThat(contactsMessageTokenDTO1).isNotEqualTo(contactsMessageTokenDTO2);
    }
}

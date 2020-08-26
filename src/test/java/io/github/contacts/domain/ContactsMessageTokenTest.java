package io.github.contacts.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.contacts.web.rest.TestUtil;

public class ContactsMessageTokenTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactsMessageToken.class);
        ContactsMessageToken contactsMessageToken1 = new ContactsMessageToken();
        contactsMessageToken1.setId(1L);
        ContactsMessageToken contactsMessageToken2 = new ContactsMessageToken();
        contactsMessageToken2.setId(contactsMessageToken1.getId());
        assertThat(contactsMessageToken1).isEqualTo(contactsMessageToken2);
        contactsMessageToken2.setId(2L);
        assertThat(contactsMessageToken1).isNotEqualTo(contactsMessageToken2);
        contactsMessageToken1.setId(null);
        assertThat(contactsMessageToken1).isNotEqualTo(contactsMessageToken2);
    }
}

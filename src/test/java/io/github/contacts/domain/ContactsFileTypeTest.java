package io.github.contacts.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.contacts.web.rest.TestUtil;

public class ContactsFileTypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactsFileType.class);
        ContactsFileType contactsFileType1 = new ContactsFileType();
        contactsFileType1.setId(1L);
        ContactsFileType contactsFileType2 = new ContactsFileType();
        contactsFileType2.setId(contactsFileType1.getId());
        assertThat(contactsFileType1).isEqualTo(contactsFileType2);
        contactsFileType2.setId(2L);
        assertThat(contactsFileType1).isNotEqualTo(contactsFileType2);
        contactsFileType1.setId(null);
        assertThat(contactsFileType1).isNotEqualTo(contactsFileType2);
    }
}

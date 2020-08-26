package io.github.contacts.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.contacts.web.rest.TestUtil;

public class ContactsFileUploadTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactsFileUpload.class);
        ContactsFileUpload contactsFileUpload1 = new ContactsFileUpload();
        contactsFileUpload1.setId(1L);
        ContactsFileUpload contactsFileUpload2 = new ContactsFileUpload();
        contactsFileUpload2.setId(contactsFileUpload1.getId());
        assertThat(contactsFileUpload1).isEqualTo(contactsFileUpload2);
        contactsFileUpload2.setId(2L);
        assertThat(contactsFileUpload1).isNotEqualTo(contactsFileUpload2);
        contactsFileUpload1.setId(null);
        assertThat(contactsFileUpload1).isNotEqualTo(contactsFileUpload2);
    }
}

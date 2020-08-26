package io.github.contacts.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.contacts.web.rest.TestUtil;

public class ContactsFileUploadDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactsFileUploadDTO.class);
        ContactsFileUploadDTO contactsFileUploadDTO1 = new ContactsFileUploadDTO();
        contactsFileUploadDTO1.setId(1L);
        ContactsFileUploadDTO contactsFileUploadDTO2 = new ContactsFileUploadDTO();
        assertThat(contactsFileUploadDTO1).isNotEqualTo(contactsFileUploadDTO2);
        contactsFileUploadDTO2.setId(contactsFileUploadDTO1.getId());
        assertThat(contactsFileUploadDTO1).isEqualTo(contactsFileUploadDTO2);
        contactsFileUploadDTO2.setId(2L);
        assertThat(contactsFileUploadDTO1).isNotEqualTo(contactsFileUploadDTO2);
        contactsFileUploadDTO1.setId(null);
        assertThat(contactsFileUploadDTO1).isNotEqualTo(contactsFileUploadDTO2);
    }
}

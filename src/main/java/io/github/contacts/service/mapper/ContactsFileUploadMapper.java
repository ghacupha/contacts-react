package io.github.contacts.service.mapper;


import io.github.contacts.domain.*;
import io.github.contacts.service.dto.ContactsFileUploadDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContactsFileUpload} and its DTO {@link ContactsFileUploadDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContactsFileUploadMapper extends EntityMapper<ContactsFileUploadDTO, ContactsFileUpload> {



    default ContactsFileUpload fromId(Long id) {
        if (id == null) {
            return null;
        }
        ContactsFileUpload contactsFileUpload = new ContactsFileUpload();
        contactsFileUpload.setId(id);
        return contactsFileUpload;
    }
}

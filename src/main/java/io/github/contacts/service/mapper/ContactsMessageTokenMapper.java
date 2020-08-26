package io.github.contacts.service.mapper;


import io.github.contacts.domain.*;
import io.github.contacts.service.dto.ContactsMessageTokenDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContactsMessageToken} and its DTO {@link ContactsMessageTokenDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContactsMessageTokenMapper extends EntityMapper<ContactsMessageTokenDTO, ContactsMessageToken> {



    default ContactsMessageToken fromId(Long id) {
        if (id == null) {
            return null;
        }
        ContactsMessageToken contactsMessageToken = new ContactsMessageToken();
        contactsMessageToken.setId(id);
        return contactsMessageToken;
    }
}

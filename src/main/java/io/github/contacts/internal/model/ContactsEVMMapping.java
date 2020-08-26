package io.github.contacts.internal.model;

import io.github.contacts.internal.Mapping;
import io.github.contacts.service.dto.ContactDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ContactsEVMMapping extends Mapping<ContactEVM, ContactDTO> {
}

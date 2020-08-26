package io.github.contacts.internal.service;

import io.github.contacts.repository.ContactRepository;
import io.github.contacts.repository.search.ContactSearchRepository;
import io.github.contacts.service.dto.ContactDTO;
import io.github.contacts.service.mapper.ContactMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("contactsBatchService")
public class ContactsBatchService implements BatchService<ContactDTO> {

    private final ContactMapper contactMapper;
    private final ContactRepository contactRepository;
    private final ContactSearchRepository contactSearchRepository;

    public ContactsBatchService(ContactMapper contactMapper, ContactRepository contactRepository, ContactSearchRepository contactSearchRepository) {
        this.contactMapper = contactMapper;
        this.contactRepository = contactRepository;
        this.contactSearchRepository = contactSearchRepository;
    }

    @Override
    public List<ContactDTO> save(List<ContactDTO> entities) {
        return contactMapper.toDto(contactRepository.saveAll(contactMapper.toEntity(entities)));
    }

    @Override
    public void index(List<ContactDTO> entities) {

        contactSearchRepository.saveAll(contactMapper.toEntity(entities));
    }
}

package io.github.contacts.web.rest;

import io.github.contacts.ContactsApp;
import io.github.contacts.domain.Contact;
import io.github.contacts.repository.ContactRepository;
import io.github.contacts.repository.search.ContactSearchRepository;
import io.github.contacts.service.ContactService;
import io.github.contacts.service.dto.ContactDTO;
import io.github.contacts.service.mapper.ContactMapper;
import io.github.contacts.service.dto.ContactCriteria;
import io.github.contacts.service.ContactQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ContactResource} REST controller.
 */
@SpringBootTest(classes = ContactsApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ContactResourceIT {

    private static final String DEFAULT_CONTACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE_EXTENSION = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE_EXTENSION = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactMapper contactMapper;

    @Autowired
    private ContactService contactService;

    /**
     * This repository is mocked in the io.github.contacts.repository.search test package.
     *
     * @see io.github.contacts.repository.search.ContactSearchRepositoryMockConfiguration
     */
    @Autowired
    private ContactSearchRepository mockContactSearchRepository;

    @Autowired
    private ContactQueryService contactQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactMockMvc;

    private Contact contact;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contact createEntity(EntityManager em) {
        Contact contact = new Contact()
            .contactName(DEFAULT_CONTACT_NAME)
            .department(DEFAULT_DEPARTMENT)
            .telephoneExtension(DEFAULT_TELEPHONE_EXTENSION)
            .phoneNumber(DEFAULT_PHONE_NUMBER);
        return contact;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contact createUpdatedEntity(EntityManager em) {
        Contact contact = new Contact()
            .contactName(UPDATED_CONTACT_NAME)
            .department(UPDATED_DEPARTMENT)
            .telephoneExtension(UPDATED_TELEPHONE_EXTENSION)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        return contact;
    }

    @BeforeEach
    public void initTest() {
        contact = createEntity(em);
    }

    @Test
    @Transactional
    public void createContact() throws Exception {
        int databaseSizeBeforeCreate = contactRepository.findAll().size();
        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);
        restContactMockMvc.perform(post("/api/contacts").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isCreated());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeCreate + 1);
        Contact testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testContact.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testContact.getTelephoneExtension()).isEqualTo(DEFAULT_TELEPHONE_EXTENSION);
        assertThat(testContact.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);

        // Validate the Contact in Elasticsearch
        verify(mockContactSearchRepository, times(1)).save(testContact);
    }

    @Test
    @Transactional
    public void createContactWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contactRepository.findAll().size();

        // Create the Contact with an existing ID
        contact.setId(1L);
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactMockMvc.perform(post("/api/contacts").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeCreate);

        // Validate the Contact in Elasticsearch
        verify(mockContactSearchRepository, times(0)).save(contact);
    }


    @Test
    @Transactional
    public void getAllContacts() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList
        restContactMockMvc.perform(get("/api/contacts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contact.getId().intValue())))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].telephoneExtension").value(hasItem(DEFAULT_TELEPHONE_EXTENSION)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }
    
    @Test
    @Transactional
    public void getContact() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get the contact
        restContactMockMvc.perform(get("/api/contacts/{id}", contact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contact.getId().intValue()))
            .andExpect(jsonPath("$.contactName").value(DEFAULT_CONTACT_NAME))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT))
            .andExpect(jsonPath("$.telephoneExtension").value(DEFAULT_TELEPHONE_EXTENSION))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }


    @Test
    @Transactional
    public void getContactsByIdFiltering() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        Long id = contact.getId();

        defaultContactShouldBeFound("id.equals=" + id);
        defaultContactShouldNotBeFound("id.notEquals=" + id);

        defaultContactShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContactShouldNotBeFound("id.greaterThan=" + id);

        defaultContactShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContactShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllContactsByContactNameIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where contactName equals to DEFAULT_CONTACT_NAME
        defaultContactShouldBeFound("contactName.equals=" + DEFAULT_CONTACT_NAME);

        // Get all the contactList where contactName equals to UPDATED_CONTACT_NAME
        defaultContactShouldNotBeFound("contactName.equals=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    public void getAllContactsByContactNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where contactName not equals to DEFAULT_CONTACT_NAME
        defaultContactShouldNotBeFound("contactName.notEquals=" + DEFAULT_CONTACT_NAME);

        // Get all the contactList where contactName not equals to UPDATED_CONTACT_NAME
        defaultContactShouldBeFound("contactName.notEquals=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    public void getAllContactsByContactNameIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where contactName in DEFAULT_CONTACT_NAME or UPDATED_CONTACT_NAME
        defaultContactShouldBeFound("contactName.in=" + DEFAULT_CONTACT_NAME + "," + UPDATED_CONTACT_NAME);

        // Get all the contactList where contactName equals to UPDATED_CONTACT_NAME
        defaultContactShouldNotBeFound("contactName.in=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    public void getAllContactsByContactNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where contactName is not null
        defaultContactShouldBeFound("contactName.specified=true");

        // Get all the contactList where contactName is null
        defaultContactShouldNotBeFound("contactName.specified=false");
    }
                @Test
    @Transactional
    public void getAllContactsByContactNameContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where contactName contains DEFAULT_CONTACT_NAME
        defaultContactShouldBeFound("contactName.contains=" + DEFAULT_CONTACT_NAME);

        // Get all the contactList where contactName contains UPDATED_CONTACT_NAME
        defaultContactShouldNotBeFound("contactName.contains=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    public void getAllContactsByContactNameNotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where contactName does not contain DEFAULT_CONTACT_NAME
        defaultContactShouldNotBeFound("contactName.doesNotContain=" + DEFAULT_CONTACT_NAME);

        // Get all the contactList where contactName does not contain UPDATED_CONTACT_NAME
        defaultContactShouldBeFound("contactName.doesNotContain=" + UPDATED_CONTACT_NAME);
    }


    @Test
    @Transactional
    public void getAllContactsByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where department equals to DEFAULT_DEPARTMENT
        defaultContactShouldBeFound("department.equals=" + DEFAULT_DEPARTMENT);

        // Get all the contactList where department equals to UPDATED_DEPARTMENT
        defaultContactShouldNotBeFound("department.equals=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    public void getAllContactsByDepartmentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where department not equals to DEFAULT_DEPARTMENT
        defaultContactShouldNotBeFound("department.notEquals=" + DEFAULT_DEPARTMENT);

        // Get all the contactList where department not equals to UPDATED_DEPARTMENT
        defaultContactShouldBeFound("department.notEquals=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    public void getAllContactsByDepartmentIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where department in DEFAULT_DEPARTMENT or UPDATED_DEPARTMENT
        defaultContactShouldBeFound("department.in=" + DEFAULT_DEPARTMENT + "," + UPDATED_DEPARTMENT);

        // Get all the contactList where department equals to UPDATED_DEPARTMENT
        defaultContactShouldNotBeFound("department.in=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    public void getAllContactsByDepartmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where department is not null
        defaultContactShouldBeFound("department.specified=true");

        // Get all the contactList where department is null
        defaultContactShouldNotBeFound("department.specified=false");
    }
                @Test
    @Transactional
    public void getAllContactsByDepartmentContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where department contains DEFAULT_DEPARTMENT
        defaultContactShouldBeFound("department.contains=" + DEFAULT_DEPARTMENT);

        // Get all the contactList where department contains UPDATED_DEPARTMENT
        defaultContactShouldNotBeFound("department.contains=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    public void getAllContactsByDepartmentNotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where department does not contain DEFAULT_DEPARTMENT
        defaultContactShouldNotBeFound("department.doesNotContain=" + DEFAULT_DEPARTMENT);

        // Get all the contactList where department does not contain UPDATED_DEPARTMENT
        defaultContactShouldBeFound("department.doesNotContain=" + UPDATED_DEPARTMENT);
    }


    @Test
    @Transactional
    public void getAllContactsByTelephoneExtensionIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where telephoneExtension equals to DEFAULT_TELEPHONE_EXTENSION
        defaultContactShouldBeFound("telephoneExtension.equals=" + DEFAULT_TELEPHONE_EXTENSION);

        // Get all the contactList where telephoneExtension equals to UPDATED_TELEPHONE_EXTENSION
        defaultContactShouldNotBeFound("telephoneExtension.equals=" + UPDATED_TELEPHONE_EXTENSION);
    }

    @Test
    @Transactional
    public void getAllContactsByTelephoneExtensionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where telephoneExtension not equals to DEFAULT_TELEPHONE_EXTENSION
        defaultContactShouldNotBeFound("telephoneExtension.notEquals=" + DEFAULT_TELEPHONE_EXTENSION);

        // Get all the contactList where telephoneExtension not equals to UPDATED_TELEPHONE_EXTENSION
        defaultContactShouldBeFound("telephoneExtension.notEquals=" + UPDATED_TELEPHONE_EXTENSION);
    }

    @Test
    @Transactional
    public void getAllContactsByTelephoneExtensionIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where telephoneExtension in DEFAULT_TELEPHONE_EXTENSION or UPDATED_TELEPHONE_EXTENSION
        defaultContactShouldBeFound("telephoneExtension.in=" + DEFAULT_TELEPHONE_EXTENSION + "," + UPDATED_TELEPHONE_EXTENSION);

        // Get all the contactList where telephoneExtension equals to UPDATED_TELEPHONE_EXTENSION
        defaultContactShouldNotBeFound("telephoneExtension.in=" + UPDATED_TELEPHONE_EXTENSION);
    }

    @Test
    @Transactional
    public void getAllContactsByTelephoneExtensionIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where telephoneExtension is not null
        defaultContactShouldBeFound("telephoneExtension.specified=true");

        // Get all the contactList where telephoneExtension is null
        defaultContactShouldNotBeFound("telephoneExtension.specified=false");
    }
                @Test
    @Transactional
    public void getAllContactsByTelephoneExtensionContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where telephoneExtension contains DEFAULT_TELEPHONE_EXTENSION
        defaultContactShouldBeFound("telephoneExtension.contains=" + DEFAULT_TELEPHONE_EXTENSION);

        // Get all the contactList where telephoneExtension contains UPDATED_TELEPHONE_EXTENSION
        defaultContactShouldNotBeFound("telephoneExtension.contains=" + UPDATED_TELEPHONE_EXTENSION);
    }

    @Test
    @Transactional
    public void getAllContactsByTelephoneExtensionNotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where telephoneExtension does not contain DEFAULT_TELEPHONE_EXTENSION
        defaultContactShouldNotBeFound("telephoneExtension.doesNotContain=" + DEFAULT_TELEPHONE_EXTENSION);

        // Get all the contactList where telephoneExtension does not contain UPDATED_TELEPHONE_EXTENSION
        defaultContactShouldBeFound("telephoneExtension.doesNotContain=" + UPDATED_TELEPHONE_EXTENSION);
    }


    @Test
    @Transactional
    public void getAllContactsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultContactShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the contactList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultContactShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllContactsByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultContactShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the contactList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultContactShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllContactsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultContactShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the contactList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultContactShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllContactsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where phoneNumber is not null
        defaultContactShouldBeFound("phoneNumber.specified=true");

        // Get all the contactList where phoneNumber is null
        defaultContactShouldNotBeFound("phoneNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllContactsByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultContactShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the contactList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultContactShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllContactsByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultContactShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the contactList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultContactShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContactShouldBeFound(String filter) throws Exception {
        restContactMockMvc.perform(get("/api/contacts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contact.getId().intValue())))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].telephoneExtension").value(hasItem(DEFAULT_TELEPHONE_EXTENSION)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restContactMockMvc.perform(get("/api/contacts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContactShouldNotBeFound(String filter) throws Exception {
        restContactMockMvc.perform(get("/api/contacts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContactMockMvc.perform(get("/api/contacts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingContact() throws Exception {
        // Get the contact
        restContactMockMvc.perform(get("/api/contacts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContact() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        int databaseSizeBeforeUpdate = contactRepository.findAll().size();

        // Update the contact
        Contact updatedContact = contactRepository.findById(contact.getId()).get();
        // Disconnect from session so that the updates on updatedContact are not directly saved in db
        em.detach(updatedContact);
        updatedContact
            .contactName(UPDATED_CONTACT_NAME)
            .department(UPDATED_DEPARTMENT)
            .telephoneExtension(UPDATED_TELEPHONE_EXTENSION)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        ContactDTO contactDTO = contactMapper.toDto(updatedContact);

        restContactMockMvc.perform(put("/api/contacts").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isOk());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
        Contact testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testContact.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testContact.getTelephoneExtension()).isEqualTo(UPDATED_TELEPHONE_EXTENSION);
        assertThat(testContact.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);

        // Validate the Contact in Elasticsearch
        verify(mockContactSearchRepository, times(1)).save(testContact);
    }

    @Test
    @Transactional
    public void updateNonExistingContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().size();

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactMockMvc.perform(put("/api/contacts").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Contact in Elasticsearch
        verify(mockContactSearchRepository, times(0)).save(contact);
    }

    @Test
    @Transactional
    public void deleteContact() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        int databaseSizeBeforeDelete = contactRepository.findAll().size();

        // Delete the contact
        restContactMockMvc.perform(delete("/api/contacts/{id}", contact.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Contact in Elasticsearch
        verify(mockContactSearchRepository, times(1)).deleteById(contact.getId());
    }

    @Test
    @Transactional
    public void searchContact() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        contactRepository.saveAndFlush(contact);
        when(mockContactSearchRepository.search(queryStringQuery("id:" + contact.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(contact), PageRequest.of(0, 1), 1));

        // Search the contact
        restContactMockMvc.perform(get("/api/_search/contacts?query=id:" + contact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contact.getId().intValue())))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].telephoneExtension").value(hasItem(DEFAULT_TELEPHONE_EXTENSION)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }
}

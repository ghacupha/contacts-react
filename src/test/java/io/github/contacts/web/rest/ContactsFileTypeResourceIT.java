package io.github.contacts.web.rest;

import io.github.contacts.ContactsApp;
import io.github.contacts.domain.ContactsFileType;
import io.github.contacts.repository.ContactsFileTypeRepository;
import io.github.contacts.repository.search.ContactsFileTypeSearchRepository;
import io.github.contacts.service.ContactsFileTypeService;
import io.github.contacts.service.dto.ContactsFileTypeCriteria;
import io.github.contacts.service.ContactsFileTypeQueryService;

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
import org.springframework.util.Base64Utils;
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

import io.github.contacts.domain.enumeration.ContactsFileMediumTypes;
import io.github.contacts.domain.enumeration.ContactsFileModelType;
/**
 * Integration tests for the {@link ContactsFileTypeResource} REST controller.
 */
@SpringBootTest(classes = ContactsApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ContactsFileTypeResourceIT {

    private static final String DEFAULT_CONTACTS_FILE_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACTS_FILE_TYPE_NAME = "BBBBBBBBBB";

    private static final ContactsFileMediumTypes DEFAULT_CONTACTS_FILE_MEDIUM_TYPE = ContactsFileMediumTypes.EXCEL;
    private static final ContactsFileMediumTypes UPDATED_CONTACTS_FILE_MEDIUM_TYPE = ContactsFileMediumTypes.EXCEL_XLS;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE_TEMPLATE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE_TEMPLATE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_TEMPLATE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_TEMPLATE_CONTENT_TYPE = "image/png";

    private static final ContactsFileModelType DEFAULT_CONTACTSFILE_TYPE = ContactsFileModelType.CURRENCY_LIST;
    private static final ContactsFileModelType UPDATED_CONTACTSFILE_TYPE = ContactsFileModelType.CONTACTS;

    @Autowired
    private ContactsFileTypeRepository contactsFileTypeRepository;

    @Autowired
    private ContactsFileTypeService contactsFileTypeService;

    /**
     * This repository is mocked in the io.github.contacts.repository.search test package.
     *
     * @see io.github.contacts.repository.search.ContactsFileTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private ContactsFileTypeSearchRepository mockContactsFileTypeSearchRepository;

    @Autowired
    private ContactsFileTypeQueryService contactsFileTypeQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactsFileTypeMockMvc;

    private ContactsFileType contactsFileType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactsFileType createEntity(EntityManager em) {
        ContactsFileType contactsFileType = new ContactsFileType()
            .contactsFileTypeName(DEFAULT_CONTACTS_FILE_TYPE_NAME)
            .contactsFileMediumType(DEFAULT_CONTACTS_FILE_MEDIUM_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .fileTemplate(DEFAULT_FILE_TEMPLATE)
            .fileTemplateContentType(DEFAULT_FILE_TEMPLATE_CONTENT_TYPE)
            .contactsfileType(DEFAULT_CONTACTSFILE_TYPE);
        return contactsFileType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactsFileType createUpdatedEntity(EntityManager em) {
        ContactsFileType contactsFileType = new ContactsFileType()
            .contactsFileTypeName(UPDATED_CONTACTS_FILE_TYPE_NAME)
            .contactsFileMediumType(UPDATED_CONTACTS_FILE_MEDIUM_TYPE)
            .description(UPDATED_DESCRIPTION)
            .fileTemplate(UPDATED_FILE_TEMPLATE)
            .fileTemplateContentType(UPDATED_FILE_TEMPLATE_CONTENT_TYPE)
            .contactsfileType(UPDATED_CONTACTSFILE_TYPE);
        return contactsFileType;
    }

    @BeforeEach
    public void initTest() {
        contactsFileType = createEntity(em);
    }

    @Test
    @Transactional
    public void createContactsFileType() throws Exception {
        int databaseSizeBeforeCreate = contactsFileTypeRepository.findAll().size();
        // Create the ContactsFileType
        restContactsFileTypeMockMvc.perform(post("/api/contacts-file-types").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactsFileType)))
            .andExpect(status().isCreated());

        // Validate the ContactsFileType in the database
        List<ContactsFileType> contactsFileTypeList = contactsFileTypeRepository.findAll();
        assertThat(contactsFileTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ContactsFileType testContactsFileType = contactsFileTypeList.get(contactsFileTypeList.size() - 1);
        assertThat(testContactsFileType.getContactsFileTypeName()).isEqualTo(DEFAULT_CONTACTS_FILE_TYPE_NAME);
        assertThat(testContactsFileType.getContactsFileMediumType()).isEqualTo(DEFAULT_CONTACTS_FILE_MEDIUM_TYPE);
        assertThat(testContactsFileType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testContactsFileType.getFileTemplate()).isEqualTo(DEFAULT_FILE_TEMPLATE);
        assertThat(testContactsFileType.getFileTemplateContentType()).isEqualTo(DEFAULT_FILE_TEMPLATE_CONTENT_TYPE);
        assertThat(testContactsFileType.getContactsfileType()).isEqualTo(DEFAULT_CONTACTSFILE_TYPE);

        // Validate the ContactsFileType in Elasticsearch
        verify(mockContactsFileTypeSearchRepository, times(1)).save(testContactsFileType);
    }

    @Test
    @Transactional
    public void createContactsFileTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contactsFileTypeRepository.findAll().size();

        // Create the ContactsFileType with an existing ID
        contactsFileType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactsFileTypeMockMvc.perform(post("/api/contacts-file-types").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactsFileType)))
            .andExpect(status().isBadRequest());

        // Validate the ContactsFileType in the database
        List<ContactsFileType> contactsFileTypeList = contactsFileTypeRepository.findAll();
        assertThat(contactsFileTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the ContactsFileType in Elasticsearch
        verify(mockContactsFileTypeSearchRepository, times(0)).save(contactsFileType);
    }


    @Test
    @Transactional
    public void checkContactsFileTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactsFileTypeRepository.findAll().size();
        // set the field null
        contactsFileType.setContactsFileTypeName(null);

        // Create the ContactsFileType, which fails.


        restContactsFileTypeMockMvc.perform(post("/api/contacts-file-types").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactsFileType)))
            .andExpect(status().isBadRequest());

        List<ContactsFileType> contactsFileTypeList = contactsFileTypeRepository.findAll();
        assertThat(contactsFileTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContactsFileMediumTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactsFileTypeRepository.findAll().size();
        // set the field null
        contactsFileType.setContactsFileMediumType(null);

        // Create the ContactsFileType, which fails.


        restContactsFileTypeMockMvc.perform(post("/api/contacts-file-types").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactsFileType)))
            .andExpect(status().isBadRequest());

        List<ContactsFileType> contactsFileTypeList = contactsFileTypeRepository.findAll();
        assertThat(contactsFileTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContactsFileTypes() throws Exception {
        // Initialize the database
        contactsFileTypeRepository.saveAndFlush(contactsFileType);

        // Get all the contactsFileTypeList
        restContactsFileTypeMockMvc.perform(get("/api/contacts-file-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactsFileType.getId().intValue())))
            .andExpect(jsonPath("$.[*].contactsFileTypeName").value(hasItem(DEFAULT_CONTACTS_FILE_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].contactsFileMediumType").value(hasItem(DEFAULT_CONTACTS_FILE_MEDIUM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileTemplateContentType").value(hasItem(DEFAULT_FILE_TEMPLATE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileTemplate").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_TEMPLATE))))
            .andExpect(jsonPath("$.[*].contactsfileType").value(hasItem(DEFAULT_CONTACTSFILE_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getContactsFileType() throws Exception {
        // Initialize the database
        contactsFileTypeRepository.saveAndFlush(contactsFileType);

        // Get the contactsFileType
        restContactsFileTypeMockMvc.perform(get("/api/contacts-file-types/{id}", contactsFileType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactsFileType.getId().intValue()))
            .andExpect(jsonPath("$.contactsFileTypeName").value(DEFAULT_CONTACTS_FILE_TYPE_NAME))
            .andExpect(jsonPath("$.contactsFileMediumType").value(DEFAULT_CONTACTS_FILE_MEDIUM_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.fileTemplateContentType").value(DEFAULT_FILE_TEMPLATE_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileTemplate").value(Base64Utils.encodeToString(DEFAULT_FILE_TEMPLATE)))
            .andExpect(jsonPath("$.contactsfileType").value(DEFAULT_CONTACTSFILE_TYPE.toString()));
    }


    @Test
    @Transactional
    public void getContactsFileTypesByIdFiltering() throws Exception {
        // Initialize the database
        contactsFileTypeRepository.saveAndFlush(contactsFileType);

        Long id = contactsFileType.getId();

        defaultContactsFileTypeShouldBeFound("id.equals=" + id);
        defaultContactsFileTypeShouldNotBeFound("id.notEquals=" + id);

        defaultContactsFileTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContactsFileTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultContactsFileTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContactsFileTypeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllContactsFileTypesByContactsFileTypeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileTypeRepository.saveAndFlush(contactsFileType);

        // Get all the contactsFileTypeList where contactsFileTypeName equals to DEFAULT_CONTACTS_FILE_TYPE_NAME
        defaultContactsFileTypeShouldBeFound("contactsFileTypeName.equals=" + DEFAULT_CONTACTS_FILE_TYPE_NAME);

        // Get all the contactsFileTypeList where contactsFileTypeName equals to UPDATED_CONTACTS_FILE_TYPE_NAME
        defaultContactsFileTypeShouldNotBeFound("contactsFileTypeName.equals=" + UPDATED_CONTACTS_FILE_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllContactsFileTypesByContactsFileTypeNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileTypeRepository.saveAndFlush(contactsFileType);

        // Get all the contactsFileTypeList where contactsFileTypeName not equals to DEFAULT_CONTACTS_FILE_TYPE_NAME
        defaultContactsFileTypeShouldNotBeFound("contactsFileTypeName.notEquals=" + DEFAULT_CONTACTS_FILE_TYPE_NAME);

        // Get all the contactsFileTypeList where contactsFileTypeName not equals to UPDATED_CONTACTS_FILE_TYPE_NAME
        defaultContactsFileTypeShouldBeFound("contactsFileTypeName.notEquals=" + UPDATED_CONTACTS_FILE_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllContactsFileTypesByContactsFileTypeNameIsInShouldWork() throws Exception {
        // Initialize the database
        contactsFileTypeRepository.saveAndFlush(contactsFileType);

        // Get all the contactsFileTypeList where contactsFileTypeName in DEFAULT_CONTACTS_FILE_TYPE_NAME or UPDATED_CONTACTS_FILE_TYPE_NAME
        defaultContactsFileTypeShouldBeFound("contactsFileTypeName.in=" + DEFAULT_CONTACTS_FILE_TYPE_NAME + "," + UPDATED_CONTACTS_FILE_TYPE_NAME);

        // Get all the contactsFileTypeList where contactsFileTypeName equals to UPDATED_CONTACTS_FILE_TYPE_NAME
        defaultContactsFileTypeShouldNotBeFound("contactsFileTypeName.in=" + UPDATED_CONTACTS_FILE_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllContactsFileTypesByContactsFileTypeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactsFileTypeRepository.saveAndFlush(contactsFileType);

        // Get all the contactsFileTypeList where contactsFileTypeName is not null
        defaultContactsFileTypeShouldBeFound("contactsFileTypeName.specified=true");

        // Get all the contactsFileTypeList where contactsFileTypeName is null
        defaultContactsFileTypeShouldNotBeFound("contactsFileTypeName.specified=false");
    }
                @Test
    @Transactional
    public void getAllContactsFileTypesByContactsFileTypeNameContainsSomething() throws Exception {
        // Initialize the database
        contactsFileTypeRepository.saveAndFlush(contactsFileType);

        // Get all the contactsFileTypeList where contactsFileTypeName contains DEFAULT_CONTACTS_FILE_TYPE_NAME
        defaultContactsFileTypeShouldBeFound("contactsFileTypeName.contains=" + DEFAULT_CONTACTS_FILE_TYPE_NAME);

        // Get all the contactsFileTypeList where contactsFileTypeName contains UPDATED_CONTACTS_FILE_TYPE_NAME
        defaultContactsFileTypeShouldNotBeFound("contactsFileTypeName.contains=" + UPDATED_CONTACTS_FILE_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllContactsFileTypesByContactsFileTypeNameNotContainsSomething() throws Exception {
        // Initialize the database
        contactsFileTypeRepository.saveAndFlush(contactsFileType);

        // Get all the contactsFileTypeList where contactsFileTypeName does not contain DEFAULT_CONTACTS_FILE_TYPE_NAME
        defaultContactsFileTypeShouldNotBeFound("contactsFileTypeName.doesNotContain=" + DEFAULT_CONTACTS_FILE_TYPE_NAME);

        // Get all the contactsFileTypeList where contactsFileTypeName does not contain UPDATED_CONTACTS_FILE_TYPE_NAME
        defaultContactsFileTypeShouldBeFound("contactsFileTypeName.doesNotContain=" + UPDATED_CONTACTS_FILE_TYPE_NAME);
    }


    @Test
    @Transactional
    public void getAllContactsFileTypesByContactsFileMediumTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileTypeRepository.saveAndFlush(contactsFileType);

        // Get all the contactsFileTypeList where contactsFileMediumType equals to DEFAULT_CONTACTS_FILE_MEDIUM_TYPE
        defaultContactsFileTypeShouldBeFound("contactsFileMediumType.equals=" + DEFAULT_CONTACTS_FILE_MEDIUM_TYPE);

        // Get all the contactsFileTypeList where contactsFileMediumType equals to UPDATED_CONTACTS_FILE_MEDIUM_TYPE
        defaultContactsFileTypeShouldNotBeFound("contactsFileMediumType.equals=" + UPDATED_CONTACTS_FILE_MEDIUM_TYPE);
    }

    @Test
    @Transactional
    public void getAllContactsFileTypesByContactsFileMediumTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileTypeRepository.saveAndFlush(contactsFileType);

        // Get all the contactsFileTypeList where contactsFileMediumType not equals to DEFAULT_CONTACTS_FILE_MEDIUM_TYPE
        defaultContactsFileTypeShouldNotBeFound("contactsFileMediumType.notEquals=" + DEFAULT_CONTACTS_FILE_MEDIUM_TYPE);

        // Get all the contactsFileTypeList where contactsFileMediumType not equals to UPDATED_CONTACTS_FILE_MEDIUM_TYPE
        defaultContactsFileTypeShouldBeFound("contactsFileMediumType.notEquals=" + UPDATED_CONTACTS_FILE_MEDIUM_TYPE);
    }

    @Test
    @Transactional
    public void getAllContactsFileTypesByContactsFileMediumTypeIsInShouldWork() throws Exception {
        // Initialize the database
        contactsFileTypeRepository.saveAndFlush(contactsFileType);

        // Get all the contactsFileTypeList where contactsFileMediumType in DEFAULT_CONTACTS_FILE_MEDIUM_TYPE or UPDATED_CONTACTS_FILE_MEDIUM_TYPE
        defaultContactsFileTypeShouldBeFound("contactsFileMediumType.in=" + DEFAULT_CONTACTS_FILE_MEDIUM_TYPE + "," + UPDATED_CONTACTS_FILE_MEDIUM_TYPE);

        // Get all the contactsFileTypeList where contactsFileMediumType equals to UPDATED_CONTACTS_FILE_MEDIUM_TYPE
        defaultContactsFileTypeShouldNotBeFound("contactsFileMediumType.in=" + UPDATED_CONTACTS_FILE_MEDIUM_TYPE);
    }

    @Test
    @Transactional
    public void getAllContactsFileTypesByContactsFileMediumTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactsFileTypeRepository.saveAndFlush(contactsFileType);

        // Get all the contactsFileTypeList where contactsFileMediumType is not null
        defaultContactsFileTypeShouldBeFound("contactsFileMediumType.specified=true");

        // Get all the contactsFileTypeList where contactsFileMediumType is null
        defaultContactsFileTypeShouldNotBeFound("contactsFileMediumType.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsFileTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileTypeRepository.saveAndFlush(contactsFileType);

        // Get all the contactsFileTypeList where description equals to DEFAULT_DESCRIPTION
        defaultContactsFileTypeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the contactsFileTypeList where description equals to UPDATED_DESCRIPTION
        defaultContactsFileTypeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllContactsFileTypesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileTypeRepository.saveAndFlush(contactsFileType);

        // Get all the contactsFileTypeList where description not equals to DEFAULT_DESCRIPTION
        defaultContactsFileTypeShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the contactsFileTypeList where description not equals to UPDATED_DESCRIPTION
        defaultContactsFileTypeShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllContactsFileTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        contactsFileTypeRepository.saveAndFlush(contactsFileType);

        // Get all the contactsFileTypeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultContactsFileTypeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the contactsFileTypeList where description equals to UPDATED_DESCRIPTION
        defaultContactsFileTypeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllContactsFileTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactsFileTypeRepository.saveAndFlush(contactsFileType);

        // Get all the contactsFileTypeList where description is not null
        defaultContactsFileTypeShouldBeFound("description.specified=true");

        // Get all the contactsFileTypeList where description is null
        defaultContactsFileTypeShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllContactsFileTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        contactsFileTypeRepository.saveAndFlush(contactsFileType);

        // Get all the contactsFileTypeList where description contains DEFAULT_DESCRIPTION
        defaultContactsFileTypeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the contactsFileTypeList where description contains UPDATED_DESCRIPTION
        defaultContactsFileTypeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllContactsFileTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        contactsFileTypeRepository.saveAndFlush(contactsFileType);

        // Get all the contactsFileTypeList where description does not contain DEFAULT_DESCRIPTION
        defaultContactsFileTypeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the contactsFileTypeList where description does not contain UPDATED_DESCRIPTION
        defaultContactsFileTypeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllContactsFileTypesByContactsfileTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileTypeRepository.saveAndFlush(contactsFileType);

        // Get all the contactsFileTypeList where contactsfileType equals to DEFAULT_CONTACTSFILE_TYPE
        defaultContactsFileTypeShouldBeFound("contactsfileType.equals=" + DEFAULT_CONTACTSFILE_TYPE);

        // Get all the contactsFileTypeList where contactsfileType equals to UPDATED_CONTACTSFILE_TYPE
        defaultContactsFileTypeShouldNotBeFound("contactsfileType.equals=" + UPDATED_CONTACTSFILE_TYPE);
    }

    @Test
    @Transactional
    public void getAllContactsFileTypesByContactsfileTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileTypeRepository.saveAndFlush(contactsFileType);

        // Get all the contactsFileTypeList where contactsfileType not equals to DEFAULT_CONTACTSFILE_TYPE
        defaultContactsFileTypeShouldNotBeFound("contactsfileType.notEquals=" + DEFAULT_CONTACTSFILE_TYPE);

        // Get all the contactsFileTypeList where contactsfileType not equals to UPDATED_CONTACTSFILE_TYPE
        defaultContactsFileTypeShouldBeFound("contactsfileType.notEquals=" + UPDATED_CONTACTSFILE_TYPE);
    }

    @Test
    @Transactional
    public void getAllContactsFileTypesByContactsfileTypeIsInShouldWork() throws Exception {
        // Initialize the database
        contactsFileTypeRepository.saveAndFlush(contactsFileType);

        // Get all the contactsFileTypeList where contactsfileType in DEFAULT_CONTACTSFILE_TYPE or UPDATED_CONTACTSFILE_TYPE
        defaultContactsFileTypeShouldBeFound("contactsfileType.in=" + DEFAULT_CONTACTSFILE_TYPE + "," + UPDATED_CONTACTSFILE_TYPE);

        // Get all the contactsFileTypeList where contactsfileType equals to UPDATED_CONTACTSFILE_TYPE
        defaultContactsFileTypeShouldNotBeFound("contactsfileType.in=" + UPDATED_CONTACTSFILE_TYPE);
    }

    @Test
    @Transactional
    public void getAllContactsFileTypesByContactsfileTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactsFileTypeRepository.saveAndFlush(contactsFileType);

        // Get all the contactsFileTypeList where contactsfileType is not null
        defaultContactsFileTypeShouldBeFound("contactsfileType.specified=true");

        // Get all the contactsFileTypeList where contactsfileType is null
        defaultContactsFileTypeShouldNotBeFound("contactsfileType.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContactsFileTypeShouldBeFound(String filter) throws Exception {
        restContactsFileTypeMockMvc.perform(get("/api/contacts-file-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactsFileType.getId().intValue())))
            .andExpect(jsonPath("$.[*].contactsFileTypeName").value(hasItem(DEFAULT_CONTACTS_FILE_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].contactsFileMediumType").value(hasItem(DEFAULT_CONTACTS_FILE_MEDIUM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileTemplateContentType").value(hasItem(DEFAULT_FILE_TEMPLATE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileTemplate").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_TEMPLATE))))
            .andExpect(jsonPath("$.[*].contactsfileType").value(hasItem(DEFAULT_CONTACTSFILE_TYPE.toString())));

        // Check, that the count call also returns 1
        restContactsFileTypeMockMvc.perform(get("/api/contacts-file-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContactsFileTypeShouldNotBeFound(String filter) throws Exception {
        restContactsFileTypeMockMvc.perform(get("/api/contacts-file-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContactsFileTypeMockMvc.perform(get("/api/contacts-file-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingContactsFileType() throws Exception {
        // Get the contactsFileType
        restContactsFileTypeMockMvc.perform(get("/api/contacts-file-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContactsFileType() throws Exception {
        // Initialize the database
        contactsFileTypeService.save(contactsFileType);

        int databaseSizeBeforeUpdate = contactsFileTypeRepository.findAll().size();

        // Update the contactsFileType
        ContactsFileType updatedContactsFileType = contactsFileTypeRepository.findById(contactsFileType.getId()).get();
        // Disconnect from session so that the updates on updatedContactsFileType are not directly saved in db
        em.detach(updatedContactsFileType);
        updatedContactsFileType
            .contactsFileTypeName(UPDATED_CONTACTS_FILE_TYPE_NAME)
            .contactsFileMediumType(UPDATED_CONTACTS_FILE_MEDIUM_TYPE)
            .description(UPDATED_DESCRIPTION)
            .fileTemplate(UPDATED_FILE_TEMPLATE)
            .fileTemplateContentType(UPDATED_FILE_TEMPLATE_CONTENT_TYPE)
            .contactsfileType(UPDATED_CONTACTSFILE_TYPE);

        restContactsFileTypeMockMvc.perform(put("/api/contacts-file-types").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedContactsFileType)))
            .andExpect(status().isOk());

        // Validate the ContactsFileType in the database
        List<ContactsFileType> contactsFileTypeList = contactsFileTypeRepository.findAll();
        assertThat(contactsFileTypeList).hasSize(databaseSizeBeforeUpdate);
        ContactsFileType testContactsFileType = contactsFileTypeList.get(contactsFileTypeList.size() - 1);
        assertThat(testContactsFileType.getContactsFileTypeName()).isEqualTo(UPDATED_CONTACTS_FILE_TYPE_NAME);
        assertThat(testContactsFileType.getContactsFileMediumType()).isEqualTo(UPDATED_CONTACTS_FILE_MEDIUM_TYPE);
        assertThat(testContactsFileType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testContactsFileType.getFileTemplate()).isEqualTo(UPDATED_FILE_TEMPLATE);
        assertThat(testContactsFileType.getFileTemplateContentType()).isEqualTo(UPDATED_FILE_TEMPLATE_CONTENT_TYPE);
        assertThat(testContactsFileType.getContactsfileType()).isEqualTo(UPDATED_CONTACTSFILE_TYPE);

        // Validate the ContactsFileType in Elasticsearch
        verify(mockContactsFileTypeSearchRepository, times(2)).save(testContactsFileType);
    }

    @Test
    @Transactional
    public void updateNonExistingContactsFileType() throws Exception {
        int databaseSizeBeforeUpdate = contactsFileTypeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactsFileTypeMockMvc.perform(put("/api/contacts-file-types").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactsFileType)))
            .andExpect(status().isBadRequest());

        // Validate the ContactsFileType in the database
        List<ContactsFileType> contactsFileTypeList = contactsFileTypeRepository.findAll();
        assertThat(contactsFileTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContactsFileType in Elasticsearch
        verify(mockContactsFileTypeSearchRepository, times(0)).save(contactsFileType);
    }

    @Test
    @Transactional
    public void deleteContactsFileType() throws Exception {
        // Initialize the database
        contactsFileTypeService.save(contactsFileType);

        int databaseSizeBeforeDelete = contactsFileTypeRepository.findAll().size();

        // Delete the contactsFileType
        restContactsFileTypeMockMvc.perform(delete("/api/contacts-file-types/{id}", contactsFileType.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactsFileType> contactsFileTypeList = contactsFileTypeRepository.findAll();
        assertThat(contactsFileTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ContactsFileType in Elasticsearch
        verify(mockContactsFileTypeSearchRepository, times(1)).deleteById(contactsFileType.getId());
    }

    @Test
    @Transactional
    public void searchContactsFileType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        contactsFileTypeService.save(contactsFileType);
        when(mockContactsFileTypeSearchRepository.search(queryStringQuery("id:" + contactsFileType.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(contactsFileType), PageRequest.of(0, 1), 1));

        // Search the contactsFileType
        restContactsFileTypeMockMvc.perform(get("/api/_search/contacts-file-types?query=id:" + contactsFileType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactsFileType.getId().intValue())))
            .andExpect(jsonPath("$.[*].contactsFileTypeName").value(hasItem(DEFAULT_CONTACTS_FILE_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].contactsFileMediumType").value(hasItem(DEFAULT_CONTACTS_FILE_MEDIUM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileTemplateContentType").value(hasItem(DEFAULT_FILE_TEMPLATE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileTemplate").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_TEMPLATE))))
            .andExpect(jsonPath("$.[*].contactsfileType").value(hasItem(DEFAULT_CONTACTSFILE_TYPE.toString())));
    }
}

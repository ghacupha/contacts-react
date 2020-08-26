package io.github.contacts.web.rest;

import io.github.contacts.ContactsApp;
import io.github.contacts.domain.ContactsFileUpload;
import io.github.contacts.repository.ContactsFileUploadRepository;
import io.github.contacts.repository.search.ContactsFileUploadSearchRepository;
import io.github.contacts.service.ContactsFileUploadService;
import io.github.contacts.service.dto.ContactsFileUploadDTO;
import io.github.contacts.service.mapper.ContactsFileUploadMapper;
import io.github.contacts.service.dto.ContactsFileUploadCriteria;
import io.github.contacts.service.ContactsFileUploadQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ContactsFileUploadResource} REST controller.
 */
@SpringBootTest(classes = ContactsApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ContactsFileUploadResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PERIOD_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_FROM = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PERIOD_FROM = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_PERIOD_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_TO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PERIOD_TO = LocalDate.ofEpochDay(-1L);

    private static final Long DEFAULT_CONTACTS_FILE_TYPE_ID = 1L;
    private static final Long UPDATED_CONTACTS_FILE_TYPE_ID = 2L;
    private static final Long SMALLER_CONTACTS_FILE_TYPE_ID = 1L - 1L;

    private static final byte[] DEFAULT_DATA_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DATA_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DATA_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DATA_FILE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_UPLOAD_SUCCESSFUL = false;
    private static final Boolean UPDATED_UPLOAD_SUCCESSFUL = true;

    private static final Boolean DEFAULT_UPLOAD_PROCESSED = false;
    private static final Boolean UPDATED_UPLOAD_PROCESSED = true;

    private static final String DEFAULT_UPLOAD_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_UPLOAD_TOKEN = "BBBBBBBBBB";

    @Autowired
    private ContactsFileUploadRepository contactsFileUploadRepository;

    @Autowired
    private ContactsFileUploadMapper contactsFileUploadMapper;

    @Autowired
    private ContactsFileUploadService contactsFileUploadService;

    /**
     * This repository is mocked in the io.github.contacts.repository.search test package.
     *
     * @see io.github.contacts.repository.search.ContactsFileUploadSearchRepositoryMockConfiguration
     */
    @Autowired
    private ContactsFileUploadSearchRepository mockContactsFileUploadSearchRepository;

    @Autowired
    private ContactsFileUploadQueryService contactsFileUploadQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactsFileUploadMockMvc;

    private ContactsFileUpload contactsFileUpload;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactsFileUpload createEntity(EntityManager em) {
        ContactsFileUpload contactsFileUpload = new ContactsFileUpload()
            .description(DEFAULT_DESCRIPTION)
            .fileName(DEFAULT_FILE_NAME)
            .periodFrom(DEFAULT_PERIOD_FROM)
            .periodTo(DEFAULT_PERIOD_TO)
            .contactsFileTypeId(DEFAULT_CONTACTS_FILE_TYPE_ID)
            .dataFile(DEFAULT_DATA_FILE)
            .dataFileContentType(DEFAULT_DATA_FILE_CONTENT_TYPE)
            .uploadSuccessful(DEFAULT_UPLOAD_SUCCESSFUL)
            .uploadProcessed(DEFAULT_UPLOAD_PROCESSED)
            .uploadToken(DEFAULT_UPLOAD_TOKEN);
        return contactsFileUpload;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactsFileUpload createUpdatedEntity(EntityManager em) {
        ContactsFileUpload contactsFileUpload = new ContactsFileUpload()
            .description(UPDATED_DESCRIPTION)
            .fileName(UPDATED_FILE_NAME)
            .periodFrom(UPDATED_PERIOD_FROM)
            .periodTo(UPDATED_PERIOD_TO)
            .contactsFileTypeId(UPDATED_CONTACTS_FILE_TYPE_ID)
            .dataFile(UPDATED_DATA_FILE)
            .dataFileContentType(UPDATED_DATA_FILE_CONTENT_TYPE)
            .uploadSuccessful(UPDATED_UPLOAD_SUCCESSFUL)
            .uploadProcessed(UPDATED_UPLOAD_PROCESSED)
            .uploadToken(UPDATED_UPLOAD_TOKEN);
        return contactsFileUpload;
    }

    @BeforeEach
    public void initTest() {
        contactsFileUpload = createEntity(em);
    }

    @Test
    @Transactional
    public void createContactsFileUpload() throws Exception {
        int databaseSizeBeforeCreate = contactsFileUploadRepository.findAll().size();
        // Create the ContactsFileUpload
        ContactsFileUploadDTO contactsFileUploadDTO = contactsFileUploadMapper.toDto(contactsFileUpload);
        restContactsFileUploadMockMvc.perform(post("/api/contacts-file-uploads").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactsFileUploadDTO)))
            .andExpect(status().isCreated());

        // Validate the ContactsFileUpload in the database
        List<ContactsFileUpload> contactsFileUploadList = contactsFileUploadRepository.findAll();
        assertThat(contactsFileUploadList).hasSize(databaseSizeBeforeCreate + 1);
        ContactsFileUpload testContactsFileUpload = contactsFileUploadList.get(contactsFileUploadList.size() - 1);
        assertThat(testContactsFileUpload.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testContactsFileUpload.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testContactsFileUpload.getPeriodFrom()).isEqualTo(DEFAULT_PERIOD_FROM);
        assertThat(testContactsFileUpload.getPeriodTo()).isEqualTo(DEFAULT_PERIOD_TO);
        assertThat(testContactsFileUpload.getContactsFileTypeId()).isEqualTo(DEFAULT_CONTACTS_FILE_TYPE_ID);
        assertThat(testContactsFileUpload.getDataFile()).isEqualTo(DEFAULT_DATA_FILE);
        assertThat(testContactsFileUpload.getDataFileContentType()).isEqualTo(DEFAULT_DATA_FILE_CONTENT_TYPE);
        assertThat(testContactsFileUpload.isUploadSuccessful()).isEqualTo(DEFAULT_UPLOAD_SUCCESSFUL);
        assertThat(testContactsFileUpload.isUploadProcessed()).isEqualTo(DEFAULT_UPLOAD_PROCESSED);
        assertThat(testContactsFileUpload.getUploadToken()).isEqualTo(DEFAULT_UPLOAD_TOKEN);

        // Validate the ContactsFileUpload in Elasticsearch
        verify(mockContactsFileUploadSearchRepository, times(1)).save(testContactsFileUpload);
    }

    @Test
    @Transactional
    public void createContactsFileUploadWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contactsFileUploadRepository.findAll().size();

        // Create the ContactsFileUpload with an existing ID
        contactsFileUpload.setId(1L);
        ContactsFileUploadDTO contactsFileUploadDTO = contactsFileUploadMapper.toDto(contactsFileUpload);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactsFileUploadMockMvc.perform(post("/api/contacts-file-uploads").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactsFileUploadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ContactsFileUpload in the database
        List<ContactsFileUpload> contactsFileUploadList = contactsFileUploadRepository.findAll();
        assertThat(contactsFileUploadList).hasSize(databaseSizeBeforeCreate);

        // Validate the ContactsFileUpload in Elasticsearch
        verify(mockContactsFileUploadSearchRepository, times(0)).save(contactsFileUpload);
    }


    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactsFileUploadRepository.findAll().size();
        // set the field null
        contactsFileUpload.setDescription(null);

        // Create the ContactsFileUpload, which fails.
        ContactsFileUploadDTO contactsFileUploadDTO = contactsFileUploadMapper.toDto(contactsFileUpload);


        restContactsFileUploadMockMvc.perform(post("/api/contacts-file-uploads").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactsFileUploadDTO)))
            .andExpect(status().isBadRequest());

        List<ContactsFileUpload> contactsFileUploadList = contactsFileUploadRepository.findAll();
        assertThat(contactsFileUploadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFileNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactsFileUploadRepository.findAll().size();
        // set the field null
        contactsFileUpload.setFileName(null);

        // Create the ContactsFileUpload, which fails.
        ContactsFileUploadDTO contactsFileUploadDTO = contactsFileUploadMapper.toDto(contactsFileUpload);


        restContactsFileUploadMockMvc.perform(post("/api/contacts-file-uploads").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactsFileUploadDTO)))
            .andExpect(status().isBadRequest());

        List<ContactsFileUpload> contactsFileUploadList = contactsFileUploadRepository.findAll();
        assertThat(contactsFileUploadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContactsFileTypeIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactsFileUploadRepository.findAll().size();
        // set the field null
        contactsFileUpload.setContactsFileTypeId(null);

        // Create the ContactsFileUpload, which fails.
        ContactsFileUploadDTO contactsFileUploadDTO = contactsFileUploadMapper.toDto(contactsFileUpload);


        restContactsFileUploadMockMvc.perform(post("/api/contacts-file-uploads").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactsFileUploadDTO)))
            .andExpect(status().isBadRequest());

        List<ContactsFileUpload> contactsFileUploadList = contactsFileUploadRepository.findAll();
        assertThat(contactsFileUploadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploads() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList
        restContactsFileUploadMockMvc.perform(get("/api/contacts-file-uploads?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactsFileUpload.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].periodFrom").value(hasItem(DEFAULT_PERIOD_FROM.toString())))
            .andExpect(jsonPath("$.[*].periodTo").value(hasItem(DEFAULT_PERIOD_TO.toString())))
            .andExpect(jsonPath("$.[*].contactsFileTypeId").value(hasItem(DEFAULT_CONTACTS_FILE_TYPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].dataFileContentType").value(hasItem(DEFAULT_DATA_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].dataFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_DATA_FILE))))
            .andExpect(jsonPath("$.[*].uploadSuccessful").value(hasItem(DEFAULT_UPLOAD_SUCCESSFUL.booleanValue())))
            .andExpect(jsonPath("$.[*].uploadProcessed").value(hasItem(DEFAULT_UPLOAD_PROCESSED.booleanValue())))
            .andExpect(jsonPath("$.[*].uploadToken").value(hasItem(DEFAULT_UPLOAD_TOKEN)));
    }
    
    @Test
    @Transactional
    public void getContactsFileUpload() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get the contactsFileUpload
        restContactsFileUploadMockMvc.perform(get("/api/contacts-file-uploads/{id}", contactsFileUpload.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactsFileUpload.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.periodFrom").value(DEFAULT_PERIOD_FROM.toString()))
            .andExpect(jsonPath("$.periodTo").value(DEFAULT_PERIOD_TO.toString()))
            .andExpect(jsonPath("$.contactsFileTypeId").value(DEFAULT_CONTACTS_FILE_TYPE_ID.intValue()))
            .andExpect(jsonPath("$.dataFileContentType").value(DEFAULT_DATA_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.dataFile").value(Base64Utils.encodeToString(DEFAULT_DATA_FILE)))
            .andExpect(jsonPath("$.uploadSuccessful").value(DEFAULT_UPLOAD_SUCCESSFUL.booleanValue()))
            .andExpect(jsonPath("$.uploadProcessed").value(DEFAULT_UPLOAD_PROCESSED.booleanValue()))
            .andExpect(jsonPath("$.uploadToken").value(DEFAULT_UPLOAD_TOKEN));
    }


    @Test
    @Transactional
    public void getContactsFileUploadsByIdFiltering() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        Long id = contactsFileUpload.getId();

        defaultContactsFileUploadShouldBeFound("id.equals=" + id);
        defaultContactsFileUploadShouldNotBeFound("id.notEquals=" + id);

        defaultContactsFileUploadShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContactsFileUploadShouldNotBeFound("id.greaterThan=" + id);

        defaultContactsFileUploadShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContactsFileUploadShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllContactsFileUploadsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where description equals to DEFAULT_DESCRIPTION
        defaultContactsFileUploadShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the contactsFileUploadList where description equals to UPDATED_DESCRIPTION
        defaultContactsFileUploadShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where description not equals to DEFAULT_DESCRIPTION
        defaultContactsFileUploadShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the contactsFileUploadList where description not equals to UPDATED_DESCRIPTION
        defaultContactsFileUploadShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultContactsFileUploadShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the contactsFileUploadList where description equals to UPDATED_DESCRIPTION
        defaultContactsFileUploadShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where description is not null
        defaultContactsFileUploadShouldBeFound("description.specified=true");

        // Get all the contactsFileUploadList where description is null
        defaultContactsFileUploadShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllContactsFileUploadsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where description contains DEFAULT_DESCRIPTION
        defaultContactsFileUploadShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the contactsFileUploadList where description contains UPDATED_DESCRIPTION
        defaultContactsFileUploadShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where description does not contain DEFAULT_DESCRIPTION
        defaultContactsFileUploadShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the contactsFileUploadList where description does not contain UPDATED_DESCRIPTION
        defaultContactsFileUploadShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllContactsFileUploadsByFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where fileName equals to DEFAULT_FILE_NAME
        defaultContactsFileUploadShouldBeFound("fileName.equals=" + DEFAULT_FILE_NAME);

        // Get all the contactsFileUploadList where fileName equals to UPDATED_FILE_NAME
        defaultContactsFileUploadShouldNotBeFound("fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByFileNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where fileName not equals to DEFAULT_FILE_NAME
        defaultContactsFileUploadShouldNotBeFound("fileName.notEquals=" + DEFAULT_FILE_NAME);

        // Get all the contactsFileUploadList where fileName not equals to UPDATED_FILE_NAME
        defaultContactsFileUploadShouldBeFound("fileName.notEquals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where fileName in DEFAULT_FILE_NAME or UPDATED_FILE_NAME
        defaultContactsFileUploadShouldBeFound("fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME);

        // Get all the contactsFileUploadList where fileName equals to UPDATED_FILE_NAME
        defaultContactsFileUploadShouldNotBeFound("fileName.in=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where fileName is not null
        defaultContactsFileUploadShouldBeFound("fileName.specified=true");

        // Get all the contactsFileUploadList where fileName is null
        defaultContactsFileUploadShouldNotBeFound("fileName.specified=false");
    }
                @Test
    @Transactional
    public void getAllContactsFileUploadsByFileNameContainsSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where fileName contains DEFAULT_FILE_NAME
        defaultContactsFileUploadShouldBeFound("fileName.contains=" + DEFAULT_FILE_NAME);

        // Get all the contactsFileUploadList where fileName contains UPDATED_FILE_NAME
        defaultContactsFileUploadShouldNotBeFound("fileName.contains=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where fileName does not contain DEFAULT_FILE_NAME
        defaultContactsFileUploadShouldNotBeFound("fileName.doesNotContain=" + DEFAULT_FILE_NAME);

        // Get all the contactsFileUploadList where fileName does not contain UPDATED_FILE_NAME
        defaultContactsFileUploadShouldBeFound("fileName.doesNotContain=" + UPDATED_FILE_NAME);
    }


    @Test
    @Transactional
    public void getAllContactsFileUploadsByPeriodFromIsEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where periodFrom equals to DEFAULT_PERIOD_FROM
        defaultContactsFileUploadShouldBeFound("periodFrom.equals=" + DEFAULT_PERIOD_FROM);

        // Get all the contactsFileUploadList where periodFrom equals to UPDATED_PERIOD_FROM
        defaultContactsFileUploadShouldNotBeFound("periodFrom.equals=" + UPDATED_PERIOD_FROM);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByPeriodFromIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where periodFrom not equals to DEFAULT_PERIOD_FROM
        defaultContactsFileUploadShouldNotBeFound("periodFrom.notEquals=" + DEFAULT_PERIOD_FROM);

        // Get all the contactsFileUploadList where periodFrom not equals to UPDATED_PERIOD_FROM
        defaultContactsFileUploadShouldBeFound("periodFrom.notEquals=" + UPDATED_PERIOD_FROM);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByPeriodFromIsInShouldWork() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where periodFrom in DEFAULT_PERIOD_FROM or UPDATED_PERIOD_FROM
        defaultContactsFileUploadShouldBeFound("periodFrom.in=" + DEFAULT_PERIOD_FROM + "," + UPDATED_PERIOD_FROM);

        // Get all the contactsFileUploadList where periodFrom equals to UPDATED_PERIOD_FROM
        defaultContactsFileUploadShouldNotBeFound("periodFrom.in=" + UPDATED_PERIOD_FROM);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByPeriodFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where periodFrom is not null
        defaultContactsFileUploadShouldBeFound("periodFrom.specified=true");

        // Get all the contactsFileUploadList where periodFrom is null
        defaultContactsFileUploadShouldNotBeFound("periodFrom.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByPeriodFromIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where periodFrom is greater than or equal to DEFAULT_PERIOD_FROM
        defaultContactsFileUploadShouldBeFound("periodFrom.greaterThanOrEqual=" + DEFAULT_PERIOD_FROM);

        // Get all the contactsFileUploadList where periodFrom is greater than or equal to UPDATED_PERIOD_FROM
        defaultContactsFileUploadShouldNotBeFound("periodFrom.greaterThanOrEqual=" + UPDATED_PERIOD_FROM);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByPeriodFromIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where periodFrom is less than or equal to DEFAULT_PERIOD_FROM
        defaultContactsFileUploadShouldBeFound("periodFrom.lessThanOrEqual=" + DEFAULT_PERIOD_FROM);

        // Get all the contactsFileUploadList where periodFrom is less than or equal to SMALLER_PERIOD_FROM
        defaultContactsFileUploadShouldNotBeFound("periodFrom.lessThanOrEqual=" + SMALLER_PERIOD_FROM);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByPeriodFromIsLessThanSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where periodFrom is less than DEFAULT_PERIOD_FROM
        defaultContactsFileUploadShouldNotBeFound("periodFrom.lessThan=" + DEFAULT_PERIOD_FROM);

        // Get all the contactsFileUploadList where periodFrom is less than UPDATED_PERIOD_FROM
        defaultContactsFileUploadShouldBeFound("periodFrom.lessThan=" + UPDATED_PERIOD_FROM);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByPeriodFromIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where periodFrom is greater than DEFAULT_PERIOD_FROM
        defaultContactsFileUploadShouldNotBeFound("periodFrom.greaterThan=" + DEFAULT_PERIOD_FROM);

        // Get all the contactsFileUploadList where periodFrom is greater than SMALLER_PERIOD_FROM
        defaultContactsFileUploadShouldBeFound("periodFrom.greaterThan=" + SMALLER_PERIOD_FROM);
    }


    @Test
    @Transactional
    public void getAllContactsFileUploadsByPeriodToIsEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where periodTo equals to DEFAULT_PERIOD_TO
        defaultContactsFileUploadShouldBeFound("periodTo.equals=" + DEFAULT_PERIOD_TO);

        // Get all the contactsFileUploadList where periodTo equals to UPDATED_PERIOD_TO
        defaultContactsFileUploadShouldNotBeFound("periodTo.equals=" + UPDATED_PERIOD_TO);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByPeriodToIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where periodTo not equals to DEFAULT_PERIOD_TO
        defaultContactsFileUploadShouldNotBeFound("periodTo.notEquals=" + DEFAULT_PERIOD_TO);

        // Get all the contactsFileUploadList where periodTo not equals to UPDATED_PERIOD_TO
        defaultContactsFileUploadShouldBeFound("periodTo.notEquals=" + UPDATED_PERIOD_TO);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByPeriodToIsInShouldWork() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where periodTo in DEFAULT_PERIOD_TO or UPDATED_PERIOD_TO
        defaultContactsFileUploadShouldBeFound("periodTo.in=" + DEFAULT_PERIOD_TO + "," + UPDATED_PERIOD_TO);

        // Get all the contactsFileUploadList where periodTo equals to UPDATED_PERIOD_TO
        defaultContactsFileUploadShouldNotBeFound("periodTo.in=" + UPDATED_PERIOD_TO);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByPeriodToIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where periodTo is not null
        defaultContactsFileUploadShouldBeFound("periodTo.specified=true");

        // Get all the contactsFileUploadList where periodTo is null
        defaultContactsFileUploadShouldNotBeFound("periodTo.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByPeriodToIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where periodTo is greater than or equal to DEFAULT_PERIOD_TO
        defaultContactsFileUploadShouldBeFound("periodTo.greaterThanOrEqual=" + DEFAULT_PERIOD_TO);

        // Get all the contactsFileUploadList where periodTo is greater than or equal to UPDATED_PERIOD_TO
        defaultContactsFileUploadShouldNotBeFound("periodTo.greaterThanOrEqual=" + UPDATED_PERIOD_TO);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByPeriodToIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where periodTo is less than or equal to DEFAULT_PERIOD_TO
        defaultContactsFileUploadShouldBeFound("periodTo.lessThanOrEqual=" + DEFAULT_PERIOD_TO);

        // Get all the contactsFileUploadList where periodTo is less than or equal to SMALLER_PERIOD_TO
        defaultContactsFileUploadShouldNotBeFound("periodTo.lessThanOrEqual=" + SMALLER_PERIOD_TO);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByPeriodToIsLessThanSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where periodTo is less than DEFAULT_PERIOD_TO
        defaultContactsFileUploadShouldNotBeFound("periodTo.lessThan=" + DEFAULT_PERIOD_TO);

        // Get all the contactsFileUploadList where periodTo is less than UPDATED_PERIOD_TO
        defaultContactsFileUploadShouldBeFound("periodTo.lessThan=" + UPDATED_PERIOD_TO);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByPeriodToIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where periodTo is greater than DEFAULT_PERIOD_TO
        defaultContactsFileUploadShouldNotBeFound("periodTo.greaterThan=" + DEFAULT_PERIOD_TO);

        // Get all the contactsFileUploadList where periodTo is greater than SMALLER_PERIOD_TO
        defaultContactsFileUploadShouldBeFound("periodTo.greaterThan=" + SMALLER_PERIOD_TO);
    }


    @Test
    @Transactional
    public void getAllContactsFileUploadsByContactsFileTypeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where contactsFileTypeId equals to DEFAULT_CONTACTS_FILE_TYPE_ID
        defaultContactsFileUploadShouldBeFound("contactsFileTypeId.equals=" + DEFAULT_CONTACTS_FILE_TYPE_ID);

        // Get all the contactsFileUploadList where contactsFileTypeId equals to UPDATED_CONTACTS_FILE_TYPE_ID
        defaultContactsFileUploadShouldNotBeFound("contactsFileTypeId.equals=" + UPDATED_CONTACTS_FILE_TYPE_ID);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByContactsFileTypeIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where contactsFileTypeId not equals to DEFAULT_CONTACTS_FILE_TYPE_ID
        defaultContactsFileUploadShouldNotBeFound("contactsFileTypeId.notEquals=" + DEFAULT_CONTACTS_FILE_TYPE_ID);

        // Get all the contactsFileUploadList where contactsFileTypeId not equals to UPDATED_CONTACTS_FILE_TYPE_ID
        defaultContactsFileUploadShouldBeFound("contactsFileTypeId.notEquals=" + UPDATED_CONTACTS_FILE_TYPE_ID);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByContactsFileTypeIdIsInShouldWork() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where contactsFileTypeId in DEFAULT_CONTACTS_FILE_TYPE_ID or UPDATED_CONTACTS_FILE_TYPE_ID
        defaultContactsFileUploadShouldBeFound("contactsFileTypeId.in=" + DEFAULT_CONTACTS_FILE_TYPE_ID + "," + UPDATED_CONTACTS_FILE_TYPE_ID);

        // Get all the contactsFileUploadList where contactsFileTypeId equals to UPDATED_CONTACTS_FILE_TYPE_ID
        defaultContactsFileUploadShouldNotBeFound("contactsFileTypeId.in=" + UPDATED_CONTACTS_FILE_TYPE_ID);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByContactsFileTypeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where contactsFileTypeId is not null
        defaultContactsFileUploadShouldBeFound("contactsFileTypeId.specified=true");

        // Get all the contactsFileUploadList where contactsFileTypeId is null
        defaultContactsFileUploadShouldNotBeFound("contactsFileTypeId.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByContactsFileTypeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where contactsFileTypeId is greater than or equal to DEFAULT_CONTACTS_FILE_TYPE_ID
        defaultContactsFileUploadShouldBeFound("contactsFileTypeId.greaterThanOrEqual=" + DEFAULT_CONTACTS_FILE_TYPE_ID);

        // Get all the contactsFileUploadList where contactsFileTypeId is greater than or equal to UPDATED_CONTACTS_FILE_TYPE_ID
        defaultContactsFileUploadShouldNotBeFound("contactsFileTypeId.greaterThanOrEqual=" + UPDATED_CONTACTS_FILE_TYPE_ID);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByContactsFileTypeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where contactsFileTypeId is less than or equal to DEFAULT_CONTACTS_FILE_TYPE_ID
        defaultContactsFileUploadShouldBeFound("contactsFileTypeId.lessThanOrEqual=" + DEFAULT_CONTACTS_FILE_TYPE_ID);

        // Get all the contactsFileUploadList where contactsFileTypeId is less than or equal to SMALLER_CONTACTS_FILE_TYPE_ID
        defaultContactsFileUploadShouldNotBeFound("contactsFileTypeId.lessThanOrEqual=" + SMALLER_CONTACTS_FILE_TYPE_ID);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByContactsFileTypeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where contactsFileTypeId is less than DEFAULT_CONTACTS_FILE_TYPE_ID
        defaultContactsFileUploadShouldNotBeFound("contactsFileTypeId.lessThan=" + DEFAULT_CONTACTS_FILE_TYPE_ID);

        // Get all the contactsFileUploadList where contactsFileTypeId is less than UPDATED_CONTACTS_FILE_TYPE_ID
        defaultContactsFileUploadShouldBeFound("contactsFileTypeId.lessThan=" + UPDATED_CONTACTS_FILE_TYPE_ID);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByContactsFileTypeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where contactsFileTypeId is greater than DEFAULT_CONTACTS_FILE_TYPE_ID
        defaultContactsFileUploadShouldNotBeFound("contactsFileTypeId.greaterThan=" + DEFAULT_CONTACTS_FILE_TYPE_ID);

        // Get all the contactsFileUploadList where contactsFileTypeId is greater than SMALLER_CONTACTS_FILE_TYPE_ID
        defaultContactsFileUploadShouldBeFound("contactsFileTypeId.greaterThan=" + SMALLER_CONTACTS_FILE_TYPE_ID);
    }


    @Test
    @Transactional
    public void getAllContactsFileUploadsByUploadSuccessfulIsEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where uploadSuccessful equals to DEFAULT_UPLOAD_SUCCESSFUL
        defaultContactsFileUploadShouldBeFound("uploadSuccessful.equals=" + DEFAULT_UPLOAD_SUCCESSFUL);

        // Get all the contactsFileUploadList where uploadSuccessful equals to UPDATED_UPLOAD_SUCCESSFUL
        defaultContactsFileUploadShouldNotBeFound("uploadSuccessful.equals=" + UPDATED_UPLOAD_SUCCESSFUL);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByUploadSuccessfulIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where uploadSuccessful not equals to DEFAULT_UPLOAD_SUCCESSFUL
        defaultContactsFileUploadShouldNotBeFound("uploadSuccessful.notEquals=" + DEFAULT_UPLOAD_SUCCESSFUL);

        // Get all the contactsFileUploadList where uploadSuccessful not equals to UPDATED_UPLOAD_SUCCESSFUL
        defaultContactsFileUploadShouldBeFound("uploadSuccessful.notEquals=" + UPDATED_UPLOAD_SUCCESSFUL);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByUploadSuccessfulIsInShouldWork() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where uploadSuccessful in DEFAULT_UPLOAD_SUCCESSFUL or UPDATED_UPLOAD_SUCCESSFUL
        defaultContactsFileUploadShouldBeFound("uploadSuccessful.in=" + DEFAULT_UPLOAD_SUCCESSFUL + "," + UPDATED_UPLOAD_SUCCESSFUL);

        // Get all the contactsFileUploadList where uploadSuccessful equals to UPDATED_UPLOAD_SUCCESSFUL
        defaultContactsFileUploadShouldNotBeFound("uploadSuccessful.in=" + UPDATED_UPLOAD_SUCCESSFUL);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByUploadSuccessfulIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where uploadSuccessful is not null
        defaultContactsFileUploadShouldBeFound("uploadSuccessful.specified=true");

        // Get all the contactsFileUploadList where uploadSuccessful is null
        defaultContactsFileUploadShouldNotBeFound("uploadSuccessful.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByUploadProcessedIsEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where uploadProcessed equals to DEFAULT_UPLOAD_PROCESSED
        defaultContactsFileUploadShouldBeFound("uploadProcessed.equals=" + DEFAULT_UPLOAD_PROCESSED);

        // Get all the contactsFileUploadList where uploadProcessed equals to UPDATED_UPLOAD_PROCESSED
        defaultContactsFileUploadShouldNotBeFound("uploadProcessed.equals=" + UPDATED_UPLOAD_PROCESSED);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByUploadProcessedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where uploadProcessed not equals to DEFAULT_UPLOAD_PROCESSED
        defaultContactsFileUploadShouldNotBeFound("uploadProcessed.notEquals=" + DEFAULT_UPLOAD_PROCESSED);

        // Get all the contactsFileUploadList where uploadProcessed not equals to UPDATED_UPLOAD_PROCESSED
        defaultContactsFileUploadShouldBeFound("uploadProcessed.notEquals=" + UPDATED_UPLOAD_PROCESSED);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByUploadProcessedIsInShouldWork() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where uploadProcessed in DEFAULT_UPLOAD_PROCESSED or UPDATED_UPLOAD_PROCESSED
        defaultContactsFileUploadShouldBeFound("uploadProcessed.in=" + DEFAULT_UPLOAD_PROCESSED + "," + UPDATED_UPLOAD_PROCESSED);

        // Get all the contactsFileUploadList where uploadProcessed equals to UPDATED_UPLOAD_PROCESSED
        defaultContactsFileUploadShouldNotBeFound("uploadProcessed.in=" + UPDATED_UPLOAD_PROCESSED);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByUploadProcessedIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where uploadProcessed is not null
        defaultContactsFileUploadShouldBeFound("uploadProcessed.specified=true");

        // Get all the contactsFileUploadList where uploadProcessed is null
        defaultContactsFileUploadShouldNotBeFound("uploadProcessed.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByUploadTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where uploadToken equals to DEFAULT_UPLOAD_TOKEN
        defaultContactsFileUploadShouldBeFound("uploadToken.equals=" + DEFAULT_UPLOAD_TOKEN);

        // Get all the contactsFileUploadList where uploadToken equals to UPDATED_UPLOAD_TOKEN
        defaultContactsFileUploadShouldNotBeFound("uploadToken.equals=" + UPDATED_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByUploadTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where uploadToken not equals to DEFAULT_UPLOAD_TOKEN
        defaultContactsFileUploadShouldNotBeFound("uploadToken.notEquals=" + DEFAULT_UPLOAD_TOKEN);

        // Get all the contactsFileUploadList where uploadToken not equals to UPDATED_UPLOAD_TOKEN
        defaultContactsFileUploadShouldBeFound("uploadToken.notEquals=" + UPDATED_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByUploadTokenIsInShouldWork() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where uploadToken in DEFAULT_UPLOAD_TOKEN or UPDATED_UPLOAD_TOKEN
        defaultContactsFileUploadShouldBeFound("uploadToken.in=" + DEFAULT_UPLOAD_TOKEN + "," + UPDATED_UPLOAD_TOKEN);

        // Get all the contactsFileUploadList where uploadToken equals to UPDATED_UPLOAD_TOKEN
        defaultContactsFileUploadShouldNotBeFound("uploadToken.in=" + UPDATED_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByUploadTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where uploadToken is not null
        defaultContactsFileUploadShouldBeFound("uploadToken.specified=true");

        // Get all the contactsFileUploadList where uploadToken is null
        defaultContactsFileUploadShouldNotBeFound("uploadToken.specified=false");
    }
                @Test
    @Transactional
    public void getAllContactsFileUploadsByUploadTokenContainsSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where uploadToken contains DEFAULT_UPLOAD_TOKEN
        defaultContactsFileUploadShouldBeFound("uploadToken.contains=" + DEFAULT_UPLOAD_TOKEN);

        // Get all the contactsFileUploadList where uploadToken contains UPDATED_UPLOAD_TOKEN
        defaultContactsFileUploadShouldNotBeFound("uploadToken.contains=" + UPDATED_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    public void getAllContactsFileUploadsByUploadTokenNotContainsSomething() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        // Get all the contactsFileUploadList where uploadToken does not contain DEFAULT_UPLOAD_TOKEN
        defaultContactsFileUploadShouldNotBeFound("uploadToken.doesNotContain=" + DEFAULT_UPLOAD_TOKEN);

        // Get all the contactsFileUploadList where uploadToken does not contain UPDATED_UPLOAD_TOKEN
        defaultContactsFileUploadShouldBeFound("uploadToken.doesNotContain=" + UPDATED_UPLOAD_TOKEN);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContactsFileUploadShouldBeFound(String filter) throws Exception {
        restContactsFileUploadMockMvc.perform(get("/api/contacts-file-uploads?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactsFileUpload.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].periodFrom").value(hasItem(DEFAULT_PERIOD_FROM.toString())))
            .andExpect(jsonPath("$.[*].periodTo").value(hasItem(DEFAULT_PERIOD_TO.toString())))
            .andExpect(jsonPath("$.[*].contactsFileTypeId").value(hasItem(DEFAULT_CONTACTS_FILE_TYPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].dataFileContentType").value(hasItem(DEFAULT_DATA_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].dataFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_DATA_FILE))))
            .andExpect(jsonPath("$.[*].uploadSuccessful").value(hasItem(DEFAULT_UPLOAD_SUCCESSFUL.booleanValue())))
            .andExpect(jsonPath("$.[*].uploadProcessed").value(hasItem(DEFAULT_UPLOAD_PROCESSED.booleanValue())))
            .andExpect(jsonPath("$.[*].uploadToken").value(hasItem(DEFAULT_UPLOAD_TOKEN)));

        // Check, that the count call also returns 1
        restContactsFileUploadMockMvc.perform(get("/api/contacts-file-uploads/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContactsFileUploadShouldNotBeFound(String filter) throws Exception {
        restContactsFileUploadMockMvc.perform(get("/api/contacts-file-uploads?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContactsFileUploadMockMvc.perform(get("/api/contacts-file-uploads/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingContactsFileUpload() throws Exception {
        // Get the contactsFileUpload
        restContactsFileUploadMockMvc.perform(get("/api/contacts-file-uploads/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContactsFileUpload() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        int databaseSizeBeforeUpdate = contactsFileUploadRepository.findAll().size();

        // Update the contactsFileUpload
        ContactsFileUpload updatedContactsFileUpload = contactsFileUploadRepository.findById(contactsFileUpload.getId()).get();
        // Disconnect from session so that the updates on updatedContactsFileUpload are not directly saved in db
        em.detach(updatedContactsFileUpload);
        updatedContactsFileUpload
            .description(UPDATED_DESCRIPTION)
            .fileName(UPDATED_FILE_NAME)
            .periodFrom(UPDATED_PERIOD_FROM)
            .periodTo(UPDATED_PERIOD_TO)
            .contactsFileTypeId(UPDATED_CONTACTS_FILE_TYPE_ID)
            .dataFile(UPDATED_DATA_FILE)
            .dataFileContentType(UPDATED_DATA_FILE_CONTENT_TYPE)
            .uploadSuccessful(UPDATED_UPLOAD_SUCCESSFUL)
            .uploadProcessed(UPDATED_UPLOAD_PROCESSED)
            .uploadToken(UPDATED_UPLOAD_TOKEN);
        ContactsFileUploadDTO contactsFileUploadDTO = contactsFileUploadMapper.toDto(updatedContactsFileUpload);

        restContactsFileUploadMockMvc.perform(put("/api/contacts-file-uploads").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactsFileUploadDTO)))
            .andExpect(status().isOk());

        // Validate the ContactsFileUpload in the database
        List<ContactsFileUpload> contactsFileUploadList = contactsFileUploadRepository.findAll();
        assertThat(contactsFileUploadList).hasSize(databaseSizeBeforeUpdate);
        ContactsFileUpload testContactsFileUpload = contactsFileUploadList.get(contactsFileUploadList.size() - 1);
        assertThat(testContactsFileUpload.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testContactsFileUpload.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testContactsFileUpload.getPeriodFrom()).isEqualTo(UPDATED_PERIOD_FROM);
        assertThat(testContactsFileUpload.getPeriodTo()).isEqualTo(UPDATED_PERIOD_TO);
        assertThat(testContactsFileUpload.getContactsFileTypeId()).isEqualTo(UPDATED_CONTACTS_FILE_TYPE_ID);
        assertThat(testContactsFileUpload.getDataFile()).isEqualTo(UPDATED_DATA_FILE);
        assertThat(testContactsFileUpload.getDataFileContentType()).isEqualTo(UPDATED_DATA_FILE_CONTENT_TYPE);
        assertThat(testContactsFileUpload.isUploadSuccessful()).isEqualTo(UPDATED_UPLOAD_SUCCESSFUL);
        assertThat(testContactsFileUpload.isUploadProcessed()).isEqualTo(UPDATED_UPLOAD_PROCESSED);
        assertThat(testContactsFileUpload.getUploadToken()).isEqualTo(UPDATED_UPLOAD_TOKEN);

        // Validate the ContactsFileUpload in Elasticsearch
        verify(mockContactsFileUploadSearchRepository, times(1)).save(testContactsFileUpload);
    }

    @Test
    @Transactional
    public void updateNonExistingContactsFileUpload() throws Exception {
        int databaseSizeBeforeUpdate = contactsFileUploadRepository.findAll().size();

        // Create the ContactsFileUpload
        ContactsFileUploadDTO contactsFileUploadDTO = contactsFileUploadMapper.toDto(contactsFileUpload);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactsFileUploadMockMvc.perform(put("/api/contacts-file-uploads").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactsFileUploadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ContactsFileUpload in the database
        List<ContactsFileUpload> contactsFileUploadList = contactsFileUploadRepository.findAll();
        assertThat(contactsFileUploadList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContactsFileUpload in Elasticsearch
        verify(mockContactsFileUploadSearchRepository, times(0)).save(contactsFileUpload);
    }

    @Test
    @Transactional
    public void deleteContactsFileUpload() throws Exception {
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);

        int databaseSizeBeforeDelete = contactsFileUploadRepository.findAll().size();

        // Delete the contactsFileUpload
        restContactsFileUploadMockMvc.perform(delete("/api/contacts-file-uploads/{id}", contactsFileUpload.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactsFileUpload> contactsFileUploadList = contactsFileUploadRepository.findAll();
        assertThat(contactsFileUploadList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ContactsFileUpload in Elasticsearch
        verify(mockContactsFileUploadSearchRepository, times(1)).deleteById(contactsFileUpload.getId());
    }

    @Test
    @Transactional
    public void searchContactsFileUpload() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        contactsFileUploadRepository.saveAndFlush(contactsFileUpload);
        when(mockContactsFileUploadSearchRepository.search(queryStringQuery("id:" + contactsFileUpload.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(contactsFileUpload), PageRequest.of(0, 1), 1));

        // Search the contactsFileUpload
        restContactsFileUploadMockMvc.perform(get("/api/_search/contacts-file-uploads?query=id:" + contactsFileUpload.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactsFileUpload.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].periodFrom").value(hasItem(DEFAULT_PERIOD_FROM.toString())))
            .andExpect(jsonPath("$.[*].periodTo").value(hasItem(DEFAULT_PERIOD_TO.toString())))
            .andExpect(jsonPath("$.[*].contactsFileTypeId").value(hasItem(DEFAULT_CONTACTS_FILE_TYPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].dataFileContentType").value(hasItem(DEFAULT_DATA_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].dataFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_DATA_FILE))))
            .andExpect(jsonPath("$.[*].uploadSuccessful").value(hasItem(DEFAULT_UPLOAD_SUCCESSFUL.booleanValue())))
            .andExpect(jsonPath("$.[*].uploadProcessed").value(hasItem(DEFAULT_UPLOAD_PROCESSED.booleanValue())))
            .andExpect(jsonPath("$.[*].uploadToken").value(hasItem(DEFAULT_UPLOAD_TOKEN)));
    }
}

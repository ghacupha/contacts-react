package io.github.contacts.web.rest;

import io.github.contacts.ContactsApp;
import io.github.contacts.domain.ContactsMessageToken;
import io.github.contacts.repository.ContactsMessageTokenRepository;
import io.github.contacts.repository.search.ContactsMessageTokenSearchRepository;
import io.github.contacts.service.ContactsMessageTokenService;
import io.github.contacts.service.dto.ContactsMessageTokenDTO;
import io.github.contacts.service.mapper.ContactsMessageTokenMapper;
import io.github.contacts.service.dto.ContactsMessageTokenCriteria;
import io.github.contacts.service.ContactsMessageTokenQueryService;

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
 * Integration tests for the {@link ContactsMessageTokenResource} REST controller.
 */
@SpringBootTest(classes = ContactsApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ContactsMessageTokenResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_TIME_SENT = 1L;
    private static final Long UPDATED_TIME_SENT = 2L;
    private static final Long SMALLER_TIME_SENT = 1L - 1L;

    private static final String DEFAULT_TOKEN_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN_VALUE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_RECEIVED = false;
    private static final Boolean UPDATED_RECEIVED = true;

    private static final Boolean DEFAULT_ACTIONED = false;
    private static final Boolean UPDATED_ACTIONED = true;

    private static final Boolean DEFAULT_CONTENT_FULLY_ENQUEUED = false;
    private static final Boolean UPDATED_CONTENT_FULLY_ENQUEUED = true;

    @Autowired
    private ContactsMessageTokenRepository contactsMessageTokenRepository;

    @Autowired
    private ContactsMessageTokenMapper contactsMessageTokenMapper;

    @Autowired
    private ContactsMessageTokenService contactsMessageTokenService;

    /**
     * This repository is mocked in the io.github.contacts.repository.search test package.
     *
     * @see io.github.contacts.repository.search.ContactsMessageTokenSearchRepositoryMockConfiguration
     */
    @Autowired
    private ContactsMessageTokenSearchRepository mockContactsMessageTokenSearchRepository;

    @Autowired
    private ContactsMessageTokenQueryService contactsMessageTokenQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactsMessageTokenMockMvc;

    private ContactsMessageToken contactsMessageToken;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactsMessageToken createEntity(EntityManager em) {
        ContactsMessageToken contactsMessageToken = new ContactsMessageToken()
            .description(DEFAULT_DESCRIPTION)
            .timeSent(DEFAULT_TIME_SENT)
            .tokenValue(DEFAULT_TOKEN_VALUE)
            .received(DEFAULT_RECEIVED)
            .actioned(DEFAULT_ACTIONED)
            .contentFullyEnqueued(DEFAULT_CONTENT_FULLY_ENQUEUED);
        return contactsMessageToken;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactsMessageToken createUpdatedEntity(EntityManager em) {
        ContactsMessageToken contactsMessageToken = new ContactsMessageToken()
            .description(UPDATED_DESCRIPTION)
            .timeSent(UPDATED_TIME_SENT)
            .tokenValue(UPDATED_TOKEN_VALUE)
            .received(UPDATED_RECEIVED)
            .actioned(UPDATED_ACTIONED)
            .contentFullyEnqueued(UPDATED_CONTENT_FULLY_ENQUEUED);
        return contactsMessageToken;
    }

    @BeforeEach
    public void initTest() {
        contactsMessageToken = createEntity(em);
    }

    @Test
    @Transactional
    public void createContactsMessageToken() throws Exception {
        int databaseSizeBeforeCreate = contactsMessageTokenRepository.findAll().size();
        // Create the ContactsMessageToken
        ContactsMessageTokenDTO contactsMessageTokenDTO = contactsMessageTokenMapper.toDto(contactsMessageToken);
        restContactsMessageTokenMockMvc.perform(post("/api/contacts-message-tokens").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactsMessageTokenDTO)))
            .andExpect(status().isCreated());

        // Validate the ContactsMessageToken in the database
        List<ContactsMessageToken> contactsMessageTokenList = contactsMessageTokenRepository.findAll();
        assertThat(contactsMessageTokenList).hasSize(databaseSizeBeforeCreate + 1);
        ContactsMessageToken testContactsMessageToken = contactsMessageTokenList.get(contactsMessageTokenList.size() - 1);
        assertThat(testContactsMessageToken.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testContactsMessageToken.getTimeSent()).isEqualTo(DEFAULT_TIME_SENT);
        assertThat(testContactsMessageToken.getTokenValue()).isEqualTo(DEFAULT_TOKEN_VALUE);
        assertThat(testContactsMessageToken.isReceived()).isEqualTo(DEFAULT_RECEIVED);
        assertThat(testContactsMessageToken.isActioned()).isEqualTo(DEFAULT_ACTIONED);
        assertThat(testContactsMessageToken.isContentFullyEnqueued()).isEqualTo(DEFAULT_CONTENT_FULLY_ENQUEUED);

        // Validate the ContactsMessageToken in Elasticsearch
        verify(mockContactsMessageTokenSearchRepository, times(1)).save(testContactsMessageToken);
    }

    @Test
    @Transactional
    public void createContactsMessageTokenWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contactsMessageTokenRepository.findAll().size();

        // Create the ContactsMessageToken with an existing ID
        contactsMessageToken.setId(1L);
        ContactsMessageTokenDTO contactsMessageTokenDTO = contactsMessageTokenMapper.toDto(contactsMessageToken);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactsMessageTokenMockMvc.perform(post("/api/contacts-message-tokens").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactsMessageTokenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ContactsMessageToken in the database
        List<ContactsMessageToken> contactsMessageTokenList = contactsMessageTokenRepository.findAll();
        assertThat(contactsMessageTokenList).hasSize(databaseSizeBeforeCreate);

        // Validate the ContactsMessageToken in Elasticsearch
        verify(mockContactsMessageTokenSearchRepository, times(0)).save(contactsMessageToken);
    }


    @Test
    @Transactional
    public void checkTimeSentIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactsMessageTokenRepository.findAll().size();
        // set the field null
        contactsMessageToken.setTimeSent(null);

        // Create the ContactsMessageToken, which fails.
        ContactsMessageTokenDTO contactsMessageTokenDTO = contactsMessageTokenMapper.toDto(contactsMessageToken);


        restContactsMessageTokenMockMvc.perform(post("/api/contacts-message-tokens").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactsMessageTokenDTO)))
            .andExpect(status().isBadRequest());

        List<ContactsMessageToken> contactsMessageTokenList = contactsMessageTokenRepository.findAll();
        assertThat(contactsMessageTokenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTokenValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactsMessageTokenRepository.findAll().size();
        // set the field null
        contactsMessageToken.setTokenValue(null);

        // Create the ContactsMessageToken, which fails.
        ContactsMessageTokenDTO contactsMessageTokenDTO = contactsMessageTokenMapper.toDto(contactsMessageToken);


        restContactsMessageTokenMockMvc.perform(post("/api/contacts-message-tokens").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactsMessageTokenDTO)))
            .andExpect(status().isBadRequest());

        List<ContactsMessageToken> contactsMessageTokenList = contactsMessageTokenRepository.findAll();
        assertThat(contactsMessageTokenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokens() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList
        restContactsMessageTokenMockMvc.perform(get("/api/contacts-message-tokens?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactsMessageToken.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].timeSent").value(hasItem(DEFAULT_TIME_SENT.intValue())))
            .andExpect(jsonPath("$.[*].tokenValue").value(hasItem(DEFAULT_TOKEN_VALUE)))
            .andExpect(jsonPath("$.[*].received").value(hasItem(DEFAULT_RECEIVED.booleanValue())))
            .andExpect(jsonPath("$.[*].actioned").value(hasItem(DEFAULT_ACTIONED.booleanValue())))
            .andExpect(jsonPath("$.[*].contentFullyEnqueued").value(hasItem(DEFAULT_CONTENT_FULLY_ENQUEUED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getContactsMessageToken() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get the contactsMessageToken
        restContactsMessageTokenMockMvc.perform(get("/api/contacts-message-tokens/{id}", contactsMessageToken.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactsMessageToken.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.timeSent").value(DEFAULT_TIME_SENT.intValue()))
            .andExpect(jsonPath("$.tokenValue").value(DEFAULT_TOKEN_VALUE))
            .andExpect(jsonPath("$.received").value(DEFAULT_RECEIVED.booleanValue()))
            .andExpect(jsonPath("$.actioned").value(DEFAULT_ACTIONED.booleanValue()))
            .andExpect(jsonPath("$.contentFullyEnqueued").value(DEFAULT_CONTENT_FULLY_ENQUEUED.booleanValue()));
    }


    @Test
    @Transactional
    public void getContactsMessageTokensByIdFiltering() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        Long id = contactsMessageToken.getId();

        defaultContactsMessageTokenShouldBeFound("id.equals=" + id);
        defaultContactsMessageTokenShouldNotBeFound("id.notEquals=" + id);

        defaultContactsMessageTokenShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContactsMessageTokenShouldNotBeFound("id.greaterThan=" + id);

        defaultContactsMessageTokenShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContactsMessageTokenShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllContactsMessageTokensByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where description equals to DEFAULT_DESCRIPTION
        defaultContactsMessageTokenShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the contactsMessageTokenList where description equals to UPDATED_DESCRIPTION
        defaultContactsMessageTokenShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where description not equals to DEFAULT_DESCRIPTION
        defaultContactsMessageTokenShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the contactsMessageTokenList where description not equals to UPDATED_DESCRIPTION
        defaultContactsMessageTokenShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultContactsMessageTokenShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the contactsMessageTokenList where description equals to UPDATED_DESCRIPTION
        defaultContactsMessageTokenShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where description is not null
        defaultContactsMessageTokenShouldBeFound("description.specified=true");

        // Get all the contactsMessageTokenList where description is null
        defaultContactsMessageTokenShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllContactsMessageTokensByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where description contains DEFAULT_DESCRIPTION
        defaultContactsMessageTokenShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the contactsMessageTokenList where description contains UPDATED_DESCRIPTION
        defaultContactsMessageTokenShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where description does not contain DEFAULT_DESCRIPTION
        defaultContactsMessageTokenShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the contactsMessageTokenList where description does not contain UPDATED_DESCRIPTION
        defaultContactsMessageTokenShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllContactsMessageTokensByTimeSentIsEqualToSomething() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where timeSent equals to DEFAULT_TIME_SENT
        defaultContactsMessageTokenShouldBeFound("timeSent.equals=" + DEFAULT_TIME_SENT);

        // Get all the contactsMessageTokenList where timeSent equals to UPDATED_TIME_SENT
        defaultContactsMessageTokenShouldNotBeFound("timeSent.equals=" + UPDATED_TIME_SENT);
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByTimeSentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where timeSent not equals to DEFAULT_TIME_SENT
        defaultContactsMessageTokenShouldNotBeFound("timeSent.notEquals=" + DEFAULT_TIME_SENT);

        // Get all the contactsMessageTokenList where timeSent not equals to UPDATED_TIME_SENT
        defaultContactsMessageTokenShouldBeFound("timeSent.notEquals=" + UPDATED_TIME_SENT);
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByTimeSentIsInShouldWork() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where timeSent in DEFAULT_TIME_SENT or UPDATED_TIME_SENT
        defaultContactsMessageTokenShouldBeFound("timeSent.in=" + DEFAULT_TIME_SENT + "," + UPDATED_TIME_SENT);

        // Get all the contactsMessageTokenList where timeSent equals to UPDATED_TIME_SENT
        defaultContactsMessageTokenShouldNotBeFound("timeSent.in=" + UPDATED_TIME_SENT);
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByTimeSentIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where timeSent is not null
        defaultContactsMessageTokenShouldBeFound("timeSent.specified=true");

        // Get all the contactsMessageTokenList where timeSent is null
        defaultContactsMessageTokenShouldNotBeFound("timeSent.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByTimeSentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where timeSent is greater than or equal to DEFAULT_TIME_SENT
        defaultContactsMessageTokenShouldBeFound("timeSent.greaterThanOrEqual=" + DEFAULT_TIME_SENT);

        // Get all the contactsMessageTokenList where timeSent is greater than or equal to UPDATED_TIME_SENT
        defaultContactsMessageTokenShouldNotBeFound("timeSent.greaterThanOrEqual=" + UPDATED_TIME_SENT);
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByTimeSentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where timeSent is less than or equal to DEFAULT_TIME_SENT
        defaultContactsMessageTokenShouldBeFound("timeSent.lessThanOrEqual=" + DEFAULT_TIME_SENT);

        // Get all the contactsMessageTokenList where timeSent is less than or equal to SMALLER_TIME_SENT
        defaultContactsMessageTokenShouldNotBeFound("timeSent.lessThanOrEqual=" + SMALLER_TIME_SENT);
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByTimeSentIsLessThanSomething() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where timeSent is less than DEFAULT_TIME_SENT
        defaultContactsMessageTokenShouldNotBeFound("timeSent.lessThan=" + DEFAULT_TIME_SENT);

        // Get all the contactsMessageTokenList where timeSent is less than UPDATED_TIME_SENT
        defaultContactsMessageTokenShouldBeFound("timeSent.lessThan=" + UPDATED_TIME_SENT);
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByTimeSentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where timeSent is greater than DEFAULT_TIME_SENT
        defaultContactsMessageTokenShouldNotBeFound("timeSent.greaterThan=" + DEFAULT_TIME_SENT);

        // Get all the contactsMessageTokenList where timeSent is greater than SMALLER_TIME_SENT
        defaultContactsMessageTokenShouldBeFound("timeSent.greaterThan=" + SMALLER_TIME_SENT);
    }


    @Test
    @Transactional
    public void getAllContactsMessageTokensByTokenValueIsEqualToSomething() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where tokenValue equals to DEFAULT_TOKEN_VALUE
        defaultContactsMessageTokenShouldBeFound("tokenValue.equals=" + DEFAULT_TOKEN_VALUE);

        // Get all the contactsMessageTokenList where tokenValue equals to UPDATED_TOKEN_VALUE
        defaultContactsMessageTokenShouldNotBeFound("tokenValue.equals=" + UPDATED_TOKEN_VALUE);
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByTokenValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where tokenValue not equals to DEFAULT_TOKEN_VALUE
        defaultContactsMessageTokenShouldNotBeFound("tokenValue.notEquals=" + DEFAULT_TOKEN_VALUE);

        // Get all the contactsMessageTokenList where tokenValue not equals to UPDATED_TOKEN_VALUE
        defaultContactsMessageTokenShouldBeFound("tokenValue.notEquals=" + UPDATED_TOKEN_VALUE);
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByTokenValueIsInShouldWork() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where tokenValue in DEFAULT_TOKEN_VALUE or UPDATED_TOKEN_VALUE
        defaultContactsMessageTokenShouldBeFound("tokenValue.in=" + DEFAULT_TOKEN_VALUE + "," + UPDATED_TOKEN_VALUE);

        // Get all the contactsMessageTokenList where tokenValue equals to UPDATED_TOKEN_VALUE
        defaultContactsMessageTokenShouldNotBeFound("tokenValue.in=" + UPDATED_TOKEN_VALUE);
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByTokenValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where tokenValue is not null
        defaultContactsMessageTokenShouldBeFound("tokenValue.specified=true");

        // Get all the contactsMessageTokenList where tokenValue is null
        defaultContactsMessageTokenShouldNotBeFound("tokenValue.specified=false");
    }
                @Test
    @Transactional
    public void getAllContactsMessageTokensByTokenValueContainsSomething() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where tokenValue contains DEFAULT_TOKEN_VALUE
        defaultContactsMessageTokenShouldBeFound("tokenValue.contains=" + DEFAULT_TOKEN_VALUE);

        // Get all the contactsMessageTokenList where tokenValue contains UPDATED_TOKEN_VALUE
        defaultContactsMessageTokenShouldNotBeFound("tokenValue.contains=" + UPDATED_TOKEN_VALUE);
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByTokenValueNotContainsSomething() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where tokenValue does not contain DEFAULT_TOKEN_VALUE
        defaultContactsMessageTokenShouldNotBeFound("tokenValue.doesNotContain=" + DEFAULT_TOKEN_VALUE);

        // Get all the contactsMessageTokenList where tokenValue does not contain UPDATED_TOKEN_VALUE
        defaultContactsMessageTokenShouldBeFound("tokenValue.doesNotContain=" + UPDATED_TOKEN_VALUE);
    }


    @Test
    @Transactional
    public void getAllContactsMessageTokensByReceivedIsEqualToSomething() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where received equals to DEFAULT_RECEIVED
        defaultContactsMessageTokenShouldBeFound("received.equals=" + DEFAULT_RECEIVED);

        // Get all the contactsMessageTokenList where received equals to UPDATED_RECEIVED
        defaultContactsMessageTokenShouldNotBeFound("received.equals=" + UPDATED_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByReceivedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where received not equals to DEFAULT_RECEIVED
        defaultContactsMessageTokenShouldNotBeFound("received.notEquals=" + DEFAULT_RECEIVED);

        // Get all the contactsMessageTokenList where received not equals to UPDATED_RECEIVED
        defaultContactsMessageTokenShouldBeFound("received.notEquals=" + UPDATED_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByReceivedIsInShouldWork() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where received in DEFAULT_RECEIVED or UPDATED_RECEIVED
        defaultContactsMessageTokenShouldBeFound("received.in=" + DEFAULT_RECEIVED + "," + UPDATED_RECEIVED);

        // Get all the contactsMessageTokenList where received equals to UPDATED_RECEIVED
        defaultContactsMessageTokenShouldNotBeFound("received.in=" + UPDATED_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByReceivedIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where received is not null
        defaultContactsMessageTokenShouldBeFound("received.specified=true");

        // Get all the contactsMessageTokenList where received is null
        defaultContactsMessageTokenShouldNotBeFound("received.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByActionedIsEqualToSomething() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where actioned equals to DEFAULT_ACTIONED
        defaultContactsMessageTokenShouldBeFound("actioned.equals=" + DEFAULT_ACTIONED);

        // Get all the contactsMessageTokenList where actioned equals to UPDATED_ACTIONED
        defaultContactsMessageTokenShouldNotBeFound("actioned.equals=" + UPDATED_ACTIONED);
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByActionedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where actioned not equals to DEFAULT_ACTIONED
        defaultContactsMessageTokenShouldNotBeFound("actioned.notEquals=" + DEFAULT_ACTIONED);

        // Get all the contactsMessageTokenList where actioned not equals to UPDATED_ACTIONED
        defaultContactsMessageTokenShouldBeFound("actioned.notEquals=" + UPDATED_ACTIONED);
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByActionedIsInShouldWork() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where actioned in DEFAULT_ACTIONED or UPDATED_ACTIONED
        defaultContactsMessageTokenShouldBeFound("actioned.in=" + DEFAULT_ACTIONED + "," + UPDATED_ACTIONED);

        // Get all the contactsMessageTokenList where actioned equals to UPDATED_ACTIONED
        defaultContactsMessageTokenShouldNotBeFound("actioned.in=" + UPDATED_ACTIONED);
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByActionedIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where actioned is not null
        defaultContactsMessageTokenShouldBeFound("actioned.specified=true");

        // Get all the contactsMessageTokenList where actioned is null
        defaultContactsMessageTokenShouldNotBeFound("actioned.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByContentFullyEnqueuedIsEqualToSomething() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where contentFullyEnqueued equals to DEFAULT_CONTENT_FULLY_ENQUEUED
        defaultContactsMessageTokenShouldBeFound("contentFullyEnqueued.equals=" + DEFAULT_CONTENT_FULLY_ENQUEUED);

        // Get all the contactsMessageTokenList where contentFullyEnqueued equals to UPDATED_CONTENT_FULLY_ENQUEUED
        defaultContactsMessageTokenShouldNotBeFound("contentFullyEnqueued.equals=" + UPDATED_CONTENT_FULLY_ENQUEUED);
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByContentFullyEnqueuedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where contentFullyEnqueued not equals to DEFAULT_CONTENT_FULLY_ENQUEUED
        defaultContactsMessageTokenShouldNotBeFound("contentFullyEnqueued.notEquals=" + DEFAULT_CONTENT_FULLY_ENQUEUED);

        // Get all the contactsMessageTokenList where contentFullyEnqueued not equals to UPDATED_CONTENT_FULLY_ENQUEUED
        defaultContactsMessageTokenShouldBeFound("contentFullyEnqueued.notEquals=" + UPDATED_CONTENT_FULLY_ENQUEUED);
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByContentFullyEnqueuedIsInShouldWork() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where contentFullyEnqueued in DEFAULT_CONTENT_FULLY_ENQUEUED or UPDATED_CONTENT_FULLY_ENQUEUED
        defaultContactsMessageTokenShouldBeFound("contentFullyEnqueued.in=" + DEFAULT_CONTENT_FULLY_ENQUEUED + "," + UPDATED_CONTENT_FULLY_ENQUEUED);

        // Get all the contactsMessageTokenList where contentFullyEnqueued equals to UPDATED_CONTENT_FULLY_ENQUEUED
        defaultContactsMessageTokenShouldNotBeFound("contentFullyEnqueued.in=" + UPDATED_CONTENT_FULLY_ENQUEUED);
    }

    @Test
    @Transactional
    public void getAllContactsMessageTokensByContentFullyEnqueuedIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        // Get all the contactsMessageTokenList where contentFullyEnqueued is not null
        defaultContactsMessageTokenShouldBeFound("contentFullyEnqueued.specified=true");

        // Get all the contactsMessageTokenList where contentFullyEnqueued is null
        defaultContactsMessageTokenShouldNotBeFound("contentFullyEnqueued.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContactsMessageTokenShouldBeFound(String filter) throws Exception {
        restContactsMessageTokenMockMvc.perform(get("/api/contacts-message-tokens?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactsMessageToken.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].timeSent").value(hasItem(DEFAULT_TIME_SENT.intValue())))
            .andExpect(jsonPath("$.[*].tokenValue").value(hasItem(DEFAULT_TOKEN_VALUE)))
            .andExpect(jsonPath("$.[*].received").value(hasItem(DEFAULT_RECEIVED.booleanValue())))
            .andExpect(jsonPath("$.[*].actioned").value(hasItem(DEFAULT_ACTIONED.booleanValue())))
            .andExpect(jsonPath("$.[*].contentFullyEnqueued").value(hasItem(DEFAULT_CONTENT_FULLY_ENQUEUED.booleanValue())));

        // Check, that the count call also returns 1
        restContactsMessageTokenMockMvc.perform(get("/api/contacts-message-tokens/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContactsMessageTokenShouldNotBeFound(String filter) throws Exception {
        restContactsMessageTokenMockMvc.perform(get("/api/contacts-message-tokens?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContactsMessageTokenMockMvc.perform(get("/api/contacts-message-tokens/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingContactsMessageToken() throws Exception {
        // Get the contactsMessageToken
        restContactsMessageTokenMockMvc.perform(get("/api/contacts-message-tokens/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContactsMessageToken() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        int databaseSizeBeforeUpdate = contactsMessageTokenRepository.findAll().size();

        // Update the contactsMessageToken
        ContactsMessageToken updatedContactsMessageToken = contactsMessageTokenRepository.findById(contactsMessageToken.getId()).get();
        // Disconnect from session so that the updates on updatedContactsMessageToken are not directly saved in db
        em.detach(updatedContactsMessageToken);
        updatedContactsMessageToken
            .description(UPDATED_DESCRIPTION)
            .timeSent(UPDATED_TIME_SENT)
            .tokenValue(UPDATED_TOKEN_VALUE)
            .received(UPDATED_RECEIVED)
            .actioned(UPDATED_ACTIONED)
            .contentFullyEnqueued(UPDATED_CONTENT_FULLY_ENQUEUED);
        ContactsMessageTokenDTO contactsMessageTokenDTO = contactsMessageTokenMapper.toDto(updatedContactsMessageToken);

        restContactsMessageTokenMockMvc.perform(put("/api/contacts-message-tokens").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactsMessageTokenDTO)))
            .andExpect(status().isOk());

        // Validate the ContactsMessageToken in the database
        List<ContactsMessageToken> contactsMessageTokenList = contactsMessageTokenRepository.findAll();
        assertThat(contactsMessageTokenList).hasSize(databaseSizeBeforeUpdate);
        ContactsMessageToken testContactsMessageToken = contactsMessageTokenList.get(contactsMessageTokenList.size() - 1);
        assertThat(testContactsMessageToken.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testContactsMessageToken.getTimeSent()).isEqualTo(UPDATED_TIME_SENT);
        assertThat(testContactsMessageToken.getTokenValue()).isEqualTo(UPDATED_TOKEN_VALUE);
        assertThat(testContactsMessageToken.isReceived()).isEqualTo(UPDATED_RECEIVED);
        assertThat(testContactsMessageToken.isActioned()).isEqualTo(UPDATED_ACTIONED);
        assertThat(testContactsMessageToken.isContentFullyEnqueued()).isEqualTo(UPDATED_CONTENT_FULLY_ENQUEUED);

        // Validate the ContactsMessageToken in Elasticsearch
        verify(mockContactsMessageTokenSearchRepository, times(1)).save(testContactsMessageToken);
    }

    @Test
    @Transactional
    public void updateNonExistingContactsMessageToken() throws Exception {
        int databaseSizeBeforeUpdate = contactsMessageTokenRepository.findAll().size();

        // Create the ContactsMessageToken
        ContactsMessageTokenDTO contactsMessageTokenDTO = contactsMessageTokenMapper.toDto(contactsMessageToken);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactsMessageTokenMockMvc.perform(put("/api/contacts-message-tokens").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactsMessageTokenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ContactsMessageToken in the database
        List<ContactsMessageToken> contactsMessageTokenList = contactsMessageTokenRepository.findAll();
        assertThat(contactsMessageTokenList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContactsMessageToken in Elasticsearch
        verify(mockContactsMessageTokenSearchRepository, times(0)).save(contactsMessageToken);
    }

    @Test
    @Transactional
    public void deleteContactsMessageToken() throws Exception {
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);

        int databaseSizeBeforeDelete = contactsMessageTokenRepository.findAll().size();

        // Delete the contactsMessageToken
        restContactsMessageTokenMockMvc.perform(delete("/api/contacts-message-tokens/{id}", contactsMessageToken.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactsMessageToken> contactsMessageTokenList = contactsMessageTokenRepository.findAll();
        assertThat(contactsMessageTokenList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ContactsMessageToken in Elasticsearch
        verify(mockContactsMessageTokenSearchRepository, times(1)).deleteById(contactsMessageToken.getId());
    }

    @Test
    @Transactional
    public void searchContactsMessageToken() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        contactsMessageTokenRepository.saveAndFlush(contactsMessageToken);
        when(mockContactsMessageTokenSearchRepository.search(queryStringQuery("id:" + contactsMessageToken.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(contactsMessageToken), PageRequest.of(0, 1), 1));

        // Search the contactsMessageToken
        restContactsMessageTokenMockMvc.perform(get("/api/_search/contacts-message-tokens?query=id:" + contactsMessageToken.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactsMessageToken.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].timeSent").value(hasItem(DEFAULT_TIME_SENT.intValue())))
            .andExpect(jsonPath("$.[*].tokenValue").value(hasItem(DEFAULT_TOKEN_VALUE)))
            .andExpect(jsonPath("$.[*].received").value(hasItem(DEFAULT_RECEIVED.booleanValue())))
            .andExpect(jsonPath("$.[*].actioned").value(hasItem(DEFAULT_ACTIONED.booleanValue())))
            .andExpect(jsonPath("$.[*].contentFullyEnqueued").value(hasItem(DEFAULT_CONTENT_FULLY_ENQUEUED.booleanValue())));
    }
}

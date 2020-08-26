package io.github.contacts.internal.fileProcessing;

import io.github.contacts.service.ContactsMessageTokenQueryService;
import io.github.contacts.service.dto.ContactsMessageTokenCriteria;
import io.github.contacts.service.dto.ContactsMessageTokenDTO;
import io.github.jhipster.service.filter.StringFilter;
import org.springframework.stereotype.Service;

/**
 * Implementation of token-search where the token value itself is of the value string
 */
@Service("stringTokenValueSearch")
public class StringTokenValueSearch implements TokenValueSearch<String> {

    private final ContactsMessageTokenQueryService messageTokenQueryService;

    public StringTokenValueSearch(final ContactsMessageTokenQueryService messageTokenQueryService) {
        this.messageTokenQueryService = messageTokenQueryService;
    }

    public ContactsMessageTokenDTO getMessageToken(final String tokenValue) {
        StringFilter tokenFilter = new StringFilter();
        tokenFilter.setEquals(tokenValue);
        ContactsMessageTokenCriteria tokenValueCriteria = new ContactsMessageTokenCriteria();
        tokenValueCriteria.setTokenValue(tokenFilter);
        return messageTokenQueryService.findByCriteria(tokenValueCriteria).get(0);
    }
}

package io.github.contacts.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.contacts.domain.enumeration.ContactsFileMediumTypes;
import io.github.contacts.domain.enumeration.ContactsFileModelType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.contacts.domain.ContactsFileType} entity. This class is used
 * in {@link io.github.contacts.web.rest.ContactsFileTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contacts-file-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContactsFileTypeCriteria implements Serializable, Criteria {
    /**
     * Class for filtering ContactsFileMediumTypes
     */
    public static class ContactsFileMediumTypesFilter extends Filter<ContactsFileMediumTypes> {

        public ContactsFileMediumTypesFilter() {
        }

        public ContactsFileMediumTypesFilter(ContactsFileMediumTypesFilter filter) {
            super(filter);
        }

        @Override
        public ContactsFileMediumTypesFilter copy() {
            return new ContactsFileMediumTypesFilter(this);
        }

    }
    /**
     * Class for filtering ContactsFileModelType
     */
    public static class ContactsFileModelTypeFilter extends Filter<ContactsFileModelType> {

        public ContactsFileModelTypeFilter() {
        }

        public ContactsFileModelTypeFilter(ContactsFileModelTypeFilter filter) {
            super(filter);
        }

        @Override
        public ContactsFileModelTypeFilter copy() {
            return new ContactsFileModelTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter contactsFileTypeName;

    private ContactsFileMediumTypesFilter contactsFileMediumType;

    private StringFilter description;

    private ContactsFileModelTypeFilter contactsfileType;

    public ContactsFileTypeCriteria() {
    }

    public ContactsFileTypeCriteria(ContactsFileTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.contactsFileTypeName = other.contactsFileTypeName == null ? null : other.contactsFileTypeName.copy();
        this.contactsFileMediumType = other.contactsFileMediumType == null ? null : other.contactsFileMediumType.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.contactsfileType = other.contactsfileType == null ? null : other.contactsfileType.copy();
    }

    @Override
    public ContactsFileTypeCriteria copy() {
        return new ContactsFileTypeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getContactsFileTypeName() {
        return contactsFileTypeName;
    }

    public void setContactsFileTypeName(StringFilter contactsFileTypeName) {
        this.contactsFileTypeName = contactsFileTypeName;
    }

    public ContactsFileMediumTypesFilter getContactsFileMediumType() {
        return contactsFileMediumType;
    }

    public void setContactsFileMediumType(ContactsFileMediumTypesFilter contactsFileMediumType) {
        this.contactsFileMediumType = contactsFileMediumType;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public ContactsFileModelTypeFilter getContactsfileType() {
        return contactsfileType;
    }

    public void setContactsfileType(ContactsFileModelTypeFilter contactsfileType) {
        this.contactsfileType = contactsfileType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ContactsFileTypeCriteria that = (ContactsFileTypeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(contactsFileTypeName, that.contactsFileTypeName) &&
            Objects.equals(contactsFileMediumType, that.contactsFileMediumType) &&
            Objects.equals(description, that.description) &&
            Objects.equals(contactsfileType, that.contactsfileType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        contactsFileTypeName,
        contactsFileMediumType,
        description,
        contactsfileType
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactsFileTypeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (contactsFileTypeName != null ? "contactsFileTypeName=" + contactsFileTypeName + ", " : "") +
                (contactsFileMediumType != null ? "contactsFileMediumType=" + contactsFileMediumType + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (contactsfileType != null ? "contactsfileType=" + contactsfileType + ", " : "") +
            "}";
    }

}

package io.github.contacts.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.contacts.domain.Contact} entity. This class is used
 * in {@link io.github.contacts.web.rest.ContactResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contacts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContactCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter contactName;

    private StringFilter department;

    private StringFilter telephoneExtension;

    private StringFilter phoneNumber;

    public ContactCriteria() {
    }

    public ContactCriteria(ContactCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.contactName = other.contactName == null ? null : other.contactName.copy();
        this.department = other.department == null ? null : other.department.copy();
        this.telephoneExtension = other.telephoneExtension == null ? null : other.telephoneExtension.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
    }

    @Override
    public ContactCriteria copy() {
        return new ContactCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getContactName() {
        return contactName;
    }

    public void setContactName(StringFilter contactName) {
        this.contactName = contactName;
    }

    public StringFilter getDepartment() {
        return department;
    }

    public void setDepartment(StringFilter department) {
        this.department = department;
    }

    public StringFilter getTelephoneExtension() {
        return telephoneExtension;
    }

    public void setTelephoneExtension(StringFilter telephoneExtension) {
        this.telephoneExtension = telephoneExtension;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ContactCriteria that = (ContactCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(contactName, that.contactName) &&
            Objects.equals(department, that.department) &&
            Objects.equals(telephoneExtension, that.telephoneExtension) &&
            Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        contactName,
        department,
        telephoneExtension,
        phoneNumber
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (contactName != null ? "contactName=" + contactName + ", " : "") +
                (department != null ? "department=" + department + ", " : "") +
                (telephoneExtension != null ? "telephoneExtension=" + telephoneExtension + ", " : "") +
                (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
            "}";
    }

}

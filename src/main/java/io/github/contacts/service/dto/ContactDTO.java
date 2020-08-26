package io.github.contacts.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link io.github.contacts.domain.Contact} entity.
 */
public class ContactDTO implements Serializable {
    
    private Long id;

    private String contactName;

    private String department;

    private String telephoneExtension;

    private String phoneNumber;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTelephoneExtension() {
        return telephoneExtension;
    }

    public void setTelephoneExtension(String telephoneExtension) {
        this.telephoneExtension = telephoneExtension;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactDTO)) {
            return false;
        }

        return id != null && id.equals(((ContactDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactDTO{" +
            "id=" + getId() +
            ", contactName='" + getContactName() + "'" +
            ", department='" + getDepartment() + "'" +
            ", telephoneExtension='" + getTelephoneExtension() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            "}";
    }
}

package io.github.contacts.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

import io.github.contacts.domain.enumeration.ContactsFileMediumTypes;

import io.github.contacts.domain.enumeration.ContactsFileModelType;

/**
 * A ContactsFileType.
 */
@Entity
@Table(name = "contacts_file_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "contactsfiletype")
public class ContactsFileType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "contacts_file_type_name", nullable = false, unique = true)
    private String contactsFileTypeName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "contacts_file_medium_type", nullable = false)
    private ContactsFileMediumTypes contactsFileMediumType;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "file_template")
    private byte[] fileTemplate;

    @Column(name = "file_template_content_type")
    private String fileTemplateContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "contactsfile_type")
    private ContactsFileModelType contactsfileType;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContactsFileTypeName() {
        return contactsFileTypeName;
    }

    public ContactsFileType contactsFileTypeName(String contactsFileTypeName) {
        this.contactsFileTypeName = contactsFileTypeName;
        return this;
    }

    public void setContactsFileTypeName(String contactsFileTypeName) {
        this.contactsFileTypeName = contactsFileTypeName;
    }

    public ContactsFileMediumTypes getContactsFileMediumType() {
        return contactsFileMediumType;
    }

    public ContactsFileType contactsFileMediumType(ContactsFileMediumTypes contactsFileMediumType) {
        this.contactsFileMediumType = contactsFileMediumType;
        return this;
    }

    public void setContactsFileMediumType(ContactsFileMediumTypes contactsFileMediumType) {
        this.contactsFileMediumType = contactsFileMediumType;
    }

    public String getDescription() {
        return description;
    }

    public ContactsFileType description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getFileTemplate() {
        return fileTemplate;
    }

    public ContactsFileType fileTemplate(byte[] fileTemplate) {
        this.fileTemplate = fileTemplate;
        return this;
    }

    public void setFileTemplate(byte[] fileTemplate) {
        this.fileTemplate = fileTemplate;
    }

    public String getFileTemplateContentType() {
        return fileTemplateContentType;
    }

    public ContactsFileType fileTemplateContentType(String fileTemplateContentType) {
        this.fileTemplateContentType = fileTemplateContentType;
        return this;
    }

    public void setFileTemplateContentType(String fileTemplateContentType) {
        this.fileTemplateContentType = fileTemplateContentType;
    }

    public ContactsFileModelType getContactsfileType() {
        return contactsfileType;
    }

    public ContactsFileType contactsfileType(ContactsFileModelType contactsfileType) {
        this.contactsfileType = contactsfileType;
        return this;
    }

    public void setContactsfileType(ContactsFileModelType contactsfileType) {
        this.contactsfileType = contactsfileType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactsFileType)) {
            return false;
        }
        return id != null && id.equals(((ContactsFileType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactsFileType{" +
            "id=" + getId() +
            ", contactsFileTypeName='" + getContactsFileTypeName() + "'" +
            ", contactsFileMediumType='" + getContactsFileMediumType() + "'" +
            ", description='" + getDescription() + "'" +
            ", fileTemplate='" + getFileTemplate() + "'" +
            ", fileTemplateContentType='" + getFileTemplateContentType() + "'" +
            ", contactsfileType='" + getContactsfileType() + "'" +
            "}";
    }
}

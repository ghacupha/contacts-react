import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './contacts-file-type.reducer';
import { IContactsFileType } from 'app/shared/model/contacts/contacts-file-type.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IContactsFileTypeUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ContactsFileTypeUpdate = (props: IContactsFileTypeUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { contactsFileTypeEntity, loading, updating } = props;

  const { fileTemplate, fileTemplateContentType } = contactsFileTypeEntity;

  const handleClose = () => {
    props.history.push('/contacts-file-type' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  const onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => props.setBlob(name, data, contentType), isAnImage);
  };

  const clearBlob = name => () => {
    props.setBlob(name, undefined, undefined);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...contactsFileTypeEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="contactsApp.contactsContactsFileType.home.createOrEditLabel">Create or edit a ContactsFileType</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : contactsFileTypeEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="contacts-file-type-id">ID</Label>
                  <AvInput id="contacts-file-type-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="contactsFileTypeNameLabel" for="contacts-file-type-contactsFileTypeName">
                  Contacts File Type Name
                </Label>
                <AvField
                  id="contacts-file-type-contactsFileTypeName"
                  type="text"
                  name="contactsFileTypeName"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="contactsFileMediumTypeLabel" for="contacts-file-type-contactsFileMediumType">
                  Contacts File Medium Type
                </Label>
                <AvInput
                  id="contacts-file-type-contactsFileMediumType"
                  type="select"
                  className="form-control"
                  name="contactsFileMediumType"
                  value={(!isNew && contactsFileTypeEntity.contactsFileMediumType) || 'EXCEL'}
                >
                  <option value="EXCEL">EXCEL</option>
                  <option value="EXCEL_XLS">EXCEL_XLS</option>
                  <option value="EXCEL_XLSX">EXCEL_XLSX</option>
                  <option value="EXCEL_XLSB">EXCEL_XLSB</option>
                  <option value="EXCEL_CSV">EXCEL_CSV</option>
                  <option value="EXCEL_XML">EXCEL_XML</option>
                  <option value="PDF">PDF</option>
                  <option value="POWERPOINT">POWERPOINT</option>
                  <option value="DOC">DOC</option>
                  <option value="TEXT">TEXT</option>
                  <option value="JSON">JSON</option>
                  <option value="HTML5">HTML5</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="descriptionLabel" for="contacts-file-type-description">
                  Description
                </Label>
                <AvField id="contacts-file-type-description" type="text" name="description" />
              </AvGroup>
              <AvGroup>
                <AvGroup>
                  <Label id="fileTemplateLabel" for="fileTemplate">
                    File Template
                  </Label>
                  <br />
                  {fileTemplate ? (
                    <div>
                      {fileTemplateContentType ? <a onClick={openFile(fileTemplateContentType, fileTemplate)}>Open</a> : null}
                      <br />
                      <Row>
                        <Col md="11">
                          <span>
                            {fileTemplateContentType}, {byteSize(fileTemplate)}
                          </span>
                        </Col>
                        <Col md="1">
                          <Button color="danger" onClick={clearBlob('fileTemplate')}>
                            <FontAwesomeIcon icon="times-circle" />
                          </Button>
                        </Col>
                      </Row>
                    </div>
                  ) : null}
                  <input id="file_fileTemplate" type="file" onChange={onBlobChange(false, 'fileTemplate')} />
                  <AvInput type="hidden" name="fileTemplate" value={fileTemplate} />
                </AvGroup>
              </AvGroup>
              <AvGroup>
                <Label id="contactsfileTypeLabel" for="contacts-file-type-contactsfileType">
                  Contactsfile Type
                </Label>
                <AvInput
                  id="contacts-file-type-contactsfileType"
                  type="select"
                  className="form-control"
                  name="contactsfileType"
                  value={(!isNew && contactsFileTypeEntity.contactsfileType) || 'CURRENCY_LIST'}
                >
                  <option value="CURRENCY_LIST">CURRENCY_LIST</option>
                  <option value="CONTACTS">CONTACTS</option>
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/contacts-file-type" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  contactsFileTypeEntity: storeState.contactsFileType.entity,
  loading: storeState.contactsFileType.loading,
  updating: storeState.contactsFileType.updating,
  updateSuccess: storeState.contactsFileType.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ContactsFileTypeUpdate);

import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './contacts-file-upload.reducer';
import { IContactsFileUpload } from 'app/shared/model/contacts/contacts-file-upload.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IContactsFileUploadUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ContactsFileUploadUpdate = (props: IContactsFileUploadUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { contactsFileUploadEntity, loading, updating } = props;

  const { dataFile, dataFileContentType } = contactsFileUploadEntity;

  const handleClose = () => {
    props.history.push('/contacts-file-upload' + props.location.search);
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
        ...contactsFileUploadEntity,
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
          <h2 id="contactsApp.contactsContactsFileUpload.home.createOrEditLabel">Create or edit a ContactsFileUpload</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : contactsFileUploadEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="contacts-file-upload-id">ID</Label>
                  <AvInput id="contacts-file-upload-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="descriptionLabel" for="contacts-file-upload-description">
                  Description
                </Label>
                <AvField
                  id="contacts-file-upload-description"
                  type="text"
                  name="description"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="fileNameLabel" for="contacts-file-upload-fileName">
                  File Name
                </Label>
                <AvField
                  id="contacts-file-upload-fileName"
                  type="text"
                  name="fileName"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="periodFromLabel" for="contacts-file-upload-periodFrom">
                  Period From
                </Label>
                <AvField id="contacts-file-upload-periodFrom" type="date" className="form-control" name="periodFrom" />
              </AvGroup>
              <AvGroup>
                <Label id="periodToLabel" for="contacts-file-upload-periodTo">
                  Period To
                </Label>
                <AvField id="contacts-file-upload-periodTo" type="date" className="form-control" name="periodTo" />
              </AvGroup>
              <AvGroup>
                <Label id="contactsFileTypeIdLabel" for="contacts-file-upload-contactsFileTypeId">
                  Contacts File Type Id
                </Label>
                <AvField
                  id="contacts-file-upload-contactsFileTypeId"
                  type="string"
                  className="form-control"
                  name="contactsFileTypeId"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    number: { value: true, errorMessage: 'This field should be a number.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <AvGroup>
                  <Label id="dataFileLabel" for="dataFile">
                    Data File
                  </Label>
                  <br />
                  {dataFile ? (
                    <div>
                      {dataFileContentType ? <a onClick={openFile(dataFileContentType, dataFile)}>Open</a> : null}
                      <br />
                      <Row>
                        <Col md="11">
                          <span>
                            {dataFileContentType}, {byteSize(dataFile)}
                          </span>
                        </Col>
                        <Col md="1">
                          <Button color="danger" onClick={clearBlob('dataFile')}>
                            <FontAwesomeIcon icon="times-circle" />
                          </Button>
                        </Col>
                      </Row>
                    </div>
                  ) : null}
                  <input id="file_dataFile" type="file" onChange={onBlobChange(false, 'dataFile')} />
                  <AvInput
                    type="hidden"
                    name="dataFile"
                    value={dataFile}
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                    }}
                  />
                </AvGroup>
              </AvGroup>
              <AvGroup check>
                <Label id="uploadSuccessfulLabel">
                  <AvInput
                    id="contacts-file-upload-uploadSuccessful"
                    type="checkbox"
                    className="form-check-input"
                    name="uploadSuccessful"
                  />
                  Upload Successful
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="uploadProcessedLabel">
                  <AvInput id="contacts-file-upload-uploadProcessed" type="checkbox" className="form-check-input" name="uploadProcessed" />
                  Upload Processed
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="uploadTokenLabel" for="contacts-file-upload-uploadToken">
                  Upload Token
                </Label>
                <AvField id="contacts-file-upload-uploadToken" type="text" name="uploadToken" validate={{}} />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/contacts-file-upload" replace color="info">
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
  contactsFileUploadEntity: storeState.contactsFileUpload.entity,
  loading: storeState.contactsFileUpload.loading,
  updating: storeState.contactsFileUpload.updating,
  updateSuccess: storeState.contactsFileUpload.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(ContactsFileUploadUpdate);

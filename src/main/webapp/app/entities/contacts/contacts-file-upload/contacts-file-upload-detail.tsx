import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './contacts-file-upload.reducer';
import { IContactsFileUpload } from 'app/shared/model/contacts/contacts-file-upload.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IContactsFileUploadDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ContactsFileUploadDetail = (props: IContactsFileUploadDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { contactsFileUploadEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          ContactsFileUpload [<b>{contactsFileUploadEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{contactsFileUploadEntity.description}</dd>
          <dt>
            <span id="fileName">File Name</span>
          </dt>
          <dd>{contactsFileUploadEntity.fileName}</dd>
          <dt>
            <span id="periodFrom">Period From</span>
          </dt>
          <dd>
            {contactsFileUploadEntity.periodFrom ? (
              <TextFormat value={contactsFileUploadEntity.periodFrom} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="periodTo">Period To</span>
          </dt>
          <dd>
            {contactsFileUploadEntity.periodTo ? (
              <TextFormat value={contactsFileUploadEntity.periodTo} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="contactsFileTypeId">Contacts File Type Id</span>
          </dt>
          <dd>{contactsFileUploadEntity.contactsFileTypeId}</dd>
          <dt>
            <span id="dataFile">Data File</span>
          </dt>
          <dd>
            {contactsFileUploadEntity.dataFile ? (
              <div>
                {contactsFileUploadEntity.dataFileContentType ? (
                  <a onClick={openFile(contactsFileUploadEntity.dataFileContentType, contactsFileUploadEntity.dataFile)}>Open&nbsp;</a>
                ) : null}
                <span>
                  {contactsFileUploadEntity.dataFileContentType}, {byteSize(contactsFileUploadEntity.dataFile)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="uploadSuccessful">Upload Successful</span>
          </dt>
          <dd>{contactsFileUploadEntity.uploadSuccessful ? 'true' : 'false'}</dd>
          <dt>
            <span id="uploadProcessed">Upload Processed</span>
          </dt>
          <dd>{contactsFileUploadEntity.uploadProcessed ? 'true' : 'false'}</dd>
          <dt>
            <span id="uploadToken">Upload Token</span>
          </dt>
          <dd>{contactsFileUploadEntity.uploadToken}</dd>
        </dl>
        <Button tag={Link} to="/contacts-file-upload" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/contacts-file-upload/${contactsFileUploadEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ contactsFileUpload }: IRootState) => ({
  contactsFileUploadEntity: contactsFileUpload.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ContactsFileUploadDetail);

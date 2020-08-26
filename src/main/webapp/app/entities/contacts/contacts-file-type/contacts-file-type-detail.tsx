import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './contacts-file-type.reducer';
import { IContactsFileType } from 'app/shared/model/contacts/contacts-file-type.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IContactsFileTypeDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ContactsFileTypeDetail = (props: IContactsFileTypeDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { contactsFileTypeEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          ContactsFileType [<b>{contactsFileTypeEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="contactsFileTypeName">Contacts File Type Name</span>
          </dt>
          <dd>{contactsFileTypeEntity.contactsFileTypeName}</dd>
          <dt>
            <span id="contactsFileMediumType">Contacts File Medium Type</span>
          </dt>
          <dd>{contactsFileTypeEntity.contactsFileMediumType}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{contactsFileTypeEntity.description}</dd>
          <dt>
            <span id="fileTemplate">File Template</span>
          </dt>
          <dd>
            {contactsFileTypeEntity.fileTemplate ? (
              <div>
                {contactsFileTypeEntity.fileTemplateContentType ? (
                  <a onClick={openFile(contactsFileTypeEntity.fileTemplateContentType, contactsFileTypeEntity.fileTemplate)}>Open&nbsp;</a>
                ) : null}
                <span>
                  {contactsFileTypeEntity.fileTemplateContentType}, {byteSize(contactsFileTypeEntity.fileTemplate)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="contactsfileType">Contactsfile Type</span>
          </dt>
          <dd>{contactsFileTypeEntity.contactsfileType}</dd>
        </dl>
        <Button tag={Link} to="/contacts-file-type" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/contacts-file-type/${contactsFileTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ contactsFileType }: IRootState) => ({
  contactsFileTypeEntity: contactsFileType.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ContactsFileTypeDetail);

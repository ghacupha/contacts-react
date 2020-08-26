import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './contacts-message-token.reducer';
import { IContactsMessageToken } from 'app/shared/model/contacts/contacts-message-token.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IContactsMessageTokenUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ContactsMessageTokenUpdate = (props: IContactsMessageTokenUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { contactsMessageTokenEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/contacts-message-token' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...contactsMessageTokenEntity,
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
          <h2 id="contactsApp.contactsContactsMessageToken.home.createOrEditLabel">Create or edit a ContactsMessageToken</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : contactsMessageTokenEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="contacts-message-token-id">ID</Label>
                  <AvInput id="contacts-message-token-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="descriptionLabel" for="contacts-message-token-description">
                  Description
                </Label>
                <AvField id="contacts-message-token-description" type="text" name="description" />
              </AvGroup>
              <AvGroup>
                <Label id="timeSentLabel" for="contacts-message-token-timeSent">
                  Time Sent
                </Label>
                <AvField
                  id="contacts-message-token-timeSent"
                  type="string"
                  className="form-control"
                  name="timeSent"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    number: { value: true, errorMessage: 'This field should be a number.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="tokenValueLabel" for="contacts-message-token-tokenValue">
                  Token Value
                </Label>
                <AvField
                  id="contacts-message-token-tokenValue"
                  type="text"
                  name="tokenValue"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="receivedLabel">
                  <AvInput id="contacts-message-token-received" type="checkbox" className="form-check-input" name="received" />
                  Received
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="actionedLabel">
                  <AvInput id="contacts-message-token-actioned" type="checkbox" className="form-check-input" name="actioned" />
                  Actioned
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="contentFullyEnqueuedLabel">
                  <AvInput
                    id="contacts-message-token-contentFullyEnqueued"
                    type="checkbox"
                    className="form-check-input"
                    name="contentFullyEnqueued"
                  />
                  Content Fully Enqueued
                </Label>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/contacts-message-token" replace color="info">
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
  contactsMessageTokenEntity: storeState.contactsMessageToken.entity,
  loading: storeState.contactsMessageToken.loading,
  updating: storeState.contactsMessageToken.updating,
  updateSuccess: storeState.contactsMessageToken.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ContactsMessageTokenUpdate);

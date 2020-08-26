import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './contacts-message-token.reducer';
import { IContactsMessageToken } from 'app/shared/model/contacts/contacts-message-token.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IContactsMessageTokenDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ContactsMessageTokenDetail = (props: IContactsMessageTokenDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { contactsMessageTokenEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          ContactsMessageToken [<b>{contactsMessageTokenEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{contactsMessageTokenEntity.description}</dd>
          <dt>
            <span id="timeSent">Time Sent</span>
          </dt>
          <dd>{contactsMessageTokenEntity.timeSent}</dd>
          <dt>
            <span id="tokenValue">Token Value</span>
          </dt>
          <dd>{contactsMessageTokenEntity.tokenValue}</dd>
          <dt>
            <span id="received">Received</span>
          </dt>
          <dd>{contactsMessageTokenEntity.received ? 'true' : 'false'}</dd>
          <dt>
            <span id="actioned">Actioned</span>
          </dt>
          <dd>{contactsMessageTokenEntity.actioned ? 'true' : 'false'}</dd>
          <dt>
            <span id="contentFullyEnqueued">Content Fully Enqueued</span>
          </dt>
          <dd>{contactsMessageTokenEntity.contentFullyEnqueued ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/contacts-message-token" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/contacts-message-token/${contactsMessageTokenEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ contactsMessageToken }: IRootState) => ({
  contactsMessageTokenEntity: contactsMessageToken.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ContactsMessageTokenDetail);

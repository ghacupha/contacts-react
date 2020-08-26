import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './currency-table.reducer';
import { ICurrencyTable } from 'app/shared/model/contacts/currency-table.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICurrencyTableDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CurrencyTableDetail = (props: ICurrencyTableDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { currencyTableEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          CurrencyTable [<b>{currencyTableEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="currencyCode">Currency Code</span>
          </dt>
          <dd>{currencyTableEntity.currencyCode}</dd>
          <dt>
            <span id="locality">Locality</span>
          </dt>
          <dd>{currencyTableEntity.locality}</dd>
          <dt>
            <span id="currencyName">Currency Name</span>
          </dt>
          <dd>{currencyTableEntity.currencyName}</dd>
          <dt>
            <span id="country">Country</span>
          </dt>
          <dd>{currencyTableEntity.country}</dd>
        </dl>
        <Button tag={Link} to="/currency-table" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/currency-table/${currencyTableEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ currencyTable }: IRootState) => ({
  currencyTableEntity: currencyTable.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CurrencyTableDetail);

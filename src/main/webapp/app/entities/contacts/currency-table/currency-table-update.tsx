import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './currency-table.reducer';
import { ICurrencyTable } from 'app/shared/model/contacts/currency-table.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICurrencyTableUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CurrencyTableUpdate = (props: ICurrencyTableUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { currencyTableEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/currency-table' + props.location.search);
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
        ...currencyTableEntity,
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
          <h2 id="contactsApp.contactsCurrencyTable.home.createOrEditLabel">Create or edit a CurrencyTable</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : currencyTableEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="currency-table-id">ID</Label>
                  <AvInput id="currency-table-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="currencyCodeLabel" for="currency-table-currencyCode">
                  Currency Code
                </Label>
                <AvField
                  id="currency-table-currencyCode"
                  type="text"
                  name="currencyCode"
                  validate={{
                    minLength: { value: 3, errorMessage: 'This field is required to be at least 3 characters.' },
                    maxLength: { value: 3, errorMessage: 'This field cannot be longer than 3 characters.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="localityLabel" for="currency-table-locality">
                  Locality
                </Label>
                <AvInput
                  id="currency-table-locality"
                  type="select"
                  className="form-control"
                  name="locality"
                  value={(!isNew && currencyTableEntity.locality) || 'LOCAL'}
                >
                  <option value="LOCAL">LOCAL</option>
                  <option value="FOREIGN">FOREIGN</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="currencyNameLabel" for="currency-table-currencyName">
                  Currency Name
                </Label>
                <AvField id="currency-table-currencyName" type="text" name="currencyName" />
              </AvGroup>
              <AvGroup>
                <Label id="countryLabel" for="currency-table-country">
                  Country
                </Label>
                <AvField id="currency-table-country" type="text" name="country" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/currency-table" replace color="info">
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
  currencyTableEntity: storeState.currencyTable.entity,
  loading: storeState.currencyTable.loading,
  updating: storeState.currencyTable.updating,
  updateSuccess: storeState.currencyTable.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CurrencyTableUpdate);

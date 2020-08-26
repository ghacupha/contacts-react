import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CurrencyTable from './currency-table';
import CurrencyTableDetail from './currency-table-detail';
import CurrencyTableUpdate from './currency-table-update';
import CurrencyTableDeleteDialog from './currency-table-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CurrencyTableUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CurrencyTableUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CurrencyTableDetail} />
      <ErrorBoundaryRoute path={match.url} component={CurrencyTable} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CurrencyTableDeleteDialog} />
  </>
);

export default Routes;

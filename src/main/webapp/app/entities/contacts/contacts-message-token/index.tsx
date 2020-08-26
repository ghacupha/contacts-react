import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ContactsMessageToken from './contacts-message-token';
import ContactsMessageTokenDetail from './contacts-message-token-detail';
import ContactsMessageTokenUpdate from './contacts-message-token-update';
import ContactsMessageTokenDeleteDialog from './contacts-message-token-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ContactsMessageTokenUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ContactsMessageTokenUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ContactsMessageTokenDetail} />
      <ErrorBoundaryRoute path={match.url} component={ContactsMessageToken} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ContactsMessageTokenDeleteDialog} />
  </>
);

export default Routes;

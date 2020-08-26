import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ContactsFileType from './contacts-file-type';
import ContactsFileTypeDetail from './contacts-file-type-detail';
import ContactsFileTypeUpdate from './contacts-file-type-update';
import ContactsFileTypeDeleteDialog from './contacts-file-type-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ContactsFileTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ContactsFileTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ContactsFileTypeDetail} />
      <ErrorBoundaryRoute path={match.url} component={ContactsFileType} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ContactsFileTypeDeleteDialog} />
  </>
);

export default Routes;

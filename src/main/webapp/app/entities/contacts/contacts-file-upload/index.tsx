import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ContactsFileUpload from './contacts-file-upload';
import ContactsFileUploadDetail from './contacts-file-upload-detail';
import ContactsFileUploadUpdate from './contacts-file-upload-update';
import ContactsFileUploadDeleteDialog from './contacts-file-upload-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ContactsFileUploadUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ContactsFileUploadUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ContactsFileUploadDetail} />
      <ErrorBoundaryRoute path={match.url} component={ContactsFileUpload} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ContactsFileUploadDeleteDialog} />
  </>
);

export default Routes;

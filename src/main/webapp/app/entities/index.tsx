import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Contact from './contact';
import ContactsFileType from './contacts/contacts-file-type';
import ContactsFileUpload from './contacts/contacts-file-upload';
import ContactsMessageToken from './contacts/contacts-message-token';
import CurrencyTable from './contacts/currency-table';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}contact`} component={Contact} />
      <ErrorBoundaryRoute path={`${match.url}contacts-file-type`} component={ContactsFileType} />
      <ErrorBoundaryRoute path={`${match.url}contacts-file-upload`} component={ContactsFileUpload} />
      <ErrorBoundaryRoute path={`${match.url}contacts-message-token`} component={ContactsMessageToken} />
      <ErrorBoundaryRoute path={`${match.url}currency-table`} component={CurrencyTable} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;

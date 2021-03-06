import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
import sessions, { SessionsState } from 'app/modules/account/sessions/sessions.reducer';
// prettier-ignore
import contact, {
  ContactState
} from 'app/entities/contact/contact.reducer';
// prettier-ignore
import contactsFileType, {
  ContactsFileTypeState
} from 'app/entities/contacts/contacts-file-type/contacts-file-type.reducer';
// prettier-ignore
import contactsFileUpload, {
  ContactsFileUploadState
} from 'app/entities/contacts/contacts-file-upload/contacts-file-upload.reducer';
// prettier-ignore
import contactsMessageToken, {
  ContactsMessageTokenState
} from 'app/entities/contacts/contacts-message-token/contacts-message-token.reducer';
// prettier-ignore
import currencyTable, {
  CurrencyTableState
} from 'app/entities/contacts/currency-table/currency-table.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly sessions: SessionsState;
  readonly contact: ContactState;
  readonly contactsFileType: ContactsFileTypeState;
  readonly contactsFileUpload: ContactsFileUploadState;
  readonly contactsMessageToken: ContactsMessageTokenState;
  readonly currencyTable: CurrencyTableState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  sessions,
  contact,
  contactsFileType,
  contactsFileUpload,
  contactsMessageToken,
  currencyTable,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
});

export default rootReducer;

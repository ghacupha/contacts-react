import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import ContactsMessageTokenComponentsPage from './contacts-message-token.page-object';
import ContactsMessageTokenUpdatePage from './contacts-message-token-update.page-object';
import {
  waitUntilDisplayed,
  waitUntilAnyDisplayed,
  click,
  getRecordsCount,
  waitUntilHidden,
  waitUntilCount,
  isVisible,
} from '../../../util/utils';

const expect = chai.expect;

describe('ContactsMessageToken e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let contactsMessageTokenComponentsPage: ContactsMessageTokenComponentsPage;
  let contactsMessageTokenUpdatePage: ContactsMessageTokenUpdatePage;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.waitUntilDisplayed();

    await signInPage.username.sendKeys('admin');
    await signInPage.password.sendKeys('admin');
    await signInPage.loginButton.click();
    await signInPage.waitUntilHidden();
    await waitUntilDisplayed(navBarPage.entityMenu);
    await waitUntilDisplayed(navBarPage.adminMenu);
    await waitUntilDisplayed(navBarPage.accountMenu);
  });

  beforeEach(async () => {
    await browser.get('/');
    await waitUntilDisplayed(navBarPage.entityMenu);
    contactsMessageTokenComponentsPage = new ContactsMessageTokenComponentsPage();
    contactsMessageTokenComponentsPage = await contactsMessageTokenComponentsPage.goToPage(navBarPage);
  });

  it('should load ContactsMessageTokens', async () => {
    expect(await contactsMessageTokenComponentsPage.title.getText()).to.match(/Contacts Message Tokens/);
    expect(await contactsMessageTokenComponentsPage.createButton.isEnabled()).to.be.true;
  });

  it('should create and delete ContactsMessageTokens', async () => {
    const beforeRecordsCount = (await isVisible(contactsMessageTokenComponentsPage.noRecords))
      ? 0
      : await getRecordsCount(contactsMessageTokenComponentsPage.table);
    contactsMessageTokenUpdatePage = await contactsMessageTokenComponentsPage.goToCreateContactsMessageToken();
    await contactsMessageTokenUpdatePage.enterData();

    expect(await contactsMessageTokenComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilDisplayed(contactsMessageTokenComponentsPage.table);
    await waitUntilCount(contactsMessageTokenComponentsPage.records, beforeRecordsCount + 1);
    expect(await contactsMessageTokenComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);

    await contactsMessageTokenComponentsPage.deleteContactsMessageToken();
    if (beforeRecordsCount !== 0) {
      await waitUntilCount(contactsMessageTokenComponentsPage.records, beforeRecordsCount);
      expect(await contactsMessageTokenComponentsPage.records.count()).to.eq(beforeRecordsCount);
    } else {
      await waitUntilDisplayed(contactsMessageTokenComponentsPage.noRecords);
    }
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

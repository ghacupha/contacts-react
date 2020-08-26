import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import ContactsFileTypeComponentsPage from './contacts-file-type.page-object';
import ContactsFileTypeUpdatePage from './contacts-file-type-update.page-object';
import {
  waitUntilDisplayed,
  waitUntilAnyDisplayed,
  click,
  getRecordsCount,
  waitUntilHidden,
  waitUntilCount,
  isVisible,
} from '../../../util/utils';
import path from 'path';

const expect = chai.expect;

describe('ContactsFileType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let contactsFileTypeComponentsPage: ContactsFileTypeComponentsPage;
  let contactsFileTypeUpdatePage: ContactsFileTypeUpdatePage;

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
    contactsFileTypeComponentsPage = new ContactsFileTypeComponentsPage();
    contactsFileTypeComponentsPage = await contactsFileTypeComponentsPage.goToPage(navBarPage);
  });

  it('should load ContactsFileTypes', async () => {
    expect(await contactsFileTypeComponentsPage.title.getText()).to.match(/Contacts File Types/);
    expect(await contactsFileTypeComponentsPage.createButton.isEnabled()).to.be.true;
  });

  it('should create and delete ContactsFileTypes', async () => {
    const beforeRecordsCount = (await isVisible(contactsFileTypeComponentsPage.noRecords))
      ? 0
      : await getRecordsCount(contactsFileTypeComponentsPage.table);
    contactsFileTypeUpdatePage = await contactsFileTypeComponentsPage.goToCreateContactsFileType();
    await contactsFileTypeUpdatePage.enterData();

    expect(await contactsFileTypeComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilDisplayed(contactsFileTypeComponentsPage.table);
    await waitUntilCount(contactsFileTypeComponentsPage.records, beforeRecordsCount + 1);
    expect(await contactsFileTypeComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);

    await contactsFileTypeComponentsPage.deleteContactsFileType();
    if (beforeRecordsCount !== 0) {
      await waitUntilCount(contactsFileTypeComponentsPage.records, beforeRecordsCount);
      expect(await contactsFileTypeComponentsPage.records.count()).to.eq(beforeRecordsCount);
    } else {
      await waitUntilDisplayed(contactsFileTypeComponentsPage.noRecords);
    }
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

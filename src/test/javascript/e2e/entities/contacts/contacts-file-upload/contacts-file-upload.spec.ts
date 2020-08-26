import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import ContactsFileUploadComponentsPage from './contacts-file-upload.page-object';
import ContactsFileUploadUpdatePage from './contacts-file-upload-update.page-object';
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

describe('ContactsFileUpload e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let contactsFileUploadComponentsPage: ContactsFileUploadComponentsPage;
  let contactsFileUploadUpdatePage: ContactsFileUploadUpdatePage;

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
    contactsFileUploadComponentsPage = new ContactsFileUploadComponentsPage();
    contactsFileUploadComponentsPage = await contactsFileUploadComponentsPage.goToPage(navBarPage);
  });

  it('should load ContactsFileUploads', async () => {
    expect(await contactsFileUploadComponentsPage.title.getText()).to.match(/Contacts File Uploads/);
    expect(await contactsFileUploadComponentsPage.createButton.isEnabled()).to.be.true;
  });

  it('should create and delete ContactsFileUploads', async () => {
    const beforeRecordsCount = (await isVisible(contactsFileUploadComponentsPage.noRecords))
      ? 0
      : await getRecordsCount(contactsFileUploadComponentsPage.table);
    contactsFileUploadUpdatePage = await contactsFileUploadComponentsPage.goToCreateContactsFileUpload();
    await contactsFileUploadUpdatePage.enterData();

    expect(await contactsFileUploadComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilDisplayed(contactsFileUploadComponentsPage.table);
    await waitUntilCount(contactsFileUploadComponentsPage.records, beforeRecordsCount + 1);
    expect(await contactsFileUploadComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);

    await contactsFileUploadComponentsPage.deleteContactsFileUpload();
    if (beforeRecordsCount !== 0) {
      await waitUntilCount(contactsFileUploadComponentsPage.records, beforeRecordsCount);
      expect(await contactsFileUploadComponentsPage.records.count()).to.eq(beforeRecordsCount);
    } else {
      await waitUntilDisplayed(contactsFileUploadComponentsPage.noRecords);
    }
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

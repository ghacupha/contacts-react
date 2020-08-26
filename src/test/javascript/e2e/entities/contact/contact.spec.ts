import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import ContactComponentsPage from './contact.page-object';
import ContactUpdatePage from './contact-update.page-object';
import {
  waitUntilDisplayed,
  waitUntilAnyDisplayed,
  click,
  getRecordsCount,
  waitUntilHidden,
  waitUntilCount,
  isVisible,
} from '../../util/utils';

const expect = chai.expect;

describe('Contact e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let contactComponentsPage: ContactComponentsPage;
  let contactUpdatePage: ContactUpdatePage;

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
    contactComponentsPage = new ContactComponentsPage();
    contactComponentsPage = await contactComponentsPage.goToPage(navBarPage);
  });

  it('should load Contacts', async () => {
    expect(await contactComponentsPage.title.getText()).to.match(/Contacts/);
    expect(await contactComponentsPage.createButton.isEnabled()).to.be.true;
  });

  it('should create and delete Contacts', async () => {
    const beforeRecordsCount = (await isVisible(contactComponentsPage.noRecords)) ? 0 : await getRecordsCount(contactComponentsPage.table);
    contactUpdatePage = await contactComponentsPage.goToCreateContact();
    await contactUpdatePage.enterData();

    expect(await contactComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilDisplayed(contactComponentsPage.table);
    await waitUntilCount(contactComponentsPage.records, beforeRecordsCount + 1);
    expect(await contactComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);

    await contactComponentsPage.deleteContact();
    if (beforeRecordsCount !== 0) {
      await waitUntilCount(contactComponentsPage.records, beforeRecordsCount);
      expect(await contactComponentsPage.records.count()).to.eq(beforeRecordsCount);
    } else {
      await waitUntilDisplayed(contactComponentsPage.noRecords);
    }
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

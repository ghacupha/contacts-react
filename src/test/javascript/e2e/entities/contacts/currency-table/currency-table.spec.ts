import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import CurrencyTableComponentsPage from './currency-table.page-object';
import CurrencyTableUpdatePage from './currency-table-update.page-object';
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

describe('CurrencyTable e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let currencyTableComponentsPage: CurrencyTableComponentsPage;
  let currencyTableUpdatePage: CurrencyTableUpdatePage;

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
    currencyTableComponentsPage = new CurrencyTableComponentsPage();
    currencyTableComponentsPage = await currencyTableComponentsPage.goToPage(navBarPage);
  });

  it('should load CurrencyTables', async () => {
    expect(await currencyTableComponentsPage.title.getText()).to.match(/Currency Tables/);
    expect(await currencyTableComponentsPage.createButton.isEnabled()).to.be.true;
  });

  it('should create and delete CurrencyTables', async () => {
    const beforeRecordsCount = (await isVisible(currencyTableComponentsPage.noRecords))
      ? 0
      : await getRecordsCount(currencyTableComponentsPage.table);
    currencyTableUpdatePage = await currencyTableComponentsPage.goToCreateCurrencyTable();
    await currencyTableUpdatePage.enterData();

    expect(await currencyTableComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilDisplayed(currencyTableComponentsPage.table);
    await waitUntilCount(currencyTableComponentsPage.records, beforeRecordsCount + 1);
    expect(await currencyTableComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);

    await currencyTableComponentsPage.deleteCurrencyTable();
    if (beforeRecordsCount !== 0) {
      await waitUntilCount(currencyTableComponentsPage.records, beforeRecordsCount);
      expect(await currencyTableComponentsPage.records.count()).to.eq(beforeRecordsCount);
    } else {
      await waitUntilDisplayed(currencyTableComponentsPage.noRecords);
    }
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

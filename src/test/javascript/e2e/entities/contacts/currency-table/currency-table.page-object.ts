import { element, by, ElementFinder, ElementArrayFinder } from 'protractor';

import { waitUntilAnyDisplayed, waitUntilDisplayed, click, waitUntilHidden, isVisible } from '../../../util/utils';

import NavBarPage from './../../../page-objects/navbar-page';

import CurrencyTableUpdatePage from './currency-table-update.page-object';

const expect = chai.expect;
export class CurrencyTableDeleteDialog {
  deleteModal = element(by.className('modal'));
  private dialogTitle: ElementFinder = element(by.id('contactsApp.contactsCurrencyTable.delete.question'));
  private confirmButton = element(by.id('jhi-confirm-delete-currencyTable'));

  getDialogTitle() {
    return this.dialogTitle;
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}

export default class CurrencyTableComponentsPage {
  createButton: ElementFinder = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('div table .btn-danger'));
  title: ElementFinder = element(by.id('currency-table-heading'));
  noRecords: ElementFinder = element(by.css('#app-view-container .table-responsive div.alert.alert-warning'));
  table: ElementFinder = element(by.css('#app-view-container div.table-responsive > table'));

  records: ElementArrayFinder = this.table.all(by.css('tbody tr'));

  getDetailsButton(record: ElementFinder) {
    return record.element(by.css('a.btn.btn-info.btn-sm'));
  }

  getEditButton(record: ElementFinder) {
    return record.element(by.css('a.btn.btn-primary.btn-sm'));
  }

  getDeleteButton(record: ElementFinder) {
    return record.element(by.css('a.btn.btn-danger.btn-sm'));
  }

  async goToPage(navBarPage: NavBarPage) {
    await navBarPage.getEntityPage('currency-table');
    await waitUntilAnyDisplayed([this.noRecords, this.table]);
    return this;
  }

  async goToCreateCurrencyTable() {
    await this.createButton.click();
    return new CurrencyTableUpdatePage();
  }

  async deleteCurrencyTable() {
    const deleteButton = this.getDeleteButton(this.records.last());
    await click(deleteButton);

    const currencyTableDeleteDialog = new CurrencyTableDeleteDialog();
    await waitUntilDisplayed(currencyTableDeleteDialog.deleteModal);
    expect(await currencyTableDeleteDialog.getDialogTitle().getAttribute('id')).to.match(
      /contactsApp.contactsCurrencyTable.delete.question/
    );
    await currencyTableDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(currencyTableDeleteDialog.deleteModal);

    expect(await isVisible(currencyTableDeleteDialog.deleteModal)).to.be.false;
  }
}

import { element, by, ElementFinder, ElementArrayFinder } from 'protractor';

import { waitUntilAnyDisplayed, waitUntilDisplayed, click, waitUntilHidden, isVisible } from '../../../util/utils';

import NavBarPage from './../../../page-objects/navbar-page';

import ContactsMessageTokenUpdatePage from './contacts-message-token-update.page-object';

const expect = chai.expect;
export class ContactsMessageTokenDeleteDialog {
  deleteModal = element(by.className('modal'));
  private dialogTitle: ElementFinder = element(by.id('contactsApp.contactsContactsMessageToken.delete.question'));
  private confirmButton = element(by.id('jhi-confirm-delete-contactsMessageToken'));

  getDialogTitle() {
    return this.dialogTitle;
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}

export default class ContactsMessageTokenComponentsPage {
  createButton: ElementFinder = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('div table .btn-danger'));
  title: ElementFinder = element(by.id('contacts-message-token-heading'));
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
    await navBarPage.getEntityPage('contacts-message-token');
    await waitUntilAnyDisplayed([this.noRecords, this.table]);
    return this;
  }

  async goToCreateContactsMessageToken() {
    await this.createButton.click();
    return new ContactsMessageTokenUpdatePage();
  }

  async deleteContactsMessageToken() {
    const deleteButton = this.getDeleteButton(this.records.last());
    await click(deleteButton);

    const contactsMessageTokenDeleteDialog = new ContactsMessageTokenDeleteDialog();
    await waitUntilDisplayed(contactsMessageTokenDeleteDialog.deleteModal);
    expect(await contactsMessageTokenDeleteDialog.getDialogTitle().getAttribute('id')).to.match(
      /contactsApp.contactsContactsMessageToken.delete.question/
    );
    await contactsMessageTokenDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(contactsMessageTokenDeleteDialog.deleteModal);

    expect(await isVisible(contactsMessageTokenDeleteDialog.deleteModal)).to.be.false;
  }
}

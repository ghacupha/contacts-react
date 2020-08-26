import { element, by, ElementFinder, ElementArrayFinder } from 'protractor';

import { waitUntilAnyDisplayed, waitUntilDisplayed, click, waitUntilHidden, isVisible } from '../../../util/utils';

import NavBarPage from './../../../page-objects/navbar-page';

import ContactsFileTypeUpdatePage from './contacts-file-type-update.page-object';

const expect = chai.expect;
export class ContactsFileTypeDeleteDialog {
  deleteModal = element(by.className('modal'));
  private dialogTitle: ElementFinder = element(by.id('contactsApp.contactsContactsFileType.delete.question'));
  private confirmButton = element(by.id('jhi-confirm-delete-contactsFileType'));

  getDialogTitle() {
    return this.dialogTitle;
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}

export default class ContactsFileTypeComponentsPage {
  createButton: ElementFinder = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('div table .btn-danger'));
  title: ElementFinder = element(by.id('contacts-file-type-heading'));
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
    await navBarPage.getEntityPage('contacts-file-type');
    await waitUntilAnyDisplayed([this.noRecords, this.table]);
    return this;
  }

  async goToCreateContactsFileType() {
    await this.createButton.click();
    return new ContactsFileTypeUpdatePage();
  }

  async deleteContactsFileType() {
    const deleteButton = this.getDeleteButton(this.records.last());
    await click(deleteButton);

    const contactsFileTypeDeleteDialog = new ContactsFileTypeDeleteDialog();
    await waitUntilDisplayed(contactsFileTypeDeleteDialog.deleteModal);
    expect(await contactsFileTypeDeleteDialog.getDialogTitle().getAttribute('id')).to.match(
      /contactsApp.contactsContactsFileType.delete.question/
    );
    await contactsFileTypeDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(contactsFileTypeDeleteDialog.deleteModal);

    expect(await isVisible(contactsFileTypeDeleteDialog.deleteModal)).to.be.false;
  }
}

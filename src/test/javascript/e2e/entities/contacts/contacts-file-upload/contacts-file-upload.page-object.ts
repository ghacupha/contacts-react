import { element, by, ElementFinder, ElementArrayFinder } from 'protractor';

import { waitUntilAnyDisplayed, waitUntilDisplayed, click, waitUntilHidden, isVisible } from '../../../util/utils';

import NavBarPage from './../../../page-objects/navbar-page';

import ContactsFileUploadUpdatePage from './contacts-file-upload-update.page-object';

const expect = chai.expect;
export class ContactsFileUploadDeleteDialog {
  deleteModal = element(by.className('modal'));
  private dialogTitle: ElementFinder = element(by.id('contactsApp.contactsContactsFileUpload.delete.question'));
  private confirmButton = element(by.id('jhi-confirm-delete-contactsFileUpload'));

  getDialogTitle() {
    return this.dialogTitle;
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}

export default class ContactsFileUploadComponentsPage {
  createButton: ElementFinder = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('div table .btn-danger'));
  title: ElementFinder = element(by.id('contacts-file-upload-heading'));
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
    await navBarPage.getEntityPage('contacts-file-upload');
    await waitUntilAnyDisplayed([this.noRecords, this.table]);
    return this;
  }

  async goToCreateContactsFileUpload() {
    await this.createButton.click();
    return new ContactsFileUploadUpdatePage();
  }

  async deleteContactsFileUpload() {
    const deleteButton = this.getDeleteButton(this.records.last());
    await click(deleteButton);

    const contactsFileUploadDeleteDialog = new ContactsFileUploadDeleteDialog();
    await waitUntilDisplayed(contactsFileUploadDeleteDialog.deleteModal);
    expect(await contactsFileUploadDeleteDialog.getDialogTitle().getAttribute('id')).to.match(
      /contactsApp.contactsContactsFileUpload.delete.question/
    );
    await contactsFileUploadDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(contactsFileUploadDeleteDialog.deleteModal);

    expect(await isVisible(contactsFileUploadDeleteDialog.deleteModal)).to.be.false;
  }
}

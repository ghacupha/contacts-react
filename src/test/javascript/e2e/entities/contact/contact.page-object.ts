import { element, by, ElementFinder, ElementArrayFinder } from 'protractor';

import { waitUntilAnyDisplayed, waitUntilDisplayed, click, waitUntilHidden, isVisible } from '../../util/utils';

import NavBarPage from './../../page-objects/navbar-page';

import ContactUpdatePage from './contact-update.page-object';

const expect = chai.expect;
export class ContactDeleteDialog {
  deleteModal = element(by.className('modal'));
  private dialogTitle: ElementFinder = element(by.id('contactsApp.contact.delete.question'));
  private confirmButton = element(by.id('jhi-confirm-delete-contact'));

  getDialogTitle() {
    return this.dialogTitle;
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}

export default class ContactComponentsPage {
  createButton: ElementFinder = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('div table .btn-danger'));
  title: ElementFinder = element(by.id('contact-heading'));
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
    await navBarPage.getEntityPage('contact');
    await waitUntilAnyDisplayed([this.noRecords, this.table]);
    return this;
  }

  async goToCreateContact() {
    await this.createButton.click();
    return new ContactUpdatePage();
  }

  async deleteContact() {
    const deleteButton = this.getDeleteButton(this.records.last());
    await click(deleteButton);

    const contactDeleteDialog = new ContactDeleteDialog();
    await waitUntilDisplayed(contactDeleteDialog.deleteModal);
    expect(await contactDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/contactsApp.contact.delete.question/);
    await contactDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(contactDeleteDialog.deleteModal);

    expect(await isVisible(contactDeleteDialog.deleteModal)).to.be.false;
  }
}

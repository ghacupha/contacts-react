import { element, by, ElementFinder } from 'protractor';
import { waitUntilDisplayed, waitUntilHidden, isVisible } from '../../../util/utils';

import path from 'path';

const expect = chai.expect;

const fileToUpload = '../../../../../../../src/main/webapp/content/images/logo-jhipster.png';
const absolutePath = path.resolve(__dirname, fileToUpload);
export default class ContactsFileTypeUpdatePage {
  pageTitle: ElementFinder = element(by.id('contactsApp.contactsContactsFileType.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  contactsFileTypeNameInput: ElementFinder = element(by.css('input#contacts-file-type-contactsFileTypeName'));
  contactsFileMediumTypeSelect: ElementFinder = element(by.css('select#contacts-file-type-contactsFileMediumType'));
  descriptionInput: ElementFinder = element(by.css('input#contacts-file-type-description'));
  fileTemplateInput: ElementFinder = element(by.css('input#file_fileTemplate'));
  contactsfileTypeSelect: ElementFinder = element(by.css('select#contacts-file-type-contactsfileType'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setContactsFileTypeNameInput(contactsFileTypeName) {
    await this.contactsFileTypeNameInput.sendKeys(contactsFileTypeName);
  }

  async getContactsFileTypeNameInput() {
    return this.contactsFileTypeNameInput.getAttribute('value');
  }

  async setContactsFileMediumTypeSelect(contactsFileMediumType) {
    await this.contactsFileMediumTypeSelect.sendKeys(contactsFileMediumType);
  }

  async getContactsFileMediumTypeSelect() {
    return this.contactsFileMediumTypeSelect.element(by.css('option:checked')).getText();
  }

  async contactsFileMediumTypeSelectLastOption() {
    await this.contactsFileMediumTypeSelect.all(by.tagName('option')).last().click();
  }
  async setDescriptionInput(description) {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput() {
    return this.descriptionInput.getAttribute('value');
  }

  async setFileTemplateInput(fileTemplate) {
    await this.fileTemplateInput.sendKeys(fileTemplate);
  }

  async getFileTemplateInput() {
    return this.fileTemplateInput.getAttribute('value');
  }

  async setContactsfileTypeSelect(contactsfileType) {
    await this.contactsfileTypeSelect.sendKeys(contactsfileType);
  }

  async getContactsfileTypeSelect() {
    return this.contactsfileTypeSelect.element(by.css('option:checked')).getText();
  }

  async contactsfileTypeSelectLastOption() {
    await this.contactsfileTypeSelect.all(by.tagName('option')).last().click();
  }
  async save() {
    await this.saveButton.click();
  }

  async cancel() {
    await this.cancelButton.click();
  }

  getSaveButton() {
    return this.saveButton;
  }

  async enterData() {
    await waitUntilDisplayed(this.saveButton);
    await this.setContactsFileTypeNameInput('contactsFileTypeName');
    expect(await this.getContactsFileTypeNameInput()).to.match(/contactsFileTypeName/);
    await waitUntilDisplayed(this.saveButton);
    await this.contactsFileMediumTypeSelectLastOption();
    await waitUntilDisplayed(this.saveButton);
    await this.setDescriptionInput('description');
    expect(await this.getDescriptionInput()).to.match(/description/);
    await waitUntilDisplayed(this.saveButton);
    await this.setFileTemplateInput(absolutePath);
    await waitUntilDisplayed(this.saveButton);
    await this.contactsfileTypeSelectLastOption();
    await this.save();
    await waitUntilHidden(this.saveButton);
    expect(await isVisible(this.saveButton)).to.be.false;
  }
}

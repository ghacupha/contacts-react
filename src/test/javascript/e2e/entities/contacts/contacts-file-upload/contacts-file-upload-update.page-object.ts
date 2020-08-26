import { element, by, ElementFinder } from 'protractor';
import { waitUntilDisplayed, waitUntilHidden, isVisible } from '../../../util/utils';

import path from 'path';

const expect = chai.expect;

const fileToUpload = '../../../../../../../src/main/webapp/content/images/logo-jhipster.png';
const absolutePath = path.resolve(__dirname, fileToUpload);
export default class ContactsFileUploadUpdatePage {
  pageTitle: ElementFinder = element(by.id('contactsApp.contactsContactsFileUpload.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  descriptionInput: ElementFinder = element(by.css('input#contacts-file-upload-description'));
  fileNameInput: ElementFinder = element(by.css('input#contacts-file-upload-fileName'));
  periodFromInput: ElementFinder = element(by.css('input#contacts-file-upload-periodFrom'));
  periodToInput: ElementFinder = element(by.css('input#contacts-file-upload-periodTo'));
  contactsFileTypeIdInput: ElementFinder = element(by.css('input#contacts-file-upload-contactsFileTypeId'));
  dataFileInput: ElementFinder = element(by.css('input#file_dataFile'));
  uploadSuccessfulInput: ElementFinder = element(by.css('input#contacts-file-upload-uploadSuccessful'));
  uploadProcessedInput: ElementFinder = element(by.css('input#contacts-file-upload-uploadProcessed'));
  uploadTokenInput: ElementFinder = element(by.css('input#contacts-file-upload-uploadToken'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setDescriptionInput(description) {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput() {
    return this.descriptionInput.getAttribute('value');
  }

  async setFileNameInput(fileName) {
    await this.fileNameInput.sendKeys(fileName);
  }

  async getFileNameInput() {
    return this.fileNameInput.getAttribute('value');
  }

  async setPeriodFromInput(periodFrom) {
    await this.periodFromInput.sendKeys(periodFrom);
  }

  async getPeriodFromInput() {
    return this.periodFromInput.getAttribute('value');
  }

  async setPeriodToInput(periodTo) {
    await this.periodToInput.sendKeys(periodTo);
  }

  async getPeriodToInput() {
    return this.periodToInput.getAttribute('value');
  }

  async setContactsFileTypeIdInput(contactsFileTypeId) {
    await this.contactsFileTypeIdInput.sendKeys(contactsFileTypeId);
  }

  async getContactsFileTypeIdInput() {
    return this.contactsFileTypeIdInput.getAttribute('value');
  }

  async setDataFileInput(dataFile) {
    await this.dataFileInput.sendKeys(dataFile);
  }

  async getDataFileInput() {
    return this.dataFileInput.getAttribute('value');
  }

  getUploadSuccessfulInput() {
    return this.uploadSuccessfulInput;
  }
  getUploadProcessedInput() {
    return this.uploadProcessedInput;
  }
  async setUploadTokenInput(uploadToken) {
    await this.uploadTokenInput.sendKeys(uploadToken);
  }

  async getUploadTokenInput() {
    return this.uploadTokenInput.getAttribute('value');
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
    await this.setDescriptionInput('description');
    expect(await this.getDescriptionInput()).to.match(/description/);
    await waitUntilDisplayed(this.saveButton);
    await this.setFileNameInput('fileName');
    expect(await this.getFileNameInput()).to.match(/fileName/);
    await waitUntilDisplayed(this.saveButton);
    await this.setPeriodFromInput('01-01-2001');
    expect(await this.getPeriodFromInput()).to.eq('2001-01-01');
    await waitUntilDisplayed(this.saveButton);
    await this.setPeriodToInput('01-01-2001');
    expect(await this.getPeriodToInput()).to.eq('2001-01-01');
    await waitUntilDisplayed(this.saveButton);
    await this.setContactsFileTypeIdInput('5');
    expect(await this.getContactsFileTypeIdInput()).to.eq('5');
    await waitUntilDisplayed(this.saveButton);
    await this.setDataFileInput(absolutePath);
    await waitUntilDisplayed(this.saveButton);
    const selectedUploadSuccessful = await this.getUploadSuccessfulInput().isSelected();
    if (selectedUploadSuccessful) {
      await this.getUploadSuccessfulInput().click();
      expect(await this.getUploadSuccessfulInput().isSelected()).to.be.false;
    } else {
      await this.getUploadSuccessfulInput().click();
      expect(await this.getUploadSuccessfulInput().isSelected()).to.be.true;
    }
    await waitUntilDisplayed(this.saveButton);
    const selectedUploadProcessed = await this.getUploadProcessedInput().isSelected();
    if (selectedUploadProcessed) {
      await this.getUploadProcessedInput().click();
      expect(await this.getUploadProcessedInput().isSelected()).to.be.false;
    } else {
      await this.getUploadProcessedInput().click();
      expect(await this.getUploadProcessedInput().isSelected()).to.be.true;
    }
    await waitUntilDisplayed(this.saveButton);
    await this.setUploadTokenInput('uploadToken');
    expect(await this.getUploadTokenInput()).to.match(/uploadToken/);
    await this.save();
    await waitUntilHidden(this.saveButton);
    expect(await isVisible(this.saveButton)).to.be.false;
  }
}

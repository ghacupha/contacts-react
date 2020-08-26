import { element, by, ElementFinder } from 'protractor';
import { waitUntilDisplayed, waitUntilHidden, isVisible } from '../../util/utils';

const expect = chai.expect;

export default class ContactUpdatePage {
  pageTitle: ElementFinder = element(by.id('contactsApp.contact.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  contactNameInput: ElementFinder = element(by.css('input#contact-contactName'));
  departmentInput: ElementFinder = element(by.css('input#contact-department'));
  telephoneExtensionInput: ElementFinder = element(by.css('input#contact-telephoneExtension'));
  phoneNumberInput: ElementFinder = element(by.css('input#contact-phoneNumber'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setContactNameInput(contactName) {
    await this.contactNameInput.sendKeys(contactName);
  }

  async getContactNameInput() {
    return this.contactNameInput.getAttribute('value');
  }

  async setDepartmentInput(department) {
    await this.departmentInput.sendKeys(department);
  }

  async getDepartmentInput() {
    return this.departmentInput.getAttribute('value');
  }

  async setTelephoneExtensionInput(telephoneExtension) {
    await this.telephoneExtensionInput.sendKeys(telephoneExtension);
  }

  async getTelephoneExtensionInput() {
    return this.telephoneExtensionInput.getAttribute('value');
  }

  async setPhoneNumberInput(phoneNumber) {
    await this.phoneNumberInput.sendKeys(phoneNumber);
  }

  async getPhoneNumberInput() {
    return this.phoneNumberInput.getAttribute('value');
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
    await this.setContactNameInput('contactName');
    expect(await this.getContactNameInput()).to.match(/contactName/);
    await waitUntilDisplayed(this.saveButton);
    await this.setDepartmentInput('department');
    expect(await this.getDepartmentInput()).to.match(/department/);
    await waitUntilDisplayed(this.saveButton);
    await this.setTelephoneExtensionInput('telephoneExtension');
    expect(await this.getTelephoneExtensionInput()).to.match(/telephoneExtension/);
    await waitUntilDisplayed(this.saveButton);
    await this.setPhoneNumberInput('phoneNumber');
    expect(await this.getPhoneNumberInput()).to.match(/phoneNumber/);
    await this.save();
    await waitUntilHidden(this.saveButton);
    expect(await isVisible(this.saveButton)).to.be.false;
  }
}

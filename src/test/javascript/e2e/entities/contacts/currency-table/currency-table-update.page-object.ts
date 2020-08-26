import { element, by, ElementFinder } from 'protractor';
import { waitUntilDisplayed, waitUntilHidden, isVisible } from '../../../util/utils';

const expect = chai.expect;

export default class CurrencyTableUpdatePage {
  pageTitle: ElementFinder = element(by.id('contactsApp.contactsCurrencyTable.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  currencyCodeInput: ElementFinder = element(by.css('input#currency-table-currencyCode'));
  localitySelect: ElementFinder = element(by.css('select#currency-table-locality'));
  currencyNameInput: ElementFinder = element(by.css('input#currency-table-currencyName'));
  countryInput: ElementFinder = element(by.css('input#currency-table-country'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setCurrencyCodeInput(currencyCode) {
    await this.currencyCodeInput.sendKeys(currencyCode);
  }

  async getCurrencyCodeInput() {
    return this.currencyCodeInput.getAttribute('value');
  }

  async setLocalitySelect(locality) {
    await this.localitySelect.sendKeys(locality);
  }

  async getLocalitySelect() {
    return this.localitySelect.element(by.css('option:checked')).getText();
  }

  async localitySelectLastOption() {
    await this.localitySelect.all(by.tagName('option')).last().click();
  }
  async setCurrencyNameInput(currencyName) {
    await this.currencyNameInput.sendKeys(currencyName);
  }

  async getCurrencyNameInput() {
    return this.currencyNameInput.getAttribute('value');
  }

  async setCountryInput(country) {
    await this.countryInput.sendKeys(country);
  }

  async getCountryInput() {
    return this.countryInput.getAttribute('value');
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
    await this.setCurrencyCodeInput('currencyCode');
    expect(await this.getCurrencyCodeInput()).to.match(/currencyCode/);
    await waitUntilDisplayed(this.saveButton);
    await this.localitySelectLastOption();
    await waitUntilDisplayed(this.saveButton);
    await this.setCurrencyNameInput('currencyName');
    expect(await this.getCurrencyNameInput()).to.match(/currencyName/);
    await waitUntilDisplayed(this.saveButton);
    await this.setCountryInput('country');
    expect(await this.getCountryInput()).to.match(/country/);
    await this.save();
    await waitUntilHidden(this.saveButton);
    expect(await isVisible(this.saveButton)).to.be.false;
  }
}

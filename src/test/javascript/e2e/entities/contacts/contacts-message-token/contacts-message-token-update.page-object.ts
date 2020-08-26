import { element, by, ElementFinder } from 'protractor';
import { waitUntilDisplayed, waitUntilHidden, isVisible } from '../../../util/utils';

const expect = chai.expect;

export default class ContactsMessageTokenUpdatePage {
  pageTitle: ElementFinder = element(by.id('contactsApp.contactsContactsMessageToken.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  descriptionInput: ElementFinder = element(by.css('input#contacts-message-token-description'));
  timeSentInput: ElementFinder = element(by.css('input#contacts-message-token-timeSent'));
  tokenValueInput: ElementFinder = element(by.css('input#contacts-message-token-tokenValue'));
  receivedInput: ElementFinder = element(by.css('input#contacts-message-token-received'));
  actionedInput: ElementFinder = element(by.css('input#contacts-message-token-actioned'));
  contentFullyEnqueuedInput: ElementFinder = element(by.css('input#contacts-message-token-contentFullyEnqueued'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setDescriptionInput(description) {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput() {
    return this.descriptionInput.getAttribute('value');
  }

  async setTimeSentInput(timeSent) {
    await this.timeSentInput.sendKeys(timeSent);
  }

  async getTimeSentInput() {
    return this.timeSentInput.getAttribute('value');
  }

  async setTokenValueInput(tokenValue) {
    await this.tokenValueInput.sendKeys(tokenValue);
  }

  async getTokenValueInput() {
    return this.tokenValueInput.getAttribute('value');
  }

  getReceivedInput() {
    return this.receivedInput;
  }
  getActionedInput() {
    return this.actionedInput;
  }
  getContentFullyEnqueuedInput() {
    return this.contentFullyEnqueuedInput;
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
    await this.setTimeSentInput('5');
    expect(await this.getTimeSentInput()).to.eq('5');
    await waitUntilDisplayed(this.saveButton);
    await this.setTokenValueInput('tokenValue');
    expect(await this.getTokenValueInput()).to.match(/tokenValue/);
    await waitUntilDisplayed(this.saveButton);
    const selectedReceived = await this.getReceivedInput().isSelected();
    if (selectedReceived) {
      await this.getReceivedInput().click();
      expect(await this.getReceivedInput().isSelected()).to.be.false;
    } else {
      await this.getReceivedInput().click();
      expect(await this.getReceivedInput().isSelected()).to.be.true;
    }
    await waitUntilDisplayed(this.saveButton);
    const selectedActioned = await this.getActionedInput().isSelected();
    if (selectedActioned) {
      await this.getActionedInput().click();
      expect(await this.getActionedInput().isSelected()).to.be.false;
    } else {
      await this.getActionedInput().click();
      expect(await this.getActionedInput().isSelected()).to.be.true;
    }
    await waitUntilDisplayed(this.saveButton);
    const selectedContentFullyEnqueued = await this.getContentFullyEnqueuedInput().isSelected();
    if (selectedContentFullyEnqueued) {
      await this.getContentFullyEnqueuedInput().click();
      expect(await this.getContentFullyEnqueuedInput().isSelected()).to.be.false;
    } else {
      await this.getContentFullyEnqueuedInput().click();
      expect(await this.getContentFullyEnqueuedInput().isSelected()).to.be.true;
    }
    await this.save();
    await waitUntilHidden(this.saveButton);
    expect(await isVisible(this.saveButton)).to.be.false;
  }
}

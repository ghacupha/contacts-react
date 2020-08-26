export interface IContactsMessageToken {
  id?: number;
  description?: string;
  timeSent?: number;
  tokenValue?: string;
  received?: boolean;
  actioned?: boolean;
  contentFullyEnqueued?: boolean;
}

export const defaultValue: Readonly<IContactsMessageToken> = {
  received: false,
  actioned: false,
  contentFullyEnqueued: false,
};

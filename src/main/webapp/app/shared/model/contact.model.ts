export interface IContact {
  id?: number;
  contactName?: string;
  department?: string;
  telephoneExtension?: string;
  phoneNumber?: string;
}

export const defaultValue: Readonly<IContact> = {};

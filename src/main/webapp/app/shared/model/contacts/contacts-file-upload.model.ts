import { Moment } from 'moment';

export interface IContactsFileUpload {
  id?: number;
  description?: string;
  fileName?: string;
  periodFrom?: string;
  periodTo?: string;
  contactsFileTypeId?: number;
  dataFileContentType?: string;
  dataFile?: any;
  uploadSuccessful?: boolean;
  uploadProcessed?: boolean;
  uploadToken?: string;
}

export const defaultValue: Readonly<IContactsFileUpload> = {
  uploadSuccessful: false,
  uploadProcessed: false,
};

import { ContactsFileMediumTypes } from 'app/shared/model/enumerations/contacts-file-medium-types.model';
import { ContactsFileModelType } from 'app/shared/model/enumerations/contacts-file-model-type.model';

export interface IContactsFileType {
  id?: number;
  contactsFileTypeName?: string;
  contactsFileMediumType?: ContactsFileMediumTypes;
  description?: string;
  fileTemplateContentType?: string;
  fileTemplate?: any;
  contactsfileType?: ContactsFileModelType;
}

export const defaultValue: Readonly<IContactsFileType> = {};

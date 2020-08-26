import { CurrencyLocality } from 'app/shared/model/enumerations/currency-locality.model';

export interface ICurrencyTable {
  id?: number;
  currencyCode?: string;
  locality?: CurrencyLocality;
  currencyName?: string;
  country?: string;
}

export const defaultValue: Readonly<ICurrencyTable> = {};

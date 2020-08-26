import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICurrencyTable, defaultValue } from 'app/shared/model/contacts/currency-table.model';

export const ACTION_TYPES = {
  SEARCH_CURRENCYTABLES: 'currencyTable/SEARCH_CURRENCYTABLES',
  FETCH_CURRENCYTABLE_LIST: 'currencyTable/FETCH_CURRENCYTABLE_LIST',
  FETCH_CURRENCYTABLE: 'currencyTable/FETCH_CURRENCYTABLE',
  CREATE_CURRENCYTABLE: 'currencyTable/CREATE_CURRENCYTABLE',
  UPDATE_CURRENCYTABLE: 'currencyTable/UPDATE_CURRENCYTABLE',
  DELETE_CURRENCYTABLE: 'currencyTable/DELETE_CURRENCYTABLE',
  RESET: 'currencyTable/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICurrencyTable>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type CurrencyTableState = Readonly<typeof initialState>;

// Reducer

export default (state: CurrencyTableState = initialState, action): CurrencyTableState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_CURRENCYTABLES):
    case REQUEST(ACTION_TYPES.FETCH_CURRENCYTABLE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CURRENCYTABLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_CURRENCYTABLE):
    case REQUEST(ACTION_TYPES.UPDATE_CURRENCYTABLE):
    case REQUEST(ACTION_TYPES.DELETE_CURRENCYTABLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_CURRENCYTABLES):
    case FAILURE(ACTION_TYPES.FETCH_CURRENCYTABLE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CURRENCYTABLE):
    case FAILURE(ACTION_TYPES.CREATE_CURRENCYTABLE):
    case FAILURE(ACTION_TYPES.UPDATE_CURRENCYTABLE):
    case FAILURE(ACTION_TYPES.DELETE_CURRENCYTABLE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_CURRENCYTABLES):
    case SUCCESS(ACTION_TYPES.FETCH_CURRENCYTABLE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_CURRENCYTABLE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_CURRENCYTABLE):
    case SUCCESS(ACTION_TYPES.UPDATE_CURRENCYTABLE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_CURRENCYTABLE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/currency-tables';
const apiSearchUrl = 'api/_search/currency-tables';

// Actions

export const getSearchEntities: ICrudSearchAction<ICurrencyTable> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_CURRENCYTABLES,
  payload: axios.get<ICurrencyTable>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<ICurrencyTable> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CURRENCYTABLE_LIST,
    payload: axios.get<ICurrencyTable>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ICurrencyTable> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CURRENCYTABLE,
    payload: axios.get<ICurrencyTable>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ICurrencyTable> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CURRENCYTABLE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICurrencyTable> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CURRENCYTABLE,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICurrencyTable> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CURRENCYTABLE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});

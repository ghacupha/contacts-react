import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IContactsMessageToken, defaultValue } from 'app/shared/model/contacts/contacts-message-token.model';

export const ACTION_TYPES = {
  SEARCH_CONTACTSMESSAGETOKENS: 'contactsMessageToken/SEARCH_CONTACTSMESSAGETOKENS',
  FETCH_CONTACTSMESSAGETOKEN_LIST: 'contactsMessageToken/FETCH_CONTACTSMESSAGETOKEN_LIST',
  FETCH_CONTACTSMESSAGETOKEN: 'contactsMessageToken/FETCH_CONTACTSMESSAGETOKEN',
  CREATE_CONTACTSMESSAGETOKEN: 'contactsMessageToken/CREATE_CONTACTSMESSAGETOKEN',
  UPDATE_CONTACTSMESSAGETOKEN: 'contactsMessageToken/UPDATE_CONTACTSMESSAGETOKEN',
  DELETE_CONTACTSMESSAGETOKEN: 'contactsMessageToken/DELETE_CONTACTSMESSAGETOKEN',
  RESET: 'contactsMessageToken/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IContactsMessageToken>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ContactsMessageTokenState = Readonly<typeof initialState>;

// Reducer

export default (state: ContactsMessageTokenState = initialState, action): ContactsMessageTokenState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_CONTACTSMESSAGETOKENS):
    case REQUEST(ACTION_TYPES.FETCH_CONTACTSMESSAGETOKEN_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CONTACTSMESSAGETOKEN):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_CONTACTSMESSAGETOKEN):
    case REQUEST(ACTION_TYPES.UPDATE_CONTACTSMESSAGETOKEN):
    case REQUEST(ACTION_TYPES.DELETE_CONTACTSMESSAGETOKEN):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_CONTACTSMESSAGETOKENS):
    case FAILURE(ACTION_TYPES.FETCH_CONTACTSMESSAGETOKEN_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CONTACTSMESSAGETOKEN):
    case FAILURE(ACTION_TYPES.CREATE_CONTACTSMESSAGETOKEN):
    case FAILURE(ACTION_TYPES.UPDATE_CONTACTSMESSAGETOKEN):
    case FAILURE(ACTION_TYPES.DELETE_CONTACTSMESSAGETOKEN):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_CONTACTSMESSAGETOKENS):
    case SUCCESS(ACTION_TYPES.FETCH_CONTACTSMESSAGETOKEN_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_CONTACTSMESSAGETOKEN):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_CONTACTSMESSAGETOKEN):
    case SUCCESS(ACTION_TYPES.UPDATE_CONTACTSMESSAGETOKEN):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_CONTACTSMESSAGETOKEN):
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

const apiUrl = 'api/contacts-message-tokens';
const apiSearchUrl = 'api/_search/contacts-message-tokens';

// Actions

export const getSearchEntities: ICrudSearchAction<IContactsMessageToken> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_CONTACTSMESSAGETOKENS,
  payload: axios.get<IContactsMessageToken>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IContactsMessageToken> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CONTACTSMESSAGETOKEN_LIST,
    payload: axios.get<IContactsMessageToken>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IContactsMessageToken> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CONTACTSMESSAGETOKEN,
    payload: axios.get<IContactsMessageToken>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IContactsMessageToken> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CONTACTSMESSAGETOKEN,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IContactsMessageToken> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CONTACTSMESSAGETOKEN,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IContactsMessageToken> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CONTACTSMESSAGETOKEN,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});

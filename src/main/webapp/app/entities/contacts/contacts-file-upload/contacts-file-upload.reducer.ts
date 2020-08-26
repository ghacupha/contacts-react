import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IContactsFileUpload, defaultValue } from 'app/shared/model/contacts/contacts-file-upload.model';

export const ACTION_TYPES = {
  SEARCH_CONTACTSFILEUPLOADS: 'contactsFileUpload/SEARCH_CONTACTSFILEUPLOADS',
  FETCH_CONTACTSFILEUPLOAD_LIST: 'contactsFileUpload/FETCH_CONTACTSFILEUPLOAD_LIST',
  FETCH_CONTACTSFILEUPLOAD: 'contactsFileUpload/FETCH_CONTACTSFILEUPLOAD',
  CREATE_CONTACTSFILEUPLOAD: 'contactsFileUpload/CREATE_CONTACTSFILEUPLOAD',
  UPDATE_CONTACTSFILEUPLOAD: 'contactsFileUpload/UPDATE_CONTACTSFILEUPLOAD',
  DELETE_CONTACTSFILEUPLOAD: 'contactsFileUpload/DELETE_CONTACTSFILEUPLOAD',
  SET_BLOB: 'contactsFileUpload/SET_BLOB',
  RESET: 'contactsFileUpload/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IContactsFileUpload>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ContactsFileUploadState = Readonly<typeof initialState>;

// Reducer

export default (state: ContactsFileUploadState = initialState, action): ContactsFileUploadState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_CONTACTSFILEUPLOADS):
    case REQUEST(ACTION_TYPES.FETCH_CONTACTSFILEUPLOAD_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CONTACTSFILEUPLOAD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_CONTACTSFILEUPLOAD):
    case REQUEST(ACTION_TYPES.UPDATE_CONTACTSFILEUPLOAD):
    case REQUEST(ACTION_TYPES.DELETE_CONTACTSFILEUPLOAD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_CONTACTSFILEUPLOADS):
    case FAILURE(ACTION_TYPES.FETCH_CONTACTSFILEUPLOAD_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CONTACTSFILEUPLOAD):
    case FAILURE(ACTION_TYPES.CREATE_CONTACTSFILEUPLOAD):
    case FAILURE(ACTION_TYPES.UPDATE_CONTACTSFILEUPLOAD):
    case FAILURE(ACTION_TYPES.DELETE_CONTACTSFILEUPLOAD):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_CONTACTSFILEUPLOADS):
    case SUCCESS(ACTION_TYPES.FETCH_CONTACTSFILEUPLOAD_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_CONTACTSFILEUPLOAD):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_CONTACTSFILEUPLOAD):
    case SUCCESS(ACTION_TYPES.UPDATE_CONTACTSFILEUPLOAD):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_CONTACTSFILEUPLOAD):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.SET_BLOB: {
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType,
        },
      };
    }
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/contacts-file-uploads';
const apiSearchUrl = 'api/_search/contacts-file-uploads';

// Actions

export const getSearchEntities: ICrudSearchAction<IContactsFileUpload> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_CONTACTSFILEUPLOADS,
  payload: axios.get<IContactsFileUpload>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IContactsFileUpload> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CONTACTSFILEUPLOAD_LIST,
    payload: axios.get<IContactsFileUpload>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IContactsFileUpload> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CONTACTSFILEUPLOAD,
    payload: axios.get<IContactsFileUpload>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IContactsFileUpload> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CONTACTSFILEUPLOAD,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IContactsFileUpload> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CONTACTSFILEUPLOAD,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IContactsFileUpload> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CONTACTSFILEUPLOAD,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType,
  },
});

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});

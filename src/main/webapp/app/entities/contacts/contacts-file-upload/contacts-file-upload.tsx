import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
import {
  openFile,
  byteSize,
  ICrudSearchAction,
  ICrudGetAllAction,
  TextFormat,
  getSortState,
  IPaginationBaseState,
  JhiPagination,
  JhiItemCount,
} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './contacts-file-upload.reducer';
import { IContactsFileUpload } from 'app/shared/model/contacts/contacts-file-upload.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';

export interface IContactsFileUploadProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const ContactsFileUpload = (props: IContactsFileUploadProps) => {
  const [search, setSearch] = useState('');
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE), props.location.search)
  );

  const getAllEntities = () => {
    if (search) {
      props.getSearchEntities(
        search,
        paginationState.activePage - 1,
        paginationState.itemsPerPage,
        `${paginationState.sort},${paginationState.order}`
      );
    } else {
      props.getEntities(paginationState.activePage - 1, paginationState.itemsPerPage, `${paginationState.sort},${paginationState.order}`);
    }
  };

  const startSearching = () => {
    if (search) {
      setPaginationState({
        ...paginationState,
        activePage: 1,
      });
      props.getSearchEntities(
        search,
        paginationState.activePage - 1,
        paginationState.itemsPerPage,
        `${paginationState.sort},${paginationState.order}`
      );
    }
  };

  const clear = () => {
    setSearch('');
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    props.getEntities();
  };

  const handleSearch = event => setSearch(event.target.value);

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (props.location.search !== endURL) {
      props.history.push(`${props.location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort, search]);

  useEffect(() => {
    const params = new URLSearchParams(props.location.search);
    const page = params.get('page');
    const sort = params.get('sort');
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [props.location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === 'asc' ? 'desc' : 'asc',
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const { contactsFileUploadList, match, loading, totalItems } = props;
  return (
    <div>
      <h2 id="contacts-file-upload-heading">
        Contacts File Uploads
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Contacts File Upload
        </Link>
      </h2>
      <Row>
        <Col sm="12">
          <AvForm onSubmit={startSearching}>
            <AvGroup>
              <InputGroup>
                <AvInput type="text" name="search" value={search} onChange={handleSearch} placeholder="Search" />
                <Button className="input-group-addon">
                  <FontAwesomeIcon icon="search" />
                </Button>
                <Button type="reset" className="input-group-addon" onClick={clear}>
                  <FontAwesomeIcon icon="trash" />
                </Button>
              </InputGroup>
            </AvGroup>
          </AvForm>
        </Col>
      </Row>
      <div className="table-responsive">
        {contactsFileUploadList && contactsFileUploadList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('description')}>
                  Description <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('fileName')}>
                  File Name <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('periodFrom')}>
                  Period From <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('periodTo')}>
                  Period To <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('contactsFileTypeId')}>
                  Contacts File Type Id <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('dataFile')}>
                  Data File <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('uploadSuccessful')}>
                  Upload Successful <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('uploadProcessed')}>
                  Upload Processed <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('uploadToken')}>
                  Upload Token <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {contactsFileUploadList.map((contactsFileUpload, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${contactsFileUpload.id}`} color="link" size="sm">
                      {contactsFileUpload.id}
                    </Button>
                  </td>
                  <td>{contactsFileUpload.description}</td>
                  <td>{contactsFileUpload.fileName}</td>
                  <td>
                    {contactsFileUpload.periodFrom ? (
                      <TextFormat type="date" value={contactsFileUpload.periodFrom} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {contactsFileUpload.periodTo ? (
                      <TextFormat type="date" value={contactsFileUpload.periodTo} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{contactsFileUpload.contactsFileTypeId}</td>
                  <td>
                    {contactsFileUpload.dataFile ? (
                      <div>
                        {contactsFileUpload.dataFileContentType ? (
                          <a onClick={openFile(contactsFileUpload.dataFileContentType, contactsFileUpload.dataFile)}>Open &nbsp;</a>
                        ) : null}
                        <span>
                          {contactsFileUpload.dataFileContentType}, {byteSize(contactsFileUpload.dataFile)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{contactsFileUpload.uploadSuccessful ? 'true' : 'false'}</td>
                  <td>{contactsFileUpload.uploadProcessed ? 'true' : 'false'}</td>
                  <td>{contactsFileUpload.uploadToken}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${contactsFileUpload.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${contactsFileUpload.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${contactsFileUpload.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Contacts File Uploads found</div>
        )}
      </div>
      {props.totalItems ? (
        <div className={contactsFileUploadList && contactsFileUploadList.length > 0 ? '' : 'd-none'}>
          <Row className="justify-content-center">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
          </Row>
          <Row className="justify-content-center">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={props.totalItems}
            />
          </Row>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

const mapStateToProps = ({ contactsFileUpload }: IRootState) => ({
  contactsFileUploadList: contactsFileUpload.entities,
  loading: contactsFileUpload.loading,
  totalItems: contactsFileUpload.totalItems,
});

const mapDispatchToProps = {
  getSearchEntities,
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ContactsFileUpload);

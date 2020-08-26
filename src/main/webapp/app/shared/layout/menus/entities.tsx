import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name="Entities" id="entity-menu" style={{ maxHeight: '80vh', overflow: 'auto' }}>
    <MenuItem icon="asterisk" to="/contact">
      Contact
    </MenuItem>
    <MenuItem icon="asterisk" to="/contacts-file-type">
      Contacts File Type
    </MenuItem>
    <MenuItem icon="asterisk" to="/contacts-file-upload">
      Contacts File Upload
    </MenuItem>
    <MenuItem icon="asterisk" to="/contacts-message-token">
      Contacts Message Token
    </MenuItem>
    <MenuItem icon="asterisk" to="/currency-table">
      Currency Table
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);

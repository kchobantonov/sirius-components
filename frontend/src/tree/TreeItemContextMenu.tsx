/*******************************************************************************
 * Copyright (c) 2019, 2021 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
import { httpOrigin } from 'common/URL';
import { ContextMenu, Entry, LEFT_START, Separator } from 'core/contextmenu/ContextMenu';
import { Delete, Edit } from 'icons';
import React from 'react';
export const TreeItemContextMenu = ({
  x,
  y,
  item,
  editingContextId,
  readOnly,
  enterEditingMode,
  openModal,
  deleteItem,
  closeContextMenu,
}) => {
  const entries = [];
  // Creation operations (type-specific)
  if (item.kind === 'Document') {
    entries.push(
      <Entry
        label="New object"
        onClick={() => openModal('CreateNewRootObject')}
        data-testid="new-object"
        disabled={readOnly}
      />
    );
    entries.push(
      <a
        href={`${httpOrigin}/api/editingcontexts/${editingContextId}/documents/${item.id}`}
        type="application/octet-stream"
        data-testid="download-link">
        <Entry label="Download" onClick={closeContextMenu} data-testid="download" />
      </a>
    );
  } else {
    entries.push(
      <Entry
        label="New object"
        onClick={() => openModal('CreateNewObject')}
        data-testid="new-object"
        disabled={readOnly}
      />
    );
    entries.push(
      <Entry
        label="New representation"
        onClick={() => openModal('CreateRepresentation')}
        data-testid="new-representation"
        disabled={readOnly}
      />
    );
  }
  entries.push(<Separator />);
  // Generic edition operations
  if (item.editable) {
    entries.push(
      <Entry
        icon={<Edit title="" />}
        label="Rename"
        onClick={enterEditingMode}
        data-testid="rename-object"
        disabled={readOnly}></Entry>
    );
  }
  if (item.deletable) {
    entries.push(
      <Entry
        icon={<Delete title="" />}
        label="Delete"
        onClick={deleteItem}
        data-testid="delete-object"
        disabled={readOnly}
      />
    );
  }

  return (
    <ContextMenu
      x={x}
      y={y}
      caretPosition={LEFT_START}
      onClose={closeContextMenu}
      data-testid="treeitemdocument-contextmenu">
      {entries}
    </ContextMenu>
  );
};

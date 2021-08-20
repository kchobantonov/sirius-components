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
  treeItemKind,
}) => {
  const entries = [];
  // Creation operations (type-specific)
  treeItemKind
    .getMenuEntries(item, editingContextId, readOnly, openModal, closeContextMenu)
    .forEach((entry) => entries.push(entry));
  if (entries.length > 0) {
    entries.push(<Separator />);
  }
  // Generic edition operations
  if (item.editable) {
    entries.push(
      <Entry
        icon={<Edit title="" />}
        label="Rename"
        onClick={enterEditingMode}
        data-testid="rename-tree-item"
        disabled={readOnly}></Entry>
    );
  }
  if (item.deletable) {
    entries.push(
      <Entry
        icon={<Delete title="" />}
        label="Delete"
        onClick={deleteItem}
        data-testid="delete-tree-item"
        disabled={readOnly}
      />
    );
  }

  return (
    <ContextMenu x={x} y={y} caretPosition={LEFT_START} onClose={closeContextMenu} data-testid="treeitem-contextmenu">
      {entries}
    </ContextMenu>
  );
};

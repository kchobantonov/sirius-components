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
import React from 'react';
import { TreeItemDocumentContextMenu } from './TreeItemDocumentContextMenu';
import { TreeItemObjectContextMenu } from './TreeItemObjectContextMenu';

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
  if (item.kind === 'Document') {
    return (
      <TreeItemDocumentContextMenu
        x={x}
        y={y}
        editingContextId={editingContextId}
        documentId={item.id}
        onNewObject={() => openModal('CreateNewRootObject')}
        onRenameDocument={enterEditingMode}
        onDownload={closeContextMenu}
        onDeleteDocument={deleteItem}
        onClose={closeContextMenu}
        readOnly={readOnly}
      />
    );
  } else {
    return (
      <TreeItemObjectContextMenu
        x={x}
        y={y}
        onCreateNewObject={() => openModal('CreateNewObject')}
        onCreateRepresentation={() => openModal('CreateRepresentation')}
        editable={item.editable}
        onRenameObject={enterEditingMode}
        onDeleteObject={deleteItem}
        onClose={closeContextMenu}
        readOnly={readOnly}
      />
    );
  }
};

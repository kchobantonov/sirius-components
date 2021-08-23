/*******************************************************************************
 * Copyright (c) 2021 Obeo.
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
import { Entry } from 'core/contextmenu/ContextMenu';
import { NewObjectModal } from 'modals/new-object/NewObjectModal';
import { NewRepresentationModal } from 'modals/new-representation/NewRepresentationModal';
import { NewRootObjectModal } from 'modals/new-root-object/NewRootObjectModal';
import React from 'react';
import { TreeItemType } from './TreeItem.types';

interface TreeItemKind {
  name: string;
  handles: (treeItem: TreeItemType) => boolean;
  getItemTitle: (treeItem: TreeItemType) => string;
  getItemLabel: (treeItem: TreeItemType) => string;
  getModal: (name: string) => any;
  getMenuEntries: (
    item: TreeItemType,
    editingContextId: string,
    readOnly: boolean,
    openModal: (modalName: string) => void,
    closeContextMenu: () => void
  ) => Array<any>;
}

const documentItemKind: TreeItemKind = {
  name: 'Document',
  handles: (treeItem) => treeItem.kind === 'Document',
  getItemTitle: (item) => 'Model',
  getItemLabel: (item) => item.label,
  getModal: (name) => {
    if (name === 'CreateNewRootObject') {
      return NewRootObjectModal;
    }
  },
  getMenuEntries: (item, editingContextId, readOnly, openModal, closeContextMenu) => {
    return [
      <Entry
        label="New object"
        onClick={() => openModal('CreateNewRootObject')}
        data-testid="new-object"
        disabled={readOnly}
      />,
      <a
        href={`${httpOrigin}/api/editingcontexts/${editingContextId}/documents/${item.id}`}
        type="application/octet-stream"
        data-testid="download-link">
        <Entry label="Download" onClick={closeContextMenu} data-testid="download" />
      </a>,
    ];
  },
};

const semanticObjectItemKind: TreeItemKind = {
  name: 'Semantic Object',
  handles: (treeItem) => treeItem.kind !== null && treeItem.kind.includes('::'),
  getItemTitle: (item) => item.kind,
  getItemLabel: (item) => {
    if (item.label) {
      return item.label;
    } else {
      return item.kind.split('::').pop();
    }
  },
  getModal: (name) => {
    if (name === 'CreateNewObject') {
      return NewObjectModal;
    } else if (name === 'CreateRepresentation') {
      return NewRepresentationModal;
    }
  },
  getMenuEntries: (item, editingContextId, readOnly, openModal, closeContextMenu) => {
    return [
      <Entry
        label="New object"
        onClick={() => openModal('CreateNewObject')}
        data-testid="new-object"
        disabled={readOnly}
      />,
      <Entry
        label="New representation"
        onClick={() => openModal('CreateRepresentation')}
        data-testid="new-representation"
        disabled={readOnly}
      />,
    ];
  },
};

// Catch-all, must come last
const unknownItemKind: TreeItemKind = {
  name: 'Unkown item type',
  handles: (treeItem) => true,
  getItemTitle: (item) => 'Unknown',
  getItemLabel: (item) => item.label,
  getModal: (name) => null,
  getMenuEntries: (item) => {
    return [];
  },
};

const registry = [documentItemKind, semanticObjectItemKind, unknownItemKind];

export function getTreeItemKind(item: any): any {
  return registry.find((entry) => entry.handles(item));
}

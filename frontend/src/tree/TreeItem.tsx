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
import { useMutation } from '@apollo/client';
import { httpOrigin } from 'common/URL';
import { IconButton } from 'core/button/Button';
import { Text } from 'core/text/Text';
import { Textfield } from 'core/textfield/Textfield';
import gql from 'graphql-tag';
import { ArrowCollapsed, ArrowExpanded, More, NoIcon } from 'icons';
import React, { useEffect, useRef, useState } from 'react';
import { v4 as uuid } from 'uuid';
import styles from './TreeItem.module.css';
import { TreeItemProps } from './TreeItem.types';
import { TreeItemContextMenu } from './TreeItemContextMenu';
import { useTreeItemHandler } from './TreeItemHandlersContext';

const deleteTreeItemMutation = gql`
  mutation deleteTreeItem($input: DeleteTreeItemInput!) {
    deleteTreeItem(input: $input) {
      __typename
      ... on ErrorPayload {
        message
      }
    }
  }
`;

const renameTreeItemMutation = gql`
  mutation renameTreeItem($input: RenameTreeItemInput!) {
    renameTreeItem(input: $input) {
      __typename
      ... on ErrorPayload {
        message
      }
    }
  }
`;

// The list of characters that will enable the direct edit mechanism.
const directEditActivationValidCharacters = /[\w&é§èàùçÔØÁÛÊË"«»’”„´$¥€£\\¿?!=+-,;:%/{}[\]–#@*.]/;

/**
 * Determines where the context menu should open relative to the actual mouse position.
 * These are relative to the bottom-left corner of the "more" icon, and to the size of the
 * caret, so that the caret at the left of the menu points to the middle of the "more" icon.
 */
const menuPositionDelta = {
  dx: 20,
  dy: -6,
};

const ItemCollapseToggle = ({ item, depth, onExpand }) => {
  if (item.hasChildren) {
    const onClick = () => onExpand(item.id, depth);
    if (item.expanded) {
      return (
        <ArrowExpanded
          title="Collapse"
          className={styles.arrow}
          width="20"
          height="20"
          onClick={onClick}
          data-testid="expand"
        />
      );
    } else {
      return (
        <ArrowCollapsed
          title="Expand"
          className={styles.arrow}
          width="20"
          height="20"
          onClick={onClick}
          data-testid="expand"
        />
      );
    }
  }
  return null;
};

export const TreeItem = ({
  editingContextId,
  item,
  depth,
  onExpand,
  selection,
  setSelection,
  readOnly,
}: TreeItemProps) => {
  const initialState = {
    modalDisplayed: null,
    x: 0,
    y: 0,
    showContextMenu: false,
    editingMode: false,
    label: item.label,
    prevSelectionId: null,
  };
  const [state, setState] = useState(initialState);
  const { x, y, showContextMenu, modalDisplayed, editingMode, label } = state;
  const refDom = useRef() as any;

  const [deleteTreeItem] = useMutation(deleteTreeItemMutation);
  const [renameTreeItem, { loading: renameTreeItemLoading, data: renameTreeItemData, error: renameTreeItemError }] =
    useMutation(renameTreeItemMutation);
  useEffect(() => {
    if (!renameTreeItemLoading && !renameTreeItemError && renameTreeItemData?.renameTreeItem) {
      const { renameTreeItem } = renameTreeItemData;
      if (renameTreeItem.__typename === 'RenameTreeItemSuccessPayload') {
        setState((prevState) => {
          return { ...prevState, editingMode: false };
        });
      }
    }
  }, [renameTreeItemData, renameTreeItemError, renameTreeItemLoading]);

  const itemHandler = useTreeItemHandler(item);

  // custom hook for getting previous value
  const usePrevious = (value) => {
    const ref = useRef();
    useEffect(() => {
      ref.current = value;
    });
    return ref.current;
  };

  const prevEditingMode = usePrevious(editingMode);
  useEffect(() => {
    if (prevEditingMode && !editingMode) {
      refDom.current.focus();
    }
  }, [editingMode, prevEditingMode]);

  // Context menu handling
  const openContextMenu = (event) => {
    const { x, y } = event.currentTarget.getBoundingClientRect();
    if (!showContextMenu) {
      setState((prevState) => {
        return {
          modalDisplayed: prevState.modalDisplayed,
          x: x + menuPositionDelta.dx,
          y: y + menuPositionDelta.dy,
          showContextMenu: true,
          editingMode: false,
          label: item.label,
          prevSelectionId: prevState.prevSelectionId,
        };
      });
    }
  };

  let contextMenu = null;
  if (showContextMenu) {
    const closeContextMenu = () => {
      setState((prevState) => {
        return {
          modalDisplayed: null,
          x: 0,
          y: 0,
          showContextMenu: false,
          editingMode: false,
          label: item.label,
          prevSelectionId: prevState.prevSelectionId,
        };
      });
    };
    const enterEditingMode = () => {
      setState((prevState) => {
        return {
          modalDisplayed: null,
          x: 0,
          y: 0,
          showContextMenu: false,
          editingMode: true,
          label: item.label,
          prevSelectionId: prevState.prevSelectionId,
        };
      });
    };
    const openModal = (modalName) => {
      setState((prevState) => {
        return {
          modalDisplayed: modalName,
          x: 0,
          y: 0,
          showContextMenu: false,
          editingMode: false,
          label: item.label,
          prevSelectionId: prevState.prevSelectionId,
        };
      });
    };
    const deleteItem = () => {
      const variables = {
        input: {
          id: uuid(),
          editingContextId,
          treeItemId: item.id,
          kind: item.kind,
        },
      };
      deleteTreeItem({ variables });
      closeContextMenu();
    };

    contextMenu = (
      <TreeItemContextMenu
        x={x}
        y={y}
        item={item}
        editingContextId={editingContextId}
        readOnly={readOnly}
        enterEditingMode={enterEditingMode}
        openModal={openModal}
        deleteItem={deleteItem}
        closeContextMenu={closeContextMenu}
        treeItemHandler={itemHandler}
      />
    );
  }

  // Modals handling
  const closeModal = () =>
    setState((prevState) => {
      return {
        modalDisplayed: null,
        x: 0,
        y: 0,
        showContextMenu: false,
        editingMode: false,
        label: item.label,
        prevSelectionId: prevState.prevSelectionId,
      };
    });
  const selectAndRevealItem = (object) => {
    if (!item.expanded && item.hasChildren) {
      onExpand(item.id, depth);
    }
    const { id, label, kind } = object;
    setSelection({ id, label, kind });
    closeModal();
  };

  let modal = null;
  if (modalDisplayed !== null) {
    const ModalComponent = itemHandler.getModal(modalDisplayed);
    const props = {
      editingContextId: editingContextId,
      item,
      onObjectCreated: selectAndRevealItem,
      onClose: closeModal,
    };
    modal = <ModalComponent {...props} />;
  }

  let children = null;
  if (item.expanded) {
    children = (
      <ul className={styles.ul}>
        {item.children.map((childItem) => {
          return (
            <li key={childItem.id}>
              <TreeItem
                editingContextId={editingContextId}
                item={childItem}
                depth={depth + 1}
                onExpand={onExpand}
                selection={selection}
                setSelection={setSelection}
                readOnly={readOnly}
              />
            </li>
          );
        })}
      </ul>
    );
  }

  let className = styles.treeItem;
  let dataTestid = undefined;

  if (selection?.id === item.id) {
    className = `${className} ${styles.selected}`;
    dataTestid = 'selected';
  }

  let image = <NoIcon title={item.kind} />;
  if (item.imageURL) {
    image = <img height="16" width="16" alt={item.kind} src={httpOrigin + item.imageURL}></img>;
  }
  const itemTitle = itemHandler.getItemTitle(item);
  const itemLabel = itemHandler.getItemLabel(item);

  let text;
  if (editingMode) {
    const handleChange = (event) => {
      const newLabel = event.target.value;
      setState((prevState) => {
        return { ...prevState, editingMode: true, label: newLabel };
      });
    };

    const doRename = () => {
      const isNameValid = label.length >= 1;
      if (isNameValid && item) {
        renameTreeItem({
          variables: {
            input: { id: uuid(), editingContextId, treeItemId: item.id, kind: item.kind, newName: label },
          },
        });
      } else {
        setState((prevState) => {
          return { ...prevState, editingMode: false, label: item.label };
        });
      }
    };
    const onFinishEditing = (event) => {
      const { key } = event;
      if (key === 'Enter') {
        doRename();
      } else if (key === 'Escape') {
        setState((prevState) => {
          return { ...prevState, editingMode: false, label: item.label };
        });
      }
    };
    const onFocusIn = (event) => {
      event.target.select();
    };
    const onFocusOut = (event) => {
      doRename();
    };
    text = (
      <Textfield
        kind={'small'}
        name="name"
        placeholder={'Enter the new name'}
        value={label}
        onChange={handleChange}
        onKeyDown={onFinishEditing}
        onFocus={onFocusIn}
        onBlur={onFocusOut}
        autoFocus
        data-testid="name-edit"
      />
    );
  } else {
    text = <Text className={styles.label}>{itemLabel}</Text>;
  }
  const onFocus = () => {
    const { id, label, kind } = item;
    setSelection({ id, label, kind });
  };

  const onClick = () => {
    if (!editingMode) {
      refDom.current.focus();
    }
  };

  const onBeginEditing = (event) => {
    if (!item.editable || editingMode || readOnly) {
      return;
    }
    const { key } = event;
    /*If a modifier key is hit alone, do nothing*/
    if ((event.altKey || event.shiftKey) && event.getModifierState(key)) {
      return;
    }
    const validFirstInputChar =
      !event.metaKey && !event.ctrlKey && key.length === 1 && directEditActivationValidCharacters.test(key);
    if (validFirstInputChar) {
      setState((prevState) => {
        return { ...prevState, editingMode: true, label: key };
      });
    }
  };

  /* ref, tabindex and onFocus are used to set the React component focusabled and to set the focus to the corresponding DOM part */
  return (
    <>
      <div
        className={className}
        ref={refDom}
        tabIndex={0}
        onFocus={onFocus}
        onKeyDown={onBeginEditing}
        data-treeitemid={item.id}
        data-haschildren={item.hasChildren.toString()}
        data-depth={depth}
        data-expanded={item.expanded.toString()}
        data-testid={dataTestid}>
        <ItemCollapseToggle item={item} depth={depth} onExpand={onExpand} />
        <div className={styles.content}>
          <div
            className={styles.imageAndLabel}
            onClick={onClick}
            onDoubleClick={() => item.hasChildren && onExpand(item.id, depth)}
            title={itemTitle}
            data-testid={itemLabel}>
            {image}
            {text}
          </div>
          <IconButton className={styles.more} onClick={openContextMenu} data-testid={`${itemLabel}-more`}>
            <More title="More" />
          </IconButton>
        </div>
      </div>
      {children}
      {contextMenu}
      {modal}
    </>
  );
};

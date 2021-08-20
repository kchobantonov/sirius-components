/*******************************************************************************
 * Copyright (c) 2021 Obeo and others.
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

import { SubscriptionResult } from '@apollo/client';
import { makeStyles, Snackbar, Typography } from '@material-ui/core';
import IconButton from '@material-ui/core/IconButton';
import CloseIcon from '@material-ui/icons/Close';
import { useMachine } from '@xstate/react';
import { List } from 'form/Form.types';
import { GQLFormRefreshedEventPayload, GQLPropertiesEventSubscription } from 'form/FormEventFragments.types';
import { PubSub } from 'graphql-subscriptions';
import { Properties } from 'properties/Properties';
import React, { useContext, useEffect, useMemo } from 'react';
import { RepresentationContext } from 'workbench/RepresentationContext';
import { RepresentationsWebSocketContainerProps } from './RepresentationsWebSocketContainer.types';
import {
  HandleSubscriptionResultEvent,
  HideToastEvent,
  RepresentationsWebSocketContainerContext,
  RepresentationsWebSocketContainerEvent,
  representationsWebSocketContainerMachine,
  SchemaValue,
  SwitchSelectionEvent,
} from './RepresentationsWebSocketContainerMachine';

const useRepresentationsWebSocketContainerStyles = makeStyles((theme) => ({
  idle: {
    padding: theme.spacing(1),
  },
}));

export const RepresentationsWebSocketContainer = ({
  editingContextId,
  selection,
  readOnly,
}: RepresentationsWebSocketContainerProps) => {
  const classes = useRepresentationsWebSocketContainerStyles();

  const [{ value, context }, dispatch] = useMachine<
    RepresentationsWebSocketContainerContext,
    RepresentationsWebSocketContainerEvent
  >(representationsWebSocketContainerMachine);

  const { toast, representationsWebSocketContainer } = value as SchemaValue;
  const { currentSelection, form, subscribers, widgetSubscriptions, message } = context;
  const { registry } = useContext(RepresentationContext);

  const pubSub = useMemo(() => new PubSub(), []);

  useEffect(() => {
    if (currentSelection?.id !== selection?.id) {
      const isRepresentation = registry.isRepresentation(selection.kind);
      const switchSelectionEvent: SwitchSelectionEvent = { type: 'SWITCH_SELECTION', selection, isRepresentation };
      dispatch(switchSelectionEvent);
    }
  }, [currentSelection, registry, selection, dispatch]);

  // Temporary useEffect, to remove before commit
  useEffect(() => {
    if (representationsWebSocketContainer === 'idle') {
      const representationsEvent: GQLFormRefreshedEventPayload = {
        __typename: 'FormRefreshedEventPayload',
        id: 'customEventRefresh',
        form: {
          id: 'form',
          label: 'Representation form',
          pages: [
            {
              id: 'page',
              label: 'representation page',
              groups: [
                {
                  id: 'widget',
                  label: 'Representations Group',
                  widgets: [
                    {
                      __typename: 'List',
                      id: 'representationList',
                      label: 'Representations',
                      diagnostics: [],
                      items: [
                        {
                          id: 'item 1',
                          imageURL: '',
                          label: 'Representation 1',
                          action: {
                            tooltip: 'Delete Representation 1',
                            iconName: 'DeleteIcon',
                          },
                        },
                        {
                          id: 'item 2',
                          imageURL: '',
                          label: 'Representation 2',
                          action: {
                            tooltip: 'Delete Representation 2',
                            iconName: 'DeleteIcon',
                          },
                        },
                        {
                          id: 'item 3',
                          imageURL: '',
                          label: 'Representation 3',
                          action: {
                            tooltip: 'Delete Representation 3',
                            iconName: 'DeleteIcon',
                          },
                        },
                      ],
                    } as List,
                  ],
                },
              ],
            },
          ],
        },
      };

      pubSub.publish('FORM_REFRESHED', {
        representationsEvent,
      });
    }
  }, [pubSub, representationsWebSocketContainer]);

  useEffect(() => {
    pubSub
      .asyncIterator<GQLPropertiesEventSubscription>(['FORM_REFRESHED'])
      .next()
      .then((value) => {
        let result: SubscriptionResult<GQLPropertiesEventSubscription> = {
          loading: false,
          data: value.value,
        };
        const handleDataEvent: HandleSubscriptionResultEvent = {
          type: 'HANDLE_SUBSCRIPTION_RESULT',
          result,
        };
        dispatch(handleDataEvent);
      });
  }, [dispatch, pubSub]);

  let content = null;
  if (!selection || representationsWebSocketContainer === 'unsupportedSelection') {
    content = (
      <div className={classes.idle}>
        <Typography variant="subtitle2">No object selected</Typography>
      </div>
    );
  }
  if ((representationsWebSocketContainer === 'idle' && form) || representationsWebSocketContainer === 'ready') {
    content = (
      <Properties
        editingContextId={editingContextId}
        subscribers={subscribers}
        widgetSubscriptions={widgetSubscriptions}
        form={form}
        readOnly={readOnly}
      />
    );
  }

  return (
    <>
      {content}
      <Snackbar
        anchorOrigin={{
          vertical: 'bottom',
          horizontal: 'right',
        }}
        open={toast === 'visible'}
        autoHideDuration={3000}
        onClose={() => dispatch({ type: 'HIDE_TOAST' } as HideToastEvent)}
        message={message}
        action={
          <IconButton
            size="small"
            aria-label="close"
            color="inherit"
            onClick={() => dispatch({ type: 'HIDE_TOAST' } as HideToastEvent)}>
            <CloseIcon fontSize="small" />
          </IconButton>
        }
        data-testid="error"
      />
    </>
  );
};

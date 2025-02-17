/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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

import { Node, NodePositionChange, Position, XYPosition } from 'reactflow';
import { NodeData } from '../DiagramRenderer.types';
import { ConnectionHandle } from '../handles/ConnectionHandles.types';
export interface EdgeParameters {
  sourcePosition: Position;
  targetPosition: Position;
}

export type GetUpdatedConnectionHandlesParameters = (
  sourceNode: Node<NodeData>,
  targetNode: Node<NodeData>,
  sourcePosition: Position,
  targetPosition: Position,
  sourceHandle: string,
  targetHandle: string
) => GetUpdatedConnectionHandles;

export interface GetUpdatedConnectionHandles {
  sourceConnectionHandles: ConnectionHandle[];
  targetConnectionHandles: ConnectionHandle[];
}

export type GetEdgeParametersWhileMoving = (
  movingNode: NodePositionChange,
  source: Node<NodeData>,
  target: Node<NodeData>,
  visiblesNodes: Node<NodeData>[]
) => EdgeParameters;

export type GetEdgeParameters = (
  source: Node<NodeData>,
  target: Node<NodeData>,
  visiblesNodes: Node<NodeData>[]
) => EdgeParameters;

export interface EdgeParameters {
  sourcePosition: Position;
  targetPosition: Position;
}

export type GetParameters = (
  movingNode: NodePositionChange | null,
  nodeA: Node<NodeData>,
  nodeB: Node<NodeData>,
  visiblesNodes: Node<NodeData>[]
) => Parameters;

export interface Parameters {
  position: Position;
}

export type GetNodeCenter = (node: Node<NodeData>, visiblesNodes: Node<NodeData>[]) => NodeCenter;

export interface NodeCenter {
  x: number;
  y: number;
}

export type GetHandleCoordinatesByPosition = (
  node: Node<NodeData>,
  handlePosition: Position,
  handleId: string
) => XYPosition;

export type GetHandleCoordinatesByPosition2 = (
  node: Node<NodeData>,
  handlePosition: Position,
  edgeId: string
) => HandleCoordinates;

export interface HandleCoordinates {
  x: number;
  y: number;
}

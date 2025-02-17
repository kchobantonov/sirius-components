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
import { NodeProps } from 'reactflow';
import { INodeConverterHandler } from '../converter/ConvertEngine.types';
import { GraphQLNodeStyleFragment } from '../graphql/subscription/nodeFragment.types';
import { NodeData } from '../renderer/DiagramRenderer.types';
import { INodeLayoutHandler } from '../renderer/layout/LayoutEngine.types';

export interface NodeTypeContributionProps {
  component: (props: NodeProps) => JSX.Element | null;
  type: string;
}

export type NodeTypeContributionElement = React.ReactElement<NodeTypeContributionProps>;

export interface NodeTypeContextValue {
  graphQLNodeStyleFragments: GraphQLNodeStyleFragment[];
  nodeLayoutHandlers: INodeLayoutHandler<NodeData>[];
  nodeConverterHandlers: INodeConverterHandler[];
  nodeTypeContributions: NodeTypeContributionElement[];
}

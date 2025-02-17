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

import React from 'react';
import { DiagramDescriptionContextValue } from './DiagramDescriptionContext.types';

const defaultValue: DiagramDescriptionContextValue = {
  diagramDescription: {
    id: '',
    nodeDescriptions: [],
    dropNodeCompatibility: [],
    debug: false,
  },
};

export const DiagramDescriptionContext = React.createContext<DiagramDescriptionContextValue>(defaultValue);

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
package org.eclipse.sirius.components.collaborative.diagrams.dto;

import java.util.UUID;

import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramInput;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;

/**
 * Input for "get child node descriptions" query.
 *
 * @author frouene
 */
public record GetNodeDescriptionChildNodeDescriptionsInput(UUID id, String editingContextId, String representationId, NodeDescription nodeDescription) implements IDiagramInput {

}

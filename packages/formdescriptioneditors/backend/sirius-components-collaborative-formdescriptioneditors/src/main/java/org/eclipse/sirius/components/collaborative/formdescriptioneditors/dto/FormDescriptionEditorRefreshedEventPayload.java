/*******************************************************************************
 * Copyright (c) 2022 Obeo.
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
package org.eclipse.sirius.components.collaborative.formdescriptioneditors.dto;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.formdescriptioneditors.FormDescriptionEditor;

/**
 * Payload used to indicate that the form description editor has been refreshed.
 *
 * @author arichard
 */
public final class FormDescriptionEditorRefreshedEventPayload implements IPayload {
    private final UUID id;

    private final FormDescriptionEditor formDescriptionEditor;

    public FormDescriptionEditorRefreshedEventPayload(UUID id, FormDescriptionEditor formDescriptionEditor) {
        this.id = Objects.requireNonNull(id);
        this.formDescriptionEditor = Objects.requireNonNull(formDescriptionEditor);
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    public FormDescriptionEditor getFormDescriptionEditor() {
        return this.formDescriptionEditor;
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'id: {1}, formDescriptionEditor: '{'id: {2}, label: {3}'}''}'";
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.id, this.formDescriptionEditor.getId(), this.formDescriptionEditor.getLabel());
    }
}

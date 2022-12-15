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
package org.eclipse.sirius.components.formdescriptioneditors;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.annotations.Immutable;
import org.eclipse.sirius.components.forms.Group;
import org.eclipse.sirius.components.representations.IRepresentation;
import org.eclipse.sirius.components.representations.ISemanticRepresentation;

/**
 * Root concept of the form description editor representation.
 *
 * @author arichard
 */
@Immutable
public final class FormDescriptionEditor implements IRepresentation, ISemanticRepresentation {

    public static final String KIND = IRepresentation.KIND_PREFIX + "?type=FormDescriptionEditor";

    public static final String LABEL = "label";

    private String id;

    private String kind;

    private String label;

    private String targetObjectId;

    private String descriptionId;

    private List<Group> groups;

    private FormDescriptionEditor() {
        // Prevent instantiation
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getKind() {
        return this.kind;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    @Override
    public String getTargetObjectId() {
        return this.targetObjectId;
    }

    @Override
    public String getDescriptionId() {
        return this.descriptionId;
    }

    public List<Group> getGroups() {
        return this.groups;
    }

    public static Builder newFormDescriptionEditor(String id) {
        return new Builder(id);
    }

    public static Builder newFormDescriptionEditor(FormDescriptionEditor formDescriptionEditor) {
        return new Builder(formDescriptionEditor);
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'id: {1}, label: {2}, targetObjectId: {3}, descriptionId: {4}'}'";
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.id, this.label, this.targetObjectId, this.descriptionId);
    }

    /**
     * The builder used to create the form description editor.
     *
     * @author arichard
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public static final class Builder {
        private String id;

        private String kind = KIND;

        private String label;

        private String targetObjectId;

        private String descriptionId;

        private List<Group> groups;

        private Builder(String id) {
            this.id = Objects.requireNonNull(id);
        }

        public Builder(FormDescriptionEditor formDescriptionEditor) {
            this.id = formDescriptionEditor.id;
            this.label = formDescriptionEditor.label;
            this.targetObjectId = formDescriptionEditor.targetObjectId;
            this.descriptionId = formDescriptionEditor.descriptionId;
            this.groups = formDescriptionEditor.groups;
        }

        public Builder label(String label) {
            this.label = Objects.requireNonNull(label);
            return this;
        }

        public Builder targetObjectId(String targetObjectId) {
            this.targetObjectId = Objects.requireNonNull(targetObjectId);
            return this;
        }

        public Builder descriptionId(String descriptionId) {
            this.descriptionId = Objects.requireNonNull(descriptionId);
            return this;
        }

        public Builder groups(List<Group> groups) {
            this.groups = Objects.requireNonNull(groups);
            return this;
        }

        public FormDescriptionEditor build() {
            FormDescriptionEditor formDescriptionEditor = new FormDescriptionEditor();
            formDescriptionEditor.id = Objects.requireNonNull(this.id);
            formDescriptionEditor.kind = Objects.requireNonNull(this.kind);
            formDescriptionEditor.label = Objects.requireNonNull(this.label);
            formDescriptionEditor.targetObjectId = Objects.requireNonNull(this.targetObjectId);
            formDescriptionEditor.descriptionId = Objects.requireNonNull(this.descriptionId);
            formDescriptionEditor.groups = Objects.requireNonNull(this.groups);
            return formDescriptionEditor;
        }
    }
}

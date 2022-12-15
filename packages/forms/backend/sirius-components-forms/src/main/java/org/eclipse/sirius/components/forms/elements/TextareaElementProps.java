/*******************************************************************************
 * Copyright (c) 2019, 2022 Obeo.
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
package org.eclipse.sirius.components.forms.elements;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.eclipse.sirius.components.annotations.Immutable;
import org.eclipse.sirius.components.forms.CompletionProposal;
import org.eclipse.sirius.components.forms.CompletionRequest;
import org.eclipse.sirius.components.forms.TextareaStyle;
import org.eclipse.sirius.components.representations.Element;
import org.eclipse.sirius.components.representations.IProps;
import org.eclipse.sirius.components.representations.IStatus;

/**
 * The properties of the textarea element.
 *
 * @author sbegaudeau
 */
@Immutable
public final class TextareaElementProps implements IProps {
    public static final String TYPE = "Textarea";

    private String id;

    private String label;

    private String iconURL;

    private String value;

    private Function<String, IStatus> newValueHandler;

    private Function<CompletionRequest, List<CompletionProposal>> completionProposalsProvider;

    private TextareaStyle style;

    private List<Element> children;

    private TextareaElementProps() {
        // Prevent instantiation
    }

    public String getId() {
        return this.id;
    }

    public String getLabel() {
        return this.label;
    }

    public String getIconURL() {
        return this.iconURL;
    }

    public String getValue() {
        return this.value;
    }

    public Function<String, IStatus> getNewValueHandler() {
        return this.newValueHandler;
    }

    public TextareaStyle getStyle() {
        return this.style;
    }

    public Function<CompletionRequest, List<CompletionProposal>> getCompletionProposalsProvider() {
        return this.completionProposalsProvider;
    }

    @Override
    public List<Element> getChildren() {
        return this.children;
    }

    public static Builder newTextareaElementProps(String id) {
        return new Builder(id);
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'id: {1}, label: {2}, value: {3}, supportsCompletion: {4}'}'";
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.id, this.label, this.value, this.completionProposalsProvider != null);
    }

    /**
     * The builder of the textarea element props.
     *
     * @author sbegaudeau
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public static final class Builder {
        private String id;

        private String label;

        private String iconURL;

        private String value;

        private Function<String, IStatus> newValueHandler;

        private TextareaStyle style;

        private Function<CompletionRequest, List<CompletionProposal>> completionProposalsProvider;

        private List<Element> children;

        private Builder(String id) {
            this.id = Objects.requireNonNull(id);
        }

        public Builder label(String label) {
            this.label = Objects.requireNonNull(label);
            return this;
        }

        public Builder iconURL(String iconURL) {
            this.iconURL = Objects.requireNonNull(iconURL);
            return this;
        }

        public Builder value(String value) {
            this.value = Objects.requireNonNull(value);
            return this;
        }

        public Builder newValueHandler(Function<String, IStatus> handler) {
            this.newValueHandler = Objects.requireNonNull(handler);
            return this;
        }

        public Builder style(TextareaStyle style) {
            this.style = Objects.requireNonNull(style);
            return this;
        }

        public Builder completionProposalsProvider(Function<CompletionRequest, List<CompletionProposal>> completionProposalsProvider) {
            this.completionProposalsProvider = Objects.requireNonNull(completionProposalsProvider);
            return this;
        }

        public Builder children(List<Element> children) {
            this.children = Objects.requireNonNull(children);
            return this;
        }

        public TextareaElementProps build() {
            TextareaElementProps textareaElementProps = new TextareaElementProps();
            textareaElementProps.id = Objects.requireNonNull(this.id);
            textareaElementProps.label = Objects.requireNonNull(this.label);
            textareaElementProps.iconURL = this.iconURL;
            textareaElementProps.value = Objects.requireNonNull(this.value);
            textareaElementProps.newValueHandler = Objects.requireNonNull(this.newValueHandler);
            textareaElementProps.style = this.style; // Optional on purpose
            textareaElementProps.completionProposalsProvider = this.completionProposalsProvider; // Optional on purpose
            textareaElementProps.children = Objects.requireNonNull(this.children);
            return textareaElementProps;
        }
    }
}

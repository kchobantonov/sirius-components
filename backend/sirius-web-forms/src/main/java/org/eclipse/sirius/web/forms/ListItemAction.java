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
package org.eclipse.sirius.web.forms;

import java.text.MessageFormat;
import java.util.Objects;

import org.eclipse.sirius.web.annotations.Immutable;
import org.eclipse.sirius.web.annotations.graphql.GraphQLField;
import org.eclipse.sirius.web.annotations.graphql.GraphQLNonNull;
import org.eclipse.sirius.web.annotations.graphql.GraphQLObjectType;

/**
 * The action of a {@link ListItem}.
 *
 * @author gcoutable
 */
@Immutable
@GraphQLObjectType
public final class ListItemAction {

    private String tooltip;

    private String iconName;

    private ListItemAction() {
        // Prevent instantiation
    }

    @GraphQLField
    @GraphQLNonNull
    public String getTooltip() {
        return this.tooltip;
    }

    @GraphQLField
    @GraphQLNonNull
    public String getIconName() {
        return this.iconName;
    }

    public static Builder newListItemAction() {
        return new Builder();
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'tooltip: {1}, iconName: {2}'}'"; //$NON-NLS-1$
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.tooltip, this.iconName);
    }

    /**
     * The builder used to create the list item action.
     *
     * @author gcoutable
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public static final class Builder {
        private String tooltip;

        private String iconName;

        public Builder tooltip(String tooltip) {
            this.tooltip = Objects.requireNonNull(tooltip);
            return this;
        }

        public Builder iconName(String iconName) {
            this.iconName = Objects.requireNonNull(iconName);
            return this;
        }

        public ListItemAction build() {
            ListItemAction listItemAction = new ListItemAction();
            listItemAction.tooltip = Objects.requireNonNull(this.tooltip);
            listItemAction.iconName = Objects.requireNonNull(this.iconName);
            return listItemAction;
        }
    }

}

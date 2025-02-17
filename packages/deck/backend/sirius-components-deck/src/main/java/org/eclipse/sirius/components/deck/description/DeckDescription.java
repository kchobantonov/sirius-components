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
package org.eclipse.sirius.components.deck.description;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import org.eclipse.sirius.components.annotations.PublicApi;
import org.eclipse.sirius.components.representations.IRepresentationDescription;
import org.eclipse.sirius.components.representations.VariableManager;

/**
 * The root concept of the description of a deck representation.
 *
 * @author fbarbin
 */
@PublicApi
public record DeckDescription(String id, String label, Function<VariableManager, String> idProvider, Function<VariableManager, String> labelProvider,
        Function<VariableManager, String> targetObjectIdProvider, Predicate<VariableManager> canCreatePredicate, List<LaneDescription> laneDescriptions) implements IRepresentationDescription {

    public static final String LABEL = "label";

    public DeckDescription {
        Objects.requireNonNull(id);
        Objects.requireNonNull(label);
        Objects.requireNonNull(idProvider);
        Objects.requireNonNull(labelProvider);
        Objects.requireNonNull(targetObjectIdProvider);
        Objects.requireNonNull(canCreatePredicate);
        Objects.requireNonNull(laneDescriptions);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    @Override
    public Predicate<VariableManager> getCanCreatePredicate() {
        return this.canCreatePredicate;
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'id: {1}, label: {2}'}'";
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.id, this.label);
    }

}

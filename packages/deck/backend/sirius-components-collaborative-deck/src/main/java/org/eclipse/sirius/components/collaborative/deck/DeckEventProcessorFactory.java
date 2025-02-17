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
package org.eclipse.sirius.components.collaborative.deck;

import java.util.Objects;
import java.util.Optional;

import org.eclipse.sirius.components.collaborative.api.IRepresentationConfiguration;
import org.eclipse.sirius.components.collaborative.api.IRepresentationEventProcessor;
import org.eclipse.sirius.components.collaborative.api.IRepresentationEventProcessorFactory;
import org.eclipse.sirius.components.collaborative.api.IRepresentationSearchService;
import org.eclipse.sirius.components.collaborative.api.ISubscriptionManagerFactory;
import org.eclipse.sirius.components.collaborative.deck.api.IDeckEventProcessor;
import org.eclipse.sirius.components.collaborative.deck.service.DeckCreationService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.deck.Deck;
import org.springframework.stereotype.Service;

/**
 * Used to create the deck event processors.
 *
 * @author fbarbin
 */
@Service
public class DeckEventProcessorFactory implements IRepresentationEventProcessorFactory {

    private final IRepresentationSearchService representationSearchService;

    private final DeckCreationService deckCreationService;

    private final ISubscriptionManagerFactory subscriptionManagerFactory;

    public DeckEventProcessorFactory(IRepresentationSearchService representationSearchService, DeckCreationService deckCreationService,
            ISubscriptionManagerFactory subscriptionManagerFactory) {
        this.representationSearchService = Objects.requireNonNull(representationSearchService);
        this.deckCreationService = Objects.requireNonNull(deckCreationService);
        this.subscriptionManagerFactory = Objects.requireNonNull(subscriptionManagerFactory);
    }

    @Override
    public <T extends IRepresentationEventProcessor> boolean canHandle(Class<T> representationEventProcessorClass, IRepresentationConfiguration configuration) {
        return IDeckEventProcessor.class.isAssignableFrom(representationEventProcessorClass) && configuration instanceof DeckConfiguration;
    }

    @Override
    public <T extends IRepresentationEventProcessor> Optional<T> createRepresentationEventProcessor(Class<T> representationEventProcessorClass, IRepresentationConfiguration configuration,
            IEditingContext editingContext) {
        if (IDeckEventProcessor.class.isAssignableFrom(representationEventProcessorClass) && configuration instanceof DeckConfiguration deckConfiguration) {
            var optionalDeck = this.representationSearchService.findById(editingContext, deckConfiguration.getId(), Deck.class);
            if (optionalDeck.isPresent()) {
                Deck deck = optionalDeck.get();

                IRepresentationEventProcessor deckEventProcessor = new DeckEventProcessor(editingContext, deck,
                        this.subscriptionManagerFactory.create(), this.deckCreationService);

                return Optional.of(deckEventProcessor)
                        .map(representationEventProcessorClass::cast);
            }
        }
        return Optional.empty();
    }
}

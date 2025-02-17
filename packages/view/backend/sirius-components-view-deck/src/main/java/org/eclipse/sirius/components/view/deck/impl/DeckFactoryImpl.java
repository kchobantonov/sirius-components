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
package org.eclipse.sirius.components.view.deck.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.sirius.components.view.deck.CardDescription;
import org.eclipse.sirius.components.view.deck.DeckDescription;
import org.eclipse.sirius.components.view.deck.DeckFactory;
import org.eclipse.sirius.components.view.deck.DeckPackage;
import org.eclipse.sirius.components.view.deck.LaneDescription;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 *
 * @generated
 */
public class DeckFactoryImpl extends EFactoryImpl implements DeckFactory {
    /**
     * Creates the default factory implementation. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public static DeckFactory init() {
        try {
            DeckFactory theDeckFactory = (DeckFactory) EPackage.Registry.INSTANCE.getEFactory(DeckPackage.eNS_URI);
            if (theDeckFactory != null) {
                return theDeckFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new DeckFactoryImpl();
    }

    /**
     * Creates an instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public DeckFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case DeckPackage.DECK_DESCRIPTION:
                return this.createDeckDescription();
            case DeckPackage.LANE_DESCRIPTION:
                return this.createLaneDescription();
            case DeckPackage.CARD_DESCRIPTION:
                return this.createCardDescription();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public DeckDescription createDeckDescription() {
        DeckDescriptionImpl deckDescription = new DeckDescriptionImpl();
        return deckDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public LaneDescription createLaneDescription() {
        LaneDescriptionImpl laneDescription = new LaneDescriptionImpl();
        return laneDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CardDescription createCardDescription() {
        CardDescriptionImpl cardDescription = new CardDescriptionImpl();
        return cardDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public DeckPackage getDeckPackage() {
        return (DeckPackage) this.getEPackage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @deprecated
     * @generated
     */
    @Deprecated
    public static DeckPackage getPackage() {
        return DeckPackage.eINSTANCE;
    }

} // DeckFactoryImpl

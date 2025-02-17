/*******************************************************************************
 * Copyright (c) 2023, 2023 Obeo.
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
package org.eclipse.sirius.web.customnodes;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.sirius.components.view.diagram.DiagramPackage;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each operation of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 *
 * @see org.eclipse.sirius.web.customnodes.CustomnodesFactory
 * @model kind="package"
 * @generated
 */
public interface CustomnodesPackage extends EPackage {

    /**
     * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    String eNAME = "customnodes";

    /**
     * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    String eNS_URI = "http://www.eclipse.org/sirius-web/customnodes";

    /**
     * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    String eNS_PREFIX = "customnodes";

    /**
     * The meta object id for the '{@link org.eclipse.sirius.web.customnodes.impl.EllipseNodeStyleDescriptionImpl
     * <em>Ellipse Node Style Description</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.eclipse.sirius.web.customnodes.impl.EllipseNodeStyleDescriptionImpl
     * @see org.eclipse.sirius.web.customnodes.impl.CustomnodesPackageImpl#getEllipseNodeStyleDescription()
     * @generated
     */
    int ELLIPSE_NODE_STYLE_DESCRIPTION = 0;

    /**
     * The singleton instance of the package. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    CustomnodesPackage eINSTANCE = org.eclipse.sirius.web.customnodes.impl.CustomnodesPackageImpl.init();

    /**
     * The feature id for the '<em><b>Color</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELLIPSE_NODE_STYLE_DESCRIPTION__COLOR = DiagramPackage.NODE_STYLE_DESCRIPTION__COLOR;

    /**
     * The feature id for the '<em><b>Font Size</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELLIPSE_NODE_STYLE_DESCRIPTION__FONT_SIZE = DiagramPackage.NODE_STYLE_DESCRIPTION__FONT_SIZE;

    /**
     * The feature id for the '<em><b>Italic</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELLIPSE_NODE_STYLE_DESCRIPTION__ITALIC = DiagramPackage.NODE_STYLE_DESCRIPTION__ITALIC;

    /**
     * The feature id for the '<em><b>Bold</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELLIPSE_NODE_STYLE_DESCRIPTION__BOLD = DiagramPackage.NODE_STYLE_DESCRIPTION__BOLD;

    /**
     * The feature id for the '<em><b>Underline</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELLIPSE_NODE_STYLE_DESCRIPTION__UNDERLINE = DiagramPackage.NODE_STYLE_DESCRIPTION__UNDERLINE;

    /**
     * The feature id for the '<em><b>Strike Through</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELLIPSE_NODE_STYLE_DESCRIPTION__STRIKE_THROUGH = DiagramPackage.NODE_STYLE_DESCRIPTION__STRIKE_THROUGH;

    /**
     * The feature id for the '<em><b>Border Color</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELLIPSE_NODE_STYLE_DESCRIPTION__BORDER_COLOR = DiagramPackage.NODE_STYLE_DESCRIPTION__BORDER_COLOR;

    /**
     * The feature id for the '<em><b>Border Radius</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELLIPSE_NODE_STYLE_DESCRIPTION__BORDER_RADIUS = DiagramPackage.NODE_STYLE_DESCRIPTION__BORDER_RADIUS;

    /**
     * The feature id for the '<em><b>Border Size</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELLIPSE_NODE_STYLE_DESCRIPTION__BORDER_SIZE = DiagramPackage.NODE_STYLE_DESCRIPTION__BORDER_SIZE;

    /**
     * The feature id for the '<em><b>Border Line Style</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @generated
     * @ordered
     */
    int ELLIPSE_NODE_STYLE_DESCRIPTION__BORDER_LINE_STYLE = DiagramPackage.NODE_STYLE_DESCRIPTION__BORDER_LINE_STYLE;

    /**
     * The feature id for the '<em><b>Label Color</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELLIPSE_NODE_STYLE_DESCRIPTION__LABEL_COLOR = DiagramPackage.NODE_STYLE_DESCRIPTION__LABEL_COLOR;

    /**
     * The feature id for the '<em><b>Show Icon</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELLIPSE_NODE_STYLE_DESCRIPTION__SHOW_ICON = DiagramPackage.NODE_STYLE_DESCRIPTION__SHOW_ICON;

    /**
     * The feature id for the '<em><b>Label Icon</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELLIPSE_NODE_STYLE_DESCRIPTION__LABEL_ICON = DiagramPackage.NODE_STYLE_DESCRIPTION__LABEL_ICON;

    /**
     * The number of structural features of the '<em>Ellipse Node Style Description</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELLIPSE_NODE_STYLE_DESCRIPTION_FEATURE_COUNT = DiagramPackage.NODE_STYLE_DESCRIPTION_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Ellipse Node Style Description</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELLIPSE_NODE_STYLE_DESCRIPTION_OPERATION_COUNT = DiagramPackage.NODE_STYLE_DESCRIPTION_OPERATION_COUNT + 0;

    /**
     * Returns the meta object for class '{@link org.eclipse.sirius.web.customnodes.EllipseNodeStyleDescription
     * <em>Ellipse Node Style Description</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Ellipse Node Style Description</em>'.
     * @see org.eclipse.sirius.web.customnodes.EllipseNodeStyleDescription
     * @generated
     */
    EClass getEllipseNodeStyleDescription();

    /**
     * Returns the factory that creates the instances of the model. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the factory that creates the instances of the model.
     * @generated
     */
    CustomnodesFactory getCustomnodesFactory();

    /**
     * <!-- begin-user-doc --> Defines literals for the meta objects that represent
     * <ul>
     * <li>each class,</li>
     * <li>each feature of each class,</li>
     * <li>each operation of each class,</li>
     * <li>each enum,</li>
     * <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     *
     * @generated
     */
    interface Literals {

        /**
         * The meta object literal for the
         * '{@link org.eclipse.sirius.web.customnodes.impl.EllipseNodeStyleDescriptionImpl <em>Ellipse Node Style
         * Description</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
         *
         * @see org.eclipse.sirius.web.customnodes.impl.EllipseNodeStyleDescriptionImpl
         * @see org.eclipse.sirius.web.customnodes.impl.CustomnodesPackageImpl#getEllipseNodeStyleDescription()
         * @generated
         */
        EClass ELLIPSE_NODE_STYLE_DESCRIPTION = eINSTANCE.getEllipseNodeStyleDescription();

    }

} // CustomnodesPackage

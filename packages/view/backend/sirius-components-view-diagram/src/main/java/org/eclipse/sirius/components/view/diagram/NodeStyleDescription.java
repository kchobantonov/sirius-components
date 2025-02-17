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
package org.eclipse.sirius.components.view.diagram;

import org.eclipse.sirius.components.view.LabelStyle;
import org.eclipse.sirius.components.view.UserColor;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Node Style Description</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.sirius.components.view.diagram.NodeStyleDescription#getLabelColor <em>Label Color</em>}</li>
 * <li>{@link org.eclipse.sirius.components.view.diagram.NodeStyleDescription#isShowIcon <em>Show Icon</em>}</li>
 * <li>{@link org.eclipse.sirius.components.view.diagram.NodeStyleDescription#getLabelIcon <em>Label Icon</em>}</li>
 * </ul>
 *
 * @see org.eclipse.sirius.components.view.diagram.DiagramPackage#getNodeStyleDescription()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface NodeStyleDescription extends Style, LabelStyle, BorderStyle {
    /**
     * Returns the value of the '<em><b>Label Color</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Label Color</em>' reference.
     * @see #setLabelColor(UserColor)
     * @see org.eclipse.sirius.components.view.diagram.DiagramPackage#getNodeStyleDescription_LabelColor()
     * @model
     * @generated
     */
    UserColor getLabelColor();

    /**
     * Sets the value of the '{@link org.eclipse.sirius.components.view.diagram.NodeStyleDescription#getLabelColor
     * <em>Label Color</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Label Color</em>' reference.
     * @see #getLabelColor()
     * @generated
     */
    void setLabelColor(UserColor value);

    /**
     * Returns the value of the '<em><b>Show Icon</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Show Icon</em>' attribute.
     * @see #setShowIcon(boolean)
     * @see org.eclipse.sirius.components.view.diagram.DiagramPackage#getNodeStyleDescription_ShowIcon()
     * @model
     * @generated
     */
    boolean isShowIcon();

    /**
     * Sets the value of the '{@link org.eclipse.sirius.components.view.diagram.NodeStyleDescription#isShowIcon <em>Show
     * Icon</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Show Icon</em>' attribute.
     * @see #isShowIcon()
     * @generated
     */
    void setShowIcon(boolean value);

    /**
     * Returns the value of the '<em><b>Label Icon</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Label Icon</em>' attribute.
     * @see #setLabelIcon(String)
     * @see org.eclipse.sirius.components.view.diagram.DiagramPackage#getNodeStyleDescription_LabelIcon()
     * @model
     * @generated
     */
    String getLabelIcon();

    /**
     * Sets the value of the '{@link org.eclipse.sirius.components.view.diagram.NodeStyleDescription#getLabelIcon
     * <em>Label Icon</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Label Icon</em>' attribute.
     * @see #getLabelIcon()
     * @generated
     */
    void setLabelIcon(String value);

} // NodeStyleDescription

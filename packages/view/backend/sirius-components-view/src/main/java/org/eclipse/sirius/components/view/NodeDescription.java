/*******************************************************************************
 * Copyright (c) 2021, 2023 Obeo.
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
package org.eclipse.sirius.components.view;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Node Description</b></em>'. <!-- end-user-doc
 * -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.sirius.components.view.NodeDescription#getChildrenDescriptions <em>Children
 * Descriptions</em>}</li>
 * </ul>
 *
 * @see org.eclipse.sirius.components.view.ViewPackage#getNodeDescription()
 * @model
 * @generated
 */
public interface NodeDescription extends DiagramElementDescription {
    /**
     * Returns the value of the '<em><b>Children Descriptions</b></em>' containment reference list. The list contents
     * are of type {@link org.eclipse.sirius.components.view.NodeDescription}. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>Children Descriptions</em>' containment reference list.
     * @see org.eclipse.sirius.components.view.ViewPackage#getNodeDescription_ChildrenDescriptions()
     * @model containment="true"
     * @generated
     */
    EList<NodeDescription> getChildrenDescriptions();

    /**
     * Returns the value of the '<em><b>Border Nodes Descriptions</b></em>' containment reference list. The list
     * contents are of type {@link org.eclipse.sirius.components.view.NodeDescription}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Border Nodes Descriptions</em>' containment reference list.
     * @see org.eclipse.sirius.components.view.ViewPackage#getNodeDescription_BorderNodesDescriptions()
     * @model containment="true"
     * @generated
     */
    EList<NodeDescription> getBorderNodesDescriptions();

    /**
     * Returns the value of the '<em><b>Reused Child Node Descriptions</b></em>' reference list. The list contents are
     * of type {@link org.eclipse.sirius.components.view.NodeDescription}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Reused Child Node Descriptions</em>' reference list.
     * @see org.eclipse.sirius.components.view.ViewPackage#getNodeDescription_ReusedChildNodeDescriptions()
     * @model
     * @generated
     */
    EList<NodeDescription> getReusedChildNodeDescriptions();

    /**
     * Returns the value of the '<em><b>Reused Border Node Descriptions</b></em>' reference list. The list contents are
     * of type {@link org.eclipse.sirius.components.view.NodeDescription}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Reused Border Node Descriptions</em>' reference list.
     * @see org.eclipse.sirius.components.view.ViewPackage#getNodeDescription_ReusedBorderNodeDescriptions()
     * @model
     * @generated
     */
    EList<NodeDescription> getReusedBorderNodeDescriptions();

    /**
     * Returns the value of the '<em><b>Style</b></em>' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>Style</em>' containment reference.
     * @see #setStyle(NodeStyle)
     * @see org.eclipse.sirius.components.view.ViewPackage#getNodeDescription_Style()
     * @model containment="true"
     * @generated
     */
    NodeStyleDescription getStyle();

    /**
     * Sets the value of the '{@link org.eclipse.sirius.components.view.NodeDescription#getStyle <em>Style</em>}'
     * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Style</em>' containment reference.
     * @see #getStyle()
     * @generated
     */
    void setStyle(NodeStyleDescription value);

    /**
     * Returns the value of the '<em><b>User Resizable</b></em>' attribute. The default value is <code>"true"</code>.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>User Resizable</em>' attribute.
     * @see #setUserResizable(boolean)
     * @see org.eclipse.sirius.components.view.ViewPackage#getNodeDescription_UserResizable()
     * @model default="true" required="true"
     * @generated
     */
    boolean isUserResizable();

    /**
     * Sets the value of the '{@link org.eclipse.sirius.components.view.NodeDescription#isUserResizable <em>User
     * Resizable</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>User Resizable</em>' attribute.
     * @see #isUserResizable()
     * @generated
     */
    void setUserResizable(boolean value);

    /**
     * Returns the value of the '<em><b>Node Tools</b></em>' containment reference list. The list contents are of type
     * {@link org.eclipse.sirius.components.view.NodeTool}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Node Tools</em>' containment reference list.
     * @see org.eclipse.sirius.components.view.ViewPackage#getNodeDescription_NodeTools()
     * @model containment="true"
     * @generated
     */
    EList<NodeTool> getNodeTools();

    /**
     * Returns the value of the '<em><b>Conditional Styles</b></em>' containment reference list. The list contents are
     * of type {@link org.eclipse.sirius.components.view.ConditionalNodeStyle}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Conditional Styles</em>' containment reference list.
     * @see org.eclipse.sirius.components.view.ViewPackage#getNodeDescription_ConditionalStyles()
     * @model containment="true"
     * @generated
     */
    EList<ConditionalNodeStyle> getConditionalStyles();

    /**
     * Returns the value of the '<em><b>Children Layout Strategy</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Children Layout Strategy</em>' containment reference.
     * @see #setChildrenLayoutStrategy(LayoutStrategyDescription)
     * @see org.eclipse.sirius.components.view.ViewPackage#getNodeDescription_ChildrenLayoutStrategy()
     * @model containment="true"
     * @generated
     */
    LayoutStrategyDescription getChildrenLayoutStrategy();

    /**
     * Sets the value of the '{@link org.eclipse.sirius.components.view.NodeDescription#getChildrenLayoutStrategy
     * <em>Children Layout Strategy</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Children Layout Strategy</em>' containment reference.
     * @see #getChildrenLayoutStrategy()
     * @generated
     */
    void setChildrenLayoutStrategy(LayoutStrategyDescription value);

    /**
     * Returns the value of the '<em><b>Collapsible</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Collapsible</em>' attribute.
     * @see #setCollapsible(boolean)
     * @see org.eclipse.sirius.components.view.ViewPackage#getNodeDescription_Collapsible()
     * @model
     * @generated
     */
    boolean isCollapsible();

    /**
     * Sets the value of the '{@link org.eclipse.sirius.components.view.NodeDescription#isCollapsible
     * <em>Collapsible</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Collapsible</em>' attribute.
     * @see #isCollapsible()
     * @generated
     */
    void setCollapsible(boolean value);

} // NodeDescription

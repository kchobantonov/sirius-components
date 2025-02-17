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
package org.eclipse.sirius.web.sample.papaya.view.logicalarchitecture;

import java.util.Objects;

import org.eclipse.sirius.components.view.ViewFactory;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.IEdgeDescriptionProvider;
import org.eclipse.sirius.components.view.diagram.ArrowStyle;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.DiagramFactory;
import org.eclipse.sirius.components.view.diagram.EdgeDescription;
import org.eclipse.sirius.components.view.diagram.LabelEditTool;
import org.eclipse.sirius.components.view.diagram.LineStyle;

/**
 * Description implements interface.
 *
 * @author sbegaudeau
 */
public class ImplementsInterfaceEdgeDescriptionProvider implements IEdgeDescriptionProvider {

    private final IColorProvider colorProvider;

    public ImplementsInterfaceEdgeDescriptionProvider(IColorProvider colorProvider) {
        this.colorProvider = Objects.requireNonNull(colorProvider);
    }

    @Override
    public EdgeDescription create() {
        var implementsInterfaceEdgeStyle = DiagramFactory.eINSTANCE.createEdgeStyle();
        implementsInterfaceEdgeStyle.setColor(this.colorProvider.getColor("color_blue_2"));
        implementsInterfaceEdgeStyle.setEdgeWidth(1);
        implementsInterfaceEdgeStyle.setLineStyle(LineStyle.DASH);
        implementsInterfaceEdgeStyle.setSourceArrowStyle(ArrowStyle.NONE);
        implementsInterfaceEdgeStyle.setTargetArrowStyle(ArrowStyle.INPUT_CLOSED_ARROW);

        var implementsInterfaceEdgeDescription = DiagramFactory.eINSTANCE.createEdgeDescription();
        implementsInterfaceEdgeDescription.setName("Edge Implements interface");
        implementsInterfaceEdgeDescription.setLabelExpression("");
        implementsInterfaceEdgeDescription.setBeginLabelExpression("aql:'implements ' + semanticEdgeTarget.name");

        implementsInterfaceEdgeDescription.setEndLabelExpression("aql:'implemented by ' + semanticEdgeSource.name");
        implementsInterfaceEdgeDescription.setStyle(implementsInterfaceEdgeStyle);
        implementsInterfaceEdgeDescription.setSourceNodesExpression("aql:self");
        implementsInterfaceEdgeDescription.setTargetNodesExpression("aql:self.implements");
        implementsInterfaceEdgeDescription.setIsDomainBasedEdge(false);

        var edgePalette = DiagramFactory.eINSTANCE.createEdgePalette();
        implementsInterfaceEdgeDescription.setPalette(edgePalette);
        edgePalette.setBeginLabelEditTool(this.editImplementsInterfaceEdgeBeginLabel());
        edgePalette.setEndLabelEditTool(this.editImplementsInterfaceEdgeEndLabel());

        return implementsInterfaceEdgeDescription;
    }

    private LabelEditTool editImplementsInterfaceEdgeBeginLabel() {
        var editLabelTool = DiagramFactory.eINSTANCE.createLabelEditTool();
        editLabelTool.setName("Edit Begin Label");
        editLabelTool.setInitialDirectEditLabelExpression("aql:semanticEdgeTarget.name");

        var changeContext = ViewFactory.eINSTANCE.createChangeContext();
        changeContext.setExpression("aql:semanticEdgeTarget");

        var setValue = ViewFactory.eINSTANCE.createSetValue();
        setValue.setFeatureName("name");
        setValue.setValueExpression("aql:newLabel");

        changeContext.getChildren().add(setValue);
        editLabelTool.getBody().add(changeContext);

        return editLabelTool;
    }

    private LabelEditTool editImplementsInterfaceEdgeEndLabel() {
        var editLabelTool = DiagramFactory.eINSTANCE.createLabelEditTool();

        editLabelTool.setName("Edit End Label");
        editLabelTool.setInitialDirectEditLabelExpression("aql:semanticEdgeSource.name");

        var changeContext = ViewFactory.eINSTANCE.createChangeContext();
        changeContext.setExpression("aql:semanticEdgeSource");

        var setValue = ViewFactory.eINSTANCE.createSetValue();
        setValue.setFeatureName("name");
        setValue.setValueExpression("aql:newLabel");

        changeContext.getChildren().add(setValue);
        editLabelTool.getBody().add(changeContext);

        return editLabelTool;
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optionalImplementsInterfaceEdgeDescription = cache.getEdgeDescription("Edge Implements interface");
        var optionalClassNodeDescription = cache.getNodeDescription("Node papaya_logical_architecture::Class");
        var optionalInterfaceNodeDescription = cache.getNodeDescription("Node papaya_logical_architecture::Interface");

        if (optionalImplementsInterfaceEdgeDescription.isPresent() && optionalClassNodeDescription.isPresent() && optionalInterfaceNodeDescription.isPresent()) {
            diagramDescription.getEdgeDescriptions().add(optionalImplementsInterfaceEdgeDescription.get());
            optionalImplementsInterfaceEdgeDescription.get().getSourceNodeDescriptions().add(optionalClassNodeDescription.get());
            optionalImplementsInterfaceEdgeDescription.get().getTargetNodeDescriptions().add(optionalInterfaceNodeDescription.get());
        }
    }

}

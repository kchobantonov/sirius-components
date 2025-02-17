/*******************************************************************************
 * Copyright (c) 2021, 2023 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.components.diagrams.layout.incremental;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.sirius.components.diagrams.CustomizableProperties;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.InsideLabel;
import org.eclipse.sirius.components.diagrams.Label;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.layout.incremental.data.DiagramLayoutData;
import org.eclipse.sirius.components.diagrams.layout.incremental.data.EdgeLayoutData;
import org.eclipse.sirius.components.diagrams.layout.incremental.data.ILayoutData;
import org.eclipse.sirius.components.diagrams.layout.incremental.data.LabelLayoutData;
import org.eclipse.sirius.components.diagrams.layout.incremental.data.NodeLayoutData;
import org.springframework.stereotype.Service;

/**
 * This class is used to include layout data in an existing diagram by producing a brand new immutable diagram with the
 * proper layout information.
 *
 * @author wpiers
 */
@Service
public class IncrementalLayoutedDiagramProvider {

    public Diagram getLayoutedDiagram(Diagram diagram, DiagramLayoutData diagramLayoutData, Map<String, ILayoutData> id2LayoutData) {
        List<Node> nodes = this.getLayoutedNodes(diagram.getNodes(), id2LayoutData);
        List<Edge> edges = this.getLayoutedEdges(diagram.getEdges(), id2LayoutData);

        return Diagram.newDiagram(diagram)
                .position(diagramLayoutData.getPosition())
                .size(diagramLayoutData.getSize())
                .nodes(nodes)
                .edges(edges)
                .build();
    }

    private List<Node> getLayoutedNodes(List<Node> nodes, Map<String, ILayoutData> id2LayoutData) {
        return nodes.stream().flatMap(node -> {
            return Optional.ofNullable(id2LayoutData.get(node.getId()))
                    .filter(NodeLayoutData.class::isInstance)
                    .map(NodeLayoutData.class::cast)
                    .map(nodeLayoutData -> this.getLayoutedNode(node, nodeLayoutData, id2LayoutData))
                    .stream();
        }).collect(Collectors.toUnmodifiableList());
    }

    private Node getLayoutedNode(Node node, NodeLayoutData nodeLayoutData, Map<String, ILayoutData> id2LayoutData) {
        InsideLabel insideLabel = this.getLayoutedInsideLabel(node.getInsideLabel(), id2LayoutData);

        List<Node> childNodes = this.getLayoutedNodes(node.getChildNodes(), id2LayoutData);
        List<Node> borderNodes = this.getLayoutedNodes(node.getBorderNodes(), id2LayoutData);

        Set<CustomizableProperties> customizableProperties = new HashSet<>(node.getCustomizedProperties());
        if (nodeLayoutData.isResizedByUser()) {
            customizableProperties.add(CustomizableProperties.Size);
        } else {
            customizableProperties.remove(CustomizableProperties.Size);
        }
        return Node.newNode(node)
                .insideLabel(insideLabel)
                .size(nodeLayoutData.getSize())
                .userResizable(nodeLayoutData.isUserResizable())
                .position(nodeLayoutData.getPosition())
                .childNodes(childNodes)
                .borderNodes(borderNodes)
                .customizedProperties(customizableProperties)
                .build();
    }

    private List<Edge> getLayoutedEdges(List<Edge> edges, Map<String, ILayoutData> id2LayoutData) {
        return edges.stream().flatMap(edge -> {
            return Optional.ofNullable(id2LayoutData.get(edge.getId()))
                    .filter(EdgeLayoutData.class::isInstance)
                    .map(EdgeLayoutData.class::cast)
                    .map(edgeLayoutData -> this.getLayoutedEdge(edge, edgeLayoutData, id2LayoutData))
                    .stream();
        }).collect(Collectors.toUnmodifiableList());
    }

    private Edge getLayoutedEdge(Edge edge, EdgeLayoutData edgeLayoutData, Map<String, ILayoutData> id2LayoutData) {
        Label beginLabel = edge.getBeginLabel();
        if (beginLabel != null) {
            beginLabel = this.getLayoutedLabel(beginLabel, id2LayoutData);
        }
        Label centerLabel = edge.getCenterLabel();
        if (centerLabel != null) {
            centerLabel = this.getLayoutedLabel(centerLabel, id2LayoutData);
        }
        Label endLabel = edge.getEndLabel();
        if (endLabel != null) {
            endLabel = this.getLayoutedLabel(endLabel, id2LayoutData);
        }

        return Edge.newEdge(edge)
                .beginLabel(beginLabel)
                .centerLabel(centerLabel)
                .endLabel(endLabel)
                .routingPoints(edgeLayoutData.getRoutingPoints())
                .sourceAnchorRelativePosition(edgeLayoutData.getSourceAnchorRelativePosition())
                .targetAnchorRelativePosition(edgeLayoutData.getTargetAnchorRelativePosition())
                .build();
    }

    private Label getLayoutedLabel(Label label, Map<String, ILayoutData> id2LayoutData) {
        Label layoutedLabel = label;
        var optionalLabelLayoutData = Optional.of(id2LayoutData.get(label.getId())).filter(LabelLayoutData.class::isInstance).map(LabelLayoutData.class::cast);
        if (optionalLabelLayoutData.isPresent()) {
            LabelLayoutData labelLayoutData = optionalLabelLayoutData.get();

            layoutedLabel = Label.newLabel(label)
                    .size(labelLayoutData.getTextBounds().getSize())
                    .position(labelLayoutData.getPosition())
                    .alignment(labelLayoutData.getTextBounds().getAlignment())
                    .type(labelLayoutData.getLabelType())
                    .build();
        }
        return layoutedLabel;
    }

    private InsideLabel getLayoutedInsideLabel(InsideLabel insideLabel, Map<String, ILayoutData> id2LayoutData) {
        InsideLabel layoutedLabel = insideLabel;
        var optionalLabelLayoutData = Optional.of(id2LayoutData.get(insideLabel.getId())).filter(LabelLayoutData.class::isInstance).map(LabelLayoutData.class::cast);
        if (optionalLabelLayoutData.isPresent()) {
            LabelLayoutData labelLayoutData = optionalLabelLayoutData.get();

            layoutedLabel = InsideLabel.newInsideLabel(insideLabel)
                    .size(labelLayoutData.getTextBounds().getSize())
                    .position(labelLayoutData.getPosition())
                    .alignment(labelLayoutData.getTextBounds().getAlignment())
                    .type(labelLayoutData.getLabelType())
                    .build();
        }
        return layoutedLabel;
    }
}

/*******************************************************************************
 * Copyright (c) 2019, 2023 Obeo.
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
package org.eclipse.sirius.components.diagrams.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.elk.graph.ElkBendPoint;
import org.eclipse.elk.graph.ElkConnectableShape;
import org.eclipse.elk.graph.ElkEdge;
import org.eclipse.elk.graph.ElkEdgeSection;
import org.eclipse.elk.graph.ElkGraphElement;
import org.eclipse.elk.graph.ElkLabel;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.sirius.components.diagrams.CustomizableProperties;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.ImageNodeStyle;
import org.eclipse.sirius.components.diagrams.InsideLabel;
import org.eclipse.sirius.components.diagrams.Label;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ParametricSVGNodeStyle;
import org.eclipse.sirius.components.diagrams.Position;
import org.eclipse.sirius.components.diagrams.Ratio;
import org.eclipse.sirius.components.diagrams.RectangularNodeStyle;
import org.eclipse.sirius.components.diagrams.Size;
import org.eclipse.sirius.components.diagrams.layout.incremental.provider.ICustomNodeLabelPositionProvider;
import org.springframework.stereotype.Service;

/**
 * This class is used to use both a non-layouted immutable diagram and the result of the computation of the layout to
 * produce a brand new immutable diagram with the proper layout information.
 *
 * @author sbegaudeau
 * @author hmarchadour
 */
@Service
public class ELKLayoutedDiagramProvider {

    private final List<ICustomNodeLabelPositionProvider> customLabelPositionProviders;

    private final ELKPropertiesService elkPropertiesService;

    public ELKLayoutedDiagramProvider(List<ICustomNodeLabelPositionProvider> customLabelPositionProviders, ELKPropertiesService elkPropertiesService) {
        this.customLabelPositionProviders = customLabelPositionProviders;
        this.elkPropertiesService = Objects.requireNonNull(elkPropertiesService);
    }

    public Diagram getLayoutedDiagram(Diagram diagram, ElkNode elkDiagram, Map<String, ElkGraphElement> id2ElkGraphElements, ISiriusWebLayoutConfigurator layoutConfigurator) {
        Size size = Size.of(elkDiagram.getWidth(), elkDiagram.getHeight());
        Position position = Position.at(elkDiagram.getX(), elkDiagram.getY());

        List<Node> nodes = this.getLayoutedNodes(diagram.getNodes(), id2ElkGraphElements, layoutConfigurator);
        List<Edge> edges = this.getLayoutedEdges(diagram.getEdges(), id2ElkGraphElements);

        return Diagram.newDiagram(diagram)
                .position(position)
                .size(size)
                .nodes(nodes)
                .edges(edges)
                .build();
    }

    private List<Node> getLayoutedNodes(List<Node> nodes, Map<String, ElkGraphElement> id2ElkGraphElements, ISiriusWebLayoutConfigurator layoutConfigurator) {
        return nodes.stream().flatMap(node -> {
            return Optional.ofNullable(id2ElkGraphElements.get(node.getId().toString()))
                    .filter(ElkConnectableShape.class::isInstance)
                    .map(ElkConnectableShape.class::cast)
                    .map(elkNode -> this.getLayoutedNode(node, elkNode, id2ElkGraphElements, layoutConfigurator))
                    .stream();
        }).collect(Collectors.toUnmodifiableList());
    }

    private Node getLayoutedNode(Node node, ElkConnectableShape elkConnectableShape, Map<String, ElkGraphElement> id2ElkGraphElements, ISiriusWebLayoutConfigurator layoutConfigurator) {
        Size size = Size.of(elkConnectableShape.getWidth(), elkConnectableShape.getHeight());
        Position position = Position.at(elkConnectableShape.getX(), elkConnectableShape.getY());

        InsideLabel label = this.getNodeLayoutedLabel(node, id2ElkGraphElements, layoutConfigurator);

        List<Node> childNodes = this.getLayoutedNodes(node.getChildNodes(), id2ElkGraphElements, layoutConfigurator);
        List<Node> borderNodes = this.getLayoutedNodes(node.getBorderNodes(), id2ElkGraphElements, layoutConfigurator);
        Set<CustomizableProperties> customizedProperties = node.getCustomizedProperties();
        if ((node.getStyle() instanceof RectangularNodeStyle || node.getStyle() instanceof ParametricSVGNodeStyle || node.getStyle() instanceof ImageNodeStyle) && !size.equals(node.getSize())) {
            // Reset the "custom size" flag if the ELK layout decided on a different size.
            customizedProperties = customizedProperties.stream().filter(property -> !CustomizableProperties.Size.equals(property)).collect(Collectors.toSet());
        }
        return Node.newNode(node)
                .insideLabel(label)
                .size(size)
                .position(position)
                .childNodes(childNodes)
                .borderNodes(borderNodes)
                .customizedProperties(customizedProperties)
                .build();
    }

    private List<Edge> getLayoutedEdges(List<Edge> edges, Map<String, ElkGraphElement> id2ElkGraphElements) {
        return edges.stream().flatMap(edge -> {
            return Optional.ofNullable(id2ElkGraphElements.get(edge.getId().toString()))
                    .filter(ElkEdge.class::isInstance)
                    .map(ElkEdge.class::cast)
                    .map(elkEdge -> this.getLayoutedEdge(edge, elkEdge, id2ElkGraphElements))
                    .stream();
        }).collect(Collectors.toUnmodifiableList());
    }

    private Edge getLayoutedEdge(Edge edge, ElkEdge elkEdge, Map<String, ElkGraphElement> id2ElkGraphElements) {
        List<Position> routingPoints = new ArrayList<>();

        ElkNode containingNode = elkEdge.getContainingNode();
        double xOffset = 0;
        double yOffset = 0;
        if (containingNode != null) {
            xOffset = containingNode.getX();
            yOffset = containingNode.getY();
            ElkNode parent = containingNode.getParent();
            while (parent instanceof ElkNode) {
                xOffset += parent.getX();
                yOffset += parent.getY();
                parent = parent.getParent();
            }
        }
        double sourceAnchorRatioX = 0.5;
        double sourceAnchorRatioY = 0.5;
        double targetAnchorRatioX = 0.5;
        double targetAnchorRatioY = 0.5;
        Ratio sourceAnchorRatio = Ratio.of(sourceAnchorRatioX, sourceAnchorRatioY);
        Ratio targetAnchorRatio = Ratio.of(targetAnchorRatioX, targetAnchorRatioY);

        if (!elkEdge.getSections().isEmpty()) {
            ElkEdgeSection section = elkEdge.getSections().get(0);

            Optional<ElkNode> optionalSource = elkEdge.getSources().stream().filter(ElkNode.class::isInstance).map(ElkNode.class::cast).findFirst();
            Optional<ElkNode> optionalTarget = elkEdge.getTargets().stream().filter(ElkNode.class::isInstance).map(ElkNode.class::cast).findFirst();
            if (optionalSource.isPresent() && optionalTarget.isPresent()) {

                ElkNode sourceNode = optionalSource.get();
                ElkNode targetNode = optionalTarget.get();

                sourceAnchorRatio = this.getSectionRatio(sourceNode, section.getStartX() + xOffset, section.getStartY() + yOffset);
                targetAnchorRatio = this.getSectionRatio(targetNode, section.getEndX() + xOffset, section.getEndY() + yOffset);
            }

            for (ElkBendPoint bendPoint : section.getBendPoints()) {
                Position position = Position.at(xOffset + bendPoint.getX(), yOffset + bendPoint.getY());
                routingPoints.add(position);
            }
        }

        Label beginLabel = edge.getBeginLabel();
        if (beginLabel != null) {
            beginLabel = this.getLayoutedLabel(beginLabel, id2ElkGraphElements, xOffset, yOffset);
        }
        Label centerLabel = edge.getCenterLabel();
        if (centerLabel != null) {
            centerLabel = this.getLayoutedLabel(centerLabel, id2ElkGraphElements, xOffset, yOffset);
        }
        Label endLabel = edge.getEndLabel();
        if (endLabel != null) {
            endLabel = this.getLayoutedLabel(endLabel, id2ElkGraphElements, xOffset, yOffset);
        }

        return Edge.newEdge(edge)
                .beginLabel(beginLabel)
                .centerLabel(centerLabel)
                .endLabel(endLabel)
                .routingPoints(routingPoints)
                .sourceAnchorRelativePosition(sourceAnchorRatio)
                .targetAnchorRelativePosition(targetAnchorRatio)
                .build();
    }

    private Ratio getSectionRatio(ElkNode node, double sectionX, double sectionY) {
        double sourceAnchorRatioX;
        double sourceAnchorRatioY;
        Position nodeAbsolutePosition = this.getAbsolutePosition(node);
        if (sectionX == nodeAbsolutePosition.getX()) {
            sourceAnchorRatioX = 0.5;
        } else if (sectionX == nodeAbsolutePosition.getX() + node.getWidth()) {
            sourceAnchorRatioX = 0.5;
        } else {
            sourceAnchorRatioX = (sectionX - nodeAbsolutePosition.getX()) / node.getWidth();
        }

        if (sectionY == nodeAbsolutePosition.getY()) {
            sourceAnchorRatioY = 0.5;
        } else if (sectionY == nodeAbsolutePosition.getY() + node.getHeight()) {
            sourceAnchorRatioY = 0.5;
        } else {
            sourceAnchorRatioY = (sectionY - nodeAbsolutePosition.getY()) / node.getHeight();
        }

        return Ratio.of(sourceAnchorRatioX, sourceAnchorRatioY);
    }

    private Label getLayoutedLabel(Label label, Map<String, ElkGraphElement> id2ElkGraphElements, double xOffset, double yOffset) {
        Label layoutedLabel = label;
        var optionalElkLabel = Optional.of(id2ElkGraphElements.get(label.getId().toString())).filter(ElkLabel.class::isInstance).map(ElkLabel.class::cast);
        if (optionalElkLabel.isPresent()) {
            ElkLabel elkLabel = optionalElkLabel.get();

            Size size = Size.of(elkLabel.getWidth(), elkLabel.getHeight());

            Position position = Position.at(xOffset + elkLabel.getX(), yOffset + elkLabel.getY());

            Position alignment = elkLabel.eAdapters().stream()
                    .findFirst()
                    .filter(AlignmentHolder.class::isInstance)
                    .map(AlignmentHolder.class::cast)
                    .map(AlignmentHolder::getAlignment)
                    .orElse(Position.UNDEFINED);

            layoutedLabel = Label.newLabel(label)
                    .size(size)
                    .position(position)
                    .alignment(alignment)
                    .build();
        }
        return layoutedLabel;
    }

    private InsideLabel getNodeLayoutedLabel(Node node, Map<String, ElkGraphElement> id2ElkGraphElements, ISiriusWebLayoutConfigurator layoutConfigurator) {
        InsideLabel layoutedInsideLabel = node.getInsideLabel();
        var optionalElkLabel = Optional.of(id2ElkGraphElements.get(layoutedInsideLabel.getId().toString())).filter(ElkLabel.class::isInstance).map(ElkLabel.class::cast);
        if (optionalElkLabel.isPresent()) {
            ElkLabel elkLabel = optionalElkLabel.get();

            Size size = Size.of(elkLabel.getWidth(), elkLabel.getHeight());
            String nodeLabelType;
            if (node.isBorderNode()) {
                nodeLabelType = this.elkPropertiesService.getBorderNodeLabelType(node, layoutConfigurator);
            } else {
                nodeLabelType = this.elkPropertiesService.getNodeLabelType(node, layoutConfigurator);
            }

            Position position = Optional.of(elkLabel.getParent())
                    .filter(ElkNode.class::isInstance)
                    .map(ElkNode.class::cast)
                    .map(elkNode -> {
                        return Size.of(elkNode.getWidth(), elkNode.getHeight());
                    })
                    .flatMap(nodeSize -> {
                        return this.customLabelPositionProviders.stream()
                                .map(customLabelPositionProvider -> customLabelPositionProvider.getLabelPosition(layoutConfigurator, size, nodeSize, node.getType(), node.getStyle()))
                                .flatMap(Optional::stream)
                                .findFirst();
                    })
                    .orElseGet(()-> {
                        if (nodeLabelType.startsWith("label:inside-v")) {
                            double maxPadding = this.elkPropertiesService.getMaxPadding(node, layoutConfigurator);
                            return Position.at(maxPadding, maxPadding);
                        }
                        return Position.at(elkLabel.getX(), elkLabel.getY());
                    });

            Position alignment = elkLabel.eAdapters().stream()
                    .filter(AlignmentHolder.class::isInstance)
                    .map(AlignmentHolder.class::cast)
                    .map(AlignmentHolder::getAlignment)
                    .findFirst()
                    .orElse(Position.UNDEFINED);

            layoutedInsideLabel = InsideLabel.newInsideLabel(node.getInsideLabel())
                    .type(nodeLabelType)
                    .size(size)
                    .position(position)
                    .alignment(alignment)
                    .build();
        }
        return layoutedInsideLabel;
    }

    private Position getAbsolutePosition(ElkNode node) {
        ElkNode currentNode = node;
        Position absolutePosition = Position.at(node.getX(), node.getY());
        while (currentNode.getParent() != null) {
            currentNode = currentNode.getParent();
            absolutePosition = Position.newPosition()
                    .x(absolutePosition.getX() + currentNode.getX())
                    .y(absolutePosition.getY() + currentNode.getY())
                    .build();
        }
        return absolutePosition;
    }
}

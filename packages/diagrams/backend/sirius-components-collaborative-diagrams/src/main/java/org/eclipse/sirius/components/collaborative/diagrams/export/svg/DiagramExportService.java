/*******************************************************************************
 * Copyright (c) 2022, 2023 Obeo.
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
package org.eclipse.sirius.components.collaborative.diagrams.export.svg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.sirius.components.collaborative.diagrams.export.api.IImageRegistry;
import org.eclipse.sirius.components.collaborative.diagrams.export.api.ISVGDiagramExportService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.InsideLabel;
import org.eclipse.sirius.components.diagrams.Label;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.Position;
import org.eclipse.sirius.components.diagrams.Size;
import org.springframework.stereotype.Service;

/**
 * Used to export a {@link Diagram} to an SVG format.
 *
 * @author rpage
 */
@Service
@SuppressWarnings("checkstyle:MultipleStringLiterals")
public class DiagramExportService implements ISVGDiagramExportService {
    private static final String SVG_NAMESPACE = "xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" ";

    private final NodeExportService nodeExport;

    private final EdgeExportService edgeExport;

    private final IImageRegistry imageRegistry;

    private final List<Double> xBeginPositions = new ArrayList<>();

    private final List<Double> yBeginPositions = new ArrayList<>();

    private final List<Double> xEndPositions = new ArrayList<>();

    private final List<Double> yEndPositions = new ArrayList<>();

    public DiagramExportService(NodeExportService nodeExport, EdgeExportService edgeExport, IImageRegistry imageRegistry) {
        this.nodeExport = Objects.requireNonNull(nodeExport);
        this.edgeExport = Objects.requireNonNull(edgeExport);
        this.imageRegistry = Objects.requireNonNull(imageRegistry);
    }

    @Override
    public String export(Diagram diagram) {
        StringBuilder svg = new StringBuilder();
        svg.append(this.addSvgRoot(diagram));

        svg.append("<g transform=\"scale(1) translate(0,0)\">");

        Map<String, NodeAndContainerId> id2NodeHierarchy = new HashMap<>();
        diagram.getNodes().forEach(node -> this.exportNode(svg, node, diagram.getId(), id2NodeHierarchy));
        diagram.getEdges().forEach(edge -> svg.append(this.edgeExport.export(edge, id2NodeHierarchy)));

        svg.append("</g>");

        svg.append(this.imageRegistry.getReferencedImageSymbols());

        return svg.append("</svg>").toString();
    }

    private void exportNode(StringBuilder svg, Node node, String diagramId, Map<String, NodeAndContainerId> id2NodeHierarchy) {
        id2NodeHierarchy.put(node.getId(), new NodeAndContainerId(diagramId, node));
        svg.append(this.nodeExport.export(node, id2NodeHierarchy));
    }

    private StringBuilder addSvgRoot(Diagram diagram) {
        StringBuilder rootSvg = new StringBuilder();

        rootSvg.append("<svg ");
        rootSvg.append(SVG_NAMESPACE);
        rootSvg.append(this.computeViewBox(diagram));
        rootSvg.append("tabindex=\"0\" ");
        rootSvg.append("style=\"cursor: pointer; outline: transparent solid 1px;\"");
        return rootSvg.append(">");
    }

    /**
     * Determines the viewBox argument of the svg element.
     *
     * <p>
     * Since the coordinates of the elements are saved depending on the viewPort of the frontend, we need to compute the
     * viewPort of the SVG. This viewPort can be declared using the viewBox argument.
     *
     * viewBox = (min-x, min-y, width, height)
     *
     * With :
     *
     * diagram.children = nodes, borderNodes, edges and their labels </br>
     * min x = { min(e.x) | e in diagram.children } </br>
     * min y = { min(e.y) | e in diagram.children } </br>
     * max x = {max(e.x + e.width) | e in diagram.children } </br>
     * max y = { max(e.y + e.height) | e in diagram.children } </br>
     * width = abs( min x - max x ) + diag.x </br>
     * height = abs( min y - max y ) + diag.y </br>
     * </p>
     *
     *
     * @param diagram
     *            The diagram being exported
     * @return The StringBuilder containing the viewBox attribute of the SVG
     */
    private StringBuilder computeViewBox(Diagram diagram) {
        StringBuilder viewBoxExport = new StringBuilder();

        viewBoxExport.append("viewBox=\"");

        this.computeElementsCoordinates(diagram);

        double minX = this.xBeginPositions.stream().min(Double::compare).orElse(0d);
        double minY = this.yBeginPositions.stream().min(Double::compare).orElse(0d);
        double maxX = this.xEndPositions.stream().max(Double::compare).orElse(0d);
        double maxY = this.yEndPositions.stream().max(Double::compare).orElse(0d);

        int padding = 20;
        double width = Math.abs(minX - maxX) + diagram.getPosition().getX();
        double height = Math.abs(minY - maxY) + diagram.getPosition().getY();

        double finalMinX = minX - padding / 2;
        double finalMinY = minY - padding / 2;
        double finalWidth = width + padding;
        double finalHeight = height + padding;

        viewBoxExport.append(finalMinX + " " + finalMinY + " " + finalWidth + " " + finalHeight);
        return viewBoxExport.append("\" ");
    }

    private void computeElementsCoordinates(Diagram diagram) {
        diagram.getNodes().forEach(node -> {
            this.addElementPositions(node.getPosition(), node.getSize(), node.getInsideLabel(), Position.at(0, 0));

            node.getBorderNodes().forEach(border -> {
                this.addElementPositions(border.getPosition(), border.getSize(), border.getInsideLabel(), node.getPosition());
            });
        });

        diagram.getEdges().forEach(edge -> {
            if (edge.getBeginLabel() != null) {
                this.addAbsoluteLabelPosition(edge.getBeginLabel(), Position.at(0, 0));
            }
            if (edge.getCenterLabel() != null) {
                this.addAbsoluteLabelPosition(edge.getCenterLabel(), Position.at(0, 0));

            }
            if (edge.getEndLabel() != null) {
                this.addAbsoluteLabelPosition(edge.getEndLabel(), Position.at(0, 0));
            }
            edge.getRoutingPoints().forEach(point -> {
                this.xBeginPositions.add(point.getX());
                this.xEndPositions.add(point.getX());
                this.yBeginPositions.add(point.getY());
                this.yEndPositions.add(point.getY());
            });
        });
    }

    private void addElementPositions(Position elementPosition, Size elementSize, InsideLabel elementLabel, Position parentPosition) {
        double nodeX = parentPosition.getX() + elementPosition.getX();
        double nodeY = parentPosition.getY() + elementPosition.getY();
        this.xBeginPositions.add(nodeX);
        this.yBeginPositions.add(nodeY);
        this.xEndPositions.add(nodeX + elementSize.getWidth());
        this.yEndPositions.add(nodeY + elementSize.getHeight());
        this.addAbsoluteInsideLabelPosition(elementLabel, elementPosition);
    }

    private void addAbsoluteInsideLabelPosition(InsideLabel insideLabel, Position parentPosition) {
        double xOffest = parentPosition.getX();
        double yOffest = parentPosition.getY();
        Position labelPosition = insideLabel.getPosition();
        Position labelAlignment = insideLabel.getAlignment();
        Size labelSize = insideLabel.getSize();

        double xLabel = xOffest + labelPosition.getX() + labelAlignment.getX();
        double yLabel = yOffest + labelPosition.getY() + labelAlignment.getY();
        this.xBeginPositions.add(xLabel);
        this.yBeginPositions.add(yLabel);
        this.xEndPositions.add(xLabel + labelSize.getWidth());
        this.yEndPositions.add(yLabel + labelSize.getHeight());
    }

    private void addAbsoluteLabelPosition(Label label, Position parentPosition) {
        double xOffest = parentPosition.getX();
        double yOffest = parentPosition.getY();
        Position labelPosition = label.getPosition();
        Position labelAlignment = label.getAlignment();
        Size labelSize = label.getSize();

        double xLabel = xOffest + labelPosition.getX() + labelAlignment.getX();
        double yLabel = yOffest + labelPosition.getY() + labelAlignment.getY();
        this.xBeginPositions.add(xLabel);
        this.yBeginPositions.add(yLabel);
        this.xEndPositions.add(xLabel + labelSize.getWidth());
        this.yEndPositions.add(yLabel + labelSize.getHeight());
    }
}

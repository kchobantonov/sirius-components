/*******************************************************************************
 * Copyright (c) 2022 Obeo.
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

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.sirius.components.collaborative.diagrams.export.api.IImageRegistry;
import org.eclipse.sirius.components.diagrams.Label;
import org.eclipse.sirius.components.diagrams.LabelStyle;
import org.eclipse.sirius.components.diagrams.LineStyle;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.Position;
import org.eclipse.sirius.components.diagrams.Size;
import org.springframework.stereotype.Service;

/**
 * Contains methods used when exporting diagram elements.
 *
 * @author rpage
 */
@Service
@SuppressWarnings("checkstyle:MultipleStringLiterals")
public class DiagramElementExportService {
    private final IImageRegistry imageRegistry;

    public DiagramElementExportService(IImageRegistry imageRegistry) {
        this.imageRegistry = Objects.requireNonNull(imageRegistry);
    }

    public StringBuilder exportLabel(Label label, float opacity) {
        StringBuilder labelExport = new StringBuilder();
        Position position = label.getPosition();
        Position alignment = label.getAlignment();
        LabelStyle style = label.getStyle();

        labelExport.append("<g ");
        labelExport.append("transform=\"");
        labelExport.append("translate(" + position.getX() + ", " + position.getY() + ") ");
        labelExport.append("translate(" + alignment.getX() + ", " + alignment.getY() + ")\" ");
        labelExport.append("style=\"");
        labelExport.append("opacity: " + opacity + ";");
        labelExport.append("\" ");
        labelExport.append(">");

        if (!(style.getIconURL().isEmpty())) {
            labelExport.append(this.exportImageElement(style.getIconURL(), -20, -12, Optional.empty(), 1));
        }

        labelExport.append(this.exportTextElement(label.getText(), label.getType(), style));

        return labelExport.append("</g>");
    }

    public StringBuilder exportImageElement(String imageURL, int x, int y, Optional<Size> size, float nodeOpacity) {
        StringBuilder imageExport = new StringBuilder();
        UUID symbolId = this.imageRegistry.registerImage(imageURL);
        if (symbolId != null) {
            imageExport.append("<use ");
            imageExport.append("xlink:href=\"#" + symbolId + "\" ");
            imageExport.append("x=\"" + x + "\" ");
            imageExport.append("y=\"" + y + "\" ");
            size.ifPresent(it -> imageExport.append(this.addSizeParam(it)));
            imageExport.append("style=\"");
            imageExport.append("opacity: " + nodeOpacity + "; ");
            imageExport.append("\" ");
            imageExport.append("/>");
        }
        return imageExport;
    }

    /**
     * Export the g element containing the position of a diagram element.
     *
     * @param node
     *            The diagram element
     * @return The g element containing its position
     */
    public StringBuilder exportGNodeElement(Node node) {
        StringBuilder gExport = new StringBuilder();

        gExport.append("<g ");
        gExport.append("transform=\"translate(" + node.getPosition().getX() + ", " + node.getPosition().getY() + ")\" ");

        return gExport.append(">");
    }

    public StringBuilder exportRectangleElement(Size size, Position position, RectangleStyle rectangleStyle) {
        StringBuilder rectangle = new StringBuilder();

        rectangle.append("<rect ");
        rectangle.append("x=\"" + position.getX() + "\" ");
        rectangle.append("y=\"" + position.getY() + "\" ");
        Optional.ofNullable(rectangleStyle.getBorderRadius()).ifPresent(radius -> rectangle.append("rx=\"" + radius + "\" "));
        rectangle.append(this.addSizeParam(size));
        rectangle.append(this.exportRectangleStyleParam(rectangleStyle));

        return rectangle.append("/> ");
    }

    private StringBuilder addSizeParam(Size size) {
        StringBuilder sizeParam = new StringBuilder();
        sizeParam.append("width=\"" + size.getWidth() + "\" ");
        return sizeParam.append("height=\"" + size.getHeight() + "\" ");
    }

    private StringBuilder exportTextElement(String text, String type, LabelStyle labelStyle) {
        StringBuilder textExport = new StringBuilder();

        textExport.append("<text ");
        textExport.append("style=\"");
        textExport.append("fill: " + labelStyle.getColor() + "; ");
        textExport.append(this.exportFont(labelStyle));
        textExport.append("\">");

        String[] lines = text.split("\\n", -1);
        if (lines.length == 1) {
            textExport.append(text);
        } else {
            textExport.append("<tspan x=\"0\">" + lines[0] + "</tspan>");
            double fontSize = labelStyle.getFontSize();
            for (int i = 1; i < lines.length; i++) {
                if (lines[i].isEmpty()) {
                    // avoid tspan to be ignored if there is only a line return
                    lines[i] = " ";
                }
                textExport.append("<tspan x=\"0\" dy=\"" + fontSize + "\">" + lines[i] + "</tspan>");
            }
        }

        return textExport.append("</text>");
    }

    private StringBuilder exportFont(LabelStyle labelStyle) {
        StringBuilder textExport = new StringBuilder();

        textExport.append("font-size: " + labelStyle.getFontSize() + "px; ");
        textExport.append("font-family: Arial, Helvetica, sans-serif; ");

        textExport.append("font-weight: ");
        if (labelStyle.isBold()) {
            textExport.append("bold");
        } else {
            textExport.append("normal");
        }
        textExport.append("; ");

        textExport.append("font-style: ");
        if (labelStyle.isItalic()) {
            textExport.append("italic");
        } else {
            textExport.append("normal");
        }
        textExport.append("; ");

        textExport.append("text-decoration: ");
        if (labelStyle.isUnderline() || labelStyle.isStrikeThrough()) {
            if (labelStyle.isUnderline()) {
                textExport.append("underline ");
            }
            if (labelStyle.isStrikeThrough()) {
                textExport.append("line-through");
            }
        } else {
            textExport.append("none");
        }
        return textExport.append(";");
    }

    private StringBuilder exportRectangleStyleParam(RectangleStyle rectangleStyle) {
        StringBuilder styleExport = new StringBuilder();

        styleExport.append("style=\"");
        if (Optional.ofNullable(rectangleStyle.getColor()).isPresent()) {
            styleExport.append("fill: " + rectangleStyle.getColor() + "; ");
        } else {
            styleExport.append("fill-opacity: " + 0 + "; ");
        }

        styleExport.append("opacity: " + rectangleStyle.getOpacity() + "; ");

        Optional.ofNullable(rectangleStyle.getBorderColor()).ifPresent(it -> styleExport.append("stroke: " + it + "; "));
        Optional.ofNullable(rectangleStyle.getBorderSize()).ifPresent(it -> styleExport.append("stroke-width: " + it + "px; "));
        Optional.ofNullable(rectangleStyle.getBorderStyle()).ifPresent(style -> styleExport.append(this.exportBorderStyle(style)));

        return styleExport.append("\"");
    }

    private StringBuilder exportBorderStyle(LineStyle borderStyle) {
        StringBuilder style = new StringBuilder();
        switch (borderStyle.toString()) {
        case "Dash":
            style.append(" stroke-dasharray: 4, 4;");
            break;
        case "Dot":
            style.append(" stroke-dasharray: 2, 2;");
            break;
        case "Dash_Dot":
            style.append(" stroke-dasharray: 2, 4, 2;");
            break;
        default:
            break;
        }
        return style;
    }
}

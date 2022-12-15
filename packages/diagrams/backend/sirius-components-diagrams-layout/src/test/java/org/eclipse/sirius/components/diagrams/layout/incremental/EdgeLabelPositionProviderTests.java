/*******************************************************************************
 * Copyright (c) 2021, 2022 THALES GLOBAL SERVICES.
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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.assertj.core.data.Offset;
import org.eclipse.elk.alg.layered.options.LayeredMetaDataProvider;
import org.eclipse.elk.core.data.LayoutMetaDataService;
import org.eclipse.sirius.components.diagrams.LabelStyle;
import org.eclipse.sirius.components.diagrams.Position;
import org.eclipse.sirius.components.diagrams.Ratio;
import org.eclipse.sirius.components.diagrams.Size;
import org.eclipse.sirius.components.diagrams.TextBounds;
import org.eclipse.sirius.components.diagrams.TextBoundsProvider;
import org.eclipse.sirius.components.diagrams.layout.LayoutConfiguratorRegistry;
import org.eclipse.sirius.components.diagrams.layout.incremental.data.DiagramLayoutData;
import org.eclipse.sirius.components.diagrams.layout.incremental.data.EdgeLayoutData;
import org.eclipse.sirius.components.diagrams.layout.incremental.data.IContainerLayoutData;
import org.eclipse.sirius.components.diagrams.layout.incremental.data.LabelLayoutData;
import org.eclipse.sirius.components.diagrams.layout.incremental.data.NodeLayoutData;
import org.eclipse.sirius.components.diagrams.layout.incremental.provider.EdgeLabelPositionProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Test cases for {@link EdgeLabelPositionProvider}.
 *
 * @author wpiers
 */
public class EdgeLabelPositionProviderTests {

    private static final int FONT_SIZE = 16;

    private static final String LABEL_COLOR = "#000000";

    private static final String ICON_URL = "";

    @BeforeAll
    public static void beforeAll() {
        LayoutMetaDataService.getInstance().registerLayoutMetaDataProviders(new LayeredMetaDataProvider());
    }

    @Test
    public void testEdgeLabelBoundsPosition() {
        EdgeLayoutData edgeLayoutData = this.createEdgeLayoutData(this.createDiagramLayoutData());
        EdgeLabelPositionProvider labelBoundsProvider = new EdgeLabelPositionProvider(new LayoutConfiguratorRegistry(List.of()).getDefaultLayoutConfigurator());
        Position centerPosition = labelBoundsProvider.getCenterPosition(edgeLayoutData, edgeLayoutData.getCenterLabel());
        assertThat(centerPosition.getX()).isCloseTo(267.539, Offset.offset(0.0001));
        assertThat(centerPosition.getY()).isCloseTo(103, Offset.offset(0.0001));
    }

    private DiagramLayoutData createDiagramLayoutData() {
        DiagramLayoutData diagramLayoutData = new DiagramLayoutData();
        diagramLayoutData.setId(UUID.randomUUID().toString());
        diagramLayoutData.setPosition(Position.at(0, 0));
        diagramLayoutData.setSize(Size.of(1000, 1000));

        return diagramLayoutData;
    }

    private LabelLayoutData createLabelLayoutData() {
        LabelLayoutData labelLayoutData = new LabelLayoutData();
        labelLayoutData.setId(UUID.randomUUID().toString());
        labelLayoutData.setPosition(Position.UNDEFINED);
        //@formatter:off
        LabelStyle labelStyle = LabelStyle.newLabelStyle()
                .color(LABEL_COLOR)
                .fontSize(FONT_SIZE)
                .iconURL(ICON_URL)
                .build();
        //@formatter:on
        TextBounds textBounds = new TextBoundsProvider().computeBounds(labelStyle, "labelText");
        labelLayoutData.setTextBounds(textBounds);
        return labelLayoutData;
    }

    private EdgeLayoutData createEdgeLayoutData(DiagramLayoutData diagramLayoutData) {
        EdgeLayoutData edgeLayoutData = new EdgeLayoutData();
        edgeLayoutData.setCenterLabel(this.createLabelLayoutData());
        edgeLayoutData.setId(UUID.randomUUID().toString());
        edgeLayoutData.setSource(this.createNodeLayoutData(Position.at(0, 0), Size.of(200, 200), diagramLayoutData));
        edgeLayoutData.setTarget(this.createNodeLayoutData(Position.at(400, 0), Size.of(200, 200), diagramLayoutData));
        edgeLayoutData.setSourceAnchorRelativePosition(Ratio.of(0.5, 0.5));
        edgeLayoutData.setTargetAnchorRelativePosition(Ratio.of(0.5, 0.5));
        edgeLayoutData.setRoutingPoints(List.of());
        return edgeLayoutData;
    }

    private NodeLayoutData createNodeLayoutData(Position position, Size size, IContainerLayoutData parent) {
        NodeLayoutData nodeLayoutData = new NodeLayoutData();
        nodeLayoutData.setId(UUID.randomUUID().toString());
        nodeLayoutData.setParent(parent);
        nodeLayoutData.setPosition(position);
        nodeLayoutData.setSize(size);
        return nodeLayoutData;
    }
}

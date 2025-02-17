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
package org.eclipse.sirius.components.view.emf.diagram.providers;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramQueryService;
import org.eclipse.sirius.components.collaborative.diagrams.api.IInitialDirectEditElementLabelProvider;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.description.EdgeLabelKind;
import org.eclipse.sirius.components.emf.services.EditingContext;
import org.eclipse.sirius.components.interpreter.AQLInterpreter;
import org.eclipse.sirius.components.interpreter.Result;
import org.eclipse.sirius.components.interpreter.Status;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.sirius.components.view.View;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.EdgeDescription;
import org.eclipse.sirius.components.view.diagram.EdgePalette;
import org.eclipse.sirius.components.view.diagram.LabelEditTool;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.emf.IJavaServiceProvider;
import org.eclipse.sirius.components.view.emf.IViewRepresentationDescriptionPredicate;
import org.eclipse.sirius.components.view.emf.IViewRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * The {@link IInitialDirectEditElementLabelProvider} for view elements.
 *
 * @author gcoutable
 */
@Service
public class ViewInitialDirectEditElementLabelProvider implements IInitialDirectEditElementLabelProvider {

    private final Logger logger = LoggerFactory.getLogger(ViewInitialDirectEditElementLabelProvider.class);

    private final IViewRepresentationDescriptionPredicate viewRepresentationDescriptionPredicate;

    private final IDiagramQueryService diagramQueryService;

    private final IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService;

    private final IObjectService objectService;

    private final List<IJavaServiceProvider> javaServiceProviders;

    private final IDiagramIdProvider idProvider;

    private final ApplicationContext applicationContext;

    public ViewInitialDirectEditElementLabelProvider(IViewRepresentationDescriptionPredicate viewRepresentationDescriptionPredicate, IDiagramQueryService diagramQueryService, IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService, IObjectService objectService,
                                                     List<IJavaServiceProvider> javaServiceProviders, IDiagramIdProvider idProvider, ApplicationContext applicationContext) {
        this.viewRepresentationDescriptionPredicate = Objects.requireNonNull(viewRepresentationDescriptionPredicate);
        this.diagramQueryService = Objects.requireNonNull(diagramQueryService);
        this.viewRepresentationDescriptionSearchService = Objects.requireNonNull(viewRepresentationDescriptionSearchService);
        this.objectService = Objects.requireNonNull(objectService);
        this.javaServiceProviders = Objects.requireNonNull(javaServiceProviders);
        this.idProvider = Objects.requireNonNull(idProvider);
        this.applicationContext = Objects.requireNonNull(applicationContext);
    }

    @Override
    public boolean canHandle(org.eclipse.sirius.components.diagrams.description.DiagramDescription diagramDescription) {
        return this.viewRepresentationDescriptionPredicate.test(diagramDescription);
    }

    @Override
    public String getInitialDirectEditElementLabel(Object diagramElement, String labelId, Diagram diagram, IEditingContext editingContext) {
        String initialDirectEditElementLabel = "";
        String diagramDescriptionId = diagram.getDescriptionId();
        var optionalDiagramDescription = this.viewRepresentationDescriptionSearchService.findById(diagramDescriptionId)
                .filter(DiagramDescription.class::isInstance)
                .map(DiagramDescription.class::cast);

        if (optionalDiagramDescription.isPresent()) {
            DiagramDescription diagramDescription = optionalDiagramDescription.get();
            Optional<LabelEditTool> optionalLabelEditTool = Optional.empty();
            Optional<Object> semanticElement = Optional.empty();

            if (diagramElement instanceof Node node) {
                String descriptionId = node.getDescriptionId();
                optionalLabelEditTool = this.getNodeDescription(diagramDescription.getNodeDescriptions(), descriptionId).map(NodeDescription::getPalette).map(NodePalette::getLabelEditTool);
                semanticElement = this.objectService.getObject(editingContext, node.getTargetObjectId());
                initialDirectEditElementLabel = node.getInsideLabel().getText();
            } else if (diagramElement instanceof Edge edge) {
                String descriptionId = edge.getDescriptionId();
                semanticElement = this.objectService.getObject(editingContext, edge.getTargetObjectId());

                var optionalEdgeDescription = this.getEdgeDescription(diagramDescription.getEdgeDescriptions(), descriptionId);

                if (edge.getBeginLabel() != null && edge.getBeginLabel().getId().equals(labelId)) {
                    optionalLabelEditTool = optionalEdgeDescription.flatMap(edgeDescription -> this.getLabelEditTool(edgeDescription, EdgeLabelKind.BEGIN_LABEL));
                    initialDirectEditElementLabel = edge.getBeginLabel().getText();
                } else if (edge.getCenterLabel() != null && edge.getCenterLabel().getId().equals(labelId)) {
                    initialDirectEditElementLabel = edge.getCenterLabel().getText();
                    optionalLabelEditTool = optionalEdgeDescription.flatMap(edgeDescription -> this.getLabelEditTool(edgeDescription, EdgeLabelKind.CENTER_LABEL));
                } else if (edge.getEndLabel() != null && edge.getEndLabel().getId().equals(labelId)) {
                    initialDirectEditElementLabel = edge.getEndLabel().getText();
                    optionalLabelEditTool = optionalEdgeDescription.flatMap(edgeDescription -> this.getLabelEditTool(edgeDescription, EdgeLabelKind.END_LABEL));
                }
            }

            if (optionalLabelEditTool.isPresent() && semanticElement.isPresent()) {
                LabelEditTool labelEditTool = optionalLabelEditTool.get();
                if (labelEditTool.getInitialDirectEditLabelExpression() != null && !labelEditTool.getInitialDirectEditLabelExpression().isBlank()) {
                    AQLInterpreter interpreter = this.createInterpreter((View) diagramDescription.eContainer(), editingContext);
                    VariableManager variableManager = new VariableManager();
                    variableManager.put(VariableManager.SELF, semanticElement.get());
                    variableManager.put("view", diagramElement);
                    variableManager.put("diagram", diagram);
                    if (diagramElement instanceof Edge edge) {
                        var semanticEdgeSource = this.diagramQueryService.findNodeById(diagram, edge.getSourceId())
                                .flatMap(node -> this.objectService.getObject(editingContext, node.getTargetObjectId()))
                                .orElse(null);
                        var semanticEdgeTarget = this.diagramQueryService.findNodeById(diagram, edge.getTargetId())
                                .flatMap(node -> this.objectService.getObject(editingContext, node.getTargetObjectId()))
                                .orElse(null);
                        variableManager.put("semanticEdgeSource", semanticEdgeSource);
                        variableManager.put("semanticEdgeTarget", semanticEdgeTarget);
                    }

                    Result result = interpreter.evaluateExpression(variableManager.getVariables(), labelEditTool.getInitialDirectEditLabelExpression());
                    if (result.getStatus().compareTo(Status.WARNING) <= 0 && result.asString().isPresent()) {
                        initialDirectEditElementLabel = result.asString().get();
                    }
                }
            }

        }

        return initialDirectEditElementLabel;
    }

    private Optional<LabelEditTool> getLabelEditTool(org.eclipse.sirius.components.view.diagram.EdgeDescription edgeDescription, EdgeLabelKind labelKind) {
        return Optional.ofNullable(edgeDescription).map(org.eclipse.sirius.components.view.diagram.EdgeDescription::getPalette).map(switch (labelKind) {
            case BEGIN_LABEL -> EdgePalette::getBeginLabelEditTool;
            case CENTER_LABEL -> EdgePalette::getCenterLabelEditTool;
            case END_LABEL -> EdgePalette::getEndLabelEditTool;
        });
    }

    private Optional<NodeDescription> getNodeDescription(List<NodeDescription> nodeDescriptions, String descriptionId) {
        if (nodeDescriptions.isEmpty()) {
            return Optional.empty();
        }
        var optionalNodeDescription = nodeDescriptions.stream().filter(nodeDescription -> descriptionId.equals(this.idProvider.getId(nodeDescription))).findFirst();

        if (optionalNodeDescription.isEmpty()) {
            Stream<NodeDescription> childrenStream = nodeDescriptions.stream().map(NodeDescription::getChildrenDescriptions).flatMap(Collection::stream);
            Stream<NodeDescription> borderNodeStream = nodeDescriptions.stream().map(NodeDescription::getBorderNodesDescriptions).flatMap(Collection::stream);
            List<NodeDescription> childrenDescription = Stream.concat(childrenStream, borderNodeStream).toList();
            optionalNodeDescription = this.getNodeDescription(childrenDescription, descriptionId);
        }

        return optionalNodeDescription;
    }

    private Optional<EdgeDescription> getEdgeDescription(List<EdgeDescription> edgeDescriptions, String descriptionId) {
        return edgeDescriptions.stream()
                .filter(edgeDescription -> descriptionId.equals(this.idProvider.getId(edgeDescription)))
                .findFirst();
    }

    private List<EPackage> getAccessibleEPackages(IEditingContext editingContext) {
        if (editingContext instanceof EditingContext) {
            Registry packageRegistry = ((EditingContext) editingContext).getDomain().getResourceSet().getPackageRegistry();
            return packageRegistry.values().stream()
                    .filter(EPackage.class::isInstance)
                    .map(EPackage.class::cast)
                    .toList();
        } else {
            return List.of();
        }
    }

    private AQLInterpreter createInterpreter(View view, IEditingContext editingContext) {
        List<EPackage> visibleEPackages = this.getAccessibleEPackages(editingContext);
        AutowireCapableBeanFactory beanFactory = this.applicationContext.getAutowireCapableBeanFactory();
        List<Object> serviceInstances = this.javaServiceProviders.stream()
                .flatMap(provider -> provider.getServiceClasses(view).stream())
                .map(serviceClass -> {
                    try {
                        return beanFactory.createBean(serviceClass);
                    } catch (BeansException beansException) {
                        this.logger.warn("Error while trying to instantiate Java service class " + serviceClass.getName(), beansException);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .map(Object.class::cast)
                .toList();
        return new AQLInterpreter(List.of(), serviceInstances, visibleEPackages);
    }

}

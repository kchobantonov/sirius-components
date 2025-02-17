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
package org.eclipse.sirius.web.services.explorer;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.sirius.components.collaborative.api.ChangeKind;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.EditingContext;
import org.eclipse.sirius.components.representations.Failure;
import org.eclipse.sirius.components.representations.IStatus;
import org.eclipse.sirius.components.representations.Success;
import org.eclipse.sirius.components.trees.TreeItem;
import org.eclipse.sirius.web.persistence.entities.DocumentEntity;
import org.eclipse.sirius.web.persistence.repositories.IDocumentRepository;
import org.eclipse.sirius.web.services.api.id.IDParser;
import org.eclipse.sirius.web.services.explorer.api.IRenameTreeItemHandler;
import org.springframework.stereotype.Service;

/**
 * Handles document renaming triggered via a tree item from the explorer.
 *
 * @author pcdavid
 */
@Service
public class RenameDocumentTreeItemHandler implements IRenameTreeItemHandler {

    private final IDocumentRepository documentRepository;

    public RenameDocumentTreeItemHandler(IDocumentRepository documentRepository) {
        this.documentRepository = Objects.requireNonNull(documentRepository);
    }

    @Override
    public boolean canHandle(IEditingContext editingContext, TreeItem treeItem, String newLabel) {
        return ExplorerDescriptionProvider.DOCUMENT_KIND.equals(treeItem.getKind());
    }

    @Override
    public IStatus handle(IEditingContext editingContext, TreeItem treeItem, String newLabel) {
        // @formatter:off
        Optional<AdapterFactoryEditingDomain> optionalEditingDomain = Optional.of(editingContext)
                .filter(EditingContext.class::isInstance)
                .map(EditingContext.class::cast)
                .map(EditingContext::getDomain);
        // @formatter:on

        var optionalDocumentEntity = new IDParser().parse(treeItem.getId()).flatMap(this.documentRepository::findById);
        if (optionalEditingDomain.isPresent() && optionalDocumentEntity.isPresent()) {
            DocumentEntity documentEntity = optionalDocumentEntity.get();
            documentEntity.setName(newLabel);
            this.documentRepository.save(documentEntity);

            AdapterFactoryEditingDomain adapterFactoryEditingDomain = optionalEditingDomain.get();
            ResourceSet resourceSet = adapterFactoryEditingDomain.getResourceSet();

            // @formatter:off
            resourceSet.getResources().stream()
                    .filter(resource -> documentEntity.getId().toString().equals(resource.getURI().path().substring(1)))
                    .findFirst()
                    .ifPresent(resource -> {
                        resource.eAdapters().stream()
                            .filter(ResourceMetadataAdapter.class::isInstance)
                            .map(ResourceMetadataAdapter.class::cast)
                            .findFirst()
                            .ifPresent(adapter -> adapter.setName(documentEntity.getName()));
                    });
            // @formatter:on
            return new Success(ChangeKind.SEMANTIC_CHANGE, Map.of());
        }
        return new Failure("");
    }
}

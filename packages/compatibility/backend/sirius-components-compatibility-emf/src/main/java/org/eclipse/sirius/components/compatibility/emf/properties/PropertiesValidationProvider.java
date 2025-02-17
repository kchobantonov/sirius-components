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
package org.eclipse.sirius.components.compatibility.emf.properties;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.sirius.components.collaborative.validation.api.IValidationService;
import org.eclipse.sirius.components.compatibility.emf.properties.api.IPropertiesValidationProvider;
import org.eclipse.sirius.components.representations.VariableManager;
import org.springframework.stereotype.Service;

/**
 * Used to provide validation support to all the widgets of the default properties description.
 *
 * @author sbegaudeau
 */
@Service
public class PropertiesValidationProvider implements IPropertiesValidationProvider {

    private final IValidationService validationService;

    public PropertiesValidationProvider(IValidationService validationService) {
        this.validationService = Objects.requireNonNull(validationService);
    }

    @Override
    public Function<VariableManager, List<?>> getDiagnosticsProvider() {
        return variableManager -> {
            var optionalEObject = variableManager.get(VariableManager.SELF, EObject.class);
            var optionalEAttribute = variableManager.get(PropertiesDefaultDescriptionProvider.ESTRUCTURAL_FEATURE, EStructuralFeature.class);
            if (optionalEObject.isPresent() && optionalEAttribute.isPresent()) {
                return this.validationService.validate(optionalEObject.get(), optionalEAttribute.get());
            }

            return List.of();
        };
    }

    @Override
    public Function<Object, String> getKindProvider() {
        return object -> {
            String kind = "Unknown";
            if (object instanceof Diagnostic diagnostic) {
                switch (diagnostic.getSeverity()) {
                    case org.eclipse.emf.common.util.Diagnostic.ERROR:
                        kind = "Error";
                        break;
                    case org.eclipse.emf.common.util.Diagnostic.WARNING:
                        kind = "Warning";
                        break;
                    case org.eclipse.emf.common.util.Diagnostic.INFO:
                        kind = "Info";
                        break;
                    default:
                        kind = "Unknown";
                        break;
                }
            }
            return kind;
        };
    }

    @Override
    public Function<Object, String> getMessageProvider() {
        return object -> {
            if (object instanceof Diagnostic diagnostic) {
                return diagnostic.getMessage();
            }
            return "";
        };
    }
}

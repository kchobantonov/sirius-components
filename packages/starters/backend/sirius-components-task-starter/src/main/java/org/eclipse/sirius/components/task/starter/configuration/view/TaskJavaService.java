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
package org.eclipse.sirius.components.task.starter.configuration.view;

import java.util.List;
import java.util.Optional;

import org.eclipse.sirius.components.gantt.TaskDetail;
import org.eclipse.sirius.components.task.Project;
import org.eclipse.sirius.components.task.Task;
import org.eclipse.sirius.components.task.TaskTag;

/**
 * Java Service for the task related views.
 *
 * @author lfasani
 */
public class TaskJavaService {

    public TaskJavaService() {
    }

    public TaskDetail getTaskDetail(Task task) {

        String name = Optional.ofNullable(task.getName()).orElse("");
        String description = Optional.ofNullable(task.getDescription()).orElse("");
        long startDate = task.getStartDate();
        long endDate = task.getEndDate();
        int progress = task.getProgress();

        return new TaskDetail(name, description, startDate, endDate, progress);
    }

    public List<Task> getTasksWithTag(TaskTag tag) {
        return Optional.ofNullable(tag.eContainer())//
                .filter(Project.class::isInstance)//
                .map(Project.class::cast)//
                .stream()//
                .map(Project::getOwnedTasks)//
                .flatMap(List::stream)//
                .filter(task -> task.getTags().contains(tag))//
                .toList();

    }
}

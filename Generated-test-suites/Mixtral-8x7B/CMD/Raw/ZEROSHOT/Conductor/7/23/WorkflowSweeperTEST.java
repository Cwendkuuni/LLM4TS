/**
 * Copyright 2022 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.netflix.conductor.core.reconciliation;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowDefinition;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.runners.ExecutionException;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflow-123";
    private static final String WORKFLOW_NAME = "test-workflow";

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private WorkflowContext workflowContext;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    private CountDownLatch latch;

    @Before
    public void setUp() {
        latch = new CountDownLatch(1);
        reset(workflowExecutor);
        reset(queueDAO);
        reset(properties);
        reset(workflowRepairService);
    }

    @Test
    public void sweepAsync_shouldCallSweep() {
        // Given
        CompletableFuture<Void> future = new CompletableFuture<>();
        doReturn(future).when(workflowSweeper).sweep(anyString());

        // When
        workflowSweeper.sweepAsync(WORKFLOW_ID);

        // Then
        verify(workflowSweeper).sweep(WORKFLOW_ID);
    }

    @Test
    public void sweep_shouldWorkCorrectly() throws Exception {
        // Given
        WorkflowModel workflowModel = createWorkflowModel();

        doReturn(workflowModel).when(workflowExecutor).decide(anyString());

        // When
        workflowSweeper.sweep(WORKFLOW_ID);

        // Then
        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void sweep_shouldRemoveWorkflowOnNotFoundException() throws Exception {
        // Given
        doThrow(new NotFoundException("")).when(workflowExecutor).decide(anyString());

        // When
        workflowSweeper.sweep(WORKFLOW_ID);

        // Then
        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void sweep_shouldSetUnackTimeoutOnWorkflowNotFound() throws Exception {
        // Given
        doThrow(new NotFoundException("")).when(workflowExecutor).decide(anyString());

        // When
        workflowSweeper.sweep(WORKFLOW_ID);

        // Then
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong());
    }

    @Test
    public void unack_shouldSetUnackTimeout() throws Exception {
        // Given
        WorkflowModel workflowModel = createWorkflowModel();
        long postponeDurationSeconds = 45;

        when(workflowModel.getWorkflowDefinition().getTimeoutSeconds())
                .thenReturn(postponeDurationSeconds);

        // When
        workflowSweeper.unack(workflowModel, postponeDurationSeconds);

        // Then
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, postponeDurationSeconds * 1000);
    }

    @Test
    public void workflowOffsetWithJitter_shouldReturnValueWithinRange() {
        // Given
        long workflowOffsetTimeout = 45;

        // When
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        // Then
        long range = workflowOffsetTimeout / 3;
        assertTrue(result >= workflowOffsetTimeout - range);
        assertTrue(result <= workflowOffsetTimeout + range);
    }

    private WorkflowModel createWorkflowModel() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(new WorkflowDefinition());
        workflowModel.setStatus(TaskModel.Status.IN_PROGRESS);
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        return workflowModel;
    }
}
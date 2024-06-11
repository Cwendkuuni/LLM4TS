package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowDefinition;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.QueueTO;

@EnableAsync
@Component
public class WorkflowSweeperTest {

 @Async(SWEEPER_EXECUTOR_NAME)
 void asyncSweepAsync() {
 // This test case is not testing the asynchronous nature of sweepAsync() method
 }

 @Test
 public void testSweepAsync() {
 // This test case is not testing the asynchronous nature of sweepAsync() method
 }

 @Test
 public void testSweep() {
 WorkflowSweeper sweeper = new WorkflowSweeper(mock(WorkflowExecutor.class), Optional.empty(),
 mock(ConductorProperties.class), mock(QueueDAO.class));

 QueueDAO queueDAO = mock(QueueDAO.class);
 WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);

 when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(getWorkflowModel());
 when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(true);

 sweeper.setWorkflowExecutor(workflowExecutor);
 sweeper.setQueueDAO(queueDAO);

 sweeper.sweep(WORKFLOW_ID);
 }

 @Test
 public void testSweepNotFoundException() {
 WorkflowSweeper sweeper = new WorkflowSweeper(mock(WorkflowExecutor.class), Optional.empty(),
 mock(ConductorProperties.class), mock(QueueDAO.class));

 QueueDAO queueDAO = mock(QueueDAO.class);
 WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);

 sweeper.setWorkflowExecutor(workflowExecutor);
 sweeper.setQueueDAO(queueDAO);

 when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(true);

 sweeper.sweep(WORKFLOW_ID);
 }

 @Test
 public void testSweepException() {
 WorkflowSweeper sweeper = new WorkflowSweeper(mock(WorkflowExecutor.class), Optional.empty(),
 mock(ConductorProperties.class), mock(QueueDAO.class));

 QueueDAO queueDAO = mock(QueueDAO.class);
 WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);

 sweeper.setWorkflowExecutor(workflowExecutor);
 sweeper.setQueueDAO(queueDAO);

 when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new RuntimeException());

 sweeper.sweep(WORKFLOW_ID);
 }

 @Test
 public void testUnack() {
 WorkflowSweeper sweeper = new WorkflowSweeper(mock(WorkflowExecutor.class), Optional.empty(),
 mock(ConductorProperties.class), mock(QueueDAO.class));

 QueueDAO queueDAO = mock(QueueDAO.class);
 WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);

 when(workflowExecutor.getWorkflow(WORKFLOW_ID)).thenReturn(getWorkflowModel());

 sweeper.setWorkflowExecutor(workflowExecutor);
 sweeper.setQueueDAO(queueDAO);

 sweeper.unack(getWorkflowModel(), 10);
 }

 @Test
 public void testWorkflowOffsetWithJitter() {
 WorkflowSweeper sweeper = new WorkflowSweeper(mock(WorkflowExecutor.class), Optional.empty(),
 mock(ConductorProperties.class), mock(QueueDAO.class));

 long result = sweeper.workflowOffsetWithJitter(45);

 // Adding and subtracting 1/3 of the input value to the expected range
 long lowerBound = (45 / 3) * 2;
 long upperBound = (45 / 3) * 4;

 assertTrue(result >= lowerBound && result <= upperBound);
 }

 private WorkflowModel getWorkflowModel() {
 return new WorkflowModel().setWorkflowId(WORKFLOW_ID).setStatus(TaskModel.Status.COMPLETED);
 }

 private static final String WORKFLOW_ID = "workflow_1";
 private static final String DECIDER_QUEUE = "decider_queue";
}
package com.netflix.conductor.core.reconciliation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowDefinition;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.runners.RunnableScheduledExecutorService;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.utils.SystemTime;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;

public class WorkflowSweeperTest {
 private static final String WORKFLOW_ID = "workflow-123";
 private static final String WORKFLOW_NAME = "workflow-name";
 private static final String APP_ID = "app-id";

 private WorkflowSweeper workflowSweeper;
 private QueueDAO queueDAO;
 private WorkflowExecutor workflowExecutor;
 private Monitors monitors;
 private WorkflowContext workflowContext;
 private WorkflowModel workflowModel;
 private WorkflowDefinition workflowDefinition;
 private Logger logger;

 @Before
 public void setUp() {
 logger = Mockito.spy(LoggerFactory.getLogger(WorkflowSweeper.class));
 queueDAO = mock(QueueDAO.class);
 workflowExecutor = mock(WorkflowExecutor.class);
 monitors = mock(Monitors.class);
 workflowContext = mock(WorkflowContext.class);
 workflowDefinition = mock(WorkflowDefinition.class);
 workflowModel = mock(WorkflowModel.class);

 when(workflowContext.getAppId()).thenReturn(APP_ID);
 when(workflowModel.getWorkflowId()).thenReturn(WORKFLOW_ID);
 when(workflowModel.getWorkflowName()).thenReturn(WORKFLOW_NAME);
 when(workflowDefinition.getTimeoutSeconds()).thenReturn(300);
 when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDefinition);

 workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.empty(), null, queueDAO);
 workflowSweeper.logger = logger;
 }

 @Test
 public void testSweepAsync() {
 CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
 Assert.assertNotNull(future);
 }

 @Test
 public void testSweep() throws Exception {
 workflowSweeper.sweep(WORKFLOW_ID);

 verify(workflowExecutor, times(1)).decide(WORKFLOW_ID);
 }

 @Test
 public void testUnack() {
 TaskModel taskModel1 = new TaskModel();
 taskModel1.setStatus(Status.IN_PROGRESS);
 taskModel1.setTaskType(TaskType.TASK_TYPE_HUMAN);
 taskModel1.setWaitTimeout(45);

 TaskModel taskModel2 = new TaskModel();
 taskModel2.setStatus(Status.SCHEDULED);

 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setTasks(new TaskModel[] {taskModel1, taskModel2});

 workflowSweeper.unack(workflowModel, 60);

 verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 61000);
 }

 @Test
 public void testWorkflowOffsetWithJitter() {
 long result = workflowSweeper.workflowOffsetWithJitter(45);

 long expectedLowerBound = 45 - (45 / 3);
 long expectedUpperBound = 45 + (45 / 3);

 Assert.assertTrue(result >= expectedLowerBound);
 Assert.assertTrue(result <= expectedUpperBound);
 }

 @Test
 public void testSweeperRemovesWorkflowFromQueueOnNotFoundException() throws Exception {
 when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException("Workflow not found"));

 workflowSweeper.sweep(WORKFLOW_ID);

 verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
 }

 @Test
 public void testSweeperLogsErrorOnUnexpectedException() throws Exception {
 when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new RuntimeException("Unexpected error"));

 workflowSweeper.sweep(WORKFLOW_ID);

 verify(logger, times(1)).error(ArgumentMatchers.anyString(), ArgumentMatchers.any(Exception.class));
 }
}
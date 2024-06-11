package com.netflix.conductor.core.reconciliation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.util.AsyncTestUtil;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.run.Workflow;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.TaskSupervisor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.utils.IDGenerator;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskDAO;
import com.netflix.conductor.dao.WorkflowDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskEvent;
import com.netflix.conductor.model.TaskModel.Status;

public class WorkflowSweeperTest {

 @Mock
 private ConductorProperties properties;

 @Mock
 private WorkflowExecutor workflowExecutor;

 @Mock
 private QueueDAO queueDAO;

 @Mock
 private WorkflowDAO workflowDAO;

 @Mock
 private TaskDAO taskDAO;

 @Mock
 private TaskSupervisor taskSupervisor;

 @Mock
 private Monitors monitors;

 private WorkflowSweeper workflowSweeper;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);

 when(properties.getAppId()).thenReturn("testAppId");
 when(properties.getWorkflowOffsetTimeout()).thenReturn(Optional.of(60L));

 workflowSweeper = new WorkflowSweeper(workflowExecutor, null, properties, queueDAO);
 }

 @Test
 public void testSweepAsync() throws Exception {
 // Test when workflow is found and its status is not terminal
 WorkflowModel workflowModel = createWorkflowModel("workflowId", "testWorkflowName",
 Status.IN_PROGRESS);
 when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);

 CompletableFuture<Void> future = workflowSweeper.sweepAsync("workflowId");

 // Verify that sweep method is called
 verify(workflowExecutor, times(1)).decide("workflowId");

 // Wait for the completion of the future
 AsyncTestUtil.getFuture(future);

 // Verify that unack method is called with correct parameters
 verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
 }

 @Test
 public void testSweepAsyncWhenWorkflowNotFound() throws Exception {
 // Test when workflow is not found
 when(workflowExecutor.decide(anyString())).thenThrow(new NotFoundException("workflowId"));

 CompletableFuture<Void> future = workflowSweeper.sweepAsync("workflowId");

 // Verify that queueDAO.remove is called
 verify(queueDAO, times(1)).remove(DECIDER_QUEUE, "workflowId");

 // Wait for the completion of the future
 AsyncTestUtil.getFuture(future);
 }

 @Test
 public void testSweepAsyncWhenException() throws Exception {
 // Test when an exception is thrown
 when(workflowExecutor.decide(anyString())).thenThrow(new RuntimeException("test exception"));

 CompletableFuture<Void> future = workflowSweeper.sweepAsync("workflowId");

 // Verify that an error is recorded in Monitors
 verify(monitors, times(1)).error(anyString(), anyString());

 // Wait for the completion of the future
 AsyncTestUtil.getFuture(future);
 }

 @Test
 public void testSweep() throws Exception {
 // Test when workflow is found and its status is not terminal
 WorkflowModel workflowModel = createWorkflowModel("workflowId", "testWorkflowName",
 Status.IN_PROGRESS);
 when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);

 workflowSweeper.sweep("workflowId");

 // Verify that queueDAO.remove is not called
 verify(queueDAO, never()).remove(DECIDER_QUEUE, "workflowId");

 // Verify that unack method is called with correct parameters
 verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
 }

 @Test
 public void testUnack() throws Exception {
 // Test with IN_PROGRESS task and wait timeout
 WorkflowModel workflowModel = createWorkflowModel("workflowId", "testWorkflowName",
 Status.IN_PROGRESS);
 TaskModel taskModel = createTaskModel(1L, "taskId", TaskType.TASK_TYPE_WAIT, Status.IN_PROGRESS,
 61L);
 workflowModel.getTasks().add(taskModel);

 workflowSweeper.unack(workflowModel, 60);

 // Verify that queueDAO.setUnackTimeout is called with correct parameters
 verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
 }

 @Test
 public void testUnackWhenScheduledTask() throws Exception {
 // Test with SCHEDULED task and task definition poll timeout
 WorkflowModel workflowModel = createWorkflowModel("workflowId", "testWorkflowName",
 Status.SCHEDULED);
 TaskModel taskModel = createTaskModel(1L, "taskId", TaskType.TASK_TYPE_SCHEDULED, Status.SCHEDULED,
 0L);
 TaskDef taskDef = new TaskDef();
 taskDef.setPollTimeoutSeconds(61);
 taskModel.setTaskDefinition(Optional.of(taskDef));
 workflowModel.getTasks().add(taskModel);

 workflowSweeper.unack(workflowModel, 60);

 // Verify that queueDAO.setUnackTimeout is called with correct parameters
 verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
 }

 @Test
 public void testWorkflowOffsetWithJitter() throws Exception {
 // Test with 60 seconds timeout
 long result = workflowSweeper.workflowOffsetWithJitter(60);

 // Verify that the result is between 40 and 80
 Assert.assertTrue(result >= 40 && result <= 80);
 }

 private WorkflowModel createWorkflowModel(String id, String name, Status status) {
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setWorkflowId(id);
 workflowModel.setWorkflowName(name);
 workflowModel.setStatus(status);
 return workflowModel;
 }

 private TaskModel createTaskModel(long id, String name, TaskType type, Status status, long timeout) {
 TaskModel taskModel = new TaskModel();
 taskModel.setId(id);
 taskModel.setName(name);
 taskModel.setTaskType(type);
 taskModel.setStatus(status);
 taskModel.setTimeout(timeout);
 return taskModel;
 }
}
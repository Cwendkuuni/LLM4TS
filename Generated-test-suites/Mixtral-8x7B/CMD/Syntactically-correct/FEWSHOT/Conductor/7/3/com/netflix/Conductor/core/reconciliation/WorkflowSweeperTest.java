package com.netflix.conductor.core.reconciliation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
import org.springframework.util.concurrent.ListenableFuture;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.runners.WorkflowRunner;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper.SweepResult;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.service.WorkflowRepairService;

public class WorkflowSweeperTest {

@Mock
private QueueDAO queueDAO;

@Mock
private WorkflowExecutor workflowExecutor;

@Mock
private WorkflowRepairService workflowRepairService;

@Mock
private Monitors monitors;

@Mock
private ConductorProperties properties;

private WorkflowSweeper workflowSweeper;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);

when(properties.getAppId()).thenReturn("testAppId");
when(properties.getWorkflowOffsetTimeout()).thenReturn(Optional.of(30L));

workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties,
queueDAO);
}

@Test
public void testSweepAsync() throws Exception {
// Given
String workflowId = "testWorkflowId";

ListenableFuture<Void> future = new ThreadPoolTaskExecutor(1).submit(
() -> workflowSweeper.sweep(workflowId));

// When
verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
verify(workflowExecutor, times(1)).decide(workflowId);

// Then
Assert.assertTrue(future.isDone());
Assert.assertFalse(future.isCancelled());
Assert.assertEquals(AsyncResult.forValue(null).getClass(), future.getClass());

verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, eq(workflowId), anyLong());
}

@Test
public void testSweepAsync_NotFoundException() throws Exception {
// Given
String workflowId = "testWorkflowId";

doThrow(new NotFoundException("Test NotFoundException")).when(workflowExecutor)
.decide(workflowId);

ListenableFuture<Void> future = new ThreadPoolTaskExecutor(1).submit(
() -> workflowSweeper.sweep(workflowId));

// When
verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
verify(workflowExecutor, times(1)).decide(workflowId);

// Then
Assert.assertTrue(future.isDone());
Assert.assertFalse(future.isCancelled());
verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, eq(workflowId), anyLong());
}

@Test
public void testSweepAsync_Exception() throws Exception {
// Given
String workflowId = "testWorkflowId";

doThrow(new RuntimeException("Test RuntimeException")).when(workflowExecutor)
.decide(workflowId);

ListenableFuture<Void> future = new ThreadPoolTaskExecutor(1).submit(
() -> workflowSweeper.sweep(workflowId));

// When
verify(queueDAO, never()).remove(DECIDER_QUEUE, workflowId);
verify(workflowExecutor, times(1)).decide(workflowId);

// Then
Assert.assertTrue(future.isDone());
Assert.assertFalse(future.isCancelled());
verify(queueDAO, never()).setUnackTimeout(DECIDER_QUEUE, eq(workflowId), anyLong());
verify(monitors, times(1)).error(eq(WorkflowSweeper.class.getSimpleName()), eq("sweep"));
}

@Test
public void testSweepAsync_WorkflowRepairService() throws Exception {
// Given
String workflowId = "testWorkflowId";

WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setStatus(Status.IN_PROGRESS);
workflowModel.setWorkflowDefinition(new WorkflowDef());

TaskModel taskModel1 = new TaskModel();
taskModel1.setStatus(Status.IN_PROGRESS);
taskModel1.setTaskType(TaskType.TASK_TYPE_WAIT);
taskModel1.setWaitTimeout(30);
workflowModel.addTask(taskModel1);

when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

doNothing().when(workflowRepairService).verifyAndRepairWorkflowTasks(workflowId);

// When
ListenableFuture<Void> future = new ThreadPoolTaskExecutor(1).submit(
() -> workflowSweeper.sweep(workflowId));

// Then
verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
verify(workflowExecutor, times(1)).decide(workflowId);
verify(workflowRepairService, times(1))
.verifyAndRepairWorkflowTasks(workflowId);

Assert.assertTrue(future.isDone());
Assert.assertFalse(future.isCancelled());
verify(queueDAO, never()).setUnackTimeout(DECIDER_QUEUE, eq(workflowId), anyLong());
}

@Test
public void testUnack() throws Exception {
// Given
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setStatus(Status.IN_PROGRESS);
workflowModel.setWorkflowDefinition(new WorkflowDef());

TaskModel taskModel1 = new TaskModel();
taskModel1.setStatus(Status.IN_PROGRESS);
taskModel1.setTaskType(TaskType.TASK_TYPE_WAIT);
taskModel1.setWaitTimeout(30);
workflowModel.addTask(taskModel1);

when(queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId()))
.thenReturn(Optional.of(30L));

// When
workflowSweeper.unack(workflowModel, 45);

// Then
verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, eq(workflowModel.getWorkflowId()),
eq(46000L));
}

@Test
public void testWorkflowOffsetWithJitter() throws Exception {
// Given
long workflowOffsetTimeout = 30;

// When
long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

// Then
Assert.assertTrue(result >= workflowOffsetTimeout - (workflowOffsetTimeout / 3));
Assert.assertTrue(result <= workflowOffsetTimeout + (workflowOffsetTimeout / 3));
}
}
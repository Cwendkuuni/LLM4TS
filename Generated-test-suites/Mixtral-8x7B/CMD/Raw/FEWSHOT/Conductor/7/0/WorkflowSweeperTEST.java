package com.netflix.conductor.core.reconciliation;

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
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.events.EventExecutor;

public class WorkflowSweeperTest {

private static final String WORKFLOW_ID = "workflow_1";
private static final String WORKFLOW_NAME = "workflow_name";
private static final String DECIDER_QUEUE = "decider_queue";
private static final String APP_ID = "app_id";
private ThreadPoolTaskExecutor executor;
private WorkflowExecutor workflowExecutor;
private QueueDAO queueDAO;
private WorkflowRepairService workflowRepairService;
private WorkflowModel workflowModel;
private WorkflowContext workflowContext;
private EventExecutor eventExecutor;
private ConductorProperties conductorProperties;
private WorkflowSweeper workflowSweeper;

@Before
public void setup() {
executor = mock(ThreadPoolTaskExecutor.class);
workflowExecutor = mock(WorkflowExecutor.class);
queueDAO = mock(QueueDAO.class);
workflowRepairService = mock(WorkflowRepairService.class);
eventExecutor = mock(EventExecutor.class);
conductorProperties = mock(ConductorProperties.class);

when(conductorProperties.getAppId()).thenReturn(APP_ID);

workflowContext = mock(WorkflowContext.class);

workflowModel = new WorkflowModel();
workflowModel.setWorkflowId(WORKFLOW_ID);
workflowModel.setWorkflowName(WORKFLOW_NAME);
workflowModel.setStatus(TaskModel.Status.IN_PROGRESS);

when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

workflowSweeper =
new WorkflowSweeper(
workflowExecutor,
Optional.of(workflowRepairService),
conductorProperties,
queueDAO);

workflowSweeper.setExecutor(executor);
workflowSweeper.setEventExecutor(eventExecutor);
}

@Test
public void sweepAsyncTest() {
CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
verify(executor, times(1)).execute(ArgumentMatchers.any());
future.join();
verify(workflowExecutor, times(1)).decide(WORKFLOW_ID);
}

@Test
public void sweepAsyncExceptionTest() {
when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new RuntimeException());
CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
verify(executor, times(1)).execute(ArgumentMatchers.any());
future.join();
verify(workflowExecutor, times(1)).decide(WORKFLOW_ID);
}

@Test
public void sweepAsyncCompletedFutureTest() {
when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);
CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
verify(executor, times(1)).execute(ArgumentMatchers.any());
future.complete(null);
verify(workflowExecutor, times(1)).decide(WORKFLOW_ID);
}

@Test
public void sweepAsyncCompletedExceptionallyTest() {
when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);
CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
verify(executor, times(1)).execute(ArgumentMatchers.any());
future.completeExceptionally(new RuntimeException());
verify(workflowExecutor, times(1)).decide(WORKFLOW_ID);
}

@Test
@VisibleForTesting
public void sweepTest() {
WorkflowModel workflow = new WorkflowModel();
workflow.setWorkflowId(WORKFLOW_ID);
workflow.setWorkflowName(WORKFLOW_NAME);
workflow.setStatus(TaskModel.Status.IN_PROGRESS);

workflowSweeper.sweep(WORKFLOW_ID);
verify(workflowExecutor, times(1)).decide(WORKFLOW_ID);
}

@Test
public void sweepTestNotfoundException() {
workflowSweeper.sweep(WORKFLOW_ID);
verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
}

@Test
public void sweepTestNonTerminalStatus() {
workflowModel.setStatus(TaskModel.Status.RUNNING);
when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);
workflowSweeper.sweep(WORKFLOW_ID);
verify(queueDAO, never()).remove(DECIDER_QUEUE, WORKFLOW_ID);
}

@Test
public void sweepTestTerminalStatus() {
workflowModel.setStatus(TaskModel.Status.COMPLETED);
when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);
workflowSweeper.sweep(WORKFLOW_ID);
verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
}

@Test
public void unackTest() {
workflowModel.setWorkflowId(WORKFLOW_ID);
workflowModel.setStatus(TaskModel.Status.IN_PROGRESS);

Mockito.when(
conductorProperties.getWorkflowOffsetTimeout()
.getSeconds())
.thenReturn(60L);

workflowSweeper.unack(workflowModel, 60);
verify(queueDAO, times(1))
.setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), 61000L);
}

@Test
public void workflowOffsetWithJitterTest() {
long workflowOffsetTimeout = 45;
long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
Assert.assertTrue("Jitter should be between -15 and 15", jitter >= -15 && jitter <= 15);
}
}
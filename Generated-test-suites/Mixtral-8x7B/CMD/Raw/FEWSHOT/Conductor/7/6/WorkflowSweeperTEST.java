package com.netflix.conductor.core.reconciliation;

import static org.mockito.Mockito.*;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.workflow.WorkflowDefinition;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.runners.WorkerTaskCoordinator;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel.Status;
import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@EnableAsync
public class WorkflowSweeperTest {

@Mock private WorkflowExecutor workflowExecutor;
@Mock private QueueDAO queueDAO;
@Mock private TaskDAO taskDAO;
@Mock private WorkflowRepairService workflowRepairService;
@Mock private Monitors monitors;

@InjectMocks private WorkflowSweeper workflowSweeper;

@Before
public void init() {
MockitoAnnotations.initMocks(this);
}

@Test
public void testSweepAsync() throws Exception {
String workflowId = "testWorkflowId";

 CompletableFuture<Void> completableFuture = new CompletableFuture<>();

 ReflectionTestUtils.setField(
workflowSweeper, "workflowExecutor",
new WorkflowExecutor() {
@Override
public CompletableFuture<WorkflowModel> decide(String workflowId) {
CompletableFuture<WorkflowModel> future = new CompletableFuture<>();
future.complete(new WorkflowModel());
return future;
}

@Override
public Optional<WorkerTaskCoordinator> getWorkerTaskCoordinator(TaskDef taskDef) {
return Optional.empty();
}

@Override
public ListenableFuture<Void> completeTask(
TaskModel taskModel,
WorkflowModel workflowModel,
boolean isRetry,
int retryCount,
String reason) {
ListenableFuture<Void> future = new ListenableFutureCallback<Void>() {};
future.addCallback(
new ListenableFutureCallback<Void>() {
@Override
public void onSuccess(Void result) {}

@Override
public void onFailure(Throwable ex) {}
});
return future;
}

@Override
public ListenableFuture<Void> failTask(
TaskModel taskModel,
WorkflowModel workflowModel,
String reason) {
ListenableFuture<Void> future = new ListenableFutureCallback<Void>() {};
future.addCallback(
new ListenableFutureCallback<Void>() {
@Override
public void onSuccess(Void result) {}

@Override
public void onFailure(Throwable ex) {}
});
return future;
}
});

Assert.assertNotNull(completableFuture);
workflowSweeper.sweepAsync(workflowId);
}

@Test
public void testSweep() {
String workflowId = "testWorkflowId";

 WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setStatus(Status.COMPLETED);

when(workflowExecutor.decide(workflowId))
.thenReturn(Optional.of(workflowModel));

queueDAO.remove(DECIDER_QUEUE, workflowId);

workflowSweeper.sweep(workflowId);

verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
}

@Test
public void testSweepNotFoundException() {
String workflowId = "testWorkflowId";

when(workflowExecutor.decide(workflowId))
.thenThrow(new NotFoundException("Test exception"));

queueDAO.remove(DECIDER_QUEUE, workflowId);

workflowSweeper.sweep(workflowId);

verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
}

@Test
public void testUnack() {
String workflowId = "testWorkflowId";
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setStatus(Status.IN_PROGRESS);

when(taskDAO.getTaskModelByWorkflowId(workflowId))
.thenReturn(workflowModel);

WorkflowDefinition workflowDefinition = new WorkflowDefinition();
workflowDefinition.setTimeoutSeconds(30);
workflowModel.setWorkflowDefinition(workflowDefinition);

workflowSweeper.unack(workflowModel, 60);

verify(queueDAO, times(1))
.setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), 61000);
}

@Test
public void testWorkflowOffsetWithJitter() {
long workflowOffsetTimeout = 45;
long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

long range = workflowOffsetTimeout / 3;
long jitter = new Random().nextInt((int) (2 * range + 1)) - range;

Assert.assertTrue(result >= (workflowOffsetTimeout - range) + jitter);
Assert.assertTrue(result <= (workflowOffsetTimeout + range) + jitter);
}
}
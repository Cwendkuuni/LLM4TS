import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.Async;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WorkflowSweeperTest {

@Mock
private WorkflowExecutor workflowExecutor;

@Mock
private QueueDAO queueDAO;

@Mock
private WorkflowRepairService workflowRepairService;

@Mock
private ConductorProperties conductorProperties;

private WorkflowSweeper workflowSweeper;
private WorkflowContext workflowContext;
private CompletableFuture<Void> completableFuture;

@BeforeEach
public void setup() {
MockitoAnnotations.openMocks(this);

workflowContext = mock(WorkflowContext.class);
when(workflowContext.getAppId()).thenReturn("test-app");

completableFuture = new CompletableFuture<>();

workflowSweeper = new WorkflowSweeper(
workflowExecutor, Optional.of(workflowRepairService), conductorProperties, queueDAO) {
@Async(WorkflowSweeper.SWEEPER_EXECUTOR_NAME)
@Override
public CompletableFuture<Void> sweepAsync(String workflowId) {
sweep(workflowId);
return completableFuture;
}
};
}

@Test
public void givenWorkflowSweeper_whenExtractPublicMethods_thenCorrectMethodsFound() {
Class<WorkflowSweeper> clazz = WorkflowSweeper.class;

assertNotNull(clazz.getDeclaredMethod("sweepAsync", String.class),
"sweepAsync method not found");
assertNotNull(clazz.getDeclaredMethod("sweep", String.class),
"sweep method not found");
assertNotNull(clazz.getDeclaredMethod("unack", WorkflowModel.class, long.class),
"unack method not found");
assertNotNull(clazz.getDeclaredMethod("workflowOffsetWithJitter", long.class),
"workflowOffsetWithJitter method not found");
}

@Test
public void givenWorkflowSweeper_whenSweepAsyncCalledWithNonExistentWorkflow_thenCompleteFuture() {
when(workflowExecutor.decide(anyString()))
.thenReturn(mock(WorkflowModel.class));

CompletableFuture<Void> future = workflowSweeper.sweepAsync("non-existent-workflow");

assertFalse(future.isCancelled(), "Future should not be cancelled");
assertFalse(future.isCompletedExceptionally(),
"Future should not be completed exceptionally");
}

@Test
public void givenWorkflowSweeper_whenSweepAsyncCalledWithExistentWorkflow_thenLogSuccess() {
final String workflowId = "existent-workflow";
WorkflowModel workflowModel = createWorkflowModel(workflowId);

when(workflowExecutor.decide(workflowId))
.thenReturn(workflowModel);

workflowSweeper.sweepAsync(workflowId);

verify(LOGGER).debug("Running sweeper for workflow {}", workflowId);
}

@Test
public void givenWorkflowSweeper_whenSweepAsyncCalledWithExistentWorkflow_thenRemoveFromQueue() {
final String workflowId = "existent-workflow";
WorkflowModel workflowModel = createWorkflowModel(workflowId);

when(workflowExecutor.decide(workflowId))
.thenReturn(workflowModel);

workflowSweeper.sweepAsync(workflowId);

verify(queueDAO, times(1))
.remove(DECIDER_QUEUE, workflowId);
}

@Test
public void givenWorkflowSweeper_whenSweepAsyncCalledWithNonExistentWorkflow_thenLogInfo() {
when(workflowExecutor.decide(anyString()))
.thenThrow(new NotFoundException("Not found exception"));

workflowSweeper.sweepAsync("non-existent-workflow");

verify(LOGGER).info("Workflow NOT found for id:{}. Removed it from decider queue",
"non-existent-workflow",
any());
}

@Test
public void givenWorkflowSweeper_whenSweepAsyncCalledWithExistentWorkflow_thenCallUnack() {
final String workflowId = "existent-workflow";
WorkflowModel workflowModel = createWorkflowModel(workflowId);

when(workflowExecutor.decide(workflowId))
.thenReturn(workflowModel);

workflowSweeper.sweepAsync(workflowId);

verify(workflowSweeper, times(1))
.unack(workflowModel, anyLong());
}

@Test
public void givenWorkflowSweeper_whenSweepAsyncCalledWithExistentWorkflowWithTerminalStatus_thenRemoveFromQueue() {
final String workflowId = "existent-workflow";
WorkflowModel workflowModel = createWorkflowModel(workflowId);
workflowModel.setStatus(WorkflowStatus.COMPLETED);

when(workflowExecutor.decide(workflowId))
.thenReturn(workflowModel);

workflowSweeper.sweepAsync(workflowId);

verify(queueDAO, times(1))
.remove(DECIDER_QUEUE, workflowId);
}

@Test
public void givenWorkflowSweeper_whenUnackCalled_thenSetUnackTimeout() {
final WorkflowModel workflowModel = mock(WorkflowModel.class);
final String workflowId = "workflow-id";

when(workflowModel.getWorkflowId())
.thenReturn(workflowId);

workflowSweeper.unack(workflowModel, 60);

verify(queueDAO, times(1))
.setUnackTimeout(DECIDER_QUEUE, workflowId, 60 * 1000L);
}

@Test
public void givenWorkflowSweeper_whenWorkflowOffsetWithJitterCalled_thenJitterValue() {
final long workflowOffsetTimeout = 45;
final long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

assertEquals(30, jitter, "Jitter value should be between [30-60]");
}

@Test
public void givenWorkflowSweeper_whenSweepCalled_thenSweepAsyncCalled() {
workflowSweeper.sweep("workflow-id");

verify(workflowSweeper, times(1))
.sweepAsync("workflow-id");
}

@Test
public void givenWorkflowSweeper_whenSweepCalled_thenCompleteFuture() {
workflowSweeper.sweep("workflow-id");

StepVerifier.create(completableFuture)
.expectComplete()
.verify();
}

@Test
public void givenWorkflowSweeper_whenWorkflowRepairServiceIsNull_thenNotException() {
workflowSweeper = new WorkflowSweeper(
workflowExecutor, Optional.empty(), conductorProperties, queueDAO);

workflowSweeper.sweep("workflow-id");

verify(workflowRepairService, never())
.verifyAndRepairWorkflowTasks(anyString());
}

@Test
public void givenWorkflowSweeper_whenWorkflowRepairServiceNotNull_thenCallVerifyAndRepairTasks() {
workflowSweeper = new WorkflowSweeper(
workflowExecutor, Optional.of(workflowRepairService), conductorProperties, queueDAO);

final String workflowId = "workflow-id";
WorkflowModel workflowModel = createWorkflowModel(workflowId);

when(workflowExecutor.decide(workflowId))
.thenReturn(workflowModel);

workflowSweeper.sweep("workflow-id");

verify(workflowRepairService, times(1))
.verifyAndRepairWorkflowTasks(workflowId);
}

@Test
public void givenWorkflowSweeper_whenWorkflowOffsetWithJitterCalledWithNegativeValue_thenException() {
assertThrows(IllegalArgumentException.class, () -> workflowSweeper.workflowOffsetWithJitter(-1));
}

@Test
public void givenWorkflowSweeper_whenWorkflowOffsetWithJitterCalledWithZeroValue_thenException() {
assertThrows(IllegalArgumentException.class, () -> workflowSweeper.workflowOffsetWithJitter(0));
}

private WorkflowModel createWorkflowModel(String workflowId) {
return WorkflowModel.builder()
.workflowId(workflowId)
.workflowName("workflow-name")
.status(WorkflowStatus.IN_PROGRESS)
.build();
}

private TaskModel createTaskModel(String taskId, TaskType taskType) {
return TaskModel.builder()
.taskId(taskId)
.taskType(taskType)
.status(TaskModel.Status.IN_PROGRESS)
.build();
}
}
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.model.tasks.TaskModel;
import com.netflix.conductor.common.model.workflow.WorkflowModel;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.test.util.AsyncTestUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WorkflowSweeperTest {

private WorkflowSweeper workflowSweeper;
private WorkflowExecutor workflowExecutor;
private QueueDAO queueDAO;
private WorkflowRepairService workflowRepairService;

@BeforeEach
public void setup() {
workflowExecutor = Mockito.mock(WorkflowExecutor.class);
queueDAO = Mockito.mock(QueueDAO.class);
workflowRepairService = Mockito.mock(WorkflowRepairService.class);

ConductorProperties properties = new ConductorProperties();

workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
}

@Test
public void sweepAsync_completesFuture() throws ExecutionException, InterruptedException, TimeoutException {
CompletableFuture<Void> future = workflowSweeper.sweepAsync("workflowId");
future.get(5, TimeUnit.SECONDS);
}

@Test
public void sweepAsync_doesNotThrowException() {
AsyncTestUtils.assertFutureDoesNotThrow("sweepAsync should not throw an exception",
workflowSweeper.sweepAsync("workflowId"));
}

@Test
public void sweep_workflowNotFound_removesFromDeciderQueue() {
WorkflowContext workflowContext = new WorkflowContext("appId");
WorkflowContext.set(workflowContext);

when(workflowExecutor.decide("workflowId")).thenThrow(new NotFoundException("workflowId"));

workflowSweeper.sweep("workflowId");

verify(queueDAO, times(1)).remove(DECIDER_QUEUE, "workflowId");
}

@Test
public void sweep_workflowFound_setsUnackTimeout() {
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setStatus(WorkflowStatus.COMPLETED);
workflowModel.setWorkflowDefinition(new WorkflowDefinition("workflowName"));

when(workflowExecutor.decide("workflowId")).thenReturn(workflowModel);

workflowSweeper.sweep("workflowId");

verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, "workflowId", 61);
}

@Test
public void unack_inProgressTask_setsUnackTimeout() {
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setStatus(WorkflowStatus.RUNNING);
TaskModel taskModel = new TaskModel();
taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
workflowModel.addTask(taskModel);

workflowSweeper.unack(workflowModel, 60);

verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, "workflowId", 61);
}

@Test
public void unack_scheduledTask_setsUnackTimeout() {
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setStatus(WorkflowStatus.RUNNING);
TaskModel taskModel = new TaskModel();
taskModel.setStatus(TaskModel.Status.SCHEDULED);
TaskDef taskDef = new TaskDef();
taskDef.setPollTimeoutSeconds(10);
taskModel.setTaskDefinition(Optional.of(taskDef));
workflowModel.addTask(taskModel);

workflowSweeper.unack(workflowModel, 60);

verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, "workflowId", 11);
}

@Test
public void workflowOffsetWithJitter_returnsValueInRange() {
long workflowOffsetTimeout = 45;
long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

Assertions.assertTrue(jitter >= (workflowOffsetTimeout - (workflowOffsetTimeout / 3))
&& jitter <= (workflowOffsetTimeout + (workflowOffsetTimeout / 3)));
}

private class WorkflowRepairService {
public void verifyAndRepairWorkflowTasks(String workflowId) {
// Mock implementation
}
}

private class WorkflowDefinition {
private String name;

public WorkflowDefinition(String name) {
this.name = name;
}

public String getName() {
return name;
}

public void setTimeoutSeconds(int timeoutSeconds) {
// Mock implementation
}
}
}
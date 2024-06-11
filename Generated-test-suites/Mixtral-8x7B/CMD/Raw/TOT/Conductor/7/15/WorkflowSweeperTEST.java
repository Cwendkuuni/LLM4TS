import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.model.tasks.TaskModel;
import com.netflix.conductor.common.model.workflow.WorkflowModel;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.events.EventQueueService;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.QueueMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.scheduling.annotation.AsyncResult;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WorkflowSweeperTest {

 private WorkflowSweeper workflowSweeper;
 private WorkflowExecutor workflowExecutor;
 private QueueDAO queueDAO;
 private TaskDAO taskDAO;
 private EventQueueService eventQueueService;
 private Monitors monitors;
 private QueueMetrics queueMetrics;

 @BeforeEach
 public void setup() {
 workflowExecutor = mock(WorkflowExecutor.class);
 queueDAO = mock(QueueDAO.class);
 taskDAO = mock(TaskDAO.class);
 eventQueueService = mock(EventQueueService.class);
 monitors = mock(Monitors.class);
 queueMetrics = mock(QueueMetrics.class);

 workflowSweeper =
 new WorkflowSweeper(
 workflowExecutor, null, new ConductorProperties(), queueDAO, taskDAO, eventQueueService, monitors);
 }

 @Test
 public void testSweepAsync() throws ExecutionException, InterruptedException {
 CompletableFuture<Void> completableFuture =
 workflowSweeper.sweepAsync("workflowId1").get(); // this is a blocking call
 Mockito.verify(workflowExecutor).decide("workflowId1");
 }

 @Test
 public void testSweep() {
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setStatus(Status.IN_PROGRESS);

 when(workflowExecutor.decide(anyString()))
 .thenReturn(workflowModel);

 workflowSweeper.sweep("workflowId1");
 Mockito.verify(queueDAO).remove(DECIDER_QUEUE, "workflowId1");
 }

 @Test
 public void testSweepNotFoundException() {
 when(workflowExecutor.decide(anyString()))
 .thenThrow(new NotFoundException("workflow not found"));

 workflowSweeper.sweep("workflowId1");
 Mockito.verify(queueDAO).remove(DECIDER_QUEUE, "workflowId1");
 }

 @Test
 public void testUnack() {
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setStatus(Status.IN_PROGRESS);

 TaskModel taskModel = new TaskModel();
 taskModel.setStatus(Status.IN_PROGRESS);
 taskModel.setTaskType(TaskType.TASK_TYPE_TASK);
 workflowModel.setTasks(Collections.singletonList(taskModel));

 when(workflowExecutor.decide(anyString()))
 .thenReturn(workflowModel);

 when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong()))
 .thenReturn(true);

 workflowSweeper.sweep("workflowId1");
 Mockito.verify(queueDAO)
 .setUnackTimeout(DECIDER_QUEUE, "workflowId1", 61L); // 61L = 45 + (45/3)
 }

 @Test
 public void testWorkflowOffsetWithJitter() {
 long workflowOffsetTimeout = 45;
 long range = workflowOffsetTimeout / 3;
 long jitter1 = new Random().nextInt((int) (2 * range + 1)) - range;
 long jitter2 = new Random().nextInt((int) (2 * range + 1)) - range;

 assertEquals(workflowOffsetTimeout - range, workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout) - jitter1, 0);
 assertEquals(workflowOffsetTimeout - range, workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout) - jitter2, 0);
 assertEquals(workflowOffsetTimeout - range, workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout) - jitter2, 0);
 }

 @Test
 public void testVerifyAndRepairWorkflowTasks() {
 WorkflowRepairService workflowRepairService = mock(WorkflowRepairService.class);
 workflowSweeper =
 new WorkflowSweeper(
 workflowExecutor, Optional.of(workflowRepairService), new ConductorProperties(), queueDAO);

 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setStatus(Status.IN_PROGRESS);

 when(workflowExecutor.decide(anyString()))
 .thenReturn(workflowModel);

 workflowSweeper.sweep("workflowId1");
 Mockito.verify(workflowRepairService).verifyAndRepairWorkflowTasks("workflowId1");
 }

 @Test
 public void testAsyncUnack() {
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setStatus(Status.IN_PROGRESS);

 TaskModel taskModel = new TaskModel();
 taskModel.setStatus(Status.IN_PROGRESS);
 taskModel.setTaskType(TaskType.TASK_TYPE_TASK);
 workflowModel.setTasks(Collections.singletonList(taskModel));

 when(workflowExecutor.decideAsync(anyString()))
 .thenReturn(new AsyncResult<>(workflowModel));

 when(queueDAO.setUnackTimeoutAsync(anyString(), anyString(), anyLong()))
 .thenReturn(Mono.just(true));

 StepVerifier.create(workflowSweeper.sweepAsync("workflowId1"))
 .expectSubscription()
 .verifyComplete();

 Mockito.verify(queueDAO)
 .setUnackTimeoutAsync(DECIDER_QUEUE, "workflowId1", 61L); // 61L = 45 + (45/3)
 }

 @Test
 public void testAsyncSweepNotFoundException() {
 when(workflowExecutor.decideAsync(anyString()))
 .thenReturn(new AsyncResult<>(new WorkflowModel().setStatus(Status.FAILED)));

 StepVerifier.create(workflowSweeper.sweepAsync("workflowId1"))
 .expectSubscription()
 .expectComplete()
 .verify();

 Mockito.verify(queueDAO).remove(DECIDER_QUEUE, "workflowId1");
 }

 @Test
 public void testAsyncSweep() {
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setStatus(Status.IN_PROGRESS);

 when(workflowExecutor.decideAsync(anyString()))
 .thenReturn(new AsyncResult<>(workflowModel));

 StepVerifier.create(workflowSweeper.sweepAsync("workflowId1"))
 .expectSubscription()
 .expectComplete()
 .verify();

 Mockito.verify(queueDAO).remove(DECIDER_QUEUE, "workflowId1");
 }
}
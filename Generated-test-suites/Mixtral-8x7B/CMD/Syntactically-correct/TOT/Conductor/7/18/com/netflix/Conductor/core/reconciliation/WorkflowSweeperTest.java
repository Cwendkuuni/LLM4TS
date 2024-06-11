import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.utils.TestUtils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.concurrent.ListenableFuture;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WorkflowSweeperTest {

 private WorkflowSweeper workflowSweeper;
 private WorkflowExecutor workflowExecutor;
 private QueueDAO queueDAO;
 private ConductorProperties conductorProperties;
 private ExecutorService executorService;

 @BeforeEach
 public void setUp() {
 workflowExecutor = mock(WorkflowExecutor.class);
 queueDAO = mock(QueueDAO.class);
 conductorProperties = mock(ConductorProperties.class);
 executorService = Executors.newSingleThreadExecutor();

 workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.empty(), conductorProperties, queueDAO) {
 @Override
 public CompletableFuture<Void> sweepAsync(String workflowId) {
 return super.sweepAsync(workflowId);
 }
 };

 ReflectionTestUtils.setField(workflowSweeper, "sweeperExecutor", executorService);
 }

 @Test
 public void testSweepAsync() throws Exception {
 String workflowId = "test-workflow-id";

 ArgumentCaptor<CompletableFuture> futureArgumentCaptor = ArgumentCaptor.forClass(CompletableFuture.class);

 when(workflowExecutor.decide(workflowId)).thenReturn(getTestWorkflowModel());

 workflowSweeper.sweepAsync(workflowId);

 verify(workflowExecutor, times(1)).decide(workflowId);
 verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);

 // Verify that the sweeperExecutor was used to run the task
 executorService.shutdown();
 executorService.awaitTermination(1000);
 verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
 }

 @Test
 public void testSweepAsync_WorkflowNotFound() throws Exception {
 String workflowId = "test-workflow-id";

 when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException("Workflow not found"));

 workflowSweeper.sweepAsync(workflowId);

 verify(workflowExecutor, times(1)).decide(workflowId);
 verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
 verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong());
 }

 @Test
 public void testSweep_WorkflowNotFound() {
 String workflowId = "test-workflow-id";

 when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException("Workflow not found"));

 workflowSweeper.sweep(workflowId);

 verify(workflowExecutor, times(1)).decide(workflowId);
 verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
 verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong());
 }

 @Test
 public void testSweep_WorkflowTerminalStatus() {
 String workflowId = "test-workflow-id";

 WorkflowModel testWorkflowModel = getTestWorkflowModel();
 testWorkflowModel.setStatus(TaskModel.Status.COMPLETED);

 when(workflowExecutor.decide(workflowId)).thenReturn(testWorkflowModel);

 workflowSweeper.sweep(workflowId);

 verify(workflowExecutor, times(1)).decide(workflowId);
 verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
 }

 @Test
 public void testUnack() {
 String workflowId = "test-workflow-id";
 WorkflowModel testWorkflowModel = getTestWorkflowModel();

 workflowSweeper.unack(testWorkflowModel, 100L);

 verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, 100L);
 }

 @Test
 public void testWorkflowOffsetWithJitter() {
 long workflowOffsetTimeout = 45;

 long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

 long expectedRangeStart = workflowOffsetTimeout - (workflowOffsetTimeout / 3);
 long expectedRangeEnd = workflowOffsetTimeout + (workflowOffsetTimeout / 3);

 assertTrue(result >= expectedRangeStart && result <= expectedRangeEnd);
 }

 @Test
 public void testSweepAsync_Exception() {
 String workflowId = "test-workflow-id";

 when(workflowExecutor.decide(workflowId)).thenReturn(getTestWorkflowModel());

 doThrow(new RuntimeException("Test exception")).when(workflowSweeper).unack(any(), anyLong());

 workflowSweeper.sweepAsync(workflowId);

 verify(workflowExecutor, times(1)).decide(workflowId);
 verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
 verify(queueDAO, never()).setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong());
 }

 private WorkflowModel getTestWorkflowModel() {
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setWorkflowId(TestUtils.getUniqueWorkflowId());
 workflowModel.setName("Test Workflow");
 workflowModel.setStatus(TaskModel.Status.IN_PROGRESS);

 TaskModel taskModel = new TaskModel();
 taskModel.setId(TestUtils.getUniqueTaskId());
 taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
 taskModel.setTaskType(TaskType.TASK_TYPE_SCHEDULED);

 workflowModel.setTasks(Collections.singletonList(taskModel));

 return workflowModel;
 }

 private class NotFoundException extends RuntimeException {
 public NotFoundException(String message) {
 super(message);
 }
 }
}
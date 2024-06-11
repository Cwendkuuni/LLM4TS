import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.test.util.ReflectionTestUtils;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowDefinition;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class WorkflowSweeperTest {

 @Mock
 private WorkflowExecutor workflowExecutor;

 @Mock
 private QueueDAO queueDAO;

 @Mock
 private ConductorProperties properties;

 @Mock
 private WorkflowRepairService workflowRepairService;

 @InjectMocks
 private WorkflowSweeper workflowSweeper;

 @Captor
 private ArgumentCaptor<String> stringArgumentCaptor;

 @Captor
 private ArgumentCaptor<WorkflowModel> workflowModelArgumentCaptor;

 @Test
 public void testSweepAsync() {
 String workflowId = "testWorkflowId";
 CompletableFuture<Void> completableFuture = new CompletableFuture<>();
 ReflectionTestUtils.setField(workflowSweeper, "workflowRepairService", workflowRepairService);
 when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenReturn(true);

 AsyncTestUtils.await(() -> {
 try {
 workflowSweeper.sweepAsync(workflowId).get();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 return true;
 });

 verifySweep(workflowId);
 }

 @Test
 public void testSweepAsyncNotFoundException() {
 String workflowId = "testWorkflowId";
 when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenReturn(false);

 AsyncTestUtils.await(() -> {
 try {
 workflowSweeper.sweepAsync(workflowId).get();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 return true;
 });

 verifySweep(workflowId);
 }

 @Test
 public void testSweepAsyncException() {
 String workflowId = "testWorkflowId";
 when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenThrow(new RuntimeException());

 AsyncTestUtils.await(() -> {
 try {
 workflowSweeper.sweepAsync(workflowId).get();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 return true;
 });

 verifySweep(workflowId);
 }

 @Test
 public void testSweep() {
 String workflowId = "testWorkflowId";
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setStatus(Status.COMPLETED);

 ReflectionTestUtils.setField(workflowSweeper, "workflowRepairService", workflowRepairService);
 when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
 when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenReturn(true);

 workflowSweeper.sweep(workflowId);

 verifySweep(workflowId);
 }

 @Test
 public void testSweepNotFoundException() {
 String workflowId = "testWorkflowId";
 when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenReturn(false);

 workflowSweeper.sweep(workflowId);

 verifySweep(workflowId);
 }

 @Test
 public void testSweepException() {
 String workflowId = "testWorkflowId";
 when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException("test"));

 workflowSweeper.sweep(workflowId);

 verifySweep(workflowId);
 }

 @Test
 public void testUnack() {
 String workflowId = "testWorkflowId";
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setStatus(Status.IN_PROGRESS);

 TaskModel taskModel1 = new TaskModel();
 taskModel1.setTaskType(TaskType.TASK_TYPE_WAIT);
 taskModel1.setStatus(Status.IN_PROGRESS);
 taskModel1.setTaskId("taskId1");
 taskModel1.setWorkflowId(workflowId);

 TaskModel taskModel2 = new TaskModel();
 taskModel2.setTaskType(TaskType.TASK_TYPE_HUMAN);
 taskModel2.setStatus(Status.SCHEDULED);
 taskModel2.setTaskId("taskId2");
 taskModel2.setWorkflowId(workflowId);

 WorkflowDefinition workflowDefinition = new WorkflowDefinition();
 workflowDefinition.setTimeoutSeconds(10);
 workflowModel.setWorkflowDefinition(workflowDefinition);

 TaskDef taskDef = new TaskDef();
 taskDef.setPollTimeoutSeconds(5);
 taskModel2.setTaskDefinition(Optional.of(taskDef));

 when(queueDAO.setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong())).thenReturn(true);

 workflowSweeper.unack(workflowModel, 15);

 ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

 verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowId, longArgumentCaptor.capture());

 Long value = longArgumentCaptor.getValue();

 assertTrue(value >= (5 + 1) * 1000 && value <= (15 + 1) * 1000);
 }

 @Test
 public void testWorkflowOffsetWithJitter() {
 long workflowOffsetTimeout = 45;
 long range = workflowOffsetTimeout / 3;
 Long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

 assertTrue(jitter >= -range && jitter <= range);
 }

 private void verifySweep(String workflowId) {
 verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
 }

}
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.runaway.RunawayWorkflowException;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.config.Configuration;
import com.netflix.conductor.core.config.SchedulerConfiguration;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutorImpl;
import com.netflix.conductor.core.metrics.MicrometerMonitor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.utils.TestUtils;
import com.netflix.conductor.model.TaskModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class WorkflowSweeperTest {

@MockBean
private QueueDAO queueDAO;

@MockBean
private WorkflowExecutor workflowExecutor;

@MockBean
private WorkflowRepairService workflowRepairService;

@Autowired
private ApplicationContext applicationContext;

private WorkflowSweeper workflowSweeper;

private static final String WORKFLOW_ID = "workflow-1";
private static final String WORKFLOW_NAME = "test-workflow";
private static final String TASK_TYPE = TaskType.TASK_TYPE_SCHEDULED.name();
private static final String TASK_DEF_NAME = "test-task-def";
private static final String APP_ID = "test-app";
private static final String DECIDER_QUEUE = "decider-queue";
private static final long WORKFLOW_OFFSET_TIMEOUT = 45;

@BeforeEach
public void setUp() {
workflowSweeper = applicationContext.getBean(WorkflowSweeper.class);
}

@AfterEach
public void tearDown() {
Mockito.reset(queueDAO, workflowExecutor, workflowRepairService);
}

@Test
public void testSweepAsync() throws ExecutionException, InterruptedException {
// Mocking the behavior of queueDAO.remove()
Mockito.doNothing().when(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);

// Mocking the behavior of workflowExecutor.decide()
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setStatus(Status.COMPLETED);
Mockito.when(workflowExecutor.decide(WORKFLOW_ID))
.thenReturn(workflowModel)
.thenThrow(new NotFoundException("Workflow not found"))
.thenReturn(workflowModel);

// Calling the method to be tested
CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);

// Verifying the behavior of queueDAO.remove()
Mockito.verify(queueDAO, Mockito.times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);

// Verifying the completion of the future
Assertions.assertTrue(future.get(1, TimeUnit.SECONDS).isDone());
}

@Test
public void testSweep() {
// Mocking the behavior of queueDAO.remove()
Mockito.doNothing().when(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);

// Mocking the behavior of workflowExecutor.decide()
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setStatus(Status.COMPLETED);
Mockito.when(workflowExecutor.decide(WORKFLOW_ID))
.thenReturn(workflowModel)
.thenThrow(new NotFoundException("Workflow not found"))
.thenReturn(workflowModel);

// Calling the method to be tested
workflowSweeper.sweep(WORKFLOW_ID);

// Verifying the behavior of queueDAO.remove()
Mockito.verify(queueDAO, Mockito.times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
}

@Test
public void testUnack() {
// Mocking the behavior of queueDAO.setUnackTimeout()
Mockito.doNothing().when(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, ArgumentMatchers.anyLong());

TaskModel taskModel = new TaskModel();
taskModel.setStatus(Status.IN_PROGRESS);
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setTasks(Collections.singletonList(taskModel));

// Calling the method to be tested
workflowSweeper.unack(workflowModel, WORKFLOW_OFFSET_TIMEOUT);

// Verifying the behavior of queueDAO.setUnackTimeout()
Mockito.verify(queueDAO, Mockito.times(1)).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, WORKFLOW_OFFSET_TIMEOUT * 1000);
}

@Test
public void testWorkflowOffsetWithJitter() {
long jitter = workflowSweeper.workflowOffsetWithJitter(WORKFLOW_OFFSET_TIMEOUT);

// Checking the range of the jitter
Assertions.assertTrue(jitter >= (2 * WORKFLOW_OFFSET_TIMEOUT / 3));
Assertions.assertTrue(jitter <= (4 * WORKFLOW_OFFSET_TIMEOUT / 3));
}

// Additional test cases for edge cases and exception handling scenarios

@Test
public void testSweepAsyncNotFoundException() throws ExecutionException, InterruptedException {
// Mocking the behavior of queueDAO.remove()
Mockito.doNothing().when(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);

// Mocking the behavior of workflowExecutor.decide()
Mockito.when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException("Workflow not found"));

// Calling the method to be tested
CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);

// Verifying the behavior of queueDAO.remove()
Mockito.verify(queueDAO, Mockito.times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);

// Verifying the completion of the future
Assertions.assertTrue(future.get(1, TimeUnit.SECONDS).isCancelled());
}

@Test
public void testSweepAsyncInterruptedException() throws ExecutionException, InterruptedException {
// Mocking the behavior of queueDAO.remove()
Mockito.doNothing().when(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);

// Mocking the behavior of workflowExecutor.decide()
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setStatus(Status.COMPLETED);
Mockito.when(workflowExecutor.decide(WORKFLOW_ID))
.thenReturn(workflowModel)
.thenThrow(new InterruptedException("Interrupted"))
.thenReturn(workflowModel);

// Calling the method to be tested
CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);

// Verifying the behavior of queueDAO.remove()
Mockito.verify(queueDAO, Mockito.times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);

// Verifying the completion of the future
Assertions.assertTrue(future.get(1, TimeUnit.SECONDS).isDone());
}

@Test
public void testUnackInterruptedException() {
// Mocking the behavior of queueDAO.setUnackTimeout()
Mockito.doNothing().when(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, ArgumentMatchers.anyLong());

TaskModel taskModel = new TaskModel();
taskModel.setStatus(Status.IN_PROGRESS);
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setTasks(Collections.singletonList(taskModel));

// Calling the method to be tested
Assertions.assertThrows(InterruptedException.class, () -> workflowSweeper.unack(workflowModel, WORKFLOW_OFFSET_TIMEOUT));
}

@Test
public void testWorkflowOffsetWithJitterNegativeInput() {
Assertions.assertThrows(IllegalArgumentException.class, () -> workflowSweeper.workflowOffsetWithJitter(-1));
}

// More test cases for edge cases and exception handling scenarios can be added here

}
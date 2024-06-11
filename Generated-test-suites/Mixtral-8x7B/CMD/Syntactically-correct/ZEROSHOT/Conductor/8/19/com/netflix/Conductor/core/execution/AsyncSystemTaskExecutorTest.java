import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AsyncSystemTaskExecutorTest {

@Mock
private ExecutionDAOFacade executionDAOFacade;

@Mock
private QueueDAO queueDAO;

@Mock
private MetadataDAO metadataDAO;

@Mock
private WorkflowExecutor workflowExecutor;

@Mock
private WorkflowSystemTask workflowSystemTask;

@Mock
private TaskModel taskModel;

@Mock
private WorkflowModel workflowModel;

@InjectMocks
private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

private static final Logger logger = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);

@Before
public void init() {
MockitoAnnotations.initMocks(this);
}

@Test
public void execute_whenTaskIsNull_thenLogErrorAndReturn() {
// Given
String taskId = "task-1";
doReturn(null).when(executionDAOFacade).getTaskModel(taskId);

// When
asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

// Then
verify(logger, times(1)).error(
"TaskId: {} could not be found while executing {}", taskId, workflowSystemTask);
}

@Test
public void execute_whenTaskIsCompleted_thenRemoveFromQueueAndReturn()
throws ExecutionException, InterruptedException {
// Given
String taskId = "task-2";
doReturn(taskModel).when(executionDAOFacade).getTaskModel(taskId);
doReturn(true).when(taskModel).getStatus()
.isTerminal();

// When
asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

// Then
verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(taskModel), taskId);
}

@Test
public void execute_whenTaskExceedsInProgressLimit_thenPostponeAndReturn()
throws ExecutionException, InterruptedException {
// Given
String taskId = "task-3";
doReturn(taskModel).when(executionDAOFacade).getTaskModel(taskId);
doReturn(false).when(taskModel).getStatus()
.isTerminal();
doReturn(true).when(executionDAOFacade).exceedsInProgressLimit(taskModel);

// When
asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

// Then
verify(queueDAO, times(1)).postpone(
QueueUtils.getQueueName(taskModel),
taskId,
taskModel.getWorkflowPriority(),
asyncSystemTaskExecutor.queueTaskMessagePostponeSecs);
}

@Test
public void execute_whenTaskExceedsRateLimitPerFrequency_thenPostponeAndReturn()
throws ExecutionException, InterruptedException {
// Given
String taskId = "task-4";
doReturn(taskModel).when(executionDAOFacade).getTaskModel(taskId);
doReturn(false).when(taskModel).getStatus()
.isTerminal();
doReturn(true).when(executionDAOFacade).exceedsRateLimitPerFrequency(
taskModel,
mock(com.netflix.conductor.dao.TaskDef.class));

// When
asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

// Then
verify(queueDAO, times(1)).postpone(
QueueUtils.getQueueName(taskModel),
taskId,
taskModel.getWorkflowPriority(),
asyncSystemTaskExecutor.queueTaskMessagePostponeSecs);
}

@Test
public void execute_whenTaskIsScheduledAndSystemTaskIsAsyncComplete_thenRemoveFromQueueAndReturn()
throws ExecutionException, InterruptedException {
// Given
String taskId = "task-5";
doReturn(taskModel).when(executionDAOFacade).getTaskModel(taskId);
doReturn(false).when(taskModel).getStatus()
.isTerminal();
doReturn(true).when(workflowSystemTask).isAsyncComplete(taskModel);
doReturn(TaskModel.Status.SCHEDULED).when(taskModel).getStatus();

// When
asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

// Then
verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(taskModel), taskId);
}

@Test
public void execute_whenTaskIsInProgressAndSystemTaskIsAsyncComplete_thenUpdateTaskAndReturn()
throws ExecutionException, InterruptedException {
// Given
String taskId = "task-6";
doReturn(taskModel).when(executionDAOFacade).getTaskModel(taskId);
doReturn(false).when(taskModel).getStatus()
.isTerminal();
doReturn(true).when(workflowSystemTask).isAsyncComplete(taskModel);
doReturn(TaskModel.Status.IN_PROGRESS).when(taskModel).getStatus();

// When
asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

// Then
verify(executionDAOFacade, times(1)).updateTask(taskModel);
}

@Test
public void execute_whenTaskHasCompleted_thenRemoveFromQueueAndDecideWorkflow()
throws ExecutionException, InterruptedException {
// Given
String taskId = "task-7";
doReturn(taskModel).when(executionDAOFacade).getTaskModel(taskId);
doReturn(true).when(taskModel).getStatus()
.isTerminal();
doReturn(TaskModel.Status.COMPLETED).when(taskModel).getStatus();

// When
asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

// Then
verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(taskModel), taskId);
verify(workflowExecutor, times(1)).decide(taskModel.getWorkflowInstanceId());
}

@Test
public void execute_whenExceptionOccurs_thenLogErrorAndUpdateTask()
throws ExecutionException, InterruptedException {
// Given
String taskId = "task-8";
doReturn(taskModel).when(executionDAOFacade).getTaskModel(taskId);
doReturn(false).when(taskModel).getStatus()
.isTerminal();
doThrow(new RuntimeException("test exception"))
.when(workflowSystemTask).start(workflowModel, taskModel, workflowExecutor);

// When
asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

// Then
verify(logger, times(1)).error("Error executing system task - {}, with id: {}",
workflowSystemTask, taskId);
verify(executionDAOFacade, times(1)).updateTask(taskModel);
}

@Test
public void postponeQuietly_whenExceptionOccurs_thenLogError() {
// Given
String queueName = "queue-1";
String taskId = "task-1";

// When
doThrow(new RuntimeException("test exception"))
.when(queueDAO).postpone(queueName, taskId, 1, 1);

// Then
asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);
verify(logger, times(1)).error("Error postponing task: {} in queue: {}", taskId, queueName);
}

@Test
public void loadTaskQuietly_whenExceptionOccurs_thenReturnNull() {
// Given
String taskId = "task-1";

// When
doThrow(new RuntimeException("test exception"))
.when(executionDAOFacade).getTaskModel(taskId);

// Then
assertEquals(asyncSystemTaskExecutor.loadTaskQuietly(taskId), null);
}
}
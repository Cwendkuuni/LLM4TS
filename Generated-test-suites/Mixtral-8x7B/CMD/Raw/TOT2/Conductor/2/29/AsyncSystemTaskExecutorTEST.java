import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.*;

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
private QueueUtils queueUtils;

private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);

asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
executionDAOFacade, queueDAO, metadataDAO, new ConductorProperties(), workflowExecutor);
}

@Test
public void testExecute_TaskDoesNotExist_LogsErrorAndRemovesFromQueue() {
// Arrange
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "123";

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(queueDAO).remove(systemTask.getTaskType(), taskId);
verify(LOGGER).error(
"TaskId: {} could not be found while executing {}", taskId, systemTask);
}

@Test
public void testExecute_TaskIsTerminal_RemovesFromQueue() {
// Arrange
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "123";
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.COMPLETED);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(queueDAO).remove(QueueUtils.getQueueName(task), taskId);
}

@Test
public void testExecute_TaskExceedsInProgressLimit_PostponesQuietly() {
// Arrange
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "123";
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.SCHEDULED);
task.setTaskType("someTaskType");

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(queueDAO).postpone(
QueueUtils.getQueueName(task),
taskId,
task.getWorkflowPriority(),
queueTaskMessagePostponeSecs);
}

@Test
public void testExecute_TaskExceedsRateLimit_PostponesQuietly() {
// Arrange
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "123";
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.SCHEDULED);
task.setRateLimitPerFrequency(1);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
when(executionDAOFacade.exceedsRateLimitPerFrequency(
task,
metadataDAO.getTaskDef(task.getTaskDefName())))
.thenReturn(true);

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(queueDAO).postpone(
QueueUtils.getQueueName(task),
taskId,
task.getWorkflowPriority(),
queueTaskMessagePostponeSecs);
}

@Test
public void testExecute_TaskIsCompleted_LogsInfoAndRemovesFromQueue() {
// Arrange
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "123";
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.COMPLETED);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(queueDAO).remove(QueueUtils.getQueueName(task), taskId);
verify(LOGGER).info("Task {}/{} was already completed.", task.getTaskType(), task.getTaskId());
}

@Test
public void testExecute_ExceptionDuringExecution_LogsError() {
// Arrange
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "123";
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.SCHEDULED);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
doThrow(new RuntimeException("Some error")).when(systemTask).execute(any(), any(), any());

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(LOGGER).error("Error executing system task - {}, with id: {}", systemTask, taskId);
}

@Test
public void testExecute_TaskCompletedAsync_RemovesFromQueue() {
// Arrange
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "123";
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.IN_PROGRESS);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
when(systemTask.isAsyncComplete(task)).thenReturn(true);

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(queueDAO).remove(QueueUtils.getQueueName(task), taskId);
}

@Test
public void testExecute_TaskExecutedSuccessfully_UpdatesTask() {
// Arrange
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "123";
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.SCHEDULED);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
when(systemTask.isAsyncComplete(task)).thenReturn(false);

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(executionDAOFacade).updateTask(task);
}

private class ConductorProperties {
long getSystemTaskWorkerCallbackDuration() {
return 0;
}

long getTaskExecutionPostponeDuration() {
return 0;
}
}

private class QueueUtils {
static String getQueueName(TaskModel task) {
return "";
}
}
}
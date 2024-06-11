import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

ConductorProperties conductorProperties = new ConductorProperties();
conductorProperties.getSystemTaskWorkerCallbackDuration().setSeconds(10);
conductorProperties.getTaskExecutionPostponeDuration().setSeconds(5);

asyncSystemTaskExecutor =
new AsyncSystemTaskExecutor(
executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
}

@Test
public void testExecute_TaskNotFound() {
// Arrange
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "task-123";

doReturn(null).when(executionDAOFacade).getTaskModel(taskId);

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(queueDAO).remove(anyString(), anyString());
verify(systemTask).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
verifyNoMoreInteractions(executionDAOFacade);
}

@Test
public void testExecute_TaskCompleted() {
// Arrange
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "task-123";
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.COMPLETED);

doReturn(task).when(executionDAOFacade).getTaskModel(taskId);

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(queueDAO).remove(anyString(), anyString());
verifyNoMoreInteractions(executionDAOFacade);
}

@Test
public void testExecute_TaskScheduled_ExceedsInProgressLimit() {
// Arrange
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "task-123";
TaskModel task = new TaskModel();

doReturn(task).when(executionDAOFacade).getTaskModel(taskId);
doReturn(true).when(executionDAOFacade).exceedsInProgressLimit(task);

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyInt());
verifyNoMoreInteractions(executionDAOFacade);
}

@Test
public void testExecute_TaskScheduled_ExceedsRateLimitPerFrequency() {
// Arrange
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "task-123";
TaskModel task = new TaskModel();
task.setRateLimitPerFrequency(1);

doReturn(task).when(executionDAOFacade).getTaskModel(taskId);
doReturn(true).when(executionDAOFacade).exceedsRateLimitPerFrequency(task, any());

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyInt());
verifyNoMoreInteractions(executionDAOFacade);
}

@Test
public void testExecute_TaskInProgress_AsyncComplete() {
// Arrange
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "task-123";
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.IN_PROGRESS);

doReturn(task).when(executionDAOFacade).getTaskModel(taskId);
doReturn(true).when(systemTask).isAsyncComplete(task);

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(queueDAO).remove(anyString(), anyString());
verify(workflowExecutor).decide(anyString());
verifyNoMoreInteractions(executionDAOFacade);
}

@Test
public void testExecute_TaskInProgress_NotAsyncComplete() {
// Arrange
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "task-123";
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.IN_PROGRESS);

doReturn(task).when(executionDAOFacade).getTaskModel(taskId);
doReturn(false).when(systemTask).isAsyncComplete(task);

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyInt());
verifyNoMoreInteractions(executionDAOFacade);
}

@Test
public void testExecute_TaskCompleted_AsyncComplete() {
// Arrange
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "task-123";
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.COMPLETED);

doReturn(task).when(executionDAOFacade).getTaskModel(taskId);
doReturn(true).when(systemTask).isAsyncComplete(task);

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(queueDAO).remove(anyString(), anyString());
verifyNoMoreInteractions(executionDAOFacade);
}

@Test
public void testExecute_TaskCompleted_NotAsyncComplete() {
// Arrange
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "task-123";
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.COMPLETED);

doReturn(task).when(executionDAOFacade).getTaskModel(taskId);
doReturn(false).when(systemTask).isAsyncComplete(task);

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyInt());
verifyNoMoreInteractions(executionDAOFacade);
}

@Test
public void testPostponeQuietly() {
// Arrange
String queueName = "queue-123";
TaskModel task = new TaskModel();
task.setTaskId("task-123");

// Act
asyncSystemTaskExecutor.postponeQuietly(queueName, task);

// Assert
verify(queueDAO).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), 5);
}

@Test
public void testLoadTaskQuietly_TaskFound() {
// Arrange
String taskId = "task-123";
TaskModel task = new TaskModel();

doReturn(task).when(executionDAOFacade).getTaskModel(taskId);

// Act
TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

// Assert
assertEquals(task, result);
verify(executionDAOFacade).getTaskModel(taskId);
verifyNoMoreInteractions(executionDAOFacade);
}

@Test
public void testLoadTaskQuietly_TaskNotFound() {
// Arrange
String taskId = "task-123";

doReturn(null).when(executionDAOFacade).getTaskModel(taskId);

// Act
TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

// Assert
assertNull(result);
verify(executionDAOFacade).getTaskModel(taskId);
verifyNoMoreInteractions(executionDAOFacade);
}
}
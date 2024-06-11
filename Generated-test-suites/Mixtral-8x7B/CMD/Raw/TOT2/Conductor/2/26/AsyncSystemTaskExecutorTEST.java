import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
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

private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);

ConductorProperties conductorProperties = new ConductorProperties();
conductorProperties.getSystemTaskWorkerCallbackDuration().setSeconds(10);
conductorProperties.getTaskExecutionPostponeDuration().setSeconds(5);

asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
}

@Test
public void testExecute_TaskNotFound() {
// Arrange
String taskId = "123";
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(queueDAO, times(1)).remove(systemTask.getTaskType(), taskId);
verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
}

@Test
public void testExecute_TaskCompleted() {
// Arrange
String taskId = "123";
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.COMPLETED);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(queueDAO, times(1)).remove(anyString(), anyString());
verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
}

@Test
public void testExecute_TaskTerminalStatus() {
// Arrange
String taskId = "123";
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.FAILED);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(queueDAO, times(1)).remove(anyString(), anyString());
verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
}

@Test
public void testExecute_TaskPostponed() {
// Arrange
String taskId = "123";
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.SCHEDULED);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
}

@Test
public void testExecute_TaskExecutionException() {
// Arrange
String taskId = "123";
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.SCHEDULED);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

doThrow(new RuntimeException("Test exception")).when(systemTask).execute(any(), any(), any());

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(executionDAOFacade, times(1)).updateTask(task);
verify(queueDAO, times(1)).remove(anyString(), anyString());
verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
}

@Test
public void testPostponeQuietly() {
// Arrange
String queueName = "testQueue";
TaskModel task = new TaskModel();

// Act
asyncSystemTaskExecutor.postponeQuietly(queueName, task);

// Assert
verifyNoMoreInteractions(queueDAO);
}

@Test
public void testLoadTaskQuietly_TaskFound() {
// Arrange
String taskId = "123";
TaskModel task = new TaskModel();

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

// Act
TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

// Assert
assertSame(task, result);
verify(executionDAOFacade, times(1)).getTaskModel(taskId);
verifyNoMoreInteractions(executionDAOFacade);
}

@Test
public void testLoadTaskQuietly_TaskNotFound() {
// Arrange
String taskId = "123";

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

// Act
TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

// Assert
assertNull(result);
verify(executionDAOFacade, times(1)).getTaskModel(taskId);
verifyNoMoreInteractions(executionDAOFacade);
}
}
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskModel;
import com.netflix.conductor.dao.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
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

@InjectMocks
private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

private WorkflowSystemTask workflowSystemTask;
private Logger logger;
private TaskModel taskModel;
private WorkflowModel workflowModel;

@Before
public void setUp() {
logger = spy(LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class));
workflowSystemTask = mock(WorkflowSystemTask.class);
taskModel = new TaskModel();
workflowModel = new WorkflowModel();
MockitoAnnotations.initMocks(this);
}

@Test
public void testExecute() {
// Arrange
String taskId = "123";
taskModel.setTaskId(taskId);
doReturn(taskModel).when(executionDAOFacade).getTaskModel(taskId);

// Act
asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

// Assert
verify(executionDAOFacade, times(1)).getTaskModel(taskId);
verify(queueDAO, times(1)).remove(anyString(), anyString());
}

@Test
public void testExecuteWithTerminalStatus() {
// Arrange
String taskId = "123";
TaskModel.Status terminalStatus = TaskModel.Status.FAILED;
taskModel.setStatus(terminalStatus);
doReturn(taskModel).when(executionDAOFacade).getTaskModel(taskId);

// Act
asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

// Assert
verify(executionDAOFacade, times(1)).getTaskModel(taskId);
verify(queueDAO, times(1)).remove(anyString(), anyString());
}

@Test
public void testExecuteWithScheduledStatus() {
// Arrange
String taskId = "123";
taskModel.setStatus(TaskModel.Status.SCHEDULED);
doReturn(taskModel).when(executionDAOFacade).getTaskModel(taskId);
doReturn(true).when(executionDAOFacade)
.exceedsInProgressLimit(any(TaskModel.class));
doReturn(false).when(workflowSystemTask).isAsyncComplete(any(TaskModel.class));

// Act
asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

// Assert
verify(executionDAOFacade, times(1)).getTaskModel(taskId);
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
}

@Test
public void testExecuteWithIsTaskAsyncComplete() {
// Arrange
String taskId = "123";
taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
doReturn(taskModel).when(executionDAOFacade).getTaskModel(taskId);
doReturn(true).when(workflowSystemTask).isAsyncComplete(any(TaskModel.class));

// Act
asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

// Assert
verify(executionDAOFacade, times(1)).getTaskModel(taskId);
verify(queueDAO, times(1)).remove(anyString(), anyString());
}

@Test
public void testExecuteWithException() {
// Arrange
String taskId = "123";
taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
doReturn(taskModel).when(executionDAOFacade).getTaskModel(taskId);
doThrow(new RuntimeException("Test exception")).when(workflowSystemTask)
.execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));

// Act
asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

// Assert
verify(executionDAOFacade, times(1)).getTaskModel(taskId);
verify(queueDAO, times(0)).remove(anyString(), anyString());
}

@Test
public void testPostponeQuietly() {
// Arrange
String queueName = "testQueue";
String taskId = "123";
TaskModel taskModel = new TaskModel();
taskModel.setTaskId(taskId);

// Act
asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

// Assert
verify(queueDAO, times(1)).postpone(queueName, taskId, anyInt(), anyInt());
}

@Test
public void testLoadTaskQuietly() {
// Arrange
String taskId = "123";
TaskModel taskModel = new TaskModel();
taskModel.setTaskId(taskId);
doReturn(taskModel).when(executionDAOFacade).getTaskModel(taskId);

// Act
TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

// Assert
assertEquals(taskModel, result);
}
}
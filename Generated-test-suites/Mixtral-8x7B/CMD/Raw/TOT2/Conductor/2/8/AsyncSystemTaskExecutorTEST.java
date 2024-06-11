import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.TasksTestUtils;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.SystemTask;
import com.netflix.conductor.core.models.TaskModel;
import com.netflix.conductor.core.models.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

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

@InjectMocks
private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

private WorkflowSystemTask workflowSystemTask;

private TaskModel taskModel;

private WorkflowModel workflowModel;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);

workflowSystemTask = mock(WorkflowSystemTask.class);

taskModel = TasksTestUtils.createTaskModel();

workflowModel = TasksTestUtils.createWorkflowModel();
}

@Test
public void testExecute() {
// Arrange
String taskId = "task-123";

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(false);
when(executionDAOFacade.exceedsRateLimitPerFrequency(
taskModel, metadataDAO.getTaskDef(taskModel.getTaskDefName())))
.thenReturn(false);

queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong());

// Act
asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

// Assert
verify(workflowSystemTask, times(1)).start(workflowModel, taskModel, workflowExecutor);
verify(workflowSystemTask, times(1)).execute(workflowModel, taskModel, workflowExecutor);
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
verify(executionDAOFacade, times(1)).updateTask(taskModel);
verify(queueDAO, times(1)).remove(anyString(), anyString());
}

@Test
public void testExecuteWithException() {
// Arrange
String taskId = "task-123";

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(false);
when(executionDAOFacade.exceedsRateLimitPerFrequency(
taskModel, metadataDAO.getTaskDef(taskModel.getTaskDefName())))
.thenReturn(false);

doThrow(new RuntimeException("Test exception")).when(workflowSystemTask).execute(
workflowModel,
taskModel,
workflowExecutor
);

queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong());

// Act and Assert
assertThrows(RuntimeException.class, () -> asyncSystemTaskExecutor.execute(workflowSystemTask, taskId));
verify(workflowSystemTask, times(1)).start(workflowModel, taskModel, workflowExecutor);
verify(workflowSystemTask, times(1)).execute(workflowModel, taskModel, workflowExecutor);
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
verify(executionDAOFacade, times(1)).updateTask(taskModel);
verify(queueDAO, times(1)).remove(anyString(), anyString());
}

@Test
public void testExecuteTaskNotFound() {
// Arrange
String taskId = "task-123";

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

// Act and Assert
assertThrows(IllegalArgumentException.class, () -> asyncSystemTaskExecutor.execute(workflowSystemTask, taskId));
verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
verify(executionDAOFacade, never()).updateTask(taskModel);
verify(queueDAO, never()).remove(anyString(), anyString());
}

@Test
public void testExecuteWithInProgressLimitExceeded() {
// Arrange
String taskId = "task-123";

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);

// Act and Assert
assertThrows(IllegalStateException.class, () -> asyncSystemTaskExecutor.execute(workflowSystemTask, taskId));
verify(workflowSystemTask, never()).start(workflowModel, taskModel, workflowExecutor);
verify(workflowSystemTask, never()).execute(workflowModel, taskModel, workflowExecutor);
verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
verify(executionDAOFacade, never()).updateTask(taskModel);
verify(queueDAO, never()).remove(anyString(), anyString());
}

@Test
public void testExecuteWithRateLimitExceeded() {
// Arrange
String taskId = "task-123";

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
when(executionDAOFacade.exceedsRateLimitPerFrequency(
taskModel, metadataDAO.getTaskDef(taskModel.getTaskDefName())))
.thenReturn(true);

// Act and Assert
assertThrows(IllegalStateException.class, () -> asyncSystemTaskExecutor.execute(workflowSystemTask, taskId));
verify(workflowSystemTask, never()).start(workflowModel, taskModel, workflowExecutor);
verify(workflowSystemTask, never()).execute(workflowModel, taskModel, workflowExecutor);
verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
verify(executionDAOFacade, never()).updateTask(taskModel);
verify(queueDAO, never()).remove(anyString(), anyString());
}

@Test
public void testPostponeQuietly() {
// Arrange
String queueName = "test-queue";
String taskId = "task-123";

// Act
asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

// Assert
verify(queueDAO, times(1)).postpone(queueName, taskId, taskModel.getWorkflowPriority(), 1);
}

@Test
public void testLoadTaskQuietlyWithException() {
// Arrange
String taskId = "task-123";

when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException("Test exception"));

// Act and Assert
assertThrows(RuntimeException.class, () -> asyncSystemTaskExecutor.loadTaskQuietly(taskId));
verify(executionDAOFacade, times(1)).getTaskModel(taskId);
}
}
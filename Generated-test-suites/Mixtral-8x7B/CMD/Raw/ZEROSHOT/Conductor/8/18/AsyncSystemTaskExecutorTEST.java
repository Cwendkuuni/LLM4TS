import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskModel;
import com.netflix.conductor.dao.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.util.concurrent.ExecutionException;

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

asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
executionDAOFacade,
queueDAO,
metadataDAO,
new ConductorProperties(),
workflowExecutor
);
}

@Test
public void testExecute_whenTaskIsNull() throws ExecutionException, InterruptedException {
// Arrange
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "taskId";

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(executionDAOFacade, times(1)).getTaskModel(taskId);
verify(queueDAO, times(0)).remove(anyString(), anyString());
verifyNoMoreInteractions(executionDAOFacade, queueDAO);
}

@Test
public void testExecute_whenTaskIsCompleted() throws ExecutionException, InterruptedException {
// Arrange
String taskId = "taskId";
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.COMPLETED);
when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

// Act
asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

// Assert
verify(executionDAOFacade, times(1)).getTaskModel(taskId);
verify(queueDAO, times(1)).remove(anyString(), anyString());
verifyNoMoreInteractions(executionDAOFacade, queueDAO);
}

@Test
public void testExecute_whenTaskIsScheduledAndExceedsInProgressLimit() throws ExecutionException, InterruptedException {
// Arrange
String taskId = "taskId";
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.SCHEDULED);
task.setTaskDefName("taskDefName");
when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

// Act
asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

// Assert
verify(executionDAOFacade, times(1)).getTaskModel(taskId);
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
verifyNoMoreInteractions(executionDAOFacade, queueDAO);
}

@Test
public void testExecute_whenTaskIsScheduledAndExceedsRateLimitPerFrequency() throws ExecutionException, InterruptedException {
// Arrange
String taskId = "taskId";
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.SCHEDULEED);
task.setTaskDefName("taskDefName");
task.setRateLimitPerFrequency(1);
when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
when(executionDAOFacade.exceedsRateLimitPerFrequency(task, mock(TaskModel.class))).thenReturn(true);

// Act
asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

// Assert
verify(executionDAOFacade, times(1)).getTaskModel(taskId);
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
verifyNoMoreInteractions(executionDAOFacade, queueDAO);
}

@Test
public void testExecute_whenWorkflowIsCompleted() throws ExecutionException, InterruptedException {
// Arrange
String workflowId = "workflowId";
TaskModel task = new TaskModel();
task.setWorkflowInstanceId(workflowId);
task.setStatus(TaskModel.Status.IN_PROGRESS);
WorkflowModel workflow = new WorkflowModel();
workflow.setStatus(WorkflowModel.Status.COMPLETED);
when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflow);

// Act
asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "taskId");

// Assert
verify(executionDAOFacade, times(1)).getWorkflowModel(workflowId, true);
verify(queueDAO, times(1)).remove(anyString(), anyString());
verifyNoMoreInteractions(executionDAOFacade, queueDAO);
}

@Test
public void testExecute_whenTaskIsExecutedSuccessfully() throws ExecutionException, InterruptedException {
// Arrange
String workflowId = "workflowId";
TaskModel task = new TaskModel();
task.setWorkflowInstanceId(workflowId);
task.setStatus(TaskModel.Status.IN_PROGRESS);
WorkflowModel workflow = new WorkflowModel();
workflow.setStatus(WorkflowModel.Status.RUNNING);
when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflow);

// Act
asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "taskId");

// Assert
verify(executionDAOFacade, times(1)).getWorkflowModel(workflowId, true);
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
verifyNoMoreInteractions(executionDAOFacade, queueDAO);
}
}
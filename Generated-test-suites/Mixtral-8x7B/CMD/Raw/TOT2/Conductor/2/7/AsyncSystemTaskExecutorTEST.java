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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
}

@Test
public void testExecute() throws ExecutionException, InterruptedException {
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "123";
TaskModel task = new TaskModel();
task.setTaskId(taskId);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
asyncSystemTaskExecutor.execute(systemTask, taskId);

verify(systemTask, times(1)).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
verify(systemTask, times(1)).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
verify(executionDAOFacade, times(1)).updateTask(task);
verify(queueDAO, times(1)).remove(anyString(), anyString());
}

@Test
public void testExecuteTaskNotFound() throws ExecutionException, InterruptedException {
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "123";

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
asyncSystemTaskExecutor.execute(systemTask, taskId);

verify(queueDAO, times(1)).remove(anyString(), anyString());
}

@Test
public void testExecuteTaskInTerminalState() throws ExecutionException, InterruptedException {
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "123";
TaskModel task = new TaskModel();
task.setTaskId(taskId);
task.setStatus(TaskModel.Status.COMPLETED);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
asyncSystemTaskExecutor.execute(systemTask, taskId);

verify(queueDAO, times(1)).remove(anyString(), anyString());
}

@Test
public void testExecuteTaskExceedsInProgressLimit() throws ExecutionException, InterruptedException {
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "123";
TaskModel task = new TaskModel();
task.setTaskId(taskId);

when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
asyncSystemTaskExecutor.execute(systemTask, taskId);

verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
}

@Test
public void testExecuteTaskExceedsRateLimitPerFrequency() throws ExecutionException, InterruptedException {
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "123";
TaskModel task = new TaskModel();
task.setTaskId(taskId);

when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);
asyncSystemTaskExecutor.execute(systemTask, taskId);

verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
}

@Test
public void testExecuteTaskHasTaskExecutionCompleted() throws ExecutionException, InterruptedException {
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "123";
TaskModel task = new TaskModel();
task.setTaskId(taskId);

when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(new WorkflowModel());
asyncSystemTaskExecutor.execute(systemTask, taskId);

verify(queueDAO, times(1)).remove(anyString(), anyString());
verify(workflowExecutor, times(1)).decide(anyString());
}

@Test
public void testPostponeQuietly() {
String queueName = "queueName";
TaskModel task = new TaskModel();

asyncSystemTaskExecutor.postponeQuietly(queueName, task);

verify(queueDAO, times(1)).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
}

@Test
public void testLoadTaskQuietlyTaskNotFound() {
String taskId = "123";

when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());

TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

assertEquals(null, result);
}
}
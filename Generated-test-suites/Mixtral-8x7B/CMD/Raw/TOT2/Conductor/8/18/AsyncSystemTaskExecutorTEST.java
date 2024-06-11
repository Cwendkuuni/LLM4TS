import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.QueueUtils;
import com.netflix.conductor.core.execution.TaskModel;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.WorkflowModel;
import com.netflix.conductor.core.execution.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils.QueueName;
import com.netflix.conductor.dao.QueueDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
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

private ScheduledExecutorService scheduledExecutorService;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);

scheduledExecutorService = Executors.newScheduledThreadPool(1);

when(executionDAOFacade.getTaskModel(anyString()))
.thenReturn(new TaskModel());

when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
.thenReturn(new WorkflowModel());

when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong()))
.thenAnswer(invocation -> scheduledExecutorService.schedule(
() -> {},
invocation.getArgument(3),
TimeUnit.SECONDS));

when(queueDAO.remove(anyString(), anyString()))
.thenReturn(true);

when(queueDAO.get(anyString(), anyString()))
.thenReturn(new TaskModel());

when(metadataDAO.getTaskDef(anyString()))
.thenReturn(new ConcurrentHashMap<>());
}

@Test
public void testExecute() {
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "task1";

asyncSystemTaskExecutor.execute(systemTask, taskId);

verify(systemTask, times(1)).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
verify(workflowExecutor, times(1)).decide(anyString());
}

@Test
public void testExecuteWithTerminalTaskStatus() {
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "task2";
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.FAILED);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

asyncSystemTaskExecutor.execute(systemTask, taskId);

verify(queueDAO, times(1)).remove(QueueName.SYSTEM_TASK_QUEUE.getName(), taskId);
verifyNoMoreInteractions(systemTask);
verifyNoMoreInteractions(executionDAOFacade);
verifyNoMoreInteractions(workflowExecutor);
}

@Test
public void testExecuteWithScheduledTaskStatusAndExceedsInProgressLimit() {
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "task3";
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.SCHEDULED);
task.setInProgress(true);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

asyncSystemTaskExecutor.execute(systemTask, taskId);

verify(queueDAO, times(1)).postpone(
QueueName.SYSTEM_TASK_QUEUE.getName(),
taskId,
task.getWorkflowPriority(),
asyncSystemTaskExecutor.queueTaskMessagePostponeSecs);
verifyNoMoreInteractions(systemTask);
verifyNoMoreInteractions(executionDAOFacade);
verifyNoMoreInteractions(workflowExecutor);
}

@Test
public void testExecuteWithScheduledTaskStatusAndExceedsRateLimitPerFrequency() {
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "task4";
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.SCHEDULED);
task.setRateLimitPerFrequency(1);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
when(executionDAOFacade.exceedsRateLimitPerFrequency(task, new ConcurrentHashMap<>()))
.thenReturn(true);

asyncSystemTaskExecutor.execute(systemTask, taskId);

verify(queueDAO, times(1)).postpone(
QueueName.SYSTEM_TASK_QUEUE.getName(),
taskId,
task.getWorkflowPriority(),
asyncSystemTaskExecutor.queueTaskMessagePostponeSecs);
verifyNoMoreInteractions(systemTask);
verifyNoMoreInteractions(executionDAOFacade);
verifyNoMoreInteractions(workflowExecutor);
}

@Test
public void testExecuteWithInProgressTaskStatus() {
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "task5";
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.IN_PROGRESS);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

asyncSystemTaskExecutor.execute(systemTask, taskId);

verify(systemTask, times(1)).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
verifyNoMoreInteractions(workflowExecutor);
}

@Test
public void testExecuteWithAsyncCompleteTask() {
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
String taskId = "task6";
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.IN_PROGRESS);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
when(systemTask.isAsyncComplete(task)).thenReturn(true);

asyncSystemTaskExecutor.execute(systemTask, taskId);

verify(queueDAO, times(1)).remove(QueueName.SYSTEM_TASK_QUEUE.getName(), taskId);
verifyNoMoreInteractions(systemTask);
verifyNoMoreInteractions(executionDAOFacade);
verifyNoMoreInteractions(workflowExecutor);
}

@Test
public void testPostponeQuietly() {
String queueName = "queue1";
String taskId = "task7";
TaskModel task = new TaskModel();

asyncSystemTaskExecutor.postponeQuietly(queueName, task);

verify(queueDAO, times(1)).postpone(queueName, taskId, task.getWorkflowPriority(), asyncSystemTaskExecutor.queueTaskMessagePostponeSecs);
}

@Test
public void testLoadTaskQuietlyWithException() {
String taskId = "task8";

when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());

asyncSystemTaskExecutor.loadTaskQuietly(taskId);

verifyNoMoreInteractions(executionDAOFacade);
}
}
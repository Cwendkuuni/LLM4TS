import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AsyncSystemTaskExecutorTest {

@InjectMocks
private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

@Mock
private ExecutionDAOFacade executionDAOFacade;

@Mock
private QueueDAO queueDAO;

@Mock
private MetadataDAO metadataDAO;

@Mock
private WorkflowExecutor workflowExecutor;

@Before
public void init() {
MockitoAnnotations.initMocks(this);
}

// Test for execute method with non-terminal task status
@Test
public void testExecuteWithNonTerminalTaskStatus() {
WorkflowSystemTask systemTask = new WorkflowSystemTask() {};
String taskId = "123";
TaskModel task = new TaskModel();
task.setTaskId(taskId);

when(executionDAOFacade.loadTask(taskId)).thenReturn(task);
when(queueDAO.getQueueName(any(TaskModel.class))).thenReturn("test-queue");

asyncSystemTaskExecutor.execute(systemTask, taskId);

verify(executionDAOFacade).updateTask(task);
verify(queueDAO).remove("test-queue", taskId);
}

// Test for execute method with terminal task status
@Test
public void testExecuteWithTerminalTaskStatus() {
WorkflowSystemTask systemTask = new WorkflowSystemTask() {};
String taskId = "123";
TaskModel task = new TaskModel();
task.setTaskId(taskId);
task.setStatus(TaskModel.Status.COMPLETED);

when(executionDAOFacade.loadTask(taskId)).thenReturn(task);
when(queueDAO.getQueueName(any(TaskModel.class))).thenReturn("test-queue");

asyncSystemTaskExecutor.execute(systemTask, taskId);

verify(executionDAOFacade).updateTask(task);
verify(queueDAO).remove("test-queue", taskId);
}

// Test for execute method with exceeded in-progress limit
@Test
public void testExecuteWithExceededInProgressLimit() {
WorkflowSystemTask systemTask = new WorkflowSystemTask() {};
String taskId = "123";
TaskModel task = new TaskModel();
task.setTaskId(taskId);
task.setStatus(TaskModel.Status.SCHEDULED);
task.setInProgressLimit(1);

when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
when(executionDAOFacade.loadTask(taskId)).thenReturn(task);
when(queueDAO.getQueueName(any(TaskModel.class))).thenReturn("test-queue");

asyncSystemTaskExecutor.execute(systemTask, taskId);

verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
}

// Test for execute method with exceeded rate limit per frequency
@Test
public void testExecuteWithExceededRateLimitPerFrequency() {
WorkflowSystemTask systemTask = new WorkflowSystemTask() {};
String taskId = "123";
TaskModel task = new TaskModel();
task.setTaskId(taskId);
task.setStatus(TaskModel.Status.SCHEDULED);
task.setRateLimitPerFrequency(1);

when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);
when(executionDAOFacade.loadTask(taskId)).thenReturn(task);
when(queueDAO.getQueueName(any(TaskModel.class))).thenReturn("test-queue");

asyncSystemTaskExecutor.execute(systemTask, taskId);

verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
}

// Test for postponeQuietly method
@Test
public void testPostponeQuietly() {
String queueName = "test-queue";
TaskModel task = new TaskModel();
task.setTaskId("123");

asyncSystemTaskExecutor.postponeQuietly(queueName, task);

verify(queueDAO).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(),
queueTaskMessagePostponeSecs);
}

// Test for loadTaskQuietly method with successful load
@Test
public void testLoadTaskQuietlyWithSuccessfulLoad() {
String taskId = "123";
TaskModel task = new TaskModel();
task.setTaskId(taskId);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

assert(result != null);
assert(result.getTaskId().equals(taskId));
}

// Test for loadTaskQuietly method with unsuccessful load
@Test
public void testLoadTaskQuietlyWithUnsuccessfulLoad() {
String taskId = "123";

when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());

TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

assert(result == null);
}
}
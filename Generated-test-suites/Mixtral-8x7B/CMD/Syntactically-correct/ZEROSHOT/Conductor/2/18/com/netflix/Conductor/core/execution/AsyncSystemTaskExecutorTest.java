import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AsyncSystemTaskExecutorTest {

private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);

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
public void setup() {
MockitoAnnotations.initMocks(this);
}

@Test
public void testExecute() {
TaskModel task = new TaskModel();
task.setTaskId("task-1");
task.setStatus(TaskModel.Status.IN_PROGRESS);

WorkflowSystemTask systemTask = new WorkflowSystemTask() {};

asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

verify(executionDAOFacade, times(1)).getTaskModel(anyString());
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
verify(queueDAO, times(1)).remove(anyString(), anyString());
verify(workflowExecutor, times(1)).decide(anyString());
}

@Test
public void testExecuteWithTerminalTask() {
TaskModel task = new TaskModel();
task.setTaskId("task-1");
task.setStatus(TaskModel.Status.CANCELED);

asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, task.getTaskId());

verify(executionDAOFacade, times(1)).getTaskModel(anyString());
verify(queueDAO, times(1)).remove(anyString(), anyString());
verify(workflowExecutor, times(0)).decide(anyString());
}

@Test
public void testExecuteWithNullTask() {
asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, null);

verify(executionDAOFacade, times(0)).getTaskModel(anyString());
verify(queueDAO, times(0)).postpone(anyString(), anyString(), anyInt(), anyInt());
verify(executionDAOFacade, times(0)).updateTask(any(TaskModel.class));
verify(queueDAO, times(0)).remove(anyString(), anyString());
verify(workflowExecutor, times(0)).decide(anyString());
}

@Test
public void testExecuteWithException() {
TaskModel task = new TaskModel();
task.setTaskId("task-1");

WorkflowSystemTask systemTask = new WorkflowSystemTask() {
@Override
public void start(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor)
throws Exception {
throw new Exception("Test Exception");
}
};

asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

verify(executionDAOFacade, times(1)).getTaskModel(anyString());
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
verify(queueDAO, times(1)).remove(anyString(), anyString());
verify(workflowExecutor, times(1)).decide(anyString());
}

@Test
public void testLoadTaskQuietly() {
String taskId = "task-1";
TaskModel task = new TaskModel();
task.setTaskId(taskId);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

assertEquals(task, result);
}

@Test
public void testLoadTaskQuietlyWithException() {
String taskId = "task-1";

when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());

TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

assertNotNull(result);
}

@Test
public void testPostponeQuietly() {
String queueName = "queue-1";
TaskModel task = new TaskModel();
task.setTaskId("task-1");

asyncSystemTaskExecutor.postponeQuietly(queueName, task);

verify(queueDAO, times(1)).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(),
queueTaskMessagePostponeSecs);
}

@Test
public void testPostponeQuietlyWithException() {
String queueName = "queue-1";
TaskModel task = new TaskModel();
task.setTaskId("task-1");

when(queueDAO.postpone(queueName, task.getTaskId(), task.getWorkflowPriority(),
queueTaskMessagePostponeSecs)).thenThrow(new RuntimeException());

asyncSystemTaskExecutor.postponeQuietly(queueName, task);

verify(queueDAO, times(1)).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(),
queueTaskMessagePostponeSecs);
}
}
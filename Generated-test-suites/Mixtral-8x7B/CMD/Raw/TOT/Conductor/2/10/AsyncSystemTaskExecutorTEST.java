import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.TaskDAO;
import com.netflix.conductor.core.dal.WorkflowDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.TestWorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.TaskResult;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AsyncSystemTaskExecutorTest {

@InjectMocks
private AsyncSystemTaskExecutor executor;

@Mock
private ExecutionDAOFacade executionDAOFacade;

@Mock
private QueueDAO queueDAO;

@Mock
private MetadataDAO metadataDAO;

@Mock
private TaskDAO taskDAO;

@Mock
private WorkflowDAO workflowDAO;

@Before
public void setUp() {
WorkflowSystemTask systemTask = mock(TestWorkflowSystemTask.class);
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.SCHEDULED);
task.setTaskId("123");

when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
when(systemTask.getTaskType()).thenReturn("testTask");
when(systemTask.isAsyncComplete(task)).thenReturn(true);
when(systemTask.getEvaluationOffset(task, 5)).thenReturn(Optional.of(10L));
}

@Test
public void testExecute() {
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.SCHEDULED);
task.setTaskId("123");

WorkflowModel workflow = new WorkflowModel();
workflow.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);
workflow.setWorkflowId("456");

queueDAO = mock(QueueDAO.class);

AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(
executionDAOFacade,
queueDAO,
metadataDAO,
new ConductorProperties(),
new WorkflowExecutor(taskDAO, workflowDAO, new ConductorProperties()));

executor.execute(new TestWorkflowSystemTask(), "123");

doNothing().when(queueDAO).remove(anyString(), anyString());
when(queueDAO.get(anyString(), anyString())).thenReturn(task);

executor.execute(systemTask, "123");

doNothing().when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyInt());
task.setStatus(TaskModel.Status.IN_PROGRESS);
executor.execute(systemTask, "123");

doNothing().when(queueDAO).remove(anyString(), anyString());
doNothing().when(executionDAOFacade).updateTask(any(TaskModel.class));
task.setEndTime(System.currentTimeMillis());
task.setStatus(TaskModel.Status.COMPLETED);
executor.execute(systemTask, "123");
}

@Test
public void testPostponeQuietly() {
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.SCHEDULED);
task.setTaskId("123");
task.setWorkflowPriority(1);

queueDAO = mock(QueueDAO.class);

AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(
executionDAOFacade,
queueDAO,
metadataDAO,
new ConductorProperties(),
new WorkflowExecutor(taskDAO, workflowDAO, new ConductorProperties()));

executor.postponeQuietly(QueueUtils.getQueueName(task), task);

doNothing().when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyInt());
executor.postponeQuietly(QueueUtils.getQueueName(task), task);
}

@Test
public void testLoadTaskQuietly() {
String taskId = "123";

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
Assert.assertNull(executor.loadTaskQuietly(taskId));

TaskModel task = new TaskModel();
task.setTaskId(taskId);
when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
Assert.assertEquals(task, executor.loadTaskQuietly(taskId));
}
}
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
public void execute_taskDoesNotExist_shouldRemoveTaskFromQueue() {
// arrange
String taskId = "task-1";
WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

// act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// assert
verify(queueDAO).remove(anyString(), eq(taskId));
}

@Test
public void execute_taskIsCompleted_shouldRemoveTaskFromQueue() {
// arrange
String taskId = "task-2";
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.COMPLETED);
when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

// act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// assert
verify(queueDAO).remove(anyString(), eq(taskId));
}

@Test
public void execute_taskExceedsInProgressLimit_shouldPostponeTask() {
// arrange
String taskId = "task-3";
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.SCHEDULED);
task.setTaskDefName("test-task");
when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

// act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// assert
verify(queueDAO).postpone(anyString(), eq(taskId), anyInt(), eq(asyncSystemTaskExecutor.queueTaskMessagePostponeSecs));
}

@Test
public void execute_taskExceedsRateLimitPerFrequency_shouldPostponeTask() {
// arrange
String taskId = "task-4";
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.SCHEDULED);
task.setTaskDefName("test-task");
task.setRateLimitPerFrequency(1);
when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
when(executionDAOFacade.exceedsRateLimitPerFrequency(task, metadataDAO.getTaskDef(task.getTaskDefName())))
.thenReturn(true);

WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

// act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// assert
verify(queueDAO).postpone(anyString(), eq(taskId), anyInt(), eq(asyncSystemTaskExecutor.queueTaskMessagePostponeSecs));
}

@Test
public void execute_workflowIsCompleted_shouldUpdateTaskStatusAndRemoveFromQueue() {
// arrange
String workflowId = "workflow-1";
String taskId = "task-5";
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.IN_PROGRESS);
task.setWorkflowInstanceId(workflowId);
WorkflowModel workflow = new WorkflowModel();
workflow.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);
when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);

WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

// act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// assert
verify(executionDAOFacade).updateTask(task);
verify(queueDAO).remove(anyString(), eq(taskId));
}
}
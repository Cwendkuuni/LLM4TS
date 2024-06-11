import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
private TaskModel updatedTaskModel;
private WorkflowModel workflowModel;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);

workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
taskModel = new TaskModel();
taskModel.setTaskId("123");
taskModel.setWorkflowInstanceId("workflow123");
taskModel.setTaskDefName("testTask");
taskModel.setStatus(TaskModel.Status.SCHEDULED);

updatedTaskModel = new TaskModel();
updatedTaskModel.setTaskId("123");
updatedTaskModel.setWorkflowInstanceId("workflow123");
updatedTaskModel.setTaskDefName("testTask");
updatedTaskModel.setStatus(TaskModel.Status.IN_PROGRESS);

workflowModel = new WorkflowModel();
workflowModel.setWorkflowInstanceId("workflow123");
workflowModel.setStatus(WorkflowModel.WorkflowStatus.RUNNING);

when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
when(executionDAOFacade.getWorkflowModel(anyString(), any())).thenReturn(workflowModel);
}

@Test
public void execute_whenTaskIsScheduled_shouldSetStartTimeAndUpdateTask() {
asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

verify(workflowSystemTask, times(1)).start(workflowModel, taskModel, workflowExecutor);
verify(executionDAOFacade, times(1)).updateTask(updatedTaskModel);
assertEquals(TaskModel.Status.IN_PROGRESS, taskModel.getStatus());
assertEquals(taskModel.getTaskId(), updatedTaskModel.getTaskId());
}

@Test
public void execute_whenTaskIsInProgress_shouldExecuteTaskAndUpdateTask() {
taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

verify(workflowSystemTask, times(1)).execute(workflowModel, taskModel, workflowExecutor);
verify(executionDAOFacade, times(1)).updateTask(updatedTaskModel);
assertEquals(TaskModel.Status.IN_PROGRESS, taskModel.getStatus());
assertEquals(taskModel.getTaskId(), updatedTaskModel.getTaskId());
}

@Test
public void execute_whenTaskIsCompleted_shouldRemoveTaskFromQueueAndDecideWorkflow() {
taskModel.setStatus(TaskModel.Status.COMPLETED);

asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

verify(queueDAO, times(1)).remove(anyString(), anyString());
verify(workflowExecutor, times(1)).decide(anyString());
}

@Test
public void execute_whenTaskIsCanceled_shouldRemoveTaskFromQueueAndDecideWorkflow() {
taskModel.setStatus(TaskModel.Status.CANCELED);

asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

verify(queueDAO, times(1)).remove(anyString(), anyString());
verify(workflowExecutor, times(1)).decide(anyString());
}

@Test
public void postponeQuietly_whenTaskIsPostponed_shouldPostponeTaskInQueue() {
String queueName = "testQueue";

asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

verify(queueDAO, times(1)).postpone(queueName, taskModel.getTaskId(), 0, 0);
}

@Test
public void loadTaskQuietly_whenTaskExists_shouldReturnTaskModel() {
when(executionDAOFacade.getTaskModel("123")).thenReturn(taskModel);

TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("123");

assertEquals(taskModel, result);
}

@Test
public void loadTaskQuietly_whenTaskDoesNotExist_shouldReturnNull() {
when(executionDAOFacade.getTaskModel("123")).thenThrow(new RuntimeException());

TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("123");

assertEquals(null, result);
}
}
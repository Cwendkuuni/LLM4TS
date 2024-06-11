import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.SystemTaskQueueDAO;
import com.netflix.conductor.core.dal.execution.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.TaskDefDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.ComponentScan;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@ComponentScan("com.netflix.conductor")
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

@Mock
private QueueUtils queueUtils;

@Mock
private TaskDefDao taskDefDao;

@Mock
private SystemTaskQueueDAO systemTaskQueueDAO;

private TaskModel taskModel;
private WorkflowModel workflowModel;
private WorkflowSystemTask workflowSystemTask;

@Before
public void setup() {
initMocks(this);
taskModel = new TaskModel();
taskModel.setTaskId("taskId");
workflowModel = new WorkflowModel();
workflowModel.setWorkflowInstanceId("workflowInstanceId");
workflowSystemTask = mock(WorkflowSystemTask.class);
}

@Test
public void testExecute() {
// Given
when(executionDAOFacade.getTaskModel(taskModel.getTaskId())).thenReturn(taskModel);
when(queueUtils.getQueueName(taskModel)).thenReturn("queueName");

// When
asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

// Then
verify(workflowSystemTask, times(1)).execute(workflowModel, taskModel, workflowExecutor);
verify(queueDAO, times(1)).postpone(
eq("queueName"),
eq(taskModel.getTaskId()),
eq(taskModel.getWorkflowPriority()),
anyLong());
}

@Test
public void testExecuteTerminalTask() {
// Given
taskModel.setStatus(TaskModel.Status.COMPLETED);
when(executionDAOFacade.getTaskModel(taskModel.getTaskId())).thenReturn(taskModel);

// When
asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

// Then
verify(queueDAO, times(1)).remove(eq("queueName"), eq(taskModel.getTaskId()));
}

@Test
public void testExecutePostponeQuietly() {
// Given
when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong()))
.thenThrow(new RuntimeException("Expected exception"));

// When
asyncSystemTaskExecutor.postponeQuietly("queueName", taskModel);

// Then
verify(queueDAO, times(1)).postpone(
eq("queueName"),
eq(taskModel.getTaskId()),
eq(taskModel.getWorkflowPriority()),
anyLong());
verify(queueDAO, times(1)).remove(eq("queueName"), eq(taskModel.getTaskId()));
}

@Test
public void testLoadTaskQuietly() {
// Given
when(executionDAOFacade.getTaskModel(taskModel.getTaskId())).thenThrow(new RuntimeException("Expected exception"));

// When
TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskModel.getTaskId());

// Then
assertEquals(null, result);
}
}
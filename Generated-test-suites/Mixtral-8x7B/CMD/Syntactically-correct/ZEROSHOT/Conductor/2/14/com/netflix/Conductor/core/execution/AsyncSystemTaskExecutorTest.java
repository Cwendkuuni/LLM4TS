import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.TaskDAOImpl;
import com.netflix.conductor.dao.MetadataDAOImpl;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.utils.QueueUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.HashMap;
import java.util.Map;
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
@Mock
private QueueUtils queueUtils;
@Mock
private ConductorProperties conductorProperties;

private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);

when(conductorProperties.getSystemTaskWorkerCallbackDuration().getSeconds())
.thenReturn(10L);
when(conductorProperties.getTaskExecutionPostponeDuration().getSeconds())
.thenReturn(20L);

asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
executionDAOFacade,
queueDAO,
metadataDAO,
conductorProperties,
workflowExecutor
);

asyncSystemTaskExecutor.queueUtils = queueUtils;
}

@Test
public void testExecute() {
WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
String taskId = "123";

TaskModel taskModel = new TaskModel();
taskModel.setTaskType("systemTaskType");
taskModel.setTaskId(taskId);
taskModel.setStatus(TaskModel.Status.SCHEDULED);

when(executionDAOFacade.getTaskModel(taskId))
.thenReturn(taskModel);

asyncSystemTaskExecutor.execute(systemTask, taskId);

verify(queueDAO, times(1)).remove(anyString(), anyString());
verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
}

@Test
public void testExecuteWhenTaskIsAlreadyCompleted() {
WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
String taskId = "123";

TaskModel taskModel = new TaskModel();
taskModel.setTaskType("systemTaskType");
taskModel.setTaskId(taskId);
taskModel.setStatus(TaskModel.Status.COMPLETED);

when(executionDAOFacade.getTaskModel(taskId))
.thenReturn(taskModel);

asyncSystemTaskExecutor.execute(systemTask, taskId);

verify(queueDAO, times(1)).remove(anyString(), anyString());
verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
verify(queueDAO, times(0)).postpone(anyString(), anyString(), anyInt(), anyInt());
}

@Test
public void testExecuteWhenTaskExceedsInProgressLimit() {
WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
String taskId = "123";

TaskModel taskModel = new TaskModel();
taskModel.setTaskType("systemTaskType");
taskModel.setTaskId(taskId);
taskModel.setStatus(TaskModel.Status.SCHEDULED);

when(executionDAOFacade.getTaskModel(taskId))
.thenReturn(taskModel);

when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class)))
.thenReturn(true);

asyncSystemTaskExecutor.execute(systemTask, taskId);

verify(queueDAO, times(0)).remove(anyString(), anyString());
verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
}

@Test
public void testExecuteWhenTaskExceedsRateLimitPerFrequency() {
WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
String taskId = "123";

TaskModel taskModel = new TaskModel();
taskModel.setTaskType("systemTaskType");
taskModel.setTaskId(taskId);
taskModel.setStatus(TaskModel.Status.SCHEDULED);

when(executionDAOFacade.getTaskModel(taskId))
.thenReturn(taskModel);

when(executionDAOFacade.exceedsRateLimitPerFrequency(any(TaskModel.class), any(Map.class)))
.thenReturn(true);

asyncSystemTaskExecutor.execute(systemTask, taskId);

verify(queueDAO, times(0)).remove(anyString(), anyString());
verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
}
}
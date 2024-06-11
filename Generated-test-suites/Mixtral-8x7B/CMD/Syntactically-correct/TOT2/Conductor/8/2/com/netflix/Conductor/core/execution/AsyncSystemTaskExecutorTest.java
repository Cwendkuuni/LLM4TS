import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AsyncSystemTaskExecutorTest {

private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);
private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
private ExecutionDAOFacade executionDAOFacade;
private QueueDAO queueDAO;
private MetadataDAO metadataDAO;
private WorkflowExecutor workflowExecutor;
private WorkflowSystemTask workflowSystemTask;
private TaskModel taskModel;
private ConcurrentHashMap<String, TaskModel> taskModels;

@Before
public void setUp() {
executionDAOFacade = mock(ExecutionDAOFacade.class);
queueDAO = mock(QueueDAO.class);
metadataDAO = mock(MetadataDAO.class);
workflowExecutor = mock(WorkflowExecutor.class);
workflowSystemTask = mock(WorkflowSystemTask.class);
taskModel = new TaskModel();
taskModel.setStatus(TaskModel.Status.SCHEDULED);
taskModels = new ConcurrentHashMap<>();
taskModels.put("1", taskModel);

ReflectionTestUtils.setField(workflowSystemTask, "taskModels", taskModels);

asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
executionDAOFacade, queueDAO, metadataDAO, mock(ConductorProperties.class), workflowExecutor);

when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
}

@Test
public void testExecute_taskDoesNotExist() {
asyncSystemTaskExecutor.execute(workflowSystemTask, "2");
assertEquals(TaskModel.Status.CANCELED, taskModel.getStatus());
}

@Test
public void testExecute_taskIsTerminal() {
ReflectionTestUtils.setField(taskModel, "status", TaskModel.Status.COMPLETED);
asyncSystemTaskExecutor.execute(workflowSystemTask, "1");
Mockito.verify(queueDAO).remove(anyString(), anyString());
}

@Test
public void testExecute_taskIsScheduledAndExceedsInProgressLimit() {
when(executionDAOFacade.exceedsInProgressLimit(any())).thenReturn(true);
asyncSystemTaskExecutor.execute(workflowSystemTask, "1");
Mockito.verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
}

@Test
public void testExecute_taskIsScheduledAndExceedsRateLimit() {
when(executionDAOFacade.exceedsRateLimitPerFrequency(any(), any())).thenReturn(true);
asyncSystemTaskExecutor.execute(workflowSystemTask, "1");
Mockito.verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
}

@Test
public void testExecute_taskIsCompleted() {
ReflectionTestUtils.setField(taskModel, "status", TaskModel.Status.COMPLETED);
asyncSystemTaskExecutor.execute(workflowSystemTask, "1");
assertNotNull(taskModel.getEndTime());
Mockito.verify(queueDAO).remove(anyString(), anyString());
}

@Test
public void testExecute_taskIsScheduled() {
asyncSystemTaskExecutor.execute(workflowSystemTask, "1");
Mockito.verify(workflowSystemTask).start(any(), any(), any());
}

@Test
public void testExecute_taskIsInProgress() {
ReflectionTestUtils.setField(taskModel, "status", TaskModel.Status.IN_PROGRESS);
asyncSystemTaskExecutor.execute(workflowSystemTask, "1");
Mockito.verify(workflowSystemTask).execute(any(), any(), any());
}

@Test
public void testPostponeQuietly() {
asyncSystemTaskExecutor.postponeQuietly("queueName", taskModel);
Mockito.verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
}

@Test
public void testLoadTaskQuietly_taskExists() {
when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
TaskModel loadedTaskModel = asyncSystemTaskExecutor.loadTaskQuietly("1");
assertEquals(taskModel, loadedTaskModel);
}

@Test
public void testLoadTaskQuietly_taskDoesNotExist() {
TaskModel loadedTaskModel = asyncSystemTaskExecutor.loadTaskQuietly("2");
assertEquals(null, loadedTaskModel);
}
}
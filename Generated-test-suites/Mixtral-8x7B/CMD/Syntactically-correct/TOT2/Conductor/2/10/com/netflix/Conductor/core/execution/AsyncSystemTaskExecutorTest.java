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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
public void setup() {
MockitoAnnotations.initMocks(this);

// Initialize ConductorProperties
ConductorProperties conductorProperties = new ConductorProperties();
conductorProperties.setSystemTaskWorkerCallbackDuration(60);
conductorProperties.setTaskExecutionPostponeDuration(60);

// Set ConductorProperties in AsyncSystemTaskExecutor
ReflectionTestUtils.setField(asyncSystemTaskExecutor, "conductorProperties", conductorProperties);
}

@Test
public void testExecuteWithTerminalTaskStatus() {
// Mock TaskModel
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.COMPLETED);

// Execute method
asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "taskId");

// Verify queueDAO.remove() is called
verify(queueDAO, times(1)).remove(anyString(), anyString());
}

@Test
public void testExecuteWithScheduledTaskStatus() throws Exception {
// Mock TaskModel
TaskModel task = new TaskModel();
task.setStatus(TaskModel.Status.SCHEDULED);

// Mock getWorkflowModel()
when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
.thenReturn(mock(WorkflowModel.class));

// Execute method
asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "taskId");

// Verify queueDAO.postpone() is called
verify(queueDAO, times(1)).postpone(
anyString(),
anyString(),
anyInt(),
anyLong()
);
}

@Test
public void testExecuteWithException() {
// Mock TaskModel
TaskModel task = new TaskModel();

// Mock getWorkflowModel() throws exception
when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
.thenThrow(new RuntimeException());

// Execute method
asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "taskId");

// Verify queueDAO.postpone() is called
verify(queueDAO, times(1)).postpone(
anyString(),
anyString(),
anyInt(),
anyLong()
);

// Verify executionDAOFacade.updateTask() is called
verify(executionDAOFacade, times(1)).updateTask(task);

// Verify queueDAO.remove() is called
verify(queueDAO, times(1)).remove(anyString(), anyString());

// Verify workflowExecutor.decide() is called
verify(workflowExecutor, times(1)).decide("workflowId");
}

@Test
public void testPostponeQuietly() {
// Mock queueDAO.postpone() throws exception
doThrow(new RuntimeException()).when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());

// Execute method
asyncSystemTaskExecutor.postponeQuietly("queueName", mock(TaskModel.class));

// Verify no exception is thrown
assertEquals(true, true);
}

@Test
public void testLoadTaskQuietly() {
// Mock executionDAOFacade.getTaskModel() to return null
when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

// Execute method
TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly("taskId");

// Verify returned TaskModel is null
assertEquals(null, task);
}
}
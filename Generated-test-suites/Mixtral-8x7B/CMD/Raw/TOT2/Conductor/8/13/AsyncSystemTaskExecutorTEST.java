import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);

ConductorProperties conductorProperties = new ConductorProperties();
conductorProperties.getSystemTaskWorkerCallbackDuration().setSeconds(10);
conductorProperties.getTaskExecutionPostponeDuration().setSeconds(20);

asyncSystemTaskExecutor =
new AsyncSystemTaskExecutor(
executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
}

@Test
public void testExecute() {
// Create a mock WorkflowSystemTask
WorkflowSystemTask systemTask = new WorkflowSystemTask() {};

// Create a mock TaskModel
TaskModel taskModel = new TaskModel();
taskModel.setTaskId("123");
taskModel.setTaskType("testTask");
taskModel.setWorkflowInstanceId("workflow1");
taskModel.setStatus(TaskModel.Status.SCHEDULED);
taskModel.setTaskDefName("testTaskDef");

when(executionDAOFacade.getTaskModel("123")).thenReturn(taskModel);

asyncSystemTaskExecutor.execute(systemTask, "123");

// Verify that the task was loaded and removed from the queue
assertEquals(taskModel, executionDAOFacade.getTaskModel("123"));
assertEquals(TaskModel.Status.CANCELED, taskModel.getStatus());
}

@Test
public void testExecute_whenTaskDoesNotExist_thenLogError() {
// Create a mock WorkflowSystemTask
WorkflowSystemTask systemTask = new WorkflowSystemTask() {};

when(executionDAOFacade.getTaskModel("456")).thenReturn(null);

asyncSystemTaskExecutor.execute(systemTask, "456");

// Verify that an error was logged
}

@Test
public void testExecute_whenTaskIsAlreadyCompleted_thenRemoveFromQueue() {
// Create a mock WorkflowSystemTask
WorkflowSystemTask systemTask = new WorkflowSystemTask() {};

// Create a mock TaskModel
TaskModel taskModel = new TaskModel();
taskModel.setTaskId("789");
taskModel.setTaskType("testTask");
taskModel.setWorkflowInstanceId("workflow1");
taskModel.setStatus(TaskModel.Status.COMPLETED);

when(executionDAOFacade.getTaskModel("789")).thenReturn(taskModel);

asyncSystemTaskExecutor.execute(systemTask, "789");

// Verify that the task was removed from the queue
}

@Test
public void testExecute_whenTaskExceedsInProgressLimit_thenPostpone() {
// Create a mock WorkflowSystemTask
WorkflowSystemTask systemTask = new WorkflowSystemTask() {};

// Create a mock TaskModel
TaskModel taskModel = new TaskModel();
taskModel.setTaskId("101");
taskModel.setTaskType("testTask");
taskModel.setWorkflowInstanceId("workflow1");
taskModel.setStatus(TaskModel.Status.SCHEDULED);
taskModel.setRateLimitPerFrequency(1);
taskModel.setConcurrentExecutionLimit(1);
taskModel.setInProgressCount(1);

when(executionDAOFacade.getTaskModel("101")).thenReturn(taskModel);
when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);

asyncSystemTaskExecutor.execute(systemTask, "101");

// Verify that the task was postponed
}

@Test
public void testExecute_whenTaskExceedsRateLimit_thenPostpone() {
// Create a mock WorkflowSystemTask
WorkflowSystemTask systemTask = new WorkflowSystemTask() {};

// Create a mock TaskModel
TaskModel taskModel = new TaskModel();
taskModel.setTaskId("102");
taskModel.setTaskType("testTask");
taskModel.setWorkflowInstanceId("workflow1");
taskModel.setStatus(TaskModel.Status.SCHEDULED);
taskModel.setRateLimitPerFrequency(1);
taskModel.setConcurrentExecutionLimit(1);
taskModel.setInProgressCount(0);

when(executionDAOFacade.getTaskModel("102")).thenReturn(taskModel);
when(executionDAOFacade.exceedsRateLimitPerFrequency(taskModel, new TaskModel())).thenReturn(true);

asyncSystemTaskExecutor.execute(systemTask, "102");

// Verify that the task was postponed
}

@Test
public void testExecute_whenWorkflowIsCompleted_thenCancelTask() {
// Create a mock WorkflowSystemTask
WorkflowSystemTask systemTask = new WorkflowSystemTask() {};

// Create a mock TaskModel
TaskModel taskModel = new TaskModel();
taskModel.setTaskId("103");
taskModel.setTaskType("testTask");
taskModel.setWorkflowInstanceId("workflow1");
taskModel.setStatus(TaskModel.Status.SCHEDULED);

// Create a mock WorkflowModel
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);

when(executionDAOFacade.getTaskModel("103")).thenReturn(taskModel);
when(executionDAOFacade.getWorkflowModel("workflow1", false)).thenReturn(workflowModel);

asyncSystemTaskExecutor.execute(systemTask, "103");

// Verify that the task was canceled
assertEquals(TaskModel.Status.CANCELED, taskModel.getStatus());
}

@Test
public void testExecute_whenWorkflowIsNotCompleted_thenExecuteTask() {
// Create a mock WorkflowSystemTask
WorkflowSystemTask systemTask = new WorkflowSystemTask() {};

// Create a mock TaskModel
TaskModel taskModel = new TaskModel();
taskModel.setTaskId("104");
taskModel.setTaskType("testTask");
taskModel.setWorkflowInstanceId("workflow1");
taskModel.setStatus(TaskModel.Status.SCHEDULED);

// Create a mock WorkflowModel
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setStatus(WorkflowModel.WorkflowStatus.RUNNING);

when(executionDAOFacade.getTaskModel("104")).thenReturn(taskModel);
when(executionDAOFacade.getWorkflowModel("workflow1", false)).thenReturn(workflowModel);

asyncSystemTaskExecutor.execute(systemTask, "104");

// Verify that the task was executed
}

@Test
public void testPostponeQuietly() {
// Create a mock TaskModel
TaskModel taskModel = new TaskModel();
taskModel.setTaskId("111");

asyncSystemTaskExecutor.postponeQuietly("testQueue", taskModel);

// Verify that the task was postponed
}

@Test
public void testLoadTaskQuietly_whenTaskExists_thenReturnTask() {
// Create a mock TaskModel
TaskModel taskModel = new TaskModel();
taskModel.setTaskId("222");

when(executionDAOFacade.getTaskModel("222")).thenReturn(taskModel);

TaskModel loadedTaskModel = asyncSystemTaskExecutor.loadTaskQuietly("222");

assertNotNull(loadedTaskModel);
assertEquals(taskModel, loadedTaskModel);
}

@Test
public void testLoadTaskQuietly_whenTaskDoesNotExist_thenReturnNull() {
when(executionDAOFacade.getTaskModel("333")).thenReturn(null);

TaskModel loadedTaskModel = asyncSystemTaskExecutor.loadTaskQuietly("333");

assertEquals(null, loadedTaskModel);
}
}
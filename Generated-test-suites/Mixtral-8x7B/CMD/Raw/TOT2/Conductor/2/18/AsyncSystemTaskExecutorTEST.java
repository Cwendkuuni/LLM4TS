import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.test.TestWorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.model.workflow.WorkflowDef;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.core.utils.TestUtils;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.service.WorkflowService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AsyncSystemTaskExecutorTest {

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
private WorkflowService workflowService;

@Mock
private ConductorProperties conductorProperties;

@Captor
private ArgumentCaptor<TaskModel> taskModelArgumentCaptor;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);

when(conductorProperties.getTaskExecutionPostponeDuration().getSeconds())
.thenReturn(10L);
when(conductorProperties.getSystemTaskWorkerCallbackDuration().getSeconds())
.thenReturn(5L);

asyncSystemTaskExecutor =
new AsyncSystemTaskExecutor(
executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);

ReflectionTestUtils.setField(asyncSystemTaskExecutor, "workflowService", workflowService);
}

@Test
public void testExecute_TaskDoesNotExist_LogsErrorAndRemovesFromQueue() {
// Arrange
WorkflowSystemTask systemTask = new TestWorkflowSystemTask();
String taskId = "task-1";

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(queueDAO).remove(anyString(), anyString());
verify(logger).error(anyString(), anyString(), any(WorkflowSystemTask.class), anyString());
}

@Test
public void testExecute_TaskIsCompleted_RemovesFromQueue() {
// Arrange
WorkflowSystemTask systemTask = new TestWorkflowSystemTask();
String taskId = "task-1";
TaskModel taskModel = new TaskModel();
taskModel.setTaskId(taskId);
taskModel.setStatus(TaskModel.Status.COMPLETED);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(queueDAO).remove(anyString(), anyString());
}

@Test
public void testExecute_TaskIsScheduledAndExceedsInProgressLimit_PostponesTask() {
// Arrange
WorkflowSystemTask systemTask = new TestWorkflowSystemTask();
String taskId = "task-1";
TaskModel taskModel = new TaskModel();
taskModel.setTaskId(taskId);
taskModel.setStatus(TaskModel.Status.SCHEDULED);

when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(queueDAO).postpone(anyString(), anyString(), anyLong());
}

@Test
public void testExecute_TaskIsScheduledAndExceedsRateLimitPerFrequency_PostponesTask() {
// Arrange
WorkflowSystemTask systemTask = new TestWorkflowSystemTask();
String taskId = "task-1";
TaskModel taskModel = new TaskModel();
taskModel.setTaskId(taskId);
taskModel.setStatus(TaskModel.Status.SCHEDULED);

when(executionDAOFacade.exceedsRateLimitPerFrequency(any(TaskModel.class), any())).thenReturn(true);

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(queueDAO).postpone(anyString(), anyString(), anyLong());
}

@Test
public void testExecute_TaskIsExecutedSuccessfully_UpdatesTaskAndRemovesFromQueue()
throws Exception {
// Arrange
WorkflowSystemTask systemTask = new TestWorkflowSystemTask();
String taskId = "task-1";
TaskModel taskModel = createTaskModel(taskId, "workflow-1");

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
.thenReturn(createWorkflowModel("workflow-1"));

// Act
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Assert
verify(executionDAOFacade).updateTask(taskModelArgumentCaptor.capture());
TaskModel capturedTaskModel = taskModelArgumentCaptor.getValue();
assertEquals(TaskModel.Status.COMPLETED, capturedTaskModel.getStatus());
verify(queueDAO).remove(anyString(), anyString());
}

private TaskModel createTaskModel(String taskId, String workflowId) {
TaskModel taskModel = new TaskModel();
taskModel.setTaskId(taskId);
taskModel.setWorkflowInstanceId(workflowId);
taskModel.setStatus(TaskModel.Status.SCHEDULED);
return taskModel;
}

private WorkflowModel createWorkflowModel(String workflowId) {
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setWorkflowId(workflowId);
workflowModel.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);
return workflowModel;
}
}
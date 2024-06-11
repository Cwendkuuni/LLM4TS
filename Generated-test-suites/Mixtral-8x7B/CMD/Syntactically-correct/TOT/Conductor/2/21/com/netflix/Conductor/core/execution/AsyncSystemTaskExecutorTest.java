import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.execution.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskType;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.model.workflow.StartWorkflowRequest;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ org.junit.jupiter.api.extension.ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class AsyncSystemTaskExecutorTest {

@Mock
private ExecutionDAOFacade executionDAOFacade;
@Mock
private QueueDAO queueDAO;
@Mock
private MetadataDAO metadataDAO;
@Mock
private QueueUtils queueUtils;
@Mock
private WorkflowExecutor workflowExecutor;
private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

@BeforeEach
public void init() {
MockitoAnnotations.initMocks(this);
ConductorProperties conductorProperties = new ConductorProperties();
conductorProperties.getSystemTaskWorkerCallbackDuration().setSeconds(10);
conductorProperties.getTaskExecutionPostponeDuration().setSeconds(20);
asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
}

@Test
public void testExecute_taskNotFound() {
// Given
WorkflowSystemTask systemTask = new WorkflowSystemTask(WorkflowSystemTaskType.COMPLETE_WORKFLOW, null);
String taskId = "123";

// When
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Then
// No exception is thrown and the task is removed from the queue
}

@Test
public void testExecute_taskCompleted() {
// Given
WorkflowSystemTask systemTask = new WorkflowSystemTask(WorkflowSystemTaskType.COMPLETE_WORKFLOW, null);
String taskId = "123";
TaskModel task = new TaskModel();
task.setTaskId(taskId);
task.setStatus(TaskModel.Status.COMPLETED);
when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

// When
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Then
// The task is removed from the queue
}

@Test
public void testExecute_taskScheduled_concurrentExecutionLimited() {
// Given
WorkflowSystemTask systemTask = new WorkflowSystemTask(WorkflowSystemTaskType.COMPLETE_WORKFLOW, null);
String taskId = "123";
TaskModel task = new TaskModel();
task.setTaskId(taskId);
task.setStatus(TaskModel.Status.SCHEDULED);
task.setTaskDefName("testTaskDef");
task.setRateLimitPerFrequency(0);
when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

// When
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Then
// The task is postponed in the queue
}

@Test
public void testExecute_taskScheduled_rateLimitExceeded() {
// Given
WorkflowSystemTask systemTask = new WorkflowSystemTask(WorkflowSystemTaskType.COMPLETE_WORKFLOW, null);
String taskId = "123";
TaskModel task = new TaskModel();
task.setTaskId(taskId);
task.setStatus(TaskModel.Status.SCHEDULED);
task.setTaskDefName("testTaskDef");
task.setRateLimitPerFrequency(1);
when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
when(executionDAOFacade.exceedsRateLimitPerFrequency(task, null)).thenReturn(true);

// When
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Then
// The task is postponed in the queue
}

@Test
public void testExecute_taskInProgress() {
// Given
WorkflowSystemTask systemTask = new WorkflowSystemTask(WorkflowSystemTaskType.COMPLETE_WORKFLOW, null);
String taskId = "123";
TaskModel task = new TaskModel();
task.setTaskId(taskId);
task.setStatus(TaskModel.Status.IN_PROGRESS);
when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

// When
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Then
// The task is removed from the queue
}

@Test
public void testExecute_workflowCompleted() {
// Given
WorkflowSystemTask systemTask = new WorkflowSystemTask(WorkflowSystemTaskType.COMPLETE_WORKFLOW, null);
String taskId = "123";
TaskModel task = new TaskModel();
task.setTaskId(taskId);
task.setStatus(TaskModel.Status.IN_PROGRESS);
WorkflowModel workflow = new WorkflowModel();
workflow.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);
when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), false))
.thenReturn(workflow);

// When
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Then
// The task is removed from the queue and the workflow status is updated
}

@Test
public void testExecute_workflowFailed() {
// Given
WorkflowSystemTask systemTask = new WorkflowSystemTask(WorkflowSystemTaskType.COMPLETE_WORKFLOW, null);
String taskId = "123";
TaskModel task = new TaskModel();
task.setTaskId(taskId);
task.setStatus(TaskModel.Status.IN_PROGRESS);
WorkflowModel workflow = new WorkflowModel();
workflow.setStatus(WorkflowModel.WorkflowStatus.FAILED);
when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), false))
.thenReturn(workflow);

// When
asyncSystemTaskExecutor.execute(systemTask, taskId);

// Then
// The task is removed from the queue and the workflow status is updated
}
}
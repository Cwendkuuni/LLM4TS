import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.SystemTaskResultDAO;
import com.netflix.conductor.core.execution.TasksTestHelper;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.SystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskTestHelper;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AsyncSystemTaskExecutorTest {

@Mock private ExecutionDAOFacade executionDAOFacade;
@Mock private QueueDAO queueDAO;
@Mock private MetadataDAO metadataDAO;
@Mock private WorkflowExecutor workflowExecutor;
@Mock private SystemTaskResultDAO systemTaskResultDAO;

@InjectMocks private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

private TaskModel taskModel;
private WorkflowSystemTask workflowSystemTask;
private WorkflowModel workflowModel;

@Before
public void init() {
MockitoAnnotations.initMocks(this);

taskModel = TasksTestHelper.getValidTaskModel();
workflowModel = TasksTestHelper.getValidWorkflowModel();

ReflectionTestUtils.setField(asyncSystemTaskExecutor, "queueTaskMessagePostponeSecs", 10);
ReflectionTestUtils.setField(asyncSystemTaskExecutor, "systemTaskCallbackTime", 5);

workflowSystemTask =
WorkflowSystemTaskTestHelper.getWorkflowSystemTask("systemTaskName", SystemTask.Type.SIMPLE);

Mockito.when(executionDAOFacade.getTaskModel(taskModel.getTaskId()))
.thenReturn(taskModel);

Mockito.when(executionDAOFacade.getWorkflowModel(taskModel.getWorkflowInstanceId(), true))
.thenReturn(workflowModel);
}

@Test
public void testExecuteSystemTask_givenTask_shouldExecuteSystemTask() {
asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

verify(workflowExecutor, times(1)).decide(workflowModel.getInstanceId());
verify(systemTaskResultDAO, times(1)).registerResult(Mockito.any(), Mockito.any());
}

@Test
public void testExecuteSystemTask_givenTaskWithException_shouldLogError() {
Exception exception = new RuntimeException("Error executing system task");

Mockito.doThrow(exception).when(workflowSystemTask).execute(Mockito.any(), Mockito.any(), Mockito.any());

asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

verify(workflowExecutor, times(1)).decide(workflowModel.getInstanceId());
verify(systemTaskResultDAO, times(1)).registerResult(Mockito.any(), Mockito.any());
assertEquals(TaskModel.Status.FAILED, taskModel.getStatus());
assertEquals(exception.getMessage(), taskModel.getReasonForIncompletion());
}

@Test
public void testExecuteSystemTask_givenTaskWithTerminalWorkflow_shouldCompleteTask() {
workflowModel.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(taskModel), taskModel.getTaskId());
}

@Test
public void testExecuteSystemTask_givenTaskWithScheduledStatusAndLimitExceeded_shouldPostponeTask() {
taskModel.setStatus(TaskModel.Status.SCHEDULED);
taskModel.setRateLimitPerFrequency(1);

Mockito.when(executionDAOFacade.exceedsRateLimitPerFrequency(Mockito.any(), Mockito.any()))
.thenReturn(true);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

verify(queueDAO, times(1)).postpone(
QueueUtils.getQueueName(taskModel),
taskModel.getTaskId(),
taskModel.getWorkflowPriority(),
10);
}

@Test
public void testLoadTaskQuietly_givenTaskId_shouldReturnTaskModel() {
Mockito.when(executionDAOFacade.getTaskModel(taskModel.getTaskId()))
.thenReturn(taskModel);

TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskModel.getTaskId());

assertEquals(taskModel, result);
}

@Test
public void testLoadTaskQuietly_givenNonExistentTaskId_shouldReturnNull() {
Mockito.when(executionDAOFacade.getTaskModel(taskModel.getTaskId()))
.thenReturn(null);

TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskModel.getTaskId());

assertEquals(null, result);
}

@Test
public void testPostponeQuietly_givenTask_shouldPostponeTask() {
Map<String, Object> customParams = new HashMap<>();
customParams.put("key", "value");

asyncSystemTaskExecutor.postponeQuietly("queueName", TasksTestHelper.getValidTaskModel(null, null, null, null, null, customParams));

verify(queueDAO, times(1)).postpone("queueName", taskModel.getTaskId(), taskModel.getWorkflowPriority(), 10);
}
}
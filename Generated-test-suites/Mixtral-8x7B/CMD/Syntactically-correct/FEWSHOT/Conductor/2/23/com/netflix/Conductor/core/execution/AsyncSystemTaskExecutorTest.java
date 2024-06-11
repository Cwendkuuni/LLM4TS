package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask.WorkflowSystemTaskBuilder;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskDAO;
import com.netflix.conductor.dao.TaskDAOImpl;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

@Component
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
private QueueUtils queueUtils;

@Mock
private TaskDAO taskDAO;

@Mock
private WorkflowExecutor workflowExecutor;

@Mock
private WorkflowSystemTask workflowSystemTask;

@Mock
private Logger logger;

private TaskModel taskModel;

private WorkflowModel workflowModel;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);

taskModel = new TaskModel();
taskModel.setTaskId("task-1");
taskModel.setTaskType("task-type-1");
taskModel.setStatus(TaskModel.Status.SCHEDULED);

workflowModel = new WorkflowModel();
workflowModel.setWorkflowInstanceId("workflow-1");
workflowModel.setStatus(WorkflowModel.WorkflowStatus.IN_PROGRESS);

asyncSystemTaskExecutor = spy(asyncSystemTaskExecutor);
}

@Test
public void testExecute_TaskDoesNotExist() {
String taskId = "task-2";

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(logger, times(1)).error(anyString(), anyString(), any(WorkflowSystemTask.class), anyString());
verify(queueDAO, times(1)).remove(anyString(), anyString());
}

@Test
public void testExecute_TaskIsCompleted() {
String queueName = "queue-1";

taskModel.setStatus(TaskModel.Status.COMPLETED);

when(queueUtils.getQueueName(taskModel)).thenReturn(queueName);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

verify(logger, times(1)).info(anyString(), any(TaskModel.class));
verify(queueDAO, times(1)).remove(queueName, taskModel.getTaskId());
}

@Test
public void testExecute_TaskExceedsInProgressLimit() {
when(executionDAOFacade.exceedsInProgressLimit(taskModel))
.thenReturn(true);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

verify(logger, times(1)).warn(
anyString(), anyString(), anyString(), anyLong());
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyLong(), anyLong());
}

@Test
public void testExecute_TaskExceedsRateLimitPerFrequency() {
when(executionDAOFacade.exceedsRateLimitPerFrequency(
taskModel, metadataDAO.getTaskDef(taskModel.getTaskDefName())))
.thenReturn(true);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

verify(logger, times(1)).warn(
anyString(), anyString(), anyString(), anyString(), anyLong());
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyLong(), anyLong());
}

@Test
public void testExecute_WorkflowIsCompleted() {
workflowModel.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);

when(executionDAOFacade.getWorkflowModel(
workflowModel.getWorkflowInstanceId(), true))
.thenReturn(workflowModel);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

verify(logger, times(1)).info(
anyString(), any(WorkflowModel.class));
verify(executionDAOFacade, times(1)).updateTask(taskModel);
verify(queueDAO, times(1)).remove(anyString(), anyString());
verify(workflowExecutor, times(1)).decide(anyString());
}

@Test
public void testExecute_WorkflowIsTerminalState() {
workflowModel.setStatus(WorkflowModel.WorkflowStatus.FAILED);

when(executionDAOFacade.getWorkflowModel(
workflowModel.getWorkflowInstanceId(), true))
.thenReturn(workflowModel);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

verify(logger, times(1)).info(
anyString(), any(TaskModel.class));
verify(executionDAOFacade, times(1)).updateTask(taskModel);
verify(queueDAO, times(1)).remove(anyString(), anyString());
}

@Test
public void testExecute_UpdateTaskQueue() {
TaskModel updatedTaskModel = new TaskModel();
updatedTaskModel.setTaskId("task-1");
updatedTaskModel.setStartTime(System.currentTimeMillis());
Map<String, Object> input = new HashMap<>();
input.put("key", "value");
updatedTaskModel.setInput(input);

asyncSystemTaskExecutor.updateTaskQueue(taskModel, updatedTaskModel);

verify(queueDAO, times(1)).postpone(
anyString(), anyString(), anyLong(), anyLong());
}

@Test
public void testExecute_UpdateTaskQueue_TaskIsAsyncComplete() {
TaskModel updatedTaskModel = new TaskModel();
updatedTaskModel.setTaskId("task-1");
updatedTaskModel.setStatus(TaskModel.Status.COMPLETED);

asyncSystemTaskExecutor.updateTaskQueue(taskModel, updatedTaskModel);

verify(queueDAO, times(1)).remove(anyString(), anyString());
}

@Test
public void testPostponeQuietly() {
String queueName = "queue-1";

doNothing().when(queueDAO).postpone(anyString(), anyString(), anyLong(), anyLong());

asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyLong(), anyLong());
}

@Test
public void testLoadTaskQuietly_TaskExists() {
when(executionDAOFacade.getTaskModel(anyString()))
.thenReturn(taskModel);

AsyncSystemTaskExecutor.TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("task-1");

assertNotNull(result);
assertEquals(taskModel, result);
}

@Test
public void testLoadTaskQuietly_TaskDoesNotExist() {
when(executionDAOFacade.getTaskModel(anyString()))
.thenReturn(null);

AsyncSystemTaskExecutor.TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("task-1");

assertNull(result);
}
}
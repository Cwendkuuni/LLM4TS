package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.run.Workflow;
import com.netflix.conductor.common.run.Workflow.WorkflowStatus;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskDAO;
import com.netflix.conductor.dao.TaskDAOFacade;
import com.netflix.conductor.dao.TaskExecutionsDAO;
import com.netflix.conductor.dao.TaskModelDAO;
import com.netflix.conductor.model.Task;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.service.MetadataService;

public class AsyncSystemTaskExecutorTest {

@Mock
private ExecutionDAOFacade executionDAOFacade;

@Mock
private QueueDAO queueDAO;

@Mock
private MetadataDAO metadataDAO;

@Mock
private TaskDAOFacade taskDAOFacade;

@Mock
private TaskModelDAO taskModelDAO;

@Mock
private TaskExecutionsDAO taskExecutionsDAO;

@Mock
private WorkflowExecutor workflowExecutor;

@Mock
private MetadataService metadataService;

@Mock
private QueueUtils queueUtils;

@Mock
private ConductorProperties conductorProperties;

@InjectMocks
private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);

// Initialize ConductorProperties bean
ReflectionTestUtils.setField(asyncSystemTaskExecutor, "conductorProperties", conductorProperties);

// Initialize QueueUtils bean
ReflectionTestUtils.setField(asyncSystemTaskExecutor, "queueUtils", queueUtils);
}

@Test
public void testExecuteAsyncSystemTaskExecutor() {
TaskModel task = new TaskModel();
task.setTaskId("task-001");
task.setTaskType(TaskType.WORKFLOW_COMPLETED.getName());
task.setWorkflowInstanceId("workflow-001");
task.setStatus(TaskModel.Status.SCHEDULED);

WorkflowSystemTask workflowSystemTask = new WorkflowSystemTask() {
@Override
public void execute(WorkflowModel workflow, Task task, WorkflowExecutor workflowExecutor) {}

@Override
public boolean isAsyncComplete(Task task) {
return true;
}
};

// Test when task is null
asyncSystemTaskExecutor.execute(workflowSystemTask, null);
verify(queueDAO, times(1)).remove(anyString(), anyString());

// Test when task is found
asyncSystemTaskExecutor.execute(workflowSystemTask, "task-001");
verify(executionDAOFacade, times(1)).getTaskModel("task-001");
verify(queueDAO, times(1)).remove(anyString(), anyString());

// Test when task is not found
doReturn(null).when(executionDAOFacade).getTaskModel(anyString());
asyncSystemTaskExecutor.execute(workflowSystemTask, "task-001");
verify(queueDAO, times(1)).remove(anyString(), anyString());

// Test when task is completed
TaskModel completedTask = new TaskModel();
completedTask.setTaskId("task-001");
completedTask.setTaskType(TaskType.WORKFLOW_COMPLETED.getName());
completedTask.setWorkflowInstanceId("workflow-001");
completedTask.setStatus(TaskModel.Status.COMPLETED);

asyncSystemTaskExecutor.execute(workflowSystemTask, completedTask);
verify(queueDAO, times(1)).remove(anyString(), anyString());

// Test when task is in terminal state
TaskModel terminatedTask = new TaskModel();
terminatedTask.setTaskId("task-001");
terminatedTask.setTaskType(TaskType.WORKFLOW_COMPLETED.getName());
terminatedTask.setWorkflowInstanceId("workflow-001");
terminatedTask.setStatus(TaskModel.Status.FAILED);

asyncSystemTaskExecutor.execute(workflowSystemTask, terminatedTask);
verify(queueDAO, times(1)).remove(anyString(), anyString());

// Test when task is scheduled and queue size exceeds limit
when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(true);
asyncSystemTaskExecutor.execute(workflowSystemTask, task);
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyLong(), anyLong());

// Test when task is scheduled and queue size does not exceed limit
when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(false);
asyncSystemTaskExecutor.execute(workflowSystemTask, task);
verify(queueDAO, times(1)).remove(anyString(), anyString());

// Test when task is scheduled and rate limit per frequency exceeds limit
when(executionDAOFacade.exceedsRateLimitPerFrequency(any(TaskModel.class), any(TaskDef.class)))
.thenReturn(true);

asyncSystemTaskExecutor.execute(workflowSystemTask, task);
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyLong(), anyLong());

// Test when task is scheduled and rate limit per frequency does not exceed limit
when(executionDAOFacade.exceedsRateLimitPerFrequency(any(TaskModel.class), any(TaskDef.class)))
.thenReturn(false);

asyncSystemTaskExecutor.execute(workflowSystemTask, task);
verify(queueDAO, times(1)).remove(anyString(), anyString());

// Test when task is in progress and is async complete
task.setStatus(TaskModel.Status.IN_PROGRESS);
task.setStartTime(System.currentTimeMillis());

asyncSystemTaskExecutor.execute(workflowSystemTask, task);
verify(queueDAO, times(1)).remove(anyString(), anyString());

// Test when task is in progress and is not async complete
task.setStartTime(0);

asyncSystemTaskExecutor.execute(workflowSystemTask, task);
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyLong(), anyLong());

// Test when task is completed and is async complete
completedTask.setEndTime(System.currentTimeMillis());

asyncSystemTaskExecutor.execute(workflowSystemTask, completedTask);
verify(queueDAO, times(1)).remove(anyString(), anyString());

// Test when task is completed and is not async complete
completedTask.setEndTime(0);

asyncSystemTaskExecutor.execute(workflowSystemTask, completedTask);
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyLong(), anyLong());
}

@Test
public void testPostponeQuietly() {
String queueName = "test-queue";
String taskId = "task-001";

asyncSystemTaskExecutor.postponeQuietly(queueName, new TaskModel());
verify(queueDAO, times(1)).postpone(queueName, taskId, anyLong(), anyLong());
}

@Test
public void testLoadTaskQuietly() {
String taskId = "task-001";

asyncSystemTaskExecutor.loadTaskQuietly(taskId);
verify(executionDAOFacade, times(1)).getTaskModel(taskId);
}
}
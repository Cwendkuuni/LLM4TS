package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.TestWorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.model.workflow.WorkflowDef;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.metrics.Monitors;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.AsyncResult;

public class AsyncSystemTaskExecutorTest {

@Mock private ExecutionDAOFacade executionDAOFacade;
@Mock private QueueDAO queueDAO;
@Mock private MetadataDAO metadataDAO;
@Mock private Monitors monitors;
@Mock private WorkflowExecutor workflowExecutor;
private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
private WorkflowSystemTask workflowSystemTask;
private TaskModel taskModel;
private WorkflowModel workflowModel;
private ArgumentCaptor<TaskModel> taskModelArgumentCaptor;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);

asyncSystemTaskExecutor =
new AsyncSystemTaskExecutor(
executionDAOFacade, queueDAO, metadataDAO, new ConductorProperties(), workflowExecutor);

workflowSystemTask = mock(WorkflowSystemTask.class);
taskModel = new TaskModel();
taskModel.setStatus(TaskModel.Status.SCHEDULED);
taskModel.setWorkflowInstanceId("workflowInstanceId");

workflowModel = new WorkflowModel();
workflowModel.setStatus(WorkflowModel.Status.RUNNING);
workflowModel.setWorkflowDefinition(
new WorkflowDef().setName("workflowName").setVersion(1));
workflowModel.setId("workflowId");

taskModelArgumentCaptor = ArgumentCaptor.forClass(TaskModel.class);

when(executionDAOFacade.getTaskModel(anyString()))
.thenReturn(taskModel);

when(executionDAOFacade.getWorkflowModel(anyString(), any()))
.thenReturn(workflowModel);

doNothing().when(queueDAO).remove(anyString(), anyString());

doNothing().when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyInt());

when(workflowSystemTask.start(any(WorkflowModel.class), any(TaskModel.class), any()))
.thenReturn(new AsyncResult<String>(null));

when(workflowSystemTask.execute(any(WorkflowModel.class), any(TaskModel.class), any()))
.thenReturn(new AsyncResult<String>(null));

when(workflowSystemTask.isAsyncComplete(any(TaskModel.class)))
.thenReturn(true);

when(workflowSystemTask.isTaskRetrievalRequired())
.thenReturn(true);
}

@Test
public void testExecuteWithTaskNotExists() {
asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

verify(executionDAOFacade, times(1))
.getTaskModel("taskId");
verify(queueDAO, times(1)).remove(anyString(), anyString());
}

@Test
public void testExecuteWithTaskInTerminalState() {
taskModel.setStatus(TaskModel.Status.COMPLETED);

asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

verify(executionDAOFacade, times(1))
.getTaskModel("taskId");
verify(queueDAO, times(1)).remove(anyString(), anyString());
}

@Test
public void testExecuteWithTaskExceedsInProgressLimit() {
taskModel.setStatus(TaskModel.Status.SCHEDULED);
taskModel.setConcurrentExecutionLimit(1);
taskModel.setInProgress(1);

asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

verify(executionDAOFacade, times(1))
.getTaskModel("taskId");
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
}

@Test
public void testExecuteWithTaskExceedsRateLimit() {
taskModel.setStatus(TaskModel.Status.SCHEDULED);
taskModel.setRateLimitPerFrequency(1);
taskModel.setInProgress(1);
taskModel.setLastExecutedAt(System.currentTimeMillis() - 1000);

when(metadataDAO.getTaskDef(anyString()))
.thenReturn(
new WorkflowDef()
.setName("taskName")
.setVersion(1)
.setRateLimitPerFrequency(1)
.setRateLimitUnit(TimeUnit.SECONDS));

asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

verify(executionDAOFacade, times(1))
.getTaskModel("taskId");
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
}

@Test
public void testExecuteWithTaskInProgress() {
taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

verify(executionDAOFacade, times(1))
.getTaskModel("taskId");
verify(workflowSystemTask, times(1)).execute(any(WorkflowModel.class), any(TaskModel.class), any());
verify(executionDAOFacade, times(1)).updateTask(taskModelArgumentCaptor.capture());

TaskModel capturedTaskModel = taskModelArgumentCaptor.getValue();
assertEquals(TaskModel.Status.IN_PROGRESS, capturedTaskModel.getStatus());
}

@Test
public void testExecuteWithTaskCompleted() throws Exception {
StopWatch stopWatch = new StopWatch();
stopWatch.start();

taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

when(workflowSystemTask.isTaskRetrievalRequired())
.thenReturn(false);

when(workflowSystemTask.getEvaluationOffset(any(TaskModel.class), anyLong()))
.thenReturn(Optional.of(5000L));

asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

stopWatch.stop();

verify(executionDAOFacade, times(1))
.getTaskModel("taskId");
verify(workflowSystemTask, times(1)).execute(any(WorkflowModel.class), any(TaskModel.class), any());
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
verify(executionDAOFacade, times(1)).updateTask(taskModelArgumentCaptor.capture());

TaskModel capturedTaskModel = taskModelArgumentCaptor.getValue();
assertEquals(TaskModel.Status.IN_PROGRESS, capturedTaskModel.getStatus());
assertTrue(stopWatch.getTime() >= 5000L);
}

@Test
public void testExecuteWithTaskScheduled() throws Exception {
StopWatch stopWatch = new StopWatch();
stopWatch.start();

taskModel.setStatus(TaskModel.Status.SCHEDULED);

when(workflowSystemTask.getEvaluationOffset(any(TaskModel.class), anyLong()))
.thenReturn(Optional.of(5000L));

asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

stopWatch.stop();

verify(executionDAOFacade, times(1))
.getTaskModel("taskId");
verify(workflowSystemTask, times(1)).start(any(WorkflowModel.class), any(TaskModel.class), any());
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
verify(executionDAOFacade, times(1)).updateTask(taskModelArgumentCaptor.capture());

TaskModel capturedTaskModel = taskModelArgumentCaptor.getValue();
assertEquals(TaskModel.Status.IN_PROGRESS, capturedTaskModel.getStatus());
assertTrue(stopWatch.getTime() >= 5000L);
}

@Test
public void testExecuteWithTaskRetrievalNotRequired() throws Exception {
StopWatch stopWatch = new StopWatch();
stopWatch.start();

taskModel.setStatus(TaskModel.Status.SCHEDULED);

when(workflowSystemTask.isTaskRetrievalRequired())
.thenReturn(false);

when(workflowSystemTask.getEvaluationOffset(any(TaskModel.class), anyLong()))
.thenReturn(Optional.of(5000L));

asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

stopWatch.stop();

verify(executionDAOFacade, times(0))
.getTaskModel("taskId");
verify(workflowSystemTask, times(1)).start(any(WorkflowModel.class), any(TaskModel.class), any());
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
verify(executionDAOFacade, times(1)).updateTask(taskModelArgumentCaptor.capture());

TaskModel capturedTaskModel = taskModelArgumentCaptor.getValue();
assertEquals(TaskModel.Status.IN_PROGRESS, capturedTaskModel.getStatus());
assertTrue(stopWatch.getTime() >= 5000L);
}

@Test
public void testExecuteWithWorkflowCompleted() {
asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

verify(executionDAOFacade, times(1))
.getTaskModel("taskId");
verify(queueDAO, times(1)).remove(anyString(), anyString());
}

@Test
public void testExecuteWithWorkflowTerminalState() {
workflowModel.setStatus(WorkflowModel.Status.FAILED);

asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

verify(executionDAOFacade, times(1))
.getTaskModel("taskId");
verify(queueDAO, times(1)).remove(anyString(), anyString());
}

@Test
public void testExecuteWithWorkflowNonTerminalState() {
workflowModel.setStatus(WorkflowModel.Status.RUNNING);

asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

verify(executionDAOFacade, times(1))
.getTaskModel("taskId");
verify(workflowSystemTask, times(1)).execute(any(WorkflowModel.class), any(TaskModel.class), any());
verify(executionDAOFacade, times(1)).updateTask(taskModelArgumentCaptor.capture());

TaskModel capturedTaskModel = taskModelArgumentCaptor.getValue();
assertEquals(TaskModel.Status.CANCELED, capturedTaskModel.getStatus());
verify(queueDAO, times(1)).remove(anyString(), anyString());
verify(workflowExecutor, times(1)).decide(anyString());
}

@Test
public void testPostponeQuietly() {
asyncSystemTaskExecutor.postponeQuietly("queueName", taskModel);

verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
}

@Test
public void testLoadTaskQuietlyWithException() {
when(executionDAOFacade.getTaskModel(anyString()))
.thenThrow(new RuntimeException());

assertNull(asyncSystemTaskExecutor.loadTaskQuietly("taskId"));
}
}
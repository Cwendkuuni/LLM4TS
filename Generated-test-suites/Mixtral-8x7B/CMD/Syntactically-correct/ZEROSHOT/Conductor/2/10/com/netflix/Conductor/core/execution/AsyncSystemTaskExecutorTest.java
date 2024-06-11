package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutor.class);
private static final String TASK_ID = "task-123";
private static final String WORKFLOW_ID = "workflow-456";
private static final String TASK_TYPE = "TASK_TYPE";
private static final String QUEUE_NAME = "QUEUE_NAME";
private TaskModel taskModel;
private WorkflowSystemTask workflowSystemTask;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);

taskModel = new TaskModel();
taskModel.setTaskId(TASK_ID);
taskModel.setWorkflowInstanceId(WORKFLOW_ID);
taskModel.setTaskType(TASK_TYPE);

workflowSystemTask = mock(WorkflowSystemTask.class);

ReflectionTestUtils.setField(asyncSystemTaskExecutor, "systemTaskCallbackTime", 60L);
ReflectionTestUtils.setField(asyncSystemTaskExecutor, "queueTaskMessagePostponeSecs", 30L);
}

@Test
public void testExecute_TaskDoesNotExist() {
asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

verify(queueDAO, times(1)).remove(TASK_TYPE, TASK_ID);
verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
}

@Test
public void testExecute_TaskIsCompleted() {
taskModel.setStatus(TaskModel.Status.COMPLETED);
when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);

asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
}

@Test
public void testExecute_TaskIsScheduledAndQueueSizeExceeded() {
taskModel.setStatus(TaskModel.Status.SCHEDULED);
when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);
when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);

asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

verify(queueDAO, times(1)).postpone(QUEUE_NAME, TASK_ID, 0, 30);
verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
}

@Test
public void testExecute_TaskIsScheduledAndRateLimitExceeded() {
taskModel.setStatus(TaskModel.TaskStatus.SCHEDULED);
when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);
when(executionDAOFacade.exceedsRateLimitPerFrequency(taskModel, any())).thenReturn(true);

asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

verify(queueDAO, times(1)).postpone(QUEUE_NAME, TASK_ID, 0, 30);
verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
}

@Test
public void testExecute_TaskIsInProgress() {
taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);

asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyInt());
verify(queueDAO, never()).remove(anyString(), anyString());
verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
}

@Test
public void testExecute_TaskIsCompletedWithWorkflowNotCompleted() {
taskModel.setStatus(TaskModel.Status.COMPLETED);
TaskModel workflow = new TaskModel();
workflow.setStatus(WorkflowModel.Status.IN_PROGRESS);
when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(workflow);

asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyInt());
verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
}

@Test
public void testExecute_TaskIsCompletedWithWorkflowCompleted() {
taskModel.setStatus(TaskModel.Status.COMPLETED);
WorkflowModel workflow = new WorkflowModel();
workflow.setStatus(WorkflowModel.Status.COMPLETED);
when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(workflow);

asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
}

@Test
public void testExecute_TaskExecutionFails() {
taskModel.setStatus(TaskModel.Status.SCHEDULED);
when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);

doThrow(new RuntimeException("Test exception")).when(workflowSystemTask).execute(any(), any(), any());

asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

verify(queueDAO, times(1)).postpone(QUEUE_NAME, TASK_ID, 0, 60);
verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
}

@Test
public void testExecute_WorkflowEvaluationNeeded() {
taskModel.setStatus(TaskModel.Status.COMPLETED);
when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);
WorkflowModel workflow = new WorkflowModel();
workflow.setStatus(WorkflowModel.Status.IN_PROGRESS);
when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(workflow);

asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

verify(workflowExecutor, times(1)).decide(WORKFLOW_ID);
verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
}

@Test
public void testPostponeQuietly() {
asyncSystemTaskExecutor.postponeQuietly(QUEUE_NAME, taskModel);

verify(queueDAO, times(1)).postpone(QUEUE_NAME, TASK_ID, 0, 30);
}

@Test
public void testLoadTaskQuietly_TaskExists() {
when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);

TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(TASK_ID);

assertEquals(taskModel, loadedTask);
}

@Test
public void testLoadTaskQuietly_TaskDoesNotExist() {
TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(TASK_ID);

assertEquals(null, loadedTask);
}
}
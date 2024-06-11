package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.SystemTaskTestImpl;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.metrics.MonitorsImpl;
import com.netflix.conductor.model.Task;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

@Component
public class AsyncSystemTaskExecutorTest {

  @Mock private ExecutionDAOFacade executionDAOFacade;
  @Mock private QueueDAO queueDAO;
  @Mock private MetadataDAO metadataDAO;
  @Mock private QueueUtils queueUtils;
  @Mock private WorkflowExecutor workflowExecutor;
  @Mock private TaskModel taskModel;
  @Mock private WorkflowModel workflowModel;
  @Mock private WorkflowSystemTask workflowSystemTask;

  @InjectMocks private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

  private static final String TASK_ID = "task-123";
  private static final String WORKFLOW_ID = "workflow-456";
  private static final String QUEUE_NAME = "test-queue";
  private static final int QUEUE_WAIT_TIME = 100;
  private static final int SYSTEM_TASK_CALLBACK_TIME = 200;
  private static final int QUEUE_TASK_MESSAGE_POSTPONE_SECS = 300;
  private static final String TASK_TYPE = "test-task-type";
  private static final String TASK_DEF_NAME = "test-task-def-name";
  private static final int RATE_LIMIT_PER_FREQUENCY = 10;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    asyncSystemTaskExecutor = spy(asyncSystemTaskExecutor);
    Monitors monitors = new MonitorsImpl();
    asyncSystemTaskExecutor.setMonitors(monitors);

    when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);

    when(queueDAO.getQueueName(TASK_TYPE)).thenReturn(QUEUE_NAME);

    when(metadataDAO.getTaskDef(TASK_DEF_NAME)).thenReturn(createTaskDef());

    when(taskModel.getWorkflowInstanceId()).thenReturn(WORKFLOW_ID);

    when(workflowModel.getStatus()).thenReturn(Task.Status.COMPLETED);

    when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(true);

    when(workflowSystemTask.getEvaluationOffset(any(TaskModel.class), anyLong()))
        .thenReturn(java.util.Optional.of(50L));
  }

  @Test
  public void testExecute_TaskNotFound() {
    when(taskModel.getStatus()).thenReturn(Task.Status.FAILED);
    when(taskModel.getTaskType()).thenReturn(TASK_TYPE);

    asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

    verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
  }

  @Test
  public void testExecute_TaskCompleted() {
    when(taskModel.getStatus()).thenReturn(Task.Status.COMPLETED);

    asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

    verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
  }

  @Test
  public void testExecute_TaskScheduled_ConcurrentExecutionLimited() {
    when(taskModel.getStatus()).thenReturn(Task.Status.SCHEDULED);
    when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);

    asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

    verify(queueDAO, times(1)).postpone(QUEUE_NAME, TASK_ID, 0, QUEUE_TASK_MESSAGE_POSTPONE_SECS);
  }

  @Test
  public void testExecute_TaskScheduled_RateLimitExceeded() {
    when(taskModel.getStatus()).thenReturn(Task.Status.SCHEDULED);
    when(executionDAOFacade.exceedsRateLimitPerFrequency(taskModel, createTaskDef()))
        .thenReturn(true);

    asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

    verify(queueDAO, times(1)).postpone(QUEUE_NAME, TASK_ID, 0, QUEUE_TASK_MESSAGE_POSTPONE_SECS);
  }

  @Test
  public void testExecute_TaskScheduled_ExecuteTask() throws Exception {
    when(taskModel.getStatus()).thenReturn(Task.Status.SCHEDULED);

    ArgumentCaptor<Date> startTimeCaptor = ArgumentCaptor.forClass(Date.class);

    asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

    verify(workflowSystemTask, times(1)).start(workflowModel, taskModel, workflowExecutor);
    verify(executionDAOFacade, times(1)).updateTask(taskModel);
    verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
    verify(taskModel, times(1)).incrementPollCount();
    verify(taskModel, times(1)).setStartTime(startTimeCaptor.capture());
    assertEquals(0, startTimeCaptor.getValue().getTime());
  }

  @Test
  public void testExecute_TaskInProgress_ExecuteTask() throws Exception {
    when(taskModel.getStatus()).thenReturn(Task.Status.IN_PROGRESS);

    asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

    verify(workflowSystemTask, times(1)).execute(workflowModel, taskModel, workflowExecutor);
    verify(executionDAOFacade, times(1)).updateTask(taskModel);
    verify(taskModel, times(1)).incrementPollCount();
  }

  @Test
  public void testExecute_TaskAsyncComplete() {
    when(taskModel.getStatus()).thenReturn(Task.Status.IN_PROGRESS);
    when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(true);
    when(workflowSystemTask.getEvaluationOffset(taskModel, SYSTEM_TASK_CALLBACK_TIME))
        .thenReturn(java.util.Optional.of(50L));

    asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

    verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
  }

  @Test
  public void testExecute_TaskInProgress_PostponeTask() throws Exception {
    when(taskModel.getStatus()).thenReturn(Task.Status.IN_PROGRESS);

    asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

    verify(queueDAO, times(1)).postpone(QUEUE_NAME, TASK_ID, 0, SYSTEM_TASK_CALLBACK_TIME);
    verify(taskModel, times(1)).setCallbackAfterSeconds(SYSTEM_TASK_CALLBACK_TIME);
  }

  @Test
  public void testExecute_TaskTerminal_UpdateTaskEndTime() throws Exception {
    when(taskModel.getStatus()).thenReturn(Task.Status.COMPLETED);

    asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

    verify(executionDAOFacade, times(1)).updateTask(taskModel);
    verify(taskModel, times(1)).setEndTime(anyLong());
  }

  @Test
  public void testExecute_TaskTerminal_RemoveTaskFromQueue() throws Exception {
    when(taskModel.getStatus()).thenReturn(Task.Status.COMPLETED);

    asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

    verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
  }

  @Test
  public void testExecute_TaskTerminal_DecideWorkflow() throws Exception {
    when(taskModel.getStatus()).thenReturn(Task.Status.COMPLETED);

    asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

    verify(workflowExecutor, times(1)).decide(WORKFLOW_ID);
  }

  @Test
  public void testExecute_TaskTerminal_WorkflowFailed() throws Exception {
    when(workflowModel.getStatus()).thenReturn(Task.Status.FAILED);

    asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

    verify(taskModel, times(1)).setStatus(Task.Status.CANCELED);
    verify(taskModel, times(1))
        .setReasonForIncompletion(
            String.format("Workflow is in %s state", workflowModel.getStatus().toString()));
    verify(executionDAOFacade, times(1)).updateTask(taskModel);
  }

  @Test
  public void testExecute_TaskTerminal_WorkflowInProgress() throws Exception {
    when(workflowModel.getStatus()).thenReturn(Task.Status.IN_PROGRESS);

    asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

    verify(taskModel, times(1)).setReasonForIncompletion(anyString());
    verify(executionDAOFacade, times(1)).updateTask(taskModel);
  }

  @Test
  public void testExecute_TaskTerminal_WorkflowCompleted() throws Exception {
    when(workflowModel.getStatus()).thenReturn(Task.Status.COMPLETED);

    asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

    verify(executionDAOFacade, times(1)).updateTask(taskModel);
  }

  @Test
  public void testExecute_TaskTerminal_WorkflowTerminating() throws Exception {
    when(workflowModel.getStatus()).thenReturn(Task.Status.TERMINATING);

    asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

    verify(executionDAOFacade, times(1)).updateTask(taskModel);
  }

  @Test
  public void testExecute_TaskTerminal_WorkflowTerminated() throws Exception {
    when(workflowModel.getStatus()).thenReturn(Task.Status.TERMINATED);

    asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

    verify(executionDAOFacade, times(1)).updateTask(taskModel);
  }

  @Test
  public void testPostponeQuietly() {
    doNothing().when(queueDAO).postpone(QUEUE_NAME, TASK_ID, 0, QUEUE_TASK_MESSAGE_POSTPONE_SECS);

    asyncSystemTaskExecutor.postponeQuietly(QUEUE_NAME, taskModel);

    verify(queueDAO, times(1)).postpone(QUEUE_NAME, TASK_ID, 0, QUEUE_TASK_MESSAGE_POSTPONE_SECS);
  }

  @Test
  public void testPostponeQuietly_Exception() {
    doNothing().when(queueDAO).postpone(QUEUE_NAME, TASK_ID, 0, QUEUE_TASK_MESSAGE_POSTPONE_SECS);
    doThrow(new RuntimeException()).when(queueDAO).postpone(QUEUE_NAME, TASK_ID, 0, QUEUE_TASK_MESSAGE_POSTPONE_SECS);

    asyncSystemTaskExecutor.postponeQuietly(QUEUE_NAME, taskModel);

    verify(queueDAO, times(1)).postpone(QUEUE_NAME, TASK_ID, 0, QUEUE_TASK_MESSAGE_POSTPONE_SECS);
  }

  @Test
  public void testLoadTaskQuietly_Exception() {
    when(executionDAOFacade.getTaskModel(TASK_ID)).thenThrow(new RuntimeException());

    TaskModel taskModel = asyncSystemTaskExecutor.loadTaskQuietly(TASK_ID);

    assertNull(taskModel);
  }

  private Map<String, Object> createTaskDef() {
    Map<String, Object> taskDef = new HashMap<>();
    taskDef.put("name", TASK_DEF_NAME);
    taskDef.put("rateLimitPerFrequency", RATE_LIMIT_PER_FREQUENCY);
    taskDef.put("queue", TASK_TYPE);
    return taskDef;
  }
}
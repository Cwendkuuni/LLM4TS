package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.dagger.DaggerConductorComponent;
import com.netflix.conductor.dagger.ConductorComponent;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AsyncSystemTaskExecutorTest {

  private static final String TASK_ID = "123";
  private static final String WORKFLOW_ID = "workflow-123";
  private static final String QUEUE_NAME = "queue-123";

  @Mock private ExecutionDAOFacade executionDAOFacade;
  @Mock private QueueDAO queueDAO;
  @Mock private MetadataDAO metadataDAO;
  @Mock private WorkflowExecutor workflowExecutor;
  @Mock private QueueUtils queueUtils;

  @InjectMocks private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

  private ConductorComponent conductorComponent;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    // Initialize Dagger ConductorComponent
    conductorComponent =
        DaggerConductorComponent.builder()
            .systemTaskWorkerCallbackDuration(60)
            .taskExecutionPostponeDuration(60)
            .build();

    // Set ConductorComponent to AsyncSystemTaskExecutor
    asyncSystemTaskExecutor.setConductorComponent(conductorComponent);
  }

  @Test
  public void testExecute_TaskNotFound() {
    TaskModel task = null;

    when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);

    asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), TASK_ID);

    verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
  }

  @Test
  public void testExecute_TaskCompleted() {
    WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
    TaskModel task =
        TaskModel.builder()
            .taskType(systemTask.getClass().getSimpleName())
            .taskId(TASK_ID)
            .status(TaskModel.Status.COMPLETED)
            .build();

    asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

    verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
  }

  @Test
  public void testExecute_TaskTerminalStatus() {
    WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
    TaskModel task =
        TaskModel.builder()
            .taskType(systemTask.getClass().getSimpleName())
            .taskId(TASK_ID)
            .status(TaskModel.Status.FAILED)
            .build();

    when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);
    when(executionDAOFacade.exceedsRateLimitPerFrequency(task, mock(TaskModel.class)))
        .thenReturn(false);

    asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

    verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
  }

  @Test
  public void testExecute_TaskScheduled_ConcurrentExecutionLimitExceeded() {
    WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
    TaskModel task =
        TaskModel.builder()
            .taskType(systemTask.getClass().getSimpleName())
            .taskId(TASK_ID)
            .status(TaskModel.Status.SCHEDULED)
            .build();

    when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

    asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

    verify(queueDAO, never()).remove(QUEUE_NAME, TASK_ID);
    verify(queueDAO, times(1)).postpone(
        QUEUE_NAME,
        TASK_ID,
        TaskModel.WorkflowPriority.NORMAL,
        asyncSystemTaskExecutor.queueTaskMessagePostponeSecs);
  }

  @Test
  public void testExecute_TaskScheduled_RateLimitExceeded() {
    WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
    TaskModel task =
        TaskModel.builder()
            .taskType(systemTask.getClass().getSimpleName())
            .taskId(TASK_ID)
            .status(TaskModel.Status.SCHEDULED)
            .rateLimitPerFrequency(1)
            .build();

    when(executionDAOFacade.exceedsRateLimitPerFrequency(task, mock(TaskModel.class)))
        .thenReturn(true);

    asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

    verify(queueDAO, never()).remove(QUEUE_NAME, TASK_ID);
    verify(queueDAO, times(1)).postpone(
        QUEUE_NAME,
        TASK_ID,
        TaskModel.WorkflowPriority.NORMAL,
        asyncSystemTaskExecutor.queueTaskMessagePostponeSecs);
  }

  @Test
  public void testExecute_WorkflowCompleted() {
    WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
    TaskModel task =
        TaskModel.builder()
            .taskType(systemTask.getClass().getSimpleName())
            .taskId(TASK_ID)
            .status(TaskModel.Status.IN_PROGRESS)
            .workflowInstanceId(WORKFLOW_ID)
            .build();

    WorkflowModel workflow =
        WorkflowModel.builder().id(WORKFLOW_ID).status(WorkflowModel.Status.COMPLETED).build();

    when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, true)).thenReturn(workflow);

    asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

    verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
  }

  @Test
  public void testExecute_WorkflowTerminalStatus() {
    WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
    TaskModel task =
        TaskModel.builder()
            .taskType(systemTask.getClass().getSimpleName())
            .taskId(TASK_ID)
            .status(TaskModel.Status.IN_PROGRESS)
            .workflowInstanceId(WORKFLOW_ID)
            .build();

    WorkflowModel workflow =
        WorkflowModel.builder().id(WORKFLOW_ID).status(WorkflowModel.Status.FAILED).build();

    when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, true)).thenReturn(workflow);

    asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

    verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
  }

  @Test
  public void testExecute_WorkflowInProgress() {
    WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
    TaskModel task =
        TaskModel.builder()
            .taskType(systemTask.getClass().getSimpleName())
            .taskId(TASK_ID)
            .status(TaskModel.Status.IN_PROGRESS)
            .workflowInstanceId(WORKFLOW_ID)
            .build();

    WorkflowModel workflow =
        WorkflowModel.builder().id(WORKFLOW_ID).status(WorkflowModel.Status.IN_PROGRESS).build();

    when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, true)).thenReturn(workflow);

    asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

    verify(queueDAO, never()).remove(QUEUE_NAME, TASK_ID);
  }

  @Test
  public void testExecute_AsyncCompleteWithNonScheduledStatus() {
    WorkflowSystemTask systemTask =
        new WorkflowSystemTask() {
          @Override
          public void start(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {}

          @Override
          public void execute(
              WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {}

          @Override
          public boolean isAsyncComplete(TaskModel task) {
            return true;
          }
        };

    TaskModel task =
        TaskModel.builder()
            .taskType(systemTask.getClass().getSimpleName())
            .taskId(TASK_ID)
            .status(TaskModel.Status.IN_PROGRESS)
            .workflowInstanceId(WORKFLOW_ID)
            .build();

    asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

    verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
  }

  @Test
  public void testExecute_AsyncCompleteWithScheduledStatus() {
    WorkflowSystemTask systemTask =
        new WorkflowSystemTask() {
          @Override
          public void start(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {}

          @Override
          public void execute(
              WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {}

          @Override
          public boolean isAsyncComplete(TaskModel task) {
            return true;
          }
        };

    TaskModel task =
        TaskModel.builder()
            .taskType(systemTask.getClass().getSimpleName())
            .taskId(TASK_ID)
            .status(TaskModel.Status.SCHEDULED)
            .workflowInstanceId(WORKFLOW_ID)
            .build();

    asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

    verify(queueDAO, never()).remove(QUEUE_NAME, TASK_ID);
  }

  @Test
  public void testExecute_AsyncCompleteWithScheduledStatus_Exception() {
    WorkflowSystemTask systemTask =
        new WorkflowSystemTask() {
          @Override
          public void start(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {}

          @Override
          public void execute(
              WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {}

          @Override
          public boolean isAsyncComplete(TaskModel task) {
            return true;
          }
        };

    TaskModel task =
        TaskModel.builder()
            .taskType(systemTask.getClass().getSimpleName())
            .taskId(TASK_ID)
            .status(TaskModel.Status.SCHEDULED)
            .workflowInstanceId(WORKFLOW_ID)
            .build();

    when(queueDAO.postpone(
            QUEUE_NAME,
            TASK_ID,
            TaskModel.WorkflowPriority.NORMAL,
            asyncSystemTaskExecutor.systemTaskCallbackTime))
        .thenThrow(new RuntimeException());

    asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

    verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
  }

  @Test
  public void testExecute_TaskError() {
    WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
    TaskModel task =
        TaskModel.builder()
            .taskType(systemTask.getClass().getSimpleName())
            .taskId(TASK_ID)
            .status(TaskModel.Status.IN_PROGRESS)
            .workflowInstanceId(WORKFLOW_ID)
            .build();

    when(systemTask.isAsyncComplete(task)).thenReturn(false);

    doThrow(new RuntimeException()).when(systemTask).start(
        any(WorkflowModel.class),
        any(TaskModel.class),
        any(WorkflowExecutor.class));

    asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

    verify(queueDAO, times(1)).postpone(
        QUEUE_NAME,
        TASK_ID,
        TaskModel.WorkflowPriority.NORMAL,
        asyncSystemTaskExecutor.systemTaskCallbackTime);
  }

  @Test
  public void testExecute_TaskError_Exception() {
    WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
    TaskModel task =
        TaskModel.builder()
            .taskType(systemTask.getClass().getSimpleName())
            .taskId(TASK_ID)
            .status(TaskModel.Status.IN_PROGRESS)
            .workflowInstanceId(WORKFLOW_ID)
            .build();

    when(systemTask.isAsyncComplete(task)).thenReturn(false);

    doThrow(new RuntimeException()).when(systemTask).start(
        any(WorkflowModel.class),
        any(TaskModel.class),
        any(WorkflowExecutor.class));

    when(queueDAO.postpone(
            QUEUE_NAME,
            TASK_ID,
            TaskModel.WorkflowPriority.NORMAL,
            asyncSystemTaskExecutor.systemTaskCallbackTime))
        .thenThrow(new RuntimeException());

    asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

    verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
  }
}
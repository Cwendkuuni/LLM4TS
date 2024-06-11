package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.metrics.Monitors;
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

  @Mock private ExecutionDAOFacade executionDAOFacade;
  @Mock private QueueDAO queueDAO;
  @Mock private MetadataDAO metadataDAO;
  @Mock private WorkflowExecutor workflowExecutor;
  @Mock private QueueUtils queueUtils;
  @Mock private Monitors monitors;
  @Mock private Logger logger;

  @InjectMocks private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    when(logger.isInfoEnabled()).thenReturn(true);
    when(logger.isDebugEnabled()).thenReturn(true);
    when(logger.isWarnEnabled()).thenReturn(true);
    when(logger.isErrorEnabled()).thenReturn(true);
  }

  @Test
  public void testExecuteWithNullTask() {
    asyncSystemTaskExecutor.execute(null, "taskId");
    verify(logger, times(1))
        .error(
            "TaskId: taskId could not be found while executing null",
            (Throwable) any()); // Verify that the error log is printed
  }

  @Test
  public void testExecuteWithMissingTask() {
    String taskId = "taskId";
    TaskModel task = null;
    when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

    asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
    verify(queueDAO, times(1)).remove(anyString(), anyString()); // Verify that the task is removed
  }

  @Test
  public void testExecuteWithTerminalTask() {
    String taskId = "taskId";
    TaskModel task = TaskModel.builder().status(TaskModel.Status.COMPLETED).build();
    when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

    asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
    verify(queueDAO, times(1)).remove(anyString(), anyString()); // Verify that the task is removed
  }

  @Test
  public void testExecuteWithScheduledTask() {
    String taskId = "taskId";
    TaskModel task =
        TaskModel.builder()
            .status(TaskModel.Status.SCHEDULED)
            .taskDefName("taskDefName")
            .rateLimitPerFrequency(1L)
            .build();
    WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
    when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

    asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

    verify(queueUtils, times(1)).getQueueName(task); // Verify that the queue name is generated
    verify(executionDAOFacade, times(1)).exceedsRateLimitPerFrequency(task, any()); // Verify the
    // rate limit check
    verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyLong(), anyLong()); // Verify
    // that the task is postponed
  }

  @Test
  public void testExecuteWithInProgressTask() {
    String taskId = "taskId";
    TaskModel task =
        TaskModel.builder()
            .status(TaskModel.Status.IN_PROGRESS)
            .startTime(System.currentTimeMillis() - 100_000)
            .build();
    WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
    when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

    asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

    verify(workflowSystemTask, times(1)).execute(any(), any(), any()); // Verify that the task is
    // executed
  }

  @Test
  public void testExecuteWithCompletedTask() {
    String taskId = "taskId";
    TaskModel task =
        TaskModel.builder()
            .status(TaskModel.Status.COMPLETED)
            .endTime(System.currentTimeMillis() - 100_000)
            .build();
    WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
    when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

    asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

    verify(queueDAO, times(1)).remove(anyString(), anyString()); // Verify that the task is removed
  }

  @Test
  public void testExecuteWithFailedTask() {
    String taskId = "taskId";
    TaskModel task =
        TaskModel.builder()
            .status(TaskModel.Status.FAILED)
            .endTime(System.currentTimeMillis() - 100_000)
            .build();
    WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
    when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

    asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

    verify(queueDAO, times(1)).remove(anyString(), anyString()); // Verify that the task is removed
  }

  @Test
  public void testPostponeQuietly() {
    String queueName = "queueName";
    TaskModel task = TaskModel.builder().build();
    asyncSystemTaskExecutor.postponeQuietly(queueName, task);

    verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyLong(), anyLong()); // Verify
    // that the task is postponed
  }

  @Test
  public void testLoadTaskQuietlyWithNullTask() {
    String taskId = "taskId";
    when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

    TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

    assertEquals(result, null); // Verify that the null task is returned
  }
}
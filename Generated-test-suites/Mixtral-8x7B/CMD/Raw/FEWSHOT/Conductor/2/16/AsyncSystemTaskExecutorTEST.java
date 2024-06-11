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
import com.netflix.conductor.dao.QueueDAO;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

@Component
public class AsyncSystemTaskExecutorTest {

  @Mock private ExecutionDAOFacade executionDAOFacade;

  @Mock private QueueDAO queueDAO;

  @Mock private MetadataDAO metadataDAO;

  @Mock private WorkflowExecutor workflowExecutor;

  @Mock private QueueUtils queueUtils;

  @InjectMocks private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void executeTest() {
    WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
    String taskId = "task_1";
    TaskModel task = new TaskModel();
    task.setTaskId(taskId);

    when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

    asyncSystemTaskExecutor.execute(systemTask, taskId);

    verify(executionDAOFacade, times(1)).getTaskModel(taskId);
    verify(queueDAO, times(1)).remove(anyString(), anyString());
    verify(workflowExecutor, times(1)).decide(anyString());
  }

  @Test
  public void executeTerminalTaskTest() {
    WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
    String taskId = "task_1";
    TaskModel task = new TaskModel();
    task.setTaskId(taskId);
    task.setStatus(TaskModel.Status.COMPLETED);

    when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

    asyncSystemTaskExecutor.execute(systemTask, taskId);

    verify(executionDAOFacade, times(1)).getTaskModel(taskId);
    verify(queueDAO, times(1)).remove(anyString(), anyString());
    verify(workflowExecutor, times(1)).decide(anyString());
  }

  @Test
  public void executeTaskQueueMessagePostponeSecsTest() {
    WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
    String taskId = "task_1";
    TaskModel task = new TaskModel();
    task.setTaskId(taskId);
    task.setStatus(TaskModel.Status.SCHEDULED);
    task.setRateLimitPerFrequency(1);

    when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
    when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);

    asyncSystemTaskExecutor.execute(systemTask, taskId);

    verify(queueDAO, times(1)).postpone(
        anyString(), anyString(), anyInt(), anyLong(), any(TimeUnit.class));
  }

  @Test
  public void executeTaskExceedsInProgressLimitTest() {
    WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
    String taskId = "task_1";
    TaskModel task = new TaskModel();
    task.setTaskId(taskId);
    task.setStatus(TaskModel.Status.SCHEDULED);

    when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
    when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

    asyncSystemTaskExecutor.execute(systemTask, taskId);

    verify(queueDAO, times(1)).postpone(
        anyString(), anyString(), anyInt(), anyLong(), any(TimeUnit.class));
  }

  @Test
  public void executeTaskNullTaskTest() {
    WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

    when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

    asyncSystemTaskExecutor.execute(systemTask, "task_1");

    verify(executionDAOFacade, times(1)).getTaskModel(anyString());
    verify(queueDAO, times(1)).remove(anyString(), anyString());
  }

  @Test
  public void postponeQuietlyTest() {
    String queueName = "queue_1";
    TaskModel task = new TaskModel();
    task.setTaskId("task_1");

    asyncSystemTaskExecutor.postponeQuietly(queueName, task);

    verify(queueDAO, times(1)).postpone(
        eq(queueName), eq(task.getTaskId()), anyInt(), anyLong(), any(TimeUnit.class));
  }

  @Test
  public void loadTaskQuietlyTest() {
    String taskId = "task_1";

    TaskModel task = new TaskModel();

    when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

    assertEquals(task, asyncSystemTaskExecutor.loadTaskQuietly(taskId));
  }

  @Test
  public void loadTaskQuietlyNullTest() {

    when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

    assertNull(asyncSystemTaskExecutor.loadTaskQuietly("task_id"));
  }
}
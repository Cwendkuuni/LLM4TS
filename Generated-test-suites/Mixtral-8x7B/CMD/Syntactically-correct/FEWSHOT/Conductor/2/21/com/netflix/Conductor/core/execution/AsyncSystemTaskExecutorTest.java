package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.SystemTaskExecutor;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;

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
 private Monitors monitors;

 @Mock
 private WorkflowExecutor workflowExecutor;

 @Mock
 private WorkflowSystemTask workflowSystemTask;

 @InjectMocks
 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testExecute_taskTerminal() {
 TaskModel task = new TaskModel();
 task.setStatus(TaskModel.Status.FAILED);

 asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

 verify(queueDAO, times(1)).remove(anyString(), anyString());
 }

 @Test
 public void testExecute_taskInTerminalState_workflowTerminal() {
 TaskModel task = new TaskModel();
 task.setStatus(TaskModel.Status.IN_PROGRESS);

 WorkflowModel workflow = new WorkflowModel();
 workflow.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);

 doReturn(workflow).when(executionDAOFacade).getWorkflowModel(anyString(), anyBoolean());

 asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

 verify(queueDAO, times(1)).remove(anyString(), anyString());
 }

 @Test
 public void testExecute_taskInTerminalState_workflowInProgress() {
 TaskModel task = new TaskModel();
 task.setStatus(TaskModel.Status.IN_PROGRESS);

 WorkflowModel workflow = new WorkflowModel();
 workflow.setStatus(WorkflowModel.WorkflowStatus.RUNNING);

 doReturn(workflow).when(executionDAOFacade).getWorkflowModel(anyString(), anyBoolean());

 asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

 verify(queueDAO, times(0)).remove(anyString(), anyString());
 }

 @Test
 public void testExecute_taskInScheduledState_exceedsInProgressLimit() {
 TaskModel task = new TaskModel();
 task.setStatus(TaskModel.Status.SCHEDULED);
 task.setTaskDefName("taskDefName");

 doReturn(true).when(executionDAOFacade).exceedsInProgressLimit(any(TaskModel.class));

 asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

 verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
 }

 @Test
 public void testExecute_taskInScheduledState_exceedsRateLimitPerFrequency() {
 TaskModel task = new TaskModel();
 task.setStatus(TaskModel.Status.SCHEDULED);
 task.setTaskDefName("taskDefName");
 task.setRateLimitPerFrequency(1);

 doReturn(true).when(executionDAOFacade).exceedsRateLimitPerFrequency(any(TaskModel.class),
 any(TaskModel.class));

 asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

 verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
 }

 @Test
 public void testExecute_taskInScheduledState_noLimitExceeded() {
 TaskModel task = new TaskModel();
 task.setStatus(TaskModel.Status.SCHEDULED);

 asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

 verify(queueDAO, times(0)).postpone(anyString(), anyString(), anyInt(), anyLong());
 }

 @Test
 public void testExecute_taskInProgressState_asyncComplete() {
 TaskModel task = new TaskModel();
 task.setStatus(TaskModel.Status.IN_PROGRESS);

 WorkflowModel workflow = new WorkflowModel();
 workflow.setStatus(WorkflowModel.WorkflowStatus.RUNNING);

 doReturn(workflow).when(executionDAOFacade).getWorkflowModel(anyString(), anyBoolean());

 doReturn(true).when(workflowSystemTask).isAsyncComplete(any(TaskModel.class));

 asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

 verify(queueDAO, times(1)).remove(anyString(), anyString());
 }

 @Test
 public void testExecute_taskInProgressState_notAsyncComplete() {
 TaskModel task = new TaskModel();
 task.setStatus(TaskModel.Status.IN_PROGRESS);
 task.setStartTime(System.currentTimeMillis());

 WorkflowModel workflow = new WorkflowModel();
 workflow.setStatus(WorkflowModel.WorkflowStatus.RUNNING);

 doReturn(workflow).when(executionDAOFacade).getWorkflowModel(anyString(), anyBoolean());

 doReturn(false).when(workflowSystemTask).isAsyncComplete(any(TaskModel.class));

 asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

 verify(queueDAO, times(0)).remove(anyString(), anyString());
 }

 @Test
 public void testExecute_taskCompleted() {
 TaskModel task = new TaskModel();
 task.setStatus(TaskModel.Status.IN_PROGRESS);
 task.setEndTime(System.currentTimeMillis());

 WorkflowModel workflow = new WorkflowModel();
 workflow.setStatus(WorkflowModel.WorkflowStatus.RUNNING);

 doReturn(workflow).when(executionDAOFacade).getWorkflowModel(anyString(), anyBoolean());

 asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

 verify(queueDAO, times(1)).remove(anyString(), anyString());
 }

 @Test
 public void testExecute_taskPostponed() {
 TaskModel task = new TaskModel();
 task.setStatus(TaskModel.Status.IN_PROGRESS);

 WorkflowModel workflow = new WorkflowModel();
 workflow.setStatus(WorkflowModel.WorkflowStatus.RUNNING);

 doReturn(workflow).when(executionDAOFacade).getWorkflowModel(anyString(), anyBoolean());

 doReturn(false).when(workflowSystemTask).isAsyncComplete(any(TaskModel.class));

 doNothing().when(asyncSystemTaskExecutor).postponeQuietly(anyString(), any(TaskModel.class));
 ReflectionTestUtils.setField(asyncSystemTaskExecutor, "queueTaskMessagePostponeSecs", 10L);

 asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

 verify(queueDAO, times(0)).remove(anyString(), anyString());
 verify(asyncSystemTaskExecutor, times(1)).postponeQuietly(anyString(), any(TaskModel.class));
 }

 @Test
 public void testPostponeQuietly_taskPostponed() {
 String queueName = "queueName";
 TaskModel task = new TaskModel();

 asyncSystemTaskExecutor.postponeQuietly(queueName, task);

 verify(queueDAO, times(1)).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), 10L);
 }

 @Test
 public void testLoadTaskQuietly_taskLoaded() {
 String taskId = "taskId";
 TaskModel task = new TaskModel();

 doReturn(task).when(executionDAOFacade).getTaskModel(taskId);

 TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

 assertEquals(loadedTask, task);
 }

 @Test
 public void testLoadTaskQuietly_taskNotLoaded() {
 String taskId = "taskId";

 doReturn(null).when(executionDAOFacade).getTaskModel(taskId);

 TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

 assertNull(loadedTask);
 }
}
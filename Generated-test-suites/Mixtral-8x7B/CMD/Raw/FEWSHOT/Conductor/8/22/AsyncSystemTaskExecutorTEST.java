package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
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
 private WorkflowExecutor workflowExecutor;

 @Mock
 private ConductorProperties conductorProperties;

 @InjectMocks
 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

 private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);

 private TaskModel task;
 private WorkflowSystemTask systemTask;
 private WorkflowModel workflow;
 private String workflowId = "workflowId";
 private String queueName = "queueName";

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 task = new TaskModel();
 task.setTaskId("taskId");
 systemTask = Mockito.mock(WorkflowSystemTask.class);
 workflow = new WorkflowModel();
 workflow.setStatus(WorkflowModel.Status.IN_PROGRESS);
 }

 @Test
 public void execute_taskNull_shouldReturn() {
 asyncSystemTaskExecutor.execute(systemTask, null);
 Mockito.verify(queueDAO, Mockito.times(0)).remove(queueName, task.getTaskId());
 Mockito.verify(executionDAOFacade, Mockito.times(0)).updateTask(task);
 }

 @Test
 public void execute_taskCompleted_shouldRemoveFromQueueAndUpdateTask() {
 Mockito.when(queueUtils.getQueueName(task)).thenReturn(queueName);
 task.setStatus(TaskModel.Status.COMPLETED);
 asyncSystemTaskExecutor.execute(systemTask, "taskId");
 Mockito.verify(queueDAO, Mockito.times(1)).remove(queueName, task.getTaskId());
 Mockito.verify(executionDAOFacade, Mockito.times(1)).updateTask(task);
 }

 @Test
 public void execute_taskTerminal_shouldThrowException() {
 task.setStatus(TaskModel.Status.FAILED);
 Mockito.when(queueUtils.getQueueName(task)).thenReturn(queueName);
 Mockito.when(executionDAOFacade.getTaskModel(task.getTaskId()))
 .thenReturn(task);
 Assert.assertThrows(
 IllegalStateException.class,
 () -> asyncSystemTaskExecutor.execute(systemTask, "taskId"));
 }

 @Test
 public void execute_workflowTerminal_shouldCompleteTaskAndDecideWorkflow()
 throws Exception {
 task.setStatus(TaskModel.Status.SCHEDULED);
 task.setWorkflowInstanceId(workflowId);
 Mockito.when(queueUtils.getQueueName(task)).thenReturn(queueName);
 Mockito.when(executionDAOFacade.getTaskModel(task.getTaskId()))
 .thenReturn(task);
 Mockito.when(executionDAOFacade.getWorkflowModel(workflowId, true))
 .thenReturn(workflow);
 Mockito.when(systemTask.isAsyncComplete(task)).thenReturn(true);
 asyncSystemTaskExecutor.execute(systemTask, "taskId");
 Mockito.verify(queueDAO, Mockito.times(1)).remove(queueName, task.getTaskId());
 Mockito.verify(executionDAOFacade, Mockito.times(1)).updateTask(task);
 Mockito.verify(workflowExecutor, Mockito.times(1)).decide(workflowId);
 }

 @Test
 public void execute_workflowNotTerminal_shouldPostponeTask() throws Exception {
 task.setStatus(TaskModel.Status.SCHEDULED);
 task.setWorkflowInstanceId(workflowId);
 Mockito.when(queueUtils.getQueueName(task)).thenReturn(queueName);
 Mockito.when(executionDAOFacade.getTaskModel(task.getTaskId()))
 .thenReturn(task);
 Mockito.when(executionDAOFacade.getWorkflowModel(workflowId, true))
 .thenReturn(workflow);
 Mockito.when(systemTask.isAsyncComplete(task)).thenReturn(false);
 asyncSystemTaskExecutor.execute(systemTask, "taskId");
 Mockito.verify(queueDAO, Mockito.times(1))
 .postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), 10L);
 }

 @Test
 public void execute_workflowFailed_shouldCompleteTaskAndDecideWorkflow()
 throws Exception {
 workflow.setStatus(WorkflowModel.Status.FAILED);
 task.setStatus(TaskModel.Status.SCHEDULED);
 task.setWorkflowInstanceId(workflowId);
 Mockito.when(queueUtils.getQueueName(task)).thenReturn(queueName);
 Mockito.when(executionDAOFacade.getTaskModel(task.getTaskId()))
 .thenReturn(task);
 Mockito.when(executionDAOFacade.getWorkflowModel(workflowId, true))
 .thenReturn(workflow);
 Mockito.when(systemTask.isAsyncComplete(task)).thenReturn(true);
 asyncSystemTaskExecutor.execute(systemTask, "taskId");
 Mockito.verify(queueDAO, Mockito.times(1)).remove(queueName, task.getTaskId());
 Mockito.verify(executionDAOFacade, Mockito.times(1)).updateTask(task);
 Mockito.verify(workflowExecutor, Mockito.times(1)).decide(workflowId);
 }

 @Test
 public void postponeQuietly_shouldPostponeTask() {
 asyncSystemTaskExecutor.postponeQuietly(queueName, task);
 Mockito.verify(queueDAO, Mockito.times(1))
 .postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), 10L);
 }

 @Test
 public void loadTaskQuietly_shouldReturnTask() {
 Mockito.when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);
 Assert.assertEquals(
 task, asyncSystemTaskExecutor.loadTaskQuietly("taskId"));
 }

 @Test
 public void loadTaskQuietly_shouldReturnNull() {
 Mockito.when(executionDAOFacade.getTaskModel("taskId")).thenReturn(null);
 Assert.assertNull(asyncSystemTaskExecutor.loadTaskQuietly("taskId"));
 }
}
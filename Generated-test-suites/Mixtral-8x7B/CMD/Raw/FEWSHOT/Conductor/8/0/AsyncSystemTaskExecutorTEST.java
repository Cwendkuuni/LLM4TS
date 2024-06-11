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

import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Component
public class AsyncSystemTaskExecutorTest {

 @Mock
 private ExecutionDAOFacade executionDAOFacade;

 @Mock
 private QueueDAO queueDAO;

 @Mock
 private MetadataDAO metadataDAO;

 @Mock
 private WorkflowExecutor workflowExecutor;

 @Mock
 private QueueUtils queueUtils;

 @InjectMocks
 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

 private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutor.class);

 private TaskModel task;
 private WorkflowModel workflow;
 private WorkflowSystemTask workflowSystemTask;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 task = new TaskModel();
 task.setTaskType("taskType");
 task.setTaskId("taskId");
 task.setWorkflowInstanceId("workflowInstanceId");
 task.setStatus(TaskModel.Status.SCHEDULED);

 workflow = new WorkflowModel();
 workflow.setStatus(WorkflowModel.Status.RUNNING);
 workflow.setWorkflowId("workflowId");

 workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
 }

 @Test
 public void execute_givenTaskAndTaskId_shouldExecuteTask() {
 asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
 verify(workflowSystemTask, times(1)).execute(workflow, task, workflowExecutor);
 }

 @Test
 public void execute_givenTaskAndTaskIdAndTaskIsAlreadyCompleted_shouldRemoveTaskFromQueue() {
 task.setStatus(TaskModel.Status.COMPLETED);
 asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
 verify(queueDAO, times(1)).remove(anyString(), anyString());
 }

 @Test
 public void execute_givenTaskAndTaskIdAndTaskExceedsInProgressLimit_shouldPostponeTask() {
 when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
 asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
 verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
 }

 @Test
 public void execute_givenTaskAndTaskIdAndTaskExceedsRateLimitPerFrequency_shouldPostponeTask() {
 when(executionDAOFacade.exceedsRateLimitPerFrequency(task, metadataDAO.getTaskDef(task.getTaskDefName())))
 .thenReturn(true);
 asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
 verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
 }

 @Test
 public void execute_givenTaskAndTaskIdAndWorkflowIsCompleted_shouldSetTaskStatusToCanceled() {
 workflow.setStatus(WorkflowModel.Status.COMPLETED);
 asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
 Assert.assertEquals(TaskModel.Status.CANCELED, task.getStatus());
 }

 @Test
 public void execute_givenTaskAndTaskIdAndTaskIsAsyncCompleteAndNotScheduled_shouldRemoveTaskFromQueue() {
 task.setStatus(TaskModel.Status.IN_PROGRESS);
 task.setAsyncComplete(true);
 asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
 verify(queueDAO, times(1)).remove(anyString(), anyString());
 }

 @Test
 public void execute_givenTaskAndTaskIdAndTaskIsTerminal_shouldRemoveTaskFromQueue() {
 task.setStatus(TaskModel.Status.FAILED);
 asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
 verify(queueDAO, times(1)).remove(anyString(), anyString());
 }

 @Test
 public void execute_givenTaskAndTaskId_shouldUpdateTask() {
 asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
 verify(executionDAOFacade, times(1)).updateTask(task);
 }

 @Test
 public void execute_givenTaskAndTaskIdAndTaskExecutionIsCompleted_shouldDecideWorkflow() {
 asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
 verify(workflowExecutor, times(1)).decide("workflowInstanceId");
 }

 @Test
 public void postponeQuietly_givenQueueNameAndTask_shouldPostponeTaskQuietly() {
 asyncSystemTaskExecutor.postponeQuietly("queueName", task);
 verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
 }

 @Test
 public void loadTaskQuietly_givenTaskId_shouldReturnTask() {
 when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
 Assert.assertEquals(task, asyncSystemTaskExecutor.loadTaskQuietly("taskId"));
 }

 @Test
 public void loadTaskQuietly_givenTaskId_shouldReturnNullIfTaskIsNotFound() {
 when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);
 Assert.assertNull(asyncSystemTaskExecutor.loadTaskQuietly("taskId"));
 }
}
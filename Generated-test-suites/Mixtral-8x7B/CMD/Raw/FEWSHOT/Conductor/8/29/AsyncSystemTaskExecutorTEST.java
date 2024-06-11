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
 private String taskId;
 private QueueDAO queueDAOMock;
 private String queueName;
 private ConcurrentHashMap<String, ConcurrentHashMap<String, TaskModel>> queueMap;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 queueMap = new ConcurrentHashMap<>();
 queueDAOMock = Mockito.mock(QueueDAO.class);
 queueUtils.initialize(queueDAOMock);
 task = new TaskModel();
 task.setTaskId("task-1");
 task.setStatus(TaskModel.Status.SCHEDULED);
 task.setTaskType("test-task");
 task.setWorkflowInstanceId("workflow-1");
 workflow = new WorkflowModel();
 workflow.setStatus(WorkflowModel.WorkflowStatus.RUNNING);
 workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
 taskId = "task-1";
 queueName = QueueUtils.getQueueName(task);
 }

 @Test
 public void testExecute() {
 Mockito.when(queueUtils.getQueueMap()).thenReturn(queueMap);
 Mockito.when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
 Mockito.when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
 Mockito.when(workflowExecutor.decide(anyString())).thenReturn(true);
 asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
 Mockito.verify(workflowSystemTask, Mockito.times(1)).start(workflow, task, workflowExecutor);
 }

 @Test
 public void testExecuteTaskCompleted() {
 Mockito.when(queueUtils.getQueueMap()).thenReturn(queueMap);
 Mockito.when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
 Mockito.when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
 task.setStatus(TaskModel.Status.COMPLETED);
 asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
 Mockito.verify(queueDAO, Mockito.times(1)).remove(queueName, taskId);
 }

 @Test
 public void testExecuteTaskError() {
 Mockito.when(queueUtils.getQueueMap()).thenReturn(queueMap);
 Mockito.when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
 Mockito.when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
 Mockito.doThrow(new RuntimeException("Test error")).when(workflowSystemTask).execute(any(), any(), any());
 asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
 Mockito.verify(queueDAO, Mockito.times(1)).remove(queueName, taskId);
 }

 @Test
 public void testExecuteTaskCancelled() {
 Mockito.when(queueUtils.getQueueMap()).thenReturn(queueMap);
 Mockito.when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
 Mockito.when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
 task.setStatus(TaskModel.Status.CANCELED);
 asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
 Mockito.verify(queueDAO, Mockito.times(1)).remove(queueName, taskId);
 }

 @Test
 public void testExecuteTaskPostponed() {
 Mockito.when(queueUtils.getQueueMap()).thenReturn(queueMap);
 Mockito.doNothing().when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyInt());
 Mockito.when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
 task.setStatus(TaskModel.Status.IN_PROGRESS);
 asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
 Mockito.verify(queueDAO, Mockito.times(1)).postpone(queueName, taskId, 0, 1);
 }

 @Test
 public void testExecuteTaskPostponedWithPriority() {
 Mockito.when(queueUtils.getQueueMap()).thenReturn(queueMap);
 Mockito.doNothing().when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyInt());
 Mockito.when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
 task.setWorkflowPriority(10);
 task.setStatus(TaskModel.Status.IN_PROGRESS);
 asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
 Mockito.verify(queueDAO, Mockito.times(1)).postpone(queueName, taskId, 10, 1);
 }

 @Test
 public void testExecuteTaskLimitConcurrentExecution() {
 Mockito.when(executionDAOFacade.exceedsInProgressLimit(any())).thenReturn(true);
 Mockito.when(queueUtils.getQueueMap()).thenReturn(queueMap);
 Mockito.doNothing().when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyInt());
 Mockito.when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
 asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
 Mockito.verify(queueDAO, Mockito.times(1)).postpone(queueName, taskId, 0, 1);
 }

 @Test
 public void testExecuteTaskLimitRatePerFrequency() {
 Mockito.when(executionDAOFacade.exceedsRateLimitPerFrequency(any(), any())).thenReturn(true);
 Mockito.when(queueUtils.getQueueMap()).thenReturn(queueMap);
 Mockito.doNothing().when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyInt());
 Mockito.when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
 asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
 Mockito.verify(queueDAO, Mockito.times(1)).postpone(queueName, taskId, 0, 1);
 }

 @Test
 public void testExecuteTaskLimitConcurrentExecutionWithPriority() {
 Mockito.when(executionDAOFacade.exceedsInProgressLimit(any())).thenReturn(true);
 Mockito.when(queueUtils.getQueueMap()).thenReturn(queueMap);
 Mockito.doNothing().when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyInt());
 Mockito.when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
 task.setWorkflowPriority(10);
 asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
 Mockito.verify(queueDAO, Mockito.times(1)).postpone(queueName, taskId, 10, 1);
 }

 @Test
 public void testExecuteTaskLimitRatePerFrequencyWithPriority() {
 Mockito.when(executionDAOFacade.exceedsRateLimitPerFrequency(any(), any())).thenReturn(true);
 Mockito.when(queueUtils.getQueueMap()).thenReturn(queueMap);
 Mockito.doNothing().when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyInt());
 Mockito.when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
 task.setWorkflowPriority(10);
 asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
 Mockito.verify(queueDAO, Mockito.times(1)).postpone(queueName, taskId, 10, 1);
 }
 }
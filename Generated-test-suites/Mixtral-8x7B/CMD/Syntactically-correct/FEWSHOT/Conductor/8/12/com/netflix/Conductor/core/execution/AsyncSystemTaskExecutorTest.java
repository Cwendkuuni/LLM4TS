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

import java.util.Arrays;
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

 @InjectMocks
 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

 private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);

 private TaskModel taskModel;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);

 taskModel = new TaskModel();
 taskModel.setTaskId("task-1");
 taskModel.setWorkflowInstanceId("workflow-1");
 taskModel.setTaskType("TASK_TYPE");
 taskModel.setTaskDefName("TASK_DEF_NAME");
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 taskModel.setPollCount(0);
 taskModel.setRateLimitPerFrequency(0);
 taskModel.setStartTime(System.currentTimeMillis());
 taskModel.setEndTime(0L);
 taskModel.setWorkflowPriority(0);
 }

 @Test
 public void testExecute_taskNotFound() {
 asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, "task-2");
 Mockito.verify(queueDAO, Mockito.times(0)).remove(anyString(), anyString());
 Mockito.verify(executionDAOFacade, Mockito.times(1)).getTaskModel(anyString());
 }

 @Test
 public void testExecute_taskIsCompleted() {
 taskModel.setStatus(TaskModel.Status.COMPLETED);
 Mockito.when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);

 asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, "task-1");

 Mockito.verify(queueDAO, Mockito.times(1)).remove(anyString(), anyString());
 Mockito.verify(executionDAOFacade, Mockito.times(1)).getTaskModel(anyString());
 }

 @Test
 public void testExecute_queueNameIsNull() {
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 Mockito.when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);

 asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, "task-1");

 Mockito.verify(queueDAO, Mockito.times(0)).remove(anyString(), anyString());
 Mockito.verify(queueDAO, Mockito.times(0)).postpone(anyString(), anyString(), anyInt(), anyInt());
 Mockito.verify(executionDAOFacade, Mockito.times(1)).getTaskModel(anyString());
 }

 @Test
 public void testExecute_taskIsInTerminalState() {
 taskModel.setStatus(TaskModel.Status.COMPLETED);
 Mockito.when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
 String queueName = "QUEUE_NAME";
 taskModel.setQueue(queueName);

 asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, "task-1");

 Mockito.verify(queueDAO, Mockito.times(1)).remove(queueName, "task-1");
 Mockito.verify(executionDAOFacade, Mockito.times(1)).getTaskModel(anyString());
 }

 @Test
 public void testExecute_exceedsInProgressLimit() {
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 taskModel.setConcurrentExecutionLimit(1);
 taskModel.setInProgressCount(1);
 Mockito.when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
 String queueName = "QUEUE_NAME";
 taskModel.setQueue(queueName);

 asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, "task-1");

 Mockito.verify(queueDAO, Mockito.times(0)).remove(anyString(), anyString());
 Mockito.verify(queueDAO, Mockito.times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
 Mockito.verify(executionDAOFacade, Mockito.times(1)).getTaskModel(anyString());
 Mockito.verify(executionDAOFacade, Mockito.times(1)).exceedsInProgressLimit(any());
 }

 @Test
 public void testExecute_exceedsRateLimitPerFrequency() {
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 taskModel.setRateLimitPerFrequency(1);
 taskModel.setInProgressCount(0);
 Mockito.when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
 Mockito.when(executionDAOFacade.getTaskDef(anyString())).thenReturn(new ConcurrentHashMap<>());
 String queueName = "QUEUE_NAME";
 taskModel.setQueue(queueName);

 asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, "task-1");

 Mockito.verify(queueDAO, Mockito.times(0)).remove(anyString(), anyString());
 Mockito.verify(queueDAO, Mockito.times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
 Mockito.verify(executionDAOFacade, Mockito.times(1)).getTaskModel(anyString());
 Mockito.verify(executionDAOFacade, Mockito.times(1)).exceedsRateLimitPerFrequency(any(), any());
 }

 @Test
 public void testExecute_workflowIsCompleted() {
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);
 Mockito.when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
 String queueName = "QUEUE_NAME";
 taskModel.setQueue(queueName);

 asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, "task-1");

 Mockito.verify(queueDAO, Mockito.times(1)).remove(queueName, "task-1");
 Mockito.verify(executionDAOFacade, Mockito.times(1)).getTaskModel(anyString());
 Mockito.verify(executionDAOFacade, Mockito.times(1)).getWorkflowModel(anyString(), anyBoolean());
 }

 @Test
 public void testExecute_workflowIsInTerminalState() {
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setStatus(WorkflowModel.WorkflowStatus.FAILED);
 Mockito.when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
 String queueName = "QUEUE_NAME";
 taskModel.setQueue(queueName);

 asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, "task-1");

 Mockito.verify(queueDAO, Mockito.times(1)).remove(queueName, "task-1");
 Mockito.verify(executionDAOFacade, Mockito.times(1)).getTaskModel(anyString());
 Mockito.verify(executionDAOFacade, Mockito.times(1)).getWorkflowModel(anyString(), anyBoolean());
 }

 @Test
 public void testExecute_workflowIsNotInTerminalState() {
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setStatus(WorkflowModel.WorkflowStatus.RUNNING);
 Mockito.when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
 String queueName = "QUEUE_NAME";
 taskModel.setQueue(queueName);

 asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, "task-1");

 Mockito.verify(queueDAO, Mockito.times(0)).remove(anyString(), anyString());
 Mockito.verify(queueDAO, Mockito.times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
 Mockito.verify(executionDAOFacade, Mockito.times(1)).getTaskModel(anyString());
 Mockito.verify(executionDAOFacade, Mockito.times(1)).getWorkflowModel(anyString(), anyBoolean());
 }

 @Test
 public void testPostponeQuietly() {
 asyncSystemTaskExecutor.postponeQuietly("QUEUE_NAME", taskModel);

 Mockito.verify(queueDAO, Mockito.times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
 }

 @Test
 public void testLoadTaskQuietly_taskModelIsFound() {
 Mockito.when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);

 TaskModel loadedTaskModel = asyncSystemTaskExecutor.loadTaskQuietly("task-1");

 Assert.assertEquals(taskModel, loadedTaskModel);
 Mockito.verify(executionDAOFacade, Mockito.times(1)).getTaskModel(anyString());
 }

 @Test
 public void testLoadTaskQuietly_taskModelIsNull() {
 Mockito.when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

 TaskModel loadedTaskModel = asyncSystemTaskExecutor.loadTaskQuietly("task-1");

 Assert.assertNull(loadedTaskModel);
 Mockito.verify(executionDAOFacade, Mockito.times(1)).getTaskModel(anyString());
 }
}
package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.AsyncResult;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.TestWorkflowSystemTask;
import com.netflix.conductor.core.events.EventPublisher;
import com.netflix.conductor.core.metrics.Monitors;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.workflow.WorkflowDef;
import com.netflix.conductor.service.WorkflowService;

public class AsyncSystemTaskExecutorTest {

 @Mock
 private ExecutionDAOFacade executionDAOFacade;

 @Mock
 private QueueDAO queueDAO;

 @Mock
 private MetadataDAO metadataDAO;

 @Mock
 private Monitors monitors;

 @Mock
 private WorkflowExecutor workflowExecutor;

 @Mock
 private WorkflowService workflowService;

 @Mock
 private EventPublisher eventPublisher;

 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

 private ConductorProperties conductorProperties;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);

 conductorProperties = mock(ConductorProperties.class);
 when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(
 TimeUnit.SECONDS.toMillis(10));
 when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(
 TimeUnit.SECONDS.toMillis(5));

 asyncSystemTaskExecutor =
 spy(new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties,
 workflowExecutor));
 }

 @Test
 public void testExecuteAsyncSystemTask() throws Exception {
 WorkflowDef workflowDef = new WorkflowDef();
 workflowDef.setName("testWorkflow");

 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setWorkflowDefName("testWorkflow");

 TaskModel taskModel = new TaskModel();
 taskModel.setWorkflowInstanceId("1234567890");
 taskModel.setTaskId("1");
 taskModel.setTaskType("TestTaskType");
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 taskModel.setWorkflowPriority(1);
 taskModel.setTaskDefName("TestTaskDefName");

 when(executionDAOFacade.getWorkflowModel(anyString(), any()))
 .thenReturn(workflowModel);
 when(executionDAOFacade.getTaskModel(anyString()))
 .thenReturn(taskModel);
 when(executionDAOFacade.exceedsInProgressLimit(any()))
 .thenReturn(false);
 when(executionDAOFacade.exceedsRateLimitPerFrequency(any(), any()))
 .thenReturn(false);
 when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyInt()))
 .thenReturn(new AsyncResult<>("postponed"));
 when(queueDAO.remove(anyString(), anyString()))
 .thenReturn(true);

 WorkflowSystemTask workflowSystemTask = new TestWorkflowSystemTask();

 asyncSystemTaskExecutor.execute(workflowSystemTask, "1");

 verify(executionDAOFacade, times(1)).getWorkflowModel(anyString(), any());
 verify(executionDAOFacade, times(1)).getTaskModel(anyString());
 verify(executionDAOFacade, times(0)).updateTask(any(TaskModel.class));
 verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
 verify(queueDAO, times(1)).remove(anyString(), anyString());
 }

 @Test
 public void testExecuteAsyncSystemTaskWithTerminalStatus() throws Exception {
 WorkflowDef workflowDef = new WorkflowDef();
 workflowDef.setName("testWorkflow");

 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setWorkflowDefName("testWorkflow");
 workflowModel.setStatus(WorkflowModel.Status.COMPLETED);

 TaskModel taskModel = new TaskModel();
 taskModel.setWorkflowInstanceId("1234567890");
 taskModel.setTaskId("1");
 taskModel.setTaskType("TestTaskType");
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 taskModel.setWorkflowPriority(1);
 taskModel.setTaskDefName("TestTaskDefName");

 when(executionDAOFacade.getWorkflowModel(anyString(), any()))
 .thenReturn(workflowModel);
 when(executionDAOFacade.getTaskModel(anyString()))
 .thenReturn(taskModel);
 when(executionDAOFacade.exceedsInProgressLimit(any()))
 .thenReturn(false);
 when(executionDAOFacade.exceedsRateLimitPerFrequency(any(), any()))
 .thenReturn(false);
 when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyInt()))
 .thenReturn(new AsyncResult<>("postponed"));
 when(queueDAO.remove(anyString(), anyString()))
 .thenReturn(true);

 WorkflowSystemTask workflowSystemTask = new TestWorkflowSystemTask();

 asyncSystemTaskExecutor.execute(workflowSystemTask, "1");

 verify(executionDAOFacade, times(1)).getWorkflowModel(anyString(), any());
 verify(executionDAOFacade, times(1)).getTaskModel(anyString());
 verify(executionDAOFacade, times(0)).updateTask(any(TaskModel.class));
 verify(queueDAO, times(1)).remove(anyString(), anyString());
 }

 @Test
 public void testExecuteAsyncSystemTaskWithInProgressLimit() throws Exception {
 WorkflowDef workflowDef = new WorkflowDef();
 workflowDef.setName("testWorkflow");

 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setWorkflowDefName("testWorkflow");

 TaskModel taskModel = new TaskModel();
 taskModel.setWorkflowInstanceId("1234567890");
 taskModel.setTaskId("1");
 taskModel.setTaskType("TestTaskType");
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 taskModel.setWorkflowPriority(1);
 taskModel.setTaskDefName("TestTaskDefName");

 when(executionDAOFacade.getWorkflowModel(anyString(), any()))
 .thenReturn(workflowModel);
 when(executionDAOFacade.getTaskModel(anyString()))
 .thenReturn(taskModel);
 when(executionDAOFacade.exceedsInProgressLimit(any()))
 .thenReturn(true);
 when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyInt()))
 .thenReturn(new AsyncResult<>("postponed"));

 WorkflowSystemTask workflowSystemTask = new TestWorkflowSystemTask();

 asyncSystemTaskExecutor.execute(workflowSystemTask, "1");

 verify(executionDAOFacade, times(1)).getWorkflowModel(anyString(), any());
 verify(executionDAOFacade, times(1)).getTaskModel(anyString());
 verify(executionDAOFacade, times(0)).updateTask(any(TaskModel.class));
 verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
 }

 @Test
 public void testExecuteAsyncSystemTaskWithRateLimit() throws Exception {
 WorkflowDef workflowDef = new WorkflowDef();
 workflowDef.setName("testWorkflow");

 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setWorkflowDefName("testWorkflow");

 TaskModel taskModel = new TaskModel();
 taskModel.setWorkflowInstanceId("1234567890");
 taskModel.setTaskId("1");
 taskModel.setTaskType("TestTaskType");
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 taskModel.setWorkflowPriority(1);
 taskModel.setTaskDefName("TestTaskDefName");

 when(executionDAOFacade.getWorkflowModel(anyString(), any()))
 .thenReturn(workflowModel);
 when(executionDAOFacade.getTaskModel(anyString()))
 .thenReturn(taskModel);
 when(executionDAOFacade.exceedsRateLimitPerFrequency(any(), any()))
 .thenReturn(true);
 when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyInt()))
 .thenReturn(new AsyncResult<>("postponed"));

 WorkflowSystemTask workflowSystemTask = new TestWorkflowSystemTask();

 asyncSystemTaskExecutor.execute(workflowSystemTask, "1");

 verify(executionDAOFacade, times(1)).getWorkflowModel(anyString(), any());
 verify(executionDAOFacade, times(1)).getTaskModel(anyString());
 verify(executionDAOFacade, times(0)).updateTask(any(TaskModel.class));
 verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
 }

 @Test
 public void testExecuteAsyncSystemTaskWithUpdateTask() throws Exception {
 WorkflowDef workflowDef = new WorkflowDef();
 workflowDef.setName("testWorkflow");

 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setWorkflowDefName("testWorkflow");

 TaskModel taskModel = new TaskModel();
 taskModel.setWorkflowInstanceId("1234567890");
 taskModel.setTaskId("1");
 taskModel.setTaskType("TestTaskType");
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 taskModel.setWorkflowPriority(1);
 taskModel.setTaskDefName("TestTaskDefName");

 when(executionDAOFacade.getWorkflowModel(anyString(), any()))
 .thenReturn(workflowModel);
 when(executionDAOFacade.getTaskModel(anyString()))
 .thenReturn(taskModel);
 when(executionDAOFacade.exceedsInProgressLimit(any()))
 .thenReturn(false);
 when(executionDAOFacade.exceedsRateLimitPerFrequency(any(), any()))
 .thenReturn(false);
 when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyInt()))
 .thenReturn(new AsyncResult<>("postponed"));
 when(queueDAO.remove(anyString(), anyString()))
 .thenReturn(true);
 doNothing().when(executionDAOFacade).updateTask(any(TaskModel.class));

 WorkflowSystemTask workflowSystemTask = new TestWorkflowSystemTask();

 asyncSystemTaskExecutor.execute(workflowSystemTask, "1");

 verify(executionDAOFacade, times(1)).getWorkflowModel(anyString(), any());
 verify(executionDAOFacade, times(1)).getTaskModel(anyString());
 verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
 verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
 verify(queueDAO, times(1)).remove(anyString(), anyString());
 }

 @Test
 public void testLoadTaskQuietly() {
 String taskId = "1";
 TaskModel taskModel = new TaskModel();
 taskModel.setTaskId(taskId);
 when(executionDAOFacade.getTaskModel(taskId))
 .thenReturn(taskModel);

 TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

 assertNotNull(result);
 }

 @Test
 public void testLoadTaskQuietlyNull() {
 String taskId = "1";
 when(executionDAOFacade.getTaskModel(taskId))
 .thenReturn(null);

 TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

 assertNull(result);
 }
}

class TestWorkflowSystemTask extends WorkflowSystemTask {

 @Override
 public void start(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {
 // Test implementation
 }

 @Override
 public void execute(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {
 // Test implementation
 }

 @Override
 public boolean isAsyncComplete(TaskModel task) {
 // Test implementation
 return true;
 }

 @Override
 public long getEvaluationOffset(TaskModel task, long systemTaskCallbackTime) {
 // Test implementation
 return 0;
 }
}
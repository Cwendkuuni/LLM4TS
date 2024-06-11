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
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Component
public class AsyncSystemTaskExecutorTest {

 private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);
 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
 private ExecutionDAOFacade executionDAOFacade;
 private QueueDAO queueDAO;
 private MetadataDAO metadataDAO;
 private WorkflowExecutor workflowExecutor;
 private WorkflowSystemTask workflowSystemTask;
 private TaskModel taskModel;
 private QueueDAO queueDAOMock;
 private ConcurrentHashMap<String, TaskModel> taskHashMap;

 @Before
 public void setup() {
 executionDAOFacade = Mockito.mock(ExecutionDAOFacade.class);
 queueDAO = Mockito.mock(QueueDAO.class);
 metadataDAO = Mockito.mock(MetadataDAO.class);
 workflowExecutor = Mockito.mock(WorkflowExecutor.class);
 workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
 taskModel = new TaskModel();
 taskModel.setTaskDefName("testTask");
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 taskModel.setTaskId("testTaskId");
 taskModel.setWorkflowInstanceId("testWorkflowInstanceId");
 queueDAOMock = Mockito.mock(QueueDAO.class);
 taskHashMap = new ConcurrentHashMap<>();
 asyncSystemTaskExecutor =
 new AsyncSystemTaskExecutor(
 executionDAOFacade, queueDAO, metadataDAO, new ConductorProperties(), workflowExecutor) {
 {
 setTaskDAO(queueDAOMock);
 setTaskMap(taskHashMap);
 }
 };
 }

 @Test
 public void testExecuteAsyncSystemTaskExecutor() {
 when(queueDAO.get(anyString(), anyString())).thenReturn(taskModel);
 when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(false);
 when(executionDAOFacade.exceedsRateLimitPerFrequency(
 any(TaskModel.class), any(WorkflowModel.class)))
 .thenReturn(false);
 when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
 when(workflowExecutor.decide(anyString())).thenReturn(true);
 when(queueDAOMock.postpone(
 anyString(), anyString(), anyInt(), anyLong()))
 .thenReturn(true);
 when(queueDAOMock.get(anyString(), anyString())).thenReturn(taskModel);
 when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
 when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
 .thenReturn(new WorkflowModel());
 when(workflowSystemTask.isAsyncComplete(any(TaskModel.class))).thenReturn(true);
 when(workflowSystemTask.start(any(WorkflowModel.class), any(TaskModel.class), any()))
 .thenReturn(true);
 when(workflowSystemTask.execute(any(WorkflowModel.class), any(TaskModel.class), any()))
 .thenReturn(true);
 when(workflowSystemTask.isTaskRetrievalRequired()).thenReturn(true);

 asyncSystemTaskExecutor.execute(workflowSystemTask, "testTaskId");

 Mockito.verify(workflowSystemTask, Mockito.times(1))
 .start(any(WorkflowModel.class), any(TaskModel.class), any());
 Mockito.verify(workflowSystemTask, Mockito.times(1))
 .execute(any(WorkflowModel.class), any(TaskModel.class), any());
 Mockito.verify(queueDAO, Mockito.times(1)).remove(anyString(), anyString());
 Mockito.verify(queueDAOMock, Mockito.times(1))
 .postpone(anyString(), anyString(), anyInt(), anyLong());
 Mockito.verify(workflowExecutor, Mockito.times(1)).decide(anyString());
 }

 @Test
 public void testExecuteAsyncSystemTaskExecutorWithException() {
 when(queueDAO.get(anyString(), anyString())).thenReturn(taskModel);
 when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(false);
 when(executionDAOFacade.exceedsRateLimitPerFrequency(
 any(TaskModel.class), any(WorkflowModel.class)))
 .thenReturn(false);
 when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
 when(workflowExecutor.decide(anyString())).thenReturn(true);
 when(queueDAOMock.postpone(
 anyString(), anyString(), anyInt(), anyLong()))
 .thenReturn(true);
 when(queueDAOMock.get(anyString(), anyString())).thenReturn(taskModel);
 when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
 when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
 .thenThrow(new RuntimeException());

 asyncSystemTaskExecutor.execute(workflowSystemTask, "testTaskId");

 Mockito.verify(workflowSystemTask, Mockito.times(0))
 .start(any(WorkflowModel.class), any(TaskModel.class), any());
 Mockito.verify(workflowSystemTask, Mockito.times(0))
 .execute(any(WorkflowModel.class), any(TaskModel.class), any());
 Mockito.verify(queueDAO, Mockito.times(0)).remove(anyString(), anyString());
 Mockito.verify(queueDAOMock, Mockito.times(0))
 .postpone(anyString(), anyString(), anyInt(), anyLong());
 Mockito.verify(workflowExecutor, Mockito.times(0)).decide(anyString());
 }

 @Test
 public void testExecuteAsyncSystemTaskExecutorWithTerminalStatus() {
 when(queueDAO.get(anyString(), anyString())).thenReturn(taskModel);
 when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(false);
 when(executionDAOFacade.exceedsRateLimitPerFrequency(
 any(TaskModel.class), any(WorkflowModel.class)))
 .thenReturn(false);
 when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
 when(workflowExecutor.decide(anyString())).thenReturn(true);
 when(queueDAOMock.postpone(
 anyString(), anyString(), anyInt(), anyLong()))
 .thenReturn(true);
 when(queueDAOMock.get(anyString(), anyString())).thenReturn(taskModel);
 when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
 when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
 .thenReturn(new WorkflowModel());
 when(workflowSystemTask.isAsyncComplete(any(TaskModel.class))).thenReturn(true);
 when(workflowSystemTask.start(any(WorkflowModel.class), any(TaskModel.class), any()))
 .thenReturn(true);
 when(workflowSystemTask.isTaskRetrievalRequired()).thenReturn(true);
 taskModel.setStatus(TaskModel.Status.COMPLETED);

 asyncSystemTaskExecutor.execute(workflowSystemTask, "testTaskId");

 Mockito.verify(workflowSystemTask, Mockito.times(0))
 .start(any(WorkflowModel.class), any(TaskModel.class), any());
 Mockito.verify(workflowSystemTask, Mockito.times(0))
 .execute(any(WorkflowModel.class), any(TaskModel.class), any());
 Mockito.verify(queueDAO, Mockito.times(0)).remove(anyString(), anyString());
 Mockito.verify(queueDAOMock, Mockito.times(0))
 .postpone(anyString(), anyString(), anyInt(), anyLong());
 Mockito.verify(workflowExecutor, Mockito.times(0)).decide(anyString());
 }

 @Test
 public void testExecuteAsyncSystemTaskExecutorPostponeQuietly() {
 when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong()))
 .thenReturn(true);

 asyncSystemTaskExecutor.postponeQuietly("queueName", taskModel);

 Mockito.verify(queueDAO, Mockito.times(1))
 .postpone(anyString(), anyString(), anyInt(), anyLong());
 }

 @Test
 public void testExecuteAsyncSystemTaskExecutorLoadTaskQuietly() {
 when(executionDAOFacade.getTaskModel(anyString()))
 .thenReturn(taskModel);

 TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("testTaskId");

 Assert.assertEquals(result, taskModel);
 }

 @Test
 public void testExecuteAsyncSystemTaskExecutorLoadTaskQuietlyNull() {
 when(executionDAOFacade.getTaskModel(anyString()))
 .thenReturn(null);

 TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("testTaskId");

 Assert.assertNull(result);
 }
}
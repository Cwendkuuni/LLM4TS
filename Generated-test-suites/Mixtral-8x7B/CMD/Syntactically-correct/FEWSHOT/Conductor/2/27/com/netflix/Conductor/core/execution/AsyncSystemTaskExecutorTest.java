package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.TestWorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.metrics.MonitoringPublisher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class AsyncSystemTaskExecutorTest {

 private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutor.class);

 private ExecutionDAOFacade executionDAOFacade;
 private QueueDAO queueDAO;
 private MetadataDAO metadataDAO;
 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
 private MonitoringPublisher monitoringPublisher;
 private WorkflowSystemTask workflowSystemTask;
 private TaskModel taskModel;
 private WorkflowModel workflowModel;
 private QueueUtils queueUtils;
 private ConductorProperties conductorProperties;
 private WorkflowExecutor workflowExecutor;

 @Before
 public void setUp() {
 executionDAOFacade = mock(ExecutionDAOFacade.class);
 queueDAO = mock(QueueDAO.class);
 metadataDAO = mock(MetadataDAO.class);
 monitoringPublisher = mock(MonitoringPublisher.class);
 workflowSystemTask = mock(WorkflowSystemTask.class);
 taskModel = new TaskModel();
 taskModel.setTaskId("123");
 workflowModel = new WorkflowModel();
 workflowModel.setStatus(WorkflowModel.Status.COMPLETED);
 queueUtils = mock(QueueUtils.class);
 conductorProperties = new ConductorProperties();
 workflowExecutor = mock(WorkflowExecutor.class);

 when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
 when(executionDAOFacade.getWorkflowModel(anyString(), any())).thenReturn(workflowModel);
 when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(false);
 when(executionDAOFacade.exceedsRateLimitPerFrequency(any(TaskModel.class), any())).thenReturn(false);
 when(queueUtils.getQueueName(any(TaskModel.class))).thenReturn("testQueue");

 asyncSystemTaskExecutor =
 new AsyncSystemTaskExecutor(
 executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor) {
 {
 LOGGER = this.LOGGER;
 }
 };
 asyncSystemTaskExecutor.setMonitoringPublisher(monitoringPublisher);
 asyncSystemTaskExecutor.setWorkflowSystemTask(workflowSystemTask);
 asyncSystemTaskExecutor.setQueueUtils(queueUtils);
 }

 @Test
 public void testExecuteWhenTaskIsNull() {
 asyncSystemTaskExecutor.execute(null, "123");

 verify(queueDAO, times(1)).remove("testQueue", "123");
 verify(LOGGER, times(1)).error(
 "TaskId: 123 could not be found while executing {}", "123", Mockito.any(WorkflowSystemTask.class));
 }

 @Test
 public void testExecuteWhenTaskIsCompleted() {
 taskModel.setStatus(TaskModel.Status.COMPLETED);

 asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

 verify(queueDAO, times(1)).remove("testQueue", "123");
 verify(LOGGER, times(1)).info(
 "Task {}/{} was already completed.", TaskModel.Status.COMPLETED, "123");
 }

 @Test
 public void testExecuteWhenTaskIsTerminal() {
 taskModel.setStatus(TaskModel.Status.FAILED);

 asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

 verify(queueDAO, times(1)).remove("testQueue", "123");
 }

 @Test
 public void testExecuteWhenTaskIsScheduled() throws Exception {
 taskModel.setStatus(TaskModel.Status.SCHEDULED);

 asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

 ArgumentCaptor<TaskModel> taskModelArgumentCaptor = ArgumentCaptor.forClass(TaskModel.class);
 verify(executionDAOFacade, times(1)).updateTask(taskModelArgumentCaptor.capture());

 TaskModel updatedTaskModel = taskModelArgumentCaptor.getValue();
 assertEquals(TaskModel.Status.IN_PROGRESS, updatedTaskModel.getStatus());
 }

 @Test
 public void testExecuteWhenTaskIsInProgressAndAsyncComplete() throws Exception {
 taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

 when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(true);

 asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

 ArgumentCaptor<TaskModel> taskModelArgumentCaptor = ArgumentCaptor.forClass(TaskModel.class);
 verify(executionDAOFacade, times(1)).updateTask(taskModelArgumentCaptor.capture());

 TaskModel updatedTaskModel = taskModelArgumentCaptor.getValue();
 assertEquals(TaskModel.Status.COMPLETED, updatedTaskModel.getStatus());
 }

 @Test
 public void testExecuteWhenTaskIsInProgressAndNotAsyncComplete() throws Exception {
 taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

 when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(false);

 asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

 ArgumentCaptor<TaskModel> taskModelArgumentCaptor = ArgumentCaptor.forClass(TaskModel.class);
 verify(executionDAOFacade, times(1)).updateTask(taskModelArgumentCaptor.capture());

 TaskModel updatedTaskModel = taskModelArgumentCaptor.getValue();
 assertEquals(TaskModel.Status.IN_PROGRESS, updatedTaskModel.getStatus());
 }

 @Test
 public void testExecuteWhenTaskIsInProgressAndExceptionOccurs() throws Exception {
 taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

 doThrow(new Exception("test exception")).when(workflowSystemTask).execute(any(), any(), any());

 asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

 ArgumentCaptor<TaskModel> taskModelArgumentCaptor = ArgumentCaptor.forClass(TaskModel.class);
 verify(executionDAOFacade, times(1)).updateTask(taskModelArgumentCaptor.capture());

 TaskModel updatedTaskModel = taskModelArgumentCaptor.getValue();
 assertEquals(TaskModel.Status.FAILED, updatedTaskModel.getStatus());
 }

 @Test
 public void testPostponeQuietly() {
 asyncSystemTaskExecutor.postponeQuietly("testQueue", taskModel);

 verify(queueDAO, times(1)).postpone(
 "testQueue", "123", taskModel.getWorkflowPriority(), conductorProperties.getTaskExecutionPostponeDuration().getSeconds());
 }

 @Test
 public void testPostponeQuietlyWhenExceptionOccurs() {
 doThrow(new Exception("test exception")).when(queueDAO).postpone(
 anyString(), anyString(), anyInt(), anyInt());

 asyncSystemTaskExecutor.postponeQuietly("testQueue", taskModel);

 verify(LOGGER, times(1)).error("Error postponing task: 123 in queue: testQueue");
 }

 @Test
 public void testLoadTaskQuietlyWhenExceptionOccurs() {
 doThrow(new Exception("test exception")).when(executionDAOFacade).getTaskModel(anyString());

 TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("123");

 assertNull(result);
 }

 @Test
 public void testLoadTaskQuietlyWhenTaskIsNull() {
 when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

 TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("123");

 assertNull(result);
 }

 @Test
 public void testLoadTaskQuietlyWhenTaskIsNotNull() {
 when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);

 TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("123");

 assertNotNull(result);
 }
}
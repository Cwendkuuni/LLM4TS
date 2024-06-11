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

 private static final Logger LOGGER =
 LoggerFactory.getLogger(AsyncSystemTaskExecutor.class);
 private TaskModel task;
 private WorkflowModel workflow;
 private WorkflowSystemTask workflowSystemTask;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 task = new TaskModel();
 workflow = new WorkflowModel();
 workflowSystemTask = new WorkflowSystemTask() {};
 }

 @Test
 public void testExecuteWhenTaskIsNull() {
 asyncSystemTaskExecutor.execute(workflowSystemTask, null);
 Mockito.verify(LOGGER).error(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
 Mockito.verify(executionDAOFacade, Mockito.never()).updateTask(task);
 }

 @Test
 public void testExecuteWhenTaskIsCompleted() {
 task.setStatus(TaskModel.Status.COMPLETED);
 asyncSystemTaskExecutor.execute(workflowSystemTask, "1234");
 Mockito.verify(queueDAO).remove(Mockito.anyString(), Mockito.anyString());
 Mockito.verify(executionDAOFacade).updateTask(task);
 }

 @Test
 public void testExecuteWhenTaskIsCancelled() {
 task.setStatus(TaskModel.Status.CANCELED);
 asyncSystemTaskExecutor.execute(workflowSystemTask, "1234");
 Mockito.verify(queueDAO).remove(Mockito.anyString(), Mockito.anyString());
 Mockito.verify(executionDAOFacade).updateTask(task);
 }

 @Test
 public void testExecuteWhenTaskIsFailed() {
 task.setStatus(TaskModel.Status.FAILED);
 asyncSystemTaskExecutor.execute(workflowSystemTask, "1234");
 Mockito.verify(queueDAO).remove(Mockito.anyString(), Mockito.anyString());
 Mockito.verify(executionDAOFacade).updateTask(task);
 }

 @Test
 public void testExecuteWhenTaskIsScheduledAndDoesNotExceedInProgressLimit() {
 task.setStatus(TaskModel.Status.SCHEDULED);
 task.setTaskId("1234");
 task.setTaskType("testTaskType");
 Mockito.when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);
 asyncSystemTaskExecutor.execute(workflowSystemTask, "1234");
 Mockito.verify(queueDAO, Mockito.never()).remove(Mockito.anyString(), Mockito.anyString());
 }

 @Test
 public void testExecuteWhenTaskIsScheduledAndExceedsInProgressLimit() {
 task.setStatus(TaskModel.Status.SCHEDULED);
 task.setTaskId("1234");
 task.setTaskType("testTaskType");
 Mockito.when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
 asyncSystemTaskExecutor.execute(workflowSystemTask, "1234");
 Mockito.verify(queueDAO).postpone(
 Mockito.anyString(),
 Mockito.anyString(),
 Mockito.anyInt(),
 Mockito.anyLong());
 }

 @Test
 public void testExecuteWhenWorkflowIsCompleted() {
 workflow.setStatus(WorkflowModel.Status.COMPLETED);
 Mockito.when(executionDAOFacade.getWorkflowModel(
 Mockito.anyString(),
 Mockito.anyBoolean()))
 .thenReturn(workflow);
 asyncSystemTaskExecutor.execute(workflowSystemTask, "1234");
 Mockito.verify(queueDAO).remove(Mockito.anyString(), Mockito.anyString());
 }

 @Test
 public void testExecuteWhenWorkflowIsFailed() {
 workflow.setStatus(WorkflowModel.Status.FAILED);
 Mockito.when(executionDAOFacade.getWorkflowModel(
 Mockito.anyString(),
 Mockito.anyBoolean()))
 .thenReturn(workflow);
 asyncSystemTaskExecutor.execute(workflowSystemTask, "1234");
 Mockito.verify(queueDAO, Mockito.never()).remove(Mockito.anyString(), Mockito.anyString());
 }

 @Test
 public void testExecuteWhenWorkflowIsInProgress() {
 workflow.setStatus(WorkflowModel.Status.IN_PROGRESS);
 Mockito.when(executionDAOFacade.getWorkflowModel(
 Mockito.anyString(),
 Mockito.anyBoolean()))
 .thenReturn(workflow);
 asyncSystemTaskExecutor.execute(workflowSystemTask, "1234");
 Mockito.verify(queueDAO, Mockito.never()).remove(Mockito.anyString(), Mockito.anyString());
 }

 @Test
 public void testExecuteWhenWorkflowIsPending() {
 workflow.setStatus(WorkflowModel.Status.PENDING);
 Mockito.when(executionDAOFacade.getWorkflowModel(
 Mockito.anyString(),
 Mockito.anyBoolean()))
 .thenReturn(workflow);
 asyncSystemTaskExecutor.execute(workflowSystemTask, "1234");
 Mockito.verify(queueDAO, Mockito.never()).remove(Mockito.anyString(), Mockito.anyString());
 }

 @Test
 public void testExecuteWhenWorkflowIsTerminated() {
 workflow.setStatus(WorkflowModel.Status.TERMINATED);
 Mockito.when(executionDAOFacade.getWorkflowModel(
 Mockito.anyString(),
 Mockito.anyBoolean()))
 .thenReturn(workflow);
 asyncSystemTaskExecutor.execute(workflowSystemTask, "1234");
 Mockito.verify(queueDAO).remove(Mockito.anyString(), Mockito.anyString());
 }

 @Test
 public void testPostponeQuietly() {
 asyncSystemTaskExecutor.postponeQuietly("testQueue", task);
 Mockito.verify(queueDAO).postpone(
 Mockito.anyString(),
 Mockito.anyString(),
 Mockito.anyInt(),
 Mockito.anyLong());
 }

 @Test
 public void testLoadTaskQuietly() {
 Mockito.when(executionDAOFacade.getTaskModel(Mockito.anyString()))
 .thenReturn(task);
 Assert.assertSame(asyncSystemTaskExecutor.loadTaskQuietly("testTaskId"), task);
 Mockito.verify(executionDAOFacade).getTaskModel(Mockito.anyString());
 }

 @Test
 public void testLoadTaskQuietlyWhenTaskIsNull() {
 Assert.assertNull(asyncSystemTaskExecutor.loadTaskQuietly(null));
 Mockito.verify(executionDAOFacade, Mockito.never()).getTaskModel(Mockito.anyString());
 }
}
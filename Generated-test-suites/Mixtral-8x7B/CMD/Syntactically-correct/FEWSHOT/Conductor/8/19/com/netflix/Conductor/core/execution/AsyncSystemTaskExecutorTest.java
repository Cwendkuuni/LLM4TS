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
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Component
public class AsyncSystemTaskExecutorTest {

 private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutor.class);
 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
 private ExecutionDAOFacade executionDAOFacade;
 private QueueDAO queueDAO;
 private MetadataDAO metadataDAO;
 private WorkflowExecutor workflowExecutor;
 private TaskModel task;
 private WorkflowModel workflow;
 private QueueDAO.QueueTask queueTask;
 private ConcurrentHashMap<String, QueueDAO.QueueTask> queueMap;

 @Before
 public void setup() {
 executionDAOFacade = mock(ExecutionDAOFacade.class);
 queueDAO = mock(QueueDAO.class);
 metadataDAO = mock(MetadataDAO.class);
 workflowExecutor = mock(WorkflowExecutor.class);
 asyncSystemTaskExecutor =
 new AsyncSystemTaskExecutor(
 executionDAOFacade, queueDAO, metadataDAO, new ConductorProperties(), workflowExecutor);

 task = new TaskModel();
 task.setTaskId("task-1");
 task.setWorkflowInstanceId("workflow-1");
 task.setTaskType("system-task");
 task.setStatus(TaskModel.Status.SCHEDULED);
 task.setRateLimitPerFrequency(5);

 workflow = new WorkflowModel();
 workflow.setStatus(WorkflowModel.WorkflowStatus.IN_PROGRESS);
 workflow.setWorkflowId("workflow-1");

 queueMap = new ConcurrentHashMap<>();
 queueTask = new QueueDAO.QueueTask();
 queueTask.setTask(task);
 queueMap.put("queue-1", queueTask);
 doReturn(queueMap).when(queueDAO).getQueue("queue-1");
 }

 @Test
 public void testExecute_TaskNotFound() {
 asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, "non-existent-task");

 verify(executionDAOFacade, times(1)).getTaskModel("non-existent-task");
 verifyNoMoreInteractions(queueDAO);
 }

 @Test
 public void testExecute_TaskIsTerminal() {
 task.setStatus(TaskModel.Status.COMPLETED);

 asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, "task-1");

 verify(queueDAO, times(1)).remove("queue-1", "task-1");
 }

 @Test
 public void testExecute_TaskExceedsInProgressLimit() {
 when(executionDAOFacade.exceedsInProgressLimit(any()))
 .thenReturn(true);

 asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, "task-1");

 verify(queueDAO, times(1)).postpone(
 anyString(),
 anyString(),
 anyInt(),
 anyLong());
 }

 @Test
 public void testExecute_TaskExceedsRateLimitPerFrequency() {
 when(executionDAOFacade.exceedsRateLimitPerFrequency(
 any(),
 any()))
 .thenReturn(true);

 asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, "task-1");

 verify(queueDAO, times(1)).postpone(
 anyString(),
 anyString(),
 anyInt(),
 anyLong());
 }

 @Test
 public void testExecute_WorkflowCompleted() {
 when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
 .thenReturn(workflow);

 workflow.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);

 asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, "task-1");

 verify(queueDAO, times(1)).remove("queue-1", "task-1");
 }

 @Test
 public void testExecute_TaskAsyncComplete() {
 task.setTaskId("task-1");
 when(executionDAOFacade.getTaskModel(anyString()))
 .thenReturn(task);

 asyncSystemTaskExecutor.execute(
 new WorkflowSystemTask() {
 @Override
 public boolean isAsyncComplete(TaskModel taskModel) {
 return true;
 }
 },
 "task-1");

 verify(queueDAO, times(1)).remove("queue-1", "task-1");
 }

 @Test
 public void testExecute_TaskNotAsyncComplete() {
 task.setTaskId("task-1");
 when(executionDAOFacade.getTaskModel(anyString()))
 .thenReturn(task);

 asyncSystemTaskExecutor.execute(
 new WorkflowSystemTask() {
 @Override
 public boolean isAsyncComplete(TaskModel taskModel) {
 return false;
 }
 },
 "task-1");

 verify(queueDAO, times(1)).postpone(
 anyString(),
 anyString(),
 anyInt(),
 anyLong());
 }

 @Test
 public void testPostponeQuietly_Exception() {
 doThrow(new RuntimeException())
 .when(queueDAO)
 .postpone(anyString(), anyString(), anyInt(), anyLong());

 asyncSystemTaskExecutor.postponeQuietly("queue-1", task);

 verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
 }

 @Test
 public void testLoadTaskQuietly_Exception() {
 when(executionDAOFacade.getTaskModel(anyString()))
 .thenThrow(new RuntimeException());

 Assert.assertNull(asyncSystemTaskExecutor.loadTaskQuietly("task-1"));
 }
}
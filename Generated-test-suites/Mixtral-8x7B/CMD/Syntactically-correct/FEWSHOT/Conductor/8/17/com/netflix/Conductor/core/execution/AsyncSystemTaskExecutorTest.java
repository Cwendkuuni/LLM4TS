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

import static org.mockito.Mockito.*;

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
 private ConductorProperties conductorProperties = new ConductorProperties();
 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
 }

 @Test
 public void testExecute() {
 TaskModel task = new TaskModel();
 task.setTaskId("task1");
 task.setStatus(TaskModel.Status.SCHEDULED);
 task.setTaskDefName("testTask");

 WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
 when(executionDAOFacade.getTaskModel("task1")).thenReturn(task);
 when(workflowSystemTask.isAsyncComplete(task)).thenReturn(true);

 asyncSystemTaskExecutor.execute(workflowSystemTask, "task1");

 verify(queueDAO, times(1)).remove("queueName", "task1");
 }

 @Test
 public void testExecuteWithTerminalStatus() {
 TaskModel task = new TaskModel();
 task.setTaskId("task2");
 task.setStatus(TaskModel.Status.COMPLETED);

 asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "task2");

 verify(queueDAO, times(1)).remove("queueName", "task2");
 }

 @Test
 public void testExecuteWithInProgressLimitExceeded() {
 TaskModel task = new TaskModel();
 task.setTaskId("task3");
 task.setStatus(TaskModel.Status.SCHEDULED);

 WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
 when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

 asyncSystemTaskExecutor.execute(workflowSystemTask, "task3");

 verify(queueDAO, times(1)).postpone("queueName", "task3", 0, 10);
 }

 @Test
 public void testExecuteWithRateLimitExceeded() {
 TaskModel task = new TaskModel();
 task.setTaskId("task4");
 task.setStatus(TaskModel.Status.SCHEDULED);

 WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
 when(executionDAOFacade.exceedsRateLimitPerFrequency(task, mock(WorkflowModel.class))).thenReturn(true);

 asyncSystemTaskExecutor.execute(workflowSystemTask, "task4");

 verify(queueDAO, times(1)).postpone("queueName", "task4", 0, 10);
 }

 @Test
 public void testExecuteWithWorkflowCompletion() {
 TaskModel task = new TaskModel();
 task.setTaskId("task5");
 task.setStatus(TaskModel.Status.IN_PROGRESS);

 WorkflowModel workflow = new WorkflowModel();
 workflow.setStatus(WorkflowModel.Status.COMPLETED);

 when(executionDAOFacade.getWorkflowModel("workflowId", true)).thenReturn(workflow);

 asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "task5");

 verify(queueDAO, times(1)).remove("queueName", "task5");
 }

 @Test
 public void testExecuteWithException() {
 TaskModel task = new TaskModel();
 task.setTaskId("task6");
 task.setStatus(TaskModel.Status.IN_PROGRESS);

 WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
 when(workflowSystemTask.execute(any(), any(), any())).thenThrow(new RuntimeException());

 asyncSystemTaskExecutor.execute(workflowSystemTask, "task6");

 verify(queueDAO, times(1)).postpone("queueName", "task6", 0, 10);
 }
}
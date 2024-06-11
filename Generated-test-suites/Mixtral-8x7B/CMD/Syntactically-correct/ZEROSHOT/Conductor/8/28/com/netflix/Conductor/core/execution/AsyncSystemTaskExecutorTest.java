package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.SystemTaskQueueDAO;
import com.netflix.conductor.core.events.SystemTaskQueueManager;
import com.netflix.conductor.core.exception.ConductorException;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.model.workflow.WorkflowDef;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.concurrent.ExecutorService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class AsyncSystemTaskExecutorTest {

@Mock
private ExecutionDAOFacade executionDAOFacade;

@Mock
private QueueDAO queueDAO;

@Mock
private MetadataDAO metadataDAO;

@Mock
private SystemTaskQueueDAO systemTaskQueueDAO;

@Mock
private SystemTaskQueueManager systemTaskQueueManager;

@Mock
private WorkflowExecutor workflowExecutor;

@InjectMocks
private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);

ReflectionTestUtils.setField(
asyncSystemTaskExecutor, "queueTaskMessagePostponeSecs", 10);

ReflectionTestUtils.setField(
asyncSystemTaskExecutor, "systemTaskCallbackTime", 20);
}

@Test
public void testExecute() throws ConductorException {
TaskModel taskModel = new TaskModel();
taskModel.setTaskId("task-1");
taskModel.setStatus(TaskModel.Status.SCHEDULED);

WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setWorkflowId("workflow-1");

when(executionDAOFacade.getTaskModel("task-1")).thenReturn(taskModel);
when(executionDAOFacade.getWorkflowModel("workflow-1", true))
.thenReturn(workflowModel);

asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, "task-1");

Mockito.verify(queueDAO, Mockito.times(1))
.postpone(
Mockito.eq("systemTasks"),
Mockito.eq("task-1"),
Mockito.anyInt(),
Mockito.eq(20L));
}

@Test
public void testExecuteWithTaskNotFound() throws ConductorException {
when(executionDAOFacade.getTaskModel("task-1"))
.thenReturn(null);

asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, "task-1");

Mockito.verify(queueDAO, Mockito.times(0)).postpone(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyLong());
}

@Test
public void testExecuteWithTaskCompleted() throws ConductorException {
TaskModel taskModel = new TaskModel();
taskModel.setTaskId("task-1");
taskModel.setStatus(TaskModel.Status.COMPLETED);

WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setWorkflowId("workflow-1");

when(executionDAOFacade.getTaskModel("task-1")).thenReturn(taskModel);
when(executionDAOFacade.getWorkflowModel("workflow-1", true))
.thenReturn(workflowModel);

asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, "task-1");

Mockito.verify(queueDAO, Mockito.times(1)).remove(Mockito.eq("systemTasks"), Mockito.eq("task-1"));
}

@Test
public void testExecuteWithWorkflowCompleted() throws ConductorException {
TaskModel taskModel = new TaskModel();
taskModel.setTaskId("task-1");
taskModel.setStatus(TaskModel.Status.SCHEDULED);

WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setWorkflowId("workflow-1");
workflowModel.setStatus(WorkflowModel.Status.COMPLETED);

when(executionDAOFacade.getTaskModel("task-1")).thenReturn(taskModel);
when(executionDAOFacade.getWorkflowModel("workflow-1", true))
.thenReturn(workflowModel);

asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, "task-1");

Mockito.verify(queueDAO, Mockito.times(1)).remove(Mockito.eq("systemTasks"), Mockito.eq("task-1"));
}

@Test
public void testExecuteWithWorkflowFailed() throws ConductorException {
TaskModel taskModel = new TaskModel();
taskModel.setTaskId("task-1");
taskModel.setStatus(TaskModel.Status.SCHEDULED);

WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setWorkflowId("workflow-1");
workflowModel.setStatus(WorkflowModel.Status.FAILED);

when(executionDAOFacade.getTaskModel("task-1")).thenReturn(taskModel);
when(executionDAOFacade.getWorkflowModel("workflow-1", true))
.thenReturn(workflowModel);

asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, "task-1");

Mockito.verify(queueDAO, Mockito.times(1)).remove(Mockito.eq("systemTasks"), Mockito.eq("task-1"));
}

@Test
public void testExecuteWithWorkflowCancelled() throws ConductorException {
TaskModel taskModel = new TaskModel();
taskModel.setTaskId("task-1");
taskModel.setStatus(TaskModel.Status.SCHEDULED);

WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setWorkflowId("workflow-1");
workflowModel.setStatus(WorkflowModel.Status.CANCELED);

when(executionDAOFacade.getTaskModel("task-1")).thenReturn(taskModel);
when(executionDAOFacade.getWorkflowModel("workflow-1", true))
.thenReturn(workflowModel);

asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, "task-1");

Mockito.verify(queueDAO, Mockito.times(1)).remove(Mockito.eq("systemTasks"), Mockito.eq("task-1"));
}

@Test
public void testExecuteWithWorkflowTerminated() throws ConductorException {
TaskModel taskModel = new TaskModel();
taskModel.setTaskId("task-1");
taskModel.setStatus(TaskModel.Status.SCHEDULED);

WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setWorkflowId("workflow-1");
workflowModel.setStatus(WorkflowModel.Status.TERMINATED);

when(executionDAOFacade.getTaskModel("task-1")).thenReturn(taskModel);
when(executionDAOFacade.getWorkflowModel("workflow-1", true))
.thenReturn(workflowModel);

asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, "task-1");

Mockito.verify(queueDAO, Mockito.times(1)).remove(Mockito.eq("systemTasks"), Mockito.eq("task-1"));
}

@Test
public void testExecuteWithWorkflowInProgress() throws ConductorException {
TaskModel taskModel = new TaskModel();
taskModel.setTaskId("task-1");
taskModel.setStatus(TaskModel.Status.SCHEDULED);

WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setWorkflowId("workflow-1");
workflowModel.setStatus(WorkflowModel.Status.IN_PROGRESS);

when(executionDAOFacade.getTaskModel("task-1")).thenReturn(taskModel);
when(executionDAOFacade.getWorkflowModel("workflow-1", true))
.thenReturn(workflowModel);

asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, "task-1");

Mockito.verify(queueDAO, Mockito.times(1))
.postpone(
Mockito.eq("systemTasks"),
Mockito.eq("task-1"),
Mockito.anyInt(),
Mockito.eq(20L));
}

@Test
public void testPostponeQuietly() {
asyncSystemTaskExecutor.postponeQuietly("systemTasks", new TaskModel());

Mockito.verify(queueDAO, Mockito.times(1))
.postpone(
Mockito.eq("systemTasks"),
Mockito.anyString(),
Mockito.anyInt(),
Mockito.anyLong());
}

@Test
public void testLoadTaskQuietly() throws ConductorException {
when(executionDAOFacade.getTaskModel("task-1"))
.thenReturn(null);

assertNotNull(asyncSystemTaskExecutor.loadTaskQuietly("task-1"));

Mockito.verify(executionDAOFacade, Mockito.times(1)).getTaskModel("task-1");
}
}
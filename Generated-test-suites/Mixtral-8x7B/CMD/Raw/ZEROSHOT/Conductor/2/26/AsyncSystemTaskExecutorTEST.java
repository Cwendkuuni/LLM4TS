package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
private Logger logger;

@Mock
private WorkflowSystemTask workflowSystemTask;

@Mock
private TaskModel taskModel;

@Mock
private WorkflowModel workflowModel;

@InjectMocks
private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

private ConductorProperties conductorProperties;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
conductorProperties = new ConductorProperties();
asyncSystemTaskExecutor.systemTaskCallbackTime = 10L;
asyncSystemTaskExecutor.queueTaskMessagePostponeSecs = 20L;
}

@Test
public void testExecute() {
String taskId = "task-1";
when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
when(taskModel.getWorkflowInstanceId()).thenReturn("workflow-1");
when(queueDAO.getQueueName(taskModel)).thenReturn("queue-1");
when(queueDAO.remove(queueDAO.getQueueName(taskModel), taskId)).thenReturn(true);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(workflowSystemTask).start(workflowModel, taskModel, workflowExecutor);
verify(workflowSystemTask).execute(workflowModel, taskModel, workflowExecutor);
verify(queueDAO).remove(queueDAO.getQueueName(taskModel), taskId);
verify(executionDAOFacade).updateTask(taskModel);
verify(workflowExecutor).decide(taskModel.getWorkflowInstanceId());
}

@Test
public void testExecuteWithTerminalTaskStatus() {
String taskId = "task-1";
when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
taskModel.setStatus(TaskModel.Status.COMPLETED);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(queueDAO).remove(queueDAO.getQueueName(taskModel), taskId);
verify(executionDAOFacade).updateTask(taskModel);
}

@Test
public void testExecuteWithScheduledTaskStatus() {
String taskId = "task-1";
when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
taskModel.setStatus(TaskModel.Status.SCHEDULED);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(queueDAO).postpone(queueDAO.getQueueName(taskModel), taskId, 0, 20L);
verify(executionDAOFacade).updateTask(taskModel);
}

@Test
public void testExecuteWithException() {
String taskId = "task-1";
when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(logger).error("Error executing system task - {}, with id: {}", workflowSystemTask, taskId);
verify(executionDAOFacade).updateTask(taskModel);
verify(queueDAO).remove(queueDAO.getQueueName(taskModel), taskId);
verify(workflowExecutor).decide(taskModel.getWorkflowInstanceId());
}

@Test
public void testPostponeQuietly() {
String queueName = "queue-1";
String taskId = "task-1";

asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

verify(queueDAO).postpone(queueName, taskId, 0, 20L);
}

@Test
public void testLoadTaskQuietly() {
String taskId = "task-1";
when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

assertEquals(taskModel, result);
}

@Test
public void testLoadTaskQuietlyWithException() {
String taskId = "task-1";
when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());

TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

assertEquals(null, result);
}
}
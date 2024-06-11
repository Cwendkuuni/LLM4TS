package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
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

@InjectMocks
private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);

// Initialize ConductorProperties
ConductorProperties conductorProperties = new ConductorProperties();
reflectionSetField(asyncSystemTaskExecutor, "conductorProperties", conductorProperties);

// Initialize WorkflowSystemTask
WorkflowSystemTask workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
reflectionSetField(asyncSystemTaskExecutor, "workflowSystemTask", workflowSystemTask);
}

@Test
public void testExecute() throws ExecutionException, InterruptedException {
TaskModel taskModel = new TaskModel();
taskModel.setTaskId("task-123");
taskModel.setTaskType("TASK_TYPE");
taskModel.setWorkflowInstanceId("workflow-123");
taskModel.setStatus(TaskModel.Status.SCHEDULED);

String taskId = "task-123";

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, taskId);

verify(queueDAO, times(1)).remove(anyString(), anyString());
verify(workflowExecutor, times(1)).decide(anyString());
}

private void reflectionSetField(Object object, String fieldName, Object value)
throws Exception {
ReflectionTestUtils.setField(object, fieldName, value, null);
}
}
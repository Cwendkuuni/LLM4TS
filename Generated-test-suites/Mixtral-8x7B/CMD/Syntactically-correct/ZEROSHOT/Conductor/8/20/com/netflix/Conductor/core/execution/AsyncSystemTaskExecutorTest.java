package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.SystemTaskQueueDAO;
import com.netflix.conductor.core.events.EventService;
import com.netflix.conductor.core.execution.tasks.PollTask;
import com.netflix.conductor.core.execution.tasks.SystemTask;
import com.netflix.conductor.core.execution.tasks.SystemTaskQueueCleaner;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.metrics.Monitors;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.QueueEntryDAO;
import com.netflix.conductor.dao.TaskDAO;
import com.netflix.conductor.dao.TaskDefinitionDAO;
import com.netflix.conductor.dao.TaskExecLogDAO;
import com.netflix.conductor.dao.WorkflowDAO;
import com.netflix.conductor.dao.WorkflowTaskDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowDef;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.tasks.SystemTaskQueue;
import com.netflix.conductor.model.tasks.TaskDefinition;
import com.netflix.conductor.model.tasks.TaskType;
import com.netflix.conductor.service.MetadataService;
import com.netflix.conductor.service.QueueService;
import com.netflix.conductor.service.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutor.class);

@Before
public void setup() {
MockitoAnnotations.initMocks(this);

// Initialize the logger with a custom name to avoid conflicts in the test logs
ReflectionTestUtils.setField(asyncSystemTaskExecutor, "LOGGER", LoggerFactory.getLogger(getClass()));
}

@Test
public void testExecute() {
// Create a mock WorkflowSystemTask and TaskModel
WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
TaskModel taskModel = new TaskModel();
taskModel.setTaskId("testTaskId");

// Call the execute method with the mock objects
asyncSystemTaskExecutor.execute(systemTask, taskModel.getTaskId());

// Verify that the method called the necessary methods and interacted with the mocks as expected
verify(systemTask, times(1)).isAsyncComplete(taskModel);
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt());
verify(executionDAOFacade, times(1)).updateTask(taskModel);
}

@Test
public void testPostponeQuietly() {
// Create a mock TaskModel and QueueDAO
TaskModel taskModel = new TaskModel();
taskModel.setTaskId("testTaskId");
QueueDAO queueDAO = Mockito.mock(QueueDAO.class);

// Call the postponeQuietly method with the mock objects
asyncSystemTaskExecutor.postponeQuietly(queueDAO, taskModel);

// Verify that the method called the necessary methods and interacted with the mocks as expected
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt());
}

@Test
public void testLoadTaskQuietly() {
// Create a mock ExecutionDAOFacade
ExecutionDAOFacade executionDAOFacade = Mockito.mock(ExecutionDAOFacade.class);

// Call the loadTaskQuietly method with the mock objects
TaskModel taskModel = asyncSystemTaskExecutor.loadTaskQuietly(executionDAOFacade, "testTaskId");

// Verify that the method called the necessary methods and interacted with the mocks as expected
verify(executionDAOFacade, times(1)).getTaskModel(anyString());
assertEquals(taskModel, asyncSystemTaskExecutor.loadTaskQuietly(executionDAOFacade, "testTaskId"));
}
}
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AsyncSystemTaskExecutorTest {

private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);
private ApplicationContext context;
private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
private ExecutionDAOFacade executionDAOFacade;
private QueueDAO queueDAO;
private MetadataDAO metadataDAO;
private WorkflowExecutor workflowExecutor;
private WorkflowSystemTask workflowSystemTask;

@Before
public void setUp() {
context = new AnnotationConfigApplicationContext(TestConfig.class);
executionDAOFacade = mock(ExecutionDAOFacade.class);
queueDAO = mock(QueueDAO.class);
metadataDAO = mock(MetadataDAO.class);
workflowExecutor = mock(WorkflowExecutor.class);
workflowSystemTask = mock(WorkflowSystemTask.class);

asyncSystemTaskExecutor =
context.getBean(
AsyncSystemTaskExecutor.class,
executionDAOFacade,
queueDAO,
metadataDAO,
new ConductorProperties(),
workflowExecutor);
}

@Test
public void testExecute_TaskDoesNotExist() throws InterruptedException {
CountDownLatch latch = new CountDownLatch(1);

when(executionDAOFacade.getTaskModel(anyString()))
.thenReturn(null);

when(workflowSystemTask.getTaskType())
.thenReturn("testTaskType");

when(workflowSystemTask.isAsyncComplete(any()))
.thenReturn(true);

asyncSystemTaskExecutor.execute(workflowSystemTask, "testTaskId");

latch.await(1, TimeUnit.SECONDS);

Mockito.verify(queueDAO, Mockito.times(1))
.remove(anyString(), anyString());
}

@Test
public void testExecute_TaskCompleted() throws InterruptedException {
TaskModel taskModel = new TaskModel();
taskModel.setTaskId("testTaskId");
taskModel.setStatus(TaskModel.Status.COMPLETED);

when(executionDAOFacade.getTaskModel(anyString()))
.thenReturn(taskModel);

when(workflowSystemTask.getTaskType())
.thenReturn("testTaskType");

when(workflowSystemTask.isAsyncComplete(any()))
.thenReturn(true);

asyncSystemTaskExecutor.execute(workflowSystemTask, "testTaskId");

Mockito.verify(queueDAO, Mockito.times(1))
.remove(anyString(), anyString());
}

@Test
public void testExecute_TaskScheduledExceedsInProgressLimit() throws InterruptedException {
TaskModel taskModel = new TaskModel();
taskModel.setTaskId("testTaskId");
taskModel.setStatus(TaskModel.Status.SCHEDULED);
taskModel.setTaskDefName("testTaskDefName");

when(executionDAOFacade.getTaskModel(anyString()))
.thenReturn(taskModel);

when(executionDAOFacade.exceedsInProgressLimit(any()))
.thenReturn(true);

when(queueDAO.postpone(
anyString(),
anyString(),
anyInt(),
anyLong()))
.thenReturn(true);

when(workflowSystemTask.getTaskType())
.thenReturn("testTaskType");

asyncSystemTaskExecutor.execute(workflowSystemTask, "testTaskId");

Mockito.verify(queueDAO, Mockito.times(1))
.postpone(anyString(), anyString(), anyInt(), anyLong());
}

@Test
public void testExecute_TaskScheduledExceedsRateLimitPerFrequency() throws InterruptedException {
TaskModel taskModel = new TaskModel();
taskModel.setTaskId("testTaskId");
taskModel.setStatus(TaskModel.Status.SCHEDULED);
taskModel.setTaskDefName("testTaskDefName");

when(executionDAOFacade.getTaskModel(anyString()))
.thenReturn(taskModel);

when(executionDAOFacade.exceedsRateLimitPerFrequency(
any(),
any()))
.thenReturn(true);

when(queueDAO.postpone(
anyString(),
anyString(),
anyInt(),
anyLong()))
.thenReturn(true);

when(workflowSystemTask.getTaskType())
.thenReturn("testTaskType");

asyncSystemTaskExecutor.execute(workflowSystemTask, "testTaskId");

Mockito.verify(queueDAO, Mockito.times(1))
.postpone(anyString(), anyString(), anyInt(), anyLong());
}

@Test
public void testExecute_TaskInProgress() throws InterruptedException {
TaskModel taskModel = new TaskModel();
taskModel.setTaskId("testTaskId");
taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

when(executionDAOFacade.getTaskModel(anyString()))
.thenReturn(taskModel);

when(workflowSystemTask.getTaskType())
.thenReturn("testTaskType");

asyncSystemTaskExecutor.execute(workflowSystemTask, "testTaskId");

Mockito.verify(queueDAO, Mockito.times(0))
.remove(anyString(), anyString());
}

@Test
public void testExecute_WorkflowCompleted() throws InterruptedException {
TaskModel taskModel = new TaskModel();
taskModel.setTaskId("testTaskId");
taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);

when(executionDAOFacade.getTaskModel(anyString()))
.thenReturn(taskModel);

when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
.thenReturn(workflowModel);

when(queueDAO.remove(anyString(), anyString()))
.thenReturn(true);

when(workflowSystemTask.getTaskType())
.thenReturn("testTaskType");

asyncSystemTaskExecutor.execute(workflowSystemTask, "testTaskId");

Mockito.verify(queueDAO, Mockito.times(1))
.remove(anyString(), anyString());
}

@Test
public void testExecute_TaskCompletedCallbackTime() throws InterruptedException {
TaskModel taskModel = new TaskModel();
taskModel.setTaskId("testTaskId");
taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setStatus(WorkflowModel.WorkflowStatus.RUNNING);

when(executionDAOFacade.getTaskModel(anyString()))
.thenReturn(taskModel);

when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
.thenReturn(workflowModel);

when(queueDAO.postpone(
anyString(),
anyString(),
anyInt(),
anyLong()))
.thenReturn(true);

when(workflowSystemTask.getTaskType())
.thenReturn("testTaskType");

asyncSystemTaskExecutor.execute(workflowSystemTask, "testTaskId");

Mockito.verify(queueDAO, Mockito.times(1))
.postpone(anyString(), anyString(), anyInt(), anyLong());
}
}

@Configuration
class TestConfig {

@Bean
public ExecutionDAOFacade executionDAOFacade() {
return mock(ExecutionDAOFacade.class);
}

@Bean
public QueueDAO queueDAO() {
return mock(QueueDAO.class);
}

@Bean
public MetadataDAO metadataDAO() {
return mock(MetadataDAO.class);
}

@Bean
public WorkflowExecutor workflowExecutor() {
return mock(WorkflowExecutor.class);
}

@Bean
public WorkflowSystemTask workflowSystemTask() {
return mock(WorkflowSystemTask.class);
}

@Bean
public ThreadPoolTaskExecutor taskExecutor() {
ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
executor.setCorePoolSize(1);
executor.setMaxPoolSize(1);
executor.setQueueCapacity(1000);
executor.setThreadNamePrefix("TestExecutor-");
executor.initialize();
return executor;
}

@Bean
public ConductorProperties conductorProperties() {
ConductorProperties conductorProperties = new ConductorProperties();
conductorProperties.setSystemTaskWorkerCallbackDuration(Collections.singletonMap("seconds", 5L));
conductorProperties.setTaskExecutionPostponeDuration(Collections.singletonMap("seconds", 10L));
return conductorProperties;
}
}
import com.netflix.conductor.core.dal.MetadataDAOFacade;
import com.netflix.conductor.core.dal.QueueDAOFacade;
import com.netflix.conductor.core.dal.SystemTaskDAOFacade;
import com.netflix.conductor.core.execution.tasks.SystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.TestSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskDAO;
import com.netflix.conductor.dao.WorkflowDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AsyncSystemTaskExecutorTest {

private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
private ExecutionDAOFacade executionDAOFacade;
private QueueDAOFacade queueDAOFacade;
private MetadataDAOFacade metadataDAOFacade;
private SystemTaskDAOFacade systemTaskDAOFacade;
private TaskDAO taskDAO;
private WorkflowDAO workflowDAO;
private WorkflowSystemTask workflowSystemTask;
private QueueUtils queueUtils;
private ApplicationContext applicationContext;

@Before
public void setup() {
executionDAOFacade = mock(ExecutionDAOFacade.class);
queueDAOFacade = mock(QueueDAOFacade.class);
metadataDAOFacade = mock(MetadataDAOFacade.class);
systemTaskDAOFacade = mock(SystemTaskDAOFacade.class);
taskDAO = mock(TaskDAO.class);
workflowDAO = mock(WorkflowDAO.class);
workflowSystemTask = mock(WorkflowSystemTask.class);
queueUtils = mock(QueueUtils.class);

applicationContext = new ClassPathXmlApplicationContext("spring/application-context-test.xml");

asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
executionDAOFacade,
queueDAOFacade,
metadataDAOFacade,
new ConductorProperties(),
applicationContext.getBean(WorkflowExecutor.class));

ReflectionTestUtils.setField(
asyncSystemTaskExecutor, "executionDAOFacade", executionDAOFacade);
ReflectionTestUtils.setField(
asyncSystemTaskExecutor, "queueDAO", queueDAOFacade);
ReflectionTestUtils.setField(
asyncSystemTaskExecutor, "metadataDAO", metadataDAOFacade);
ReflectionTestUtils.setField(
asyncSystemTaskExecutor, "systemTaskDAOFacade", systemTaskDAOFacade);
ReflectionTestUtils.setField(
asyncSystemTaskExecutor, "queueUtils", queueUtils);
}

@Test
public void testExecute_TaskNotFound() {
String taskId = "task-1";

Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(queueDAOFacade, times(1)).remove(anyString(), anyString());
verify(workflowSystemTask, never()).execute(any(), any(), any());
}

@Test
public void testExecute_TerminalTask() {
String taskId = "task-2";
TaskModel taskModel = new TaskModel();
taskModel.setStatus(TaskModel.Status.COMPLETED);

Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(queueDAOFacade, times(1)).remove(anyString(), anyString());
verify(workflowSystemTask, never()).execute(any(), any(), any());
}

@Test
public void testExecute_ScheduledTaskWithConcurrentLimit() {
String taskId = "task-3";
TaskModel taskModel = new TaskModel();
taskModel.setStatus(TaskModel.Status.SCHEDULED);

Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
Mockito.when(executionDAOFacade.exceedsInProgressLimit(any())).thenReturn(true);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(queueUtils, times(1)).getQueueName(any());
verify(queueDAOFacade, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
verify(workflowSystemTask, never()).execute(any(), any(), any());
}

@Test
public void testExecute_ScheduledTaskWithRateLimit() {
String taskId = "task-4";
TaskModel taskModel = new TaskModel();
taskModel.setStatus(TaskModel.Status.SCHEDULED);

Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
Mockito.when(executionDAOFacade.exceedsRateLimitPerFrequency(any(), any()))
.thenReturn(true);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(queueUtils, times(1)).getQueueName(any());
verify(queueDAOFacade, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
verify(workflowSystemTask, never()).execute(any(), any(), any());
}

@Test
public void testExecute_InProgressTask() throws Exception {
String taskId = "task-5";
TaskModel taskModel = new TaskModel();
taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(workflowSystemTask, times(1)).execute(any(), any(), any());
}

@Test
public void testExecute_CompletedTask() throws Exception {
String taskId = "task-6";
TaskModel taskModel = new TaskModel();
taskModel.setStatus(TaskModel.Status.COMPLETED);

Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(queueDAOFacade, times(1)).remove(anyString(), anyString());
verify(workflowSystemTask, never()).execute(any(), any(), any());
}

@Test
public void testExecute_FailedTask() throws Exception {
String taskId = "task-7";
TaskModel taskModel = new TaskModel();
taskModel.setStatus(TaskModel.Status.FAILED);

Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(queueDAOFacade, times(1)).remove(anyString(), anyString());
verify(workflowSystemTask, never()).execute(any(), any(), any());
}

@Test
public void testExecute_CancelledTask() throws Exception {
String taskId = "task-8";
TaskModel taskModel = new TaskModel();
taskModel.setStatus(TaskModel.Status.CANCELED);

Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(queueDAOFacade, times(1)).remove(anyString(), anyString());
verify(workflowSystemTask, never()).execute(any(), any(), any());
}

@Test
public void testExecute_PostponeTask() throws Exception {
String taskId = "task-9";
TaskModel taskModel = new TaskModel();
taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
Mockito.when(workflowSystemTask.isAsyncComplete(any())).thenReturn(true);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(queueDAOFacade, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
}

@Test
public void testExecute_TerminalTaskWithWorkflowCompletion() throws Exception {
String taskId = "task-10";
TaskModel taskModel = new TaskModel();
taskModel.setStatus(TaskModel.Status.COMPLETED);
WorkflowModel workflowModel = new WorkflowModel();

Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
Mockito.when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
.thenReturn(workflowModel);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(queueDAOFacade, times(1)).remove(anyString(), anyString());
verify(workflowSystemTask, never()).execute(any(), any(), any());
verify(workflowExecutor, times(1)).decide(anyString());
}

@Test
public void testExecute_NonTerminalTaskWithWorkflowCompletion() throws Exception {
String taskId = "task-11";
TaskModel taskModel = new TaskModel();
taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
WorkflowModel workflowModel = new WorkflowModel();

Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
Mockito.when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
.thenReturn(workflowModel);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(queueDAOFacade, never()).remove(anyString(), anyString());
verify(workflowSystemTask, times(1)).execute(any(), any(), any());
verify(workflowExecutor, never()).decide(anyString());
}

@Test
public void testPostponeQuietly_Success() {
String queueName = "queue-1";
String taskId = "task-1";

asyncSystemTaskExecutor.postponeQuietly(queueName, new TaskModel());

verify(queueDAOFacade, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
}

@Test
public void testPostponeQuietly_Failure() {
String queueName = "queue-1";
String taskId = "task-1";

Mockito.doThrow(new RuntimeException()).when(queueDAOFacade)
.postpone(anyString(), anyString(), anyInt(), anyLong());

asyncSystemTaskExecutor.postponeQuietly(queueName, new TaskModel());

verify(queueDAOFacade, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
}

@Test
public void testLoadTaskQuietly_Success() {
String taskId = "task-1";

Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(new TaskModel());

TaskModel taskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

verify(executionDAOFacade, times(1)).getTaskModel(taskId);
assert (taskModel != null);
}

@Test
public void testLoadTaskQuietly_Failure() {
String taskId = "task-1";

Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

TaskModel taskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

verify(executionDAOFacade, times(1)).getTaskModel(taskId);
assert (taskModel == null);
}
}
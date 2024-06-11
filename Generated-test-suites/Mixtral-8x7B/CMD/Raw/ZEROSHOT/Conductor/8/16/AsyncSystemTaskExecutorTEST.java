package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.SystemTaskQueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskDAO;
import com.netflix.conductor.dao.TaskRunnerDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.*;

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
private QueueUtils queueUtils;

@Mock
private TaskDAO taskDAO;

@Mock
private TaskRunnerDAO taskRunnerDAO;

@Mock
private ExecutorService executorService;

@InjectMocks
private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

private WorkflowSystemTask workflowSystemTask;
private TaskModel taskModel;
private WorkflowModel workflowModel;
private String taskId;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);

workflowSystemTask = mock(WorkflowSystemTask.class);
taskModel = new TaskModel();
taskModel.setTaskId("task-1");
workflowModel = new WorkflowModel();
workflowModel.setWorkflowId("workflow-1");
taskId = "task-id";

ReflectionTestUtils.setField(asyncSystemTaskExecutor, "systemTaskCallbackTime", 10);
ReflectionTestUtils.setField(asyncSystemTaskExecutor, "queueTaskMessagePostponeSecs", 20);
}

@Test
public void testExecute_TaskDoesNotExist() {
asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(executionDAOFacade, times(1)).getTaskModel(taskId);
verifyNoMoreInteractions(executionDAOFacade);
}

@Test
public void testExecute_TaskIsTerminal() {
taskModel.setStatus(TaskModel.Status.CANCELED);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(taskModel), taskModel.getTaskId());
verifyNoMoreInteractions(queueDAO);
}

@Test
public void testExecute_TaskExceedsInProgressLimit() {
taskModel.setStatus(TaskModel.Status.SCHEDULED);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(queueDAO, times(1)).postpone(
QueueUtils.getQueueName(taskModel),
taskModel.getTaskId(),
taskModel.getWorkflowPriority(),
queueTaskMessagePostponeSecs);
verifyNoMoreInteractions(queueDAO);
}

@Test
public void testExecute_TaskExceedsRateLimitPerFrequency() {
taskModel.setStatus(TaskModel.Status.SCHEDULED);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
when(executionDAOFacade.exceedsRateLimitPerFrequency(
taskModel, metadataDAO.getTaskDef(taskModel.getTaskDefName())))
.thenReturn(true);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(queueDAO, times(1)).postpone(
QueueUtils.getQueueName(taskModel),
taskModel.getTaskId(),
taskModel.getWorkflowPriority(),
queueTaskMessagePostponeSecs);
verifyNoMoreInteractions(queueDAO);
}

@Test
public void testExecute_WorkflowIsCompleted() {
taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
workflowModel.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
when(executionDAOFacade.getWorkflowModel(
workflowModel.getWorkflowId(), true))
.thenReturn(workflowModel);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(taskModel), taskModel.getTaskId());
verifyNoMoreInteractions(queueDAO);
}

@Test
public void testExecute_TaskIsScheduled() {
taskModel.setStatus(TaskModel.Status.SCHEDULED);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(workflowSystemTask, times(1)).start(workflowModel, taskModel, any());
}

@Test
public void testExecute_TaskIsInProgress() {
taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(workflowSystemTask, times(1)).execute(workflowModel, taskModel, any());
}

@Test
public void testExecute_TaskIsCompleted() {
taskModel.setStatus(TaskModel.Status.COMPLETED);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(taskModel), taskModel.getTaskId());
verifyNoMoreInteractions(queueDAO);
}

@Test
public void testExecute_TaskIsFailed() {
taskModel.setStatus(TaskModel.Status.FAILED);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(taskModel), taskModel.getTaskId());
verifyNoMoreInteractions(queueDAO);
}

@Test
public void testExecute_TaskIsCancelled() {
taskModel.setStatus(TaskModel.Status.CANCELED);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(taskModel), taskModel.getTaskId());
verifyNoMoreInteractions(queueDAO);
}

@Test
public void testExecute_TaskIsExpired() {
taskModel.setStatus(TaskModel.Status.EXPIRED);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(taskModel), taskModel.getTaskId());
verifyNoMoreInteractions(queueDAO);
}

@Test
public void testExecute_TaskIsTimedOut() {
taskModel.setStatus(TaskModel.Status.TIMEDOUT);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(taskModel), taskModel.getTaskId());
verifyNoMoreInteractions(queueDAO);
}

@Test
public void testExecute_TaskIsSkipped() {
taskModel.setStatus(TaskModel.Status.SKIPPED);

when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(taskModel), taskModel.getTaskId());
verifyNoMoreInteractions(queueDAO);
}

@Test
public void testPostponeQuietly() {
asyncSystemTaskExecutor.postponeQuietly("queue-name", taskModel);

verify(queueDAO, times(1)).postpone(
"queue-name",
taskModel.getTaskId(),
taskModel.getWorkflowPriority(),
queueTaskMessagePostponeSecs);
verifyNoMoreInteractions(queueDAO);
}

@Test
public void testLoadTaskQuietly_TaskExists() {
when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

assertSame(taskModel, result);
verifyNoMoreInteractions(executionDAOFacade);
}

@Test
public void testLoadTaskQuietly_TaskDoesNotExist() {
when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

assertNull(result);
verifyNoMoreInteractions(executionDAOFacade);
}
}
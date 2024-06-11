import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

private WorkflowSystemTask workflowSystemTask;
private TaskModel taskModel;
private WorkflowModel workflowModel;

@Before
public void init() {
MockitoAnnotations.initMocks(this);

workflowSystemTask = mock(WorkflowSystemTask.class);
taskModel = new TaskModel();
taskModel.setTaskId("taskId");
workflowModel = new WorkflowModel();
workflowModel.setStatus(WorkflowModel.Status.COMPLETED);
}

@Test
public void testExecute_TaskDoesNotExist() throws ExecutionException {
// Given
when(executionDAOFacade.getTaskModel(anyString()))
.thenReturn(null);

// When
asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

// Then
verify(queueDAO, times(1)).remove(anyString(), anyString());
verifyNoMoreInteractions(queueDAO);
verifyNoMoreInteractions(executionDAOFacade);
}

@Test
public void testExecute_TaskIsTerminal() throws ExecutionException {
// Given
when(executionDAOFacade.getTaskModel(anyString()))
.thenReturn(taskModel);
taskModel.setStatus(TaskModel.Status.COMPLETED);

// When
asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

// Then
verify(queueDAO, times(1)).remove(anyString(), anyString());
verifyNoMoreInteractions(queueDAO);
verifyNoMoreInteractions(executionDAOFacade);
}

@Test
public void testExecute_TaskIsScheduled_ExceedsInProgressLimit() throws ExecutionException {
// Given
when(executionDAOFacade.getTaskModel(anyString()))
.thenReturn(taskModel);
taskModel.setStatus(TaskModel.Status.SCHEDULED);

when(executionDAOFacade.exceedsInProgressLimit(any()))
.thenReturn(true);

// When
asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

// Then
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
verifyNoMoreInteractions(queueDAO);
verify(executionDAOFacade, times(1)).exceedsInProgressLimit(any());
verifyNoMoreInteractions(executionDAOFacade);
}

@Test
public void testExecute_TaskIsScheduled_ExceedsRateLimitPerFrequency() throws ExecutionException {
// Given
when(executionDAOFacade.getTaskModel(anyString()))
.thenReturn(taskModel);
taskModel.setStatus(TaskModel.Status.SCHEDULED);
taskModel.setRateLimitPerFrequency(1);

when(metadataDAO.getTaskDef(anyString()))
.thenReturn(new com.netflix.conductor.dao.TaskDef());

when(executionDAOFacade.exceedsRateLimitPerFrequency(any(), any()))
.thenReturn(true);

// When
asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

// Then
verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
verifyNoMoreInteractions(queueDAO);
verify(executionDAOFacade, times(1))
.exceedsRateLimitPerFrequency(any(), any());
verifyNoMoreInteractions(executionDAOFacade);
}

@Test
public void testExecute_TaskIsTerminal_WorkflowIsTerminal() throws ExecutionException {
// Given
when(executionDAOFacade.getTaskModel(anyString()))
.thenReturn(taskModel);
taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
.thenReturn(workflowModel);

// When
asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

// Then
verify(queueDAO, times(1)).remove(anyString(), anyString());
verifyNoMoreInteractions(queueDAO);
verifyNoMoreInteractions(executionDAOFacade);
}

@Test
public void testExecute_TaskIsSCHEDULED_WorkflowIsTerminal() throws ExecutionException {
// Given
when(executionDAOFacade.getTaskModel(anyString()))
.thenReturn(taskModel);
taskModel.setStatus(TaskModel.Status.SCHEDULED);

when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
.thenReturn(workflowModel);

// When
asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

// Then
verify(workflowExecutor, times(1)).decide(anyString());
verifyNoMoreInteractions(workflowExecutor);
verify(queueDAO, times(1)).remove(anyString(), anyString());
verifyNoMoreInteractions(queueDAO);
verifyNoMoreInteractions(executionDAOFacade);
}

@Test
public void testExecute_TaskIsIN_PROGRESS_WorkflowIsTerminal() throws ExecutionException {
// Given
when(executionDAOFacade.getTaskModel(anyString()))
.thenReturn(taskModel);
taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
.thenReturn(workflowModel);

// When
asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

// Then
verifyNoMoreInteractions(workflowExecutor);
verifyNoMoreInteractions(queueDAO);
verifyNoMoreInteractions(executionDAOFacade);
}

@Test
public void testExecute_Exception() throws ExecutionException {
// Given
when(executionDAOFacade.getTaskModel(anyString()))
.thenReturn(taskModel);
taskModel.setStatus(TaskModel.Status.SCHEDULED);

doThrow(new ExecutionException("test"))
.when(workflowSystemTask).execute(any(), any(), any());

// When
asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

// Then
verify(executionDAOFacade, times(1)).updateTask(any());
verifyNoMoreInteractions(workflowExecutor);
verifyNoMoreInteractions(queueDAO);
verifyNoMoreInteractions(executionDAOFacade);
}

@Test
public void testPostponeQuietly() {
// Given
queueDAO = mock(QueueDAO.class);

// When
asyncSystemTaskExecutor.postponeQuietly("queueName", taskModel);

// Then
verifyNoMoreInteractions(queueDAO);
}

@Test
public void testLoadTaskQuietly_TaskExists() {
// Given
executionDAOFacade = mock(ExecutionDAOFacade.class);

when(executionDAOFacade.getTaskModel(anyString()))
.thenReturn(taskModel);

// When
AsyncSystemTaskExecutor asyncSystemTaskExecutorSpy = spy(asyncSystemTaskExecutor);
asyncSystemTaskExecutorSpy.loadTaskQuietly("taskId");

// Then
verify(executionDAOFacade, times(1)).getTaskModel(anyString());
verifyNoMoreInteractions(executionDAOFacade);
}

@Test
public void testLoadTaskQuietly_TaskDoesNotExist() {
// Given
executionDAOFacade = mock(ExecutionDAOFacade.class);

when(executionDAOFacade.getTaskModel(anyString()))
.thenReturn(null);

// When
AsyncSystemTaskExecutor asyncSystemTaskExecutorSpy = spy(asyncSystemTaskExecutor);
asyncSystemTaskExecutorSpy.loadTaskQuietly("taskId");

// Then
verify(executionDAOFacade, times(1)).getTaskModel(anyString());
verifyNoMoreInteractions(executionDAOFacade);
}

private void assertTaskModelFields(TaskModel taskModel, String taskId, long startTime, long endTime, long callbackAfterSeconds, TaskModel.Status status) {
taskModel.setTaskId(taskId);
taskModel.setStartTime(startTime);
taskModel.setEndTime(endTime);
taskModel.setCallbackAfterSeconds(callbackAfterSeconds);
taskModel.setStatus(status);
}
}
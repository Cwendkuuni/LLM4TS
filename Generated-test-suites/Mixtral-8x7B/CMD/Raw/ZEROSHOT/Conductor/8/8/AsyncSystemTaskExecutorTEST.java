import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.TaskModel;
import com.netflix.conductor.dao.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ConcurrentHashMap;

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

    @Mock
    private Monitors monitors;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ConductorProperties conductorProperties = new ConductorProperties();
        conductorProperties.getSystemTaskWorkerCallbackDuration().setSeconds(10);
        conductorProperties.getTaskExecutionPostponeDuration().setSeconds(20);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void testExecute() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecuteWithTerminalTask() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecuteWithRemovedTask() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-123";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, never()).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecuteWithPostponeTask() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setRateLimitPerFrequency(1);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(eq(task), any())).thenReturn(true);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).postpone(anyString(), eq(taskId), anyInt(), eq(20));
    }

    @Test
    public void testExecuteWithAsyncCompleteTask() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setAsyncComplete(true);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecuteWithAsyncCompleteTaskAndNonScheduledStatus() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.COMPLETED);
        task.setAsyncComplete(true);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecuteWithAsyncCompleteTaskAndNonScheduledStatusAndWorkflowExecutorDecideCalled() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.COMPLETED);
        task.setAsyncComplete(true);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(workflowExecutor, times(1)).decide(anyString());
    }

    @Test
    public void testExecuteWithNonAsyncCompleteTaskAndNonScheduledStatusAndWorkflowExecutorDecideCalled() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setAsyncComplete(false);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(workflowExecutor, never()).decide(anyString());
    }

    @Test
    public void testExecuteWithNonAsyncCompleteTaskAndNonScheduledStatusAndWorkflowExecutorNotCalled() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.COMPLETED);
        task.setAsyncComplete(false);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(workflowExecutor, never()).decide(anyString());
    }

    @Test
    public void testExecuteWithException() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        doThrow(new RuntimeException("Test exception")).when(systemTask).execute(any(), any(), any());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).postpone(anyString(), eq(taskId), anyInt(), eq(10));
        verify(monitors, times(1)).error(anyString(), anyString());
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "test-queue";
        TaskModel task = new TaskModel();
        task.setTaskId("task-123");
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO, times(1)).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), 20);
    }

    @Test
    public void testLoadTaskQuietlyWithTask() {
        String taskId = "task-123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        assertEquals(task, result);
    }

    @Test
    public void testLoadTaskQuietlyWithoutTask() {
        String taskId = "task-123";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        assertNull(result);
    }
}
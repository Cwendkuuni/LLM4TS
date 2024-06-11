import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
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

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade, queueDAO, metadataDAO, new ConductorProperties(), workflowExecutor);
    }

    @Test
    public void execute_givenSystemTaskAndTaskId_whenTaskDoesNotExist_thenLogErrorAndRemoveFromQueue() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-1";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(anyString(), eq(taskId));
        verify(systemTask).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
    }

    @Test
    public void execute_givenSystemTaskAndTaskId_whenTaskIsCompleted_thenRemoveFromQueue() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-1";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(anyString(), eq(taskId));
    }

    @Test
    public void execute_givenSystemTaskAndTaskId_whenTaskIsScheduledAndExceedsInProgressLimit_thenPostpone() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-1";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).postpone(anyString(), eq(taskId), anyInt(), eq(0L));
    }

    @Test
    public void execute_givenSystemTaskAndTaskId_whenTaskIsScheduledAndExceedsRateLimitPerFrequency_thenPostpone() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-1";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setRateLimitPerFrequency(1);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(TaskModel.class), any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).postpone(anyString(), eq(taskId), anyInt(), eq(0L));
    }

    @Test
    public void execute_givenSystemTaskAndTaskId_whenWorkflowIsCompleted_thenSetTaskStatusToCanceled() throws ExecutionException, InterruptedException {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-1";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).updateTask(any(TaskModel.class));
        assertEquals(TaskModel.Status.CANCELED, task.getStatus());
    }

    @Test
    public void execute_givenSystemTaskAndTaskId_whenWorkflowIsNotCompleted_thenExecuteSystemTask() throws ExecutionException, InterruptedException {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-1";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.WorkflowStatus.RUNNING);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(queueDAO).postpone(anyString(), eq(taskId), anyInt(), eq(0L));
    }

    @Test
    public void postponeQuietly_givenQueueNameAndTask_whenNoExceptionIsThrown_thenPostponeTask() {
        String queueName = "queue-1";
        TaskModel task = new TaskModel();

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO).postpone(eq(queueName), eq(task.getTaskId()), eq(task.getWorkflowPriority()), eq(0L));
    }

    @Test
    public void postponeQuietly_givenQueueNameAndTask_whenExceptionIsThrown_thenLogError() {
        String queueName = "queue-1";
        TaskModel task = new TaskModel();

        doThrow(new RuntimeException()).when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO).postpone(eq(queueName), eq(task.getTaskId()), eq(task.getWorkflowPriority()), eq(0L));
        verify(LOGGER).error(anyString(), anyString(), anyString());
    }

    @Test
    public void loadTaskQuietly_givenTaskId_whenNoExceptionIsThrown_thenReturnTask() {
        String taskId = "task-1";
        TaskModel task = new TaskModel();

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(task, result);
    }

    @Test
    public void loadTaskQuietly_givenTaskId_whenExceptionIsThrown_thenReturnNull() {
        String taskId = "task-1";

        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(null, result);
    }
}
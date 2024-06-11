import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@Component
public class AsyncSystemTaskExecutorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                new ConductorProperties(),
                workflowExecutor);
    }

    @Test
    public void testExecute_TaskNotFound() {
        String taskId = "123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, systemTask);
    }

    @Test
    public void testExecute_TaskTerminal() {
        String taskId = "123";
        TaskModel task = createTaskModel(taskId, TaskModel.Status.COMPLETED);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAOFacade, times(1)).remove(anyString(), eq(taskId));
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, systemTask);
    }

    @Test
    public void testExecute_TaskScheduled_ExceedsInProgressLimit() {
        String taskId = "123";
        TaskModel task = createTaskModel(taskId, TaskModel.Status.SCHEDULED);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).postpone(anyString(), eq(taskId), anyInt(), anyLong());
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, systemTask);
    }

    @Test
    public void testExecute_TaskScheduled_ExceedsRateLimit() {
        String taskId = "123";
        TaskModel task = createTaskModel(taskId, TaskModel.Status.SCHEDULED);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, new TaskModel())).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).postpone(anyString(), eq(taskId), anyInt(), anyLong());
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, systemTask);
    }

    @Test
    public void testExecute_TaskScheduled_Completed() {
        String taskId = "123";
        TaskModel task = createTaskModel(taskId, TaskModel.Status.SCHEDULED);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, systemTask);
    }

    @Test
    public void testExecute_TaskInProgress() {
        String taskId = "123";
        TaskModel task = createTaskModel(taskId, TaskModel.Status.IN_PROGRESS);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, systemTask);
    }

    @Test
    public void testExecute_TaskCompleted() {
        String taskId = "123";
        TaskModel task = createTaskModel(taskId, TaskModel.Status.COMPLETED);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, systemTask);
    }

    @Test
    public void testExecute_TaskFailed() {
        String taskId = "123";
        TaskModel task = createTaskModel(taskId, TaskModel.Status.FAILED);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, systemTask);
    }

    @Test
    public void testExecute_TaskCallbackTimeExceeded() {
        String taskId = "123";
        TaskModel task = createTaskModel(taskId, TaskModel.Status.IN_PROGRESS);
        task.setCallbackAfterSeconds(1);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).postpone(anyString(), eq(taskId), anyInt(), anyLong());
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, systemTask);
    }

    @Test
    public void testExecute_TaskSuccess() {
        String taskId = "123";
        TaskModel task = createTaskModel(taskId, TaskModel.Status.IN_PROGRESS);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        WorkflowModel workflowModel = mock(WorkflowModel.class);

        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflowModel);
        when(workflowModel.getStatus())
                .thenReturn(WorkflowModel.WorkflowStatus.RUNNING);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).postpone(
                anyString(),
                eq(taskId),
                eq(task.getWorkflowPriority()),
                anyLong());
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, systemTask);
    }

    @Test
    public void testExecute_TaskSuccess_WorkflowCompleted() {
        String taskId = "123";
        TaskModel task = createTaskModel(taskId, TaskModel.Status.IN_PROGRESS);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        WorkflowModel workflowModel = mock(WorkflowModel.class);

        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflowModel);
        when(workflowModel.getStatus())
                .thenReturn(WorkflowModel.WorkflowStatus.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, systemTask);
    }

    private TaskModel createTaskModel(String taskId, TaskModel.Status status) {
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(status);
        task.setWorkflowInstanceId("workflowId");
        return task;
    }
}
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskModel;
import com.netflix.conductor.dao.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Component
public class AsyncSystemTaskExecutorTest {

    private final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);

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
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void execute_taskDoesNotExist_taskIsLoggedAsNotFound() {
        String taskId = "task-123";
        TaskModel task = null;

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(LOGGER, times(1))
                .error("TaskId: {} could not be found while executing {}", taskId, any());
    }

    @Test
    public void execute_taskIsCompleted_taskIsRemovedFromQueue() {
        String taskId = "task-123";
        TaskModel task = createTaskModel(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(1)).remove(any(), eq(taskId));
    }

    @Test
    public void execute_taskIsScheduledAndExceedsInProgressLimit_taskIsPostponed() {
        String taskId = "task-123";
        TaskModel task = createTaskModel(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(1)).postpone(any(), eq(taskId), anyInt(), anyLong());
    }

    @Test
    public void execute_taskIsScheduledAndExceedsRateLimit_taskIsPostponed() {
        String taskId = "task-123";
        TaskModel task = createTaskModel(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(1)).postpone(any(), eq(taskId), anyInt(), anyLong());
    }

    @Test
    public void execute_taskIsCompleted_workflowIsEvaluated() {
        String workflowId = "workflow-123";
        String taskId = "task-123";
        TaskModel task = createTaskModel(TaskModel.Status.COMPLETED);

        when(task.getWorkflowInstanceId()).thenReturn(workflowId);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void postponeQuietly_taskIsPostponed() {
        String queueName = "queue-123";
        String taskId = "task-123";
        TaskModel task = createTaskModel(TaskModel.Status.SCHEDULED);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO, times(1)).postpone(eq(queueName), eq(taskId), anyInt(), anyLong());
    }

    @Test
    public void loadTaskQuietly_taskIsReturnedFromDAO() {
        String taskId = "task-123";
        TaskModel task = createTaskModel(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(task, result);
    }

    private TaskModel createTaskModel(TaskModel.Status status) {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(status);
        taskModel.setTaskId("task-123");
        taskModel.setWorkflowInstanceId("workflow-123");
        return taskModel;
    }
}
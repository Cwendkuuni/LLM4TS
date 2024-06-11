import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.execution.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.model.WorkflowModel;
import com.netflix.conductor.core.execution.model.TaskModel;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.workers.WorkflowExecutor;
import com.netflix.conductor.core.metrics.Monitors;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Configuration
@ComponentScan("com.netflix.conductor.core.execution")
public class AsyncSystemTaskExecutorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private WorkflowSystemTask workflowSystemTask;

    private TaskModel taskModel;

    private WorkflowModel workflowModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(anyString(), any()))
                .thenReturn(workflowModel);
        when(executionDAOFacade.exceedsInProgressLimit(any())).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(), any()))
                .thenReturn(false);

        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong()))
                .thenReturn(true);

        ConductorProperties conductorProperties = new ConductorProperties();
        conductorProperties.setSystemTaskWorkerCallbackDuration(10);
        conductorProperties.setTaskExecutionPostponeDuration(10);

        asyncSystemTaskExecutor =
                new AsyncSystemTaskExecutor(
                        executionDAOFacade, queueDAO, metadataDAO, conductorProperties, new WorkflowExecutor(new ThreadPoolTaskExecutor()));

        workflowSystemTask = mock(WorkflowSystemTask.class);

        taskModel = new TaskModel();
        taskModel.setWorkflowInstanceId("workflowId");
        taskModel.setTaskId("taskId");
        taskModel.setTaskDefName("taskDefName");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);
    }

    @Test
    public void execute_taskDoesNotExist_logError() {
        asyncSystemTaskExecutor.execute(workflowSystemTask, "nonExistentTaskId");

        verify(LOGGER, times(1)).error(anyString(), anyString(), any());
    }

    @Test
    public void execute_taskIsTerminal_removeFromQueue() {
        taskModel.setStatus(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void execute_taskExceedsInProgressLimit_postponeQuietly() {
        when(executionDAOFacade.exceedsInProgressLimit(any()))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void execute_taskExceedsRateLimitPerFrequency_postponeQuietly() {
        taskModel.setRateLimitPerFrequency(1);

        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(), any()))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void execute_workflowIsCompleted_cancelTask() {
        workflowModel.setStatus(WorkflowModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        assertEquals(TaskModel.Status.CANCELED, taskModel.getStatus());
        assertEquals("Workflow is in COMPLETED state", taskModel.getReasonForIncompletion());
    }

    @Test
    public void execute_workflowIsTerminal_removeFromQueue() {
        workflowModel.setStatus(WorkflowModel.Status.FAILED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void execute_taskIsUpdated_updateTask() {
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(executionDAOFacade, times(1)).updateTask(taskModel);
    }

    @Test
    public void execute_taskHasCompleted_decideWorkflow() {
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(workflowExecutor, times(1)).decide("workflowId");
    }

    @Test
    public void postponeQuietly_taskIsPostponed_returnTrue() {
        boolean result = asyncSystemTaskExecutor.postponeQuietly("queueName", taskModel);

        assertTrue(result);
    }

    @Test
    public void postponeQuietly_exceptionIsThrown_logError() {
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong()))
                .thenThrow(new RuntimeException("Test exception"));

        asyncSystemTaskExecutor.postponeQuietly("queueName", taskModel);

        verify(LOGGER, times(1)).error(anyString(), anyString(), any());
    }

    @Test
    public void loadTaskQuietly_taskIsLoaded_returnTask() {
        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("taskId");

        assertEquals(taskModel, result);
    }

    @Test
    public void loadTaskQuietly_exceptionIsThrown_returnNull() {
        when(executionDAOFacade.getTaskModel(anyString()))
                .thenThrow(new RuntimeException("Test exception"));

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("taskId");

        assertNull(result);
    }
}
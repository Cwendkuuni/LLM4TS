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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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
    private QueueUtils queueUtils;

    @Mock
    private Logger logger;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private WorkflowSystemTask workflowSystemTask;
    private TaskModel taskModel;
    private WorkflowModel workflowModel;

    @Before
    public void setUp() {
        workflowSystemTask = mock(WorkflowSystemTask.class);
        taskModel = mock(TaskModel.class);
        workflowModel = mock(WorkflowModel.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                mock(ConductorProperties.class),
                workflowExecutor
        );

        logger = mock(Logger.class);
        asyncSystemTaskExecutor.LOGGER = logger;
    }

    @Test
    public void testExecute() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(queueUtils.getQueueName(taskModel)).thenReturn("testQueue");

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(logger, times(1)).debug("Task: {} fetched from execution DAO for taskId: taskId", taskModel);
        verify(queueDAO, times(1)).remove("testQueue", "taskId");
        verify(executionDAOFacade, times(1)).updateTask(taskModel);
        verify(workflowExecutor, times(1)).decide(anyString());
    }

    @Test
    public void testExecuteTaskAlreadyCompleted() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO, times(1)).remove("testQueue", "taskId");
    }

    @Test
    public void testExecuteTaskWithRateLimitExceeded() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(taskModel.getRateLimitPerFrequency()).thenReturn(1);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(), any()))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO, times(1)).postpone("testQueue", "taskId", anyInt(), anyLong());
    }

    @Test
    public void testExecuteTaskWithConcurrentExecutionLimitExceeded() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(executionDAOFacade.exceedsInProgressLimit(any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO, times(1)).postpone("testQueue", "taskId", anyInt(), anyLong());
    }

    @Test
    public void testExecuteTaskWithWorkflowCompleted() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(taskModel.getWorkflowInstanceId()).thenReturn("workflowId");
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.WorkflowStatus.COMPLETED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO, times(1)).remove("testQueue", "taskId");
    }

    @Test
    public void testExecuteTaskWithWorkflowNotStarted() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(taskModel.getWorkflowInstanceId()).thenReturn("workflowId");
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.WorkflowStatus.STARTED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(workflowExecutor, times(1)).decide("workflowId");
    }

    @Test
    public void testExecuteTaskWithWorkflowCancelled() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(taskModel.getWorkflowInstanceId()).thenReturn("workflowId");
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.WorkflowStatus.CANCELED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO, times(1)).remove("testQueue", "taskId");
    }

    @Test
    public void testExecuteTaskWithWorkflowFailed() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(taskModel.getWorkflowInstanceId()).thenReturn("workflowId");
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.WorkflowStatus.FAILED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO, times(1)).remove("testQueue", "taskId");
    }

    @Test
    public void testExecuteTaskWithWorkflowTerminalState() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(taskModel.getWorkflowInstanceId()).thenReturn("workflowId");
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.WorkflowStatus.TIMEDOUT);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO, times(1)).remove("testQueue", "taskId");
    }

    @Test
    public void testExecuteAsyncCompleteTask() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO, times(1)).remove("testQueue", "taskId");
    }
}
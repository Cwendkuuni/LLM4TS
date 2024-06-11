import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.TaskModel;
import com.netflix.conductor.dao.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Component
public class AsyncSystemTaskExecutorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        asyncSystemTaskExecutor =
                new AsyncSystemTaskExecutor(
                        executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute_TaskNotFound_LogsErrorAndRemovesFromQueue() {
        String taskId = "123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(LOGGER, times(1)).error(anyString(), anyString(), any(WorkflowSystemTask.class));
    }

    @Test
    public void testExecute_TaskTerminalStatus_LogsInfoAndRemovesFromQueue() {
        String taskId = "123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = createTaskModel(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(LOGGER, times(1))
                .info("Task {}/{} was already completed.", task.getTaskType(), task.getTaskId());
    }

    @Test
    public void testExecute_TaskScheduledAndRateLimitExceeded_PostponesTask() {
        String taskId = "123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = createTaskModel(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt());
    }

    @Test
    public void testExecute_TaskScheduledAndConcurrentLimitExceeded_PostponesTask() {
        String taskId = "123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = createTaskModel(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any()))
                .thenReturn(false);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt());
    }

    @Test
    public void testExecute_TaskInProgress_ExecutesSystemTask() {
        String taskId = "123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = createTaskModel(TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask, times(1)).execute(any(), any(), any());
    }

    @Test
    public void testExecute_TaskAsyncCompleteAndNotScheduled_RemovesFromQueue() {
        String taskId = "123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = createTaskModel(TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_TaskTerminalStatusAndWorkflowTerminal_CancelsTask() {
        String taskId = "123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = createTaskModel(TaskModel.Status.IN_PROGRESS);
        WorkflowModel workflow = createWorkflowModel(WorkflowModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(task, times(1)).setStatus(TaskModel.Status.CANCELED);
        verify(task, times(1)).setReasonForIncompletion(anyString());
    }

    @Test
    public void testExecute_ExceptionDuringExecution_LogsError() {
        String taskId = "123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = createTaskModel(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        doThrow(new RuntimeException("Test exception")).when(systemTask).execute(any(), any(), any());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(LOGGER, times(1)).error(anyString(), any(RuntimeException.class));
    }

    @Test
    public void testPostponeQuietly_ExceptionDuringPostpone_LogsError() {
        String queueName = "test-queue";
        TaskModel task = createTaskModel(TaskModel.Status.SCHEDULED);

        doThrow(new RuntimeException("Test exception")).when(queueDAO).postpone(anyString(), anyString(), anyInt());

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(LOGGER, times(1)).error(anyString());
    }

    @Test
    public void testLoadTaskQuietly_ExceptionDuringLoad_ReturnsNull() {
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException("Test exception"));

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(result);
    }

    private TaskModel createTaskModel(TaskModel.Status status) {
        TaskModel task = new TaskModel();
        task.setStatus(status);
        task.setTaskType("test-task");
        task.setTaskId("123");
        task.setWorkflowInstanceId("workflow-123");
        task.setTaskDefName("test-task-def");
        task.setStartTime(System.currentTimeMillis());
        task.setEndTime(System.currentTimeMillis());
        task.setWorkflowPriority(0);
        return task;
    }

    private WorkflowModel createWorkflowModel(WorkflowModel.Status status) {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(status);
        workflow.setWorkflowId("workflow-123");
        return workflow;
    }
}
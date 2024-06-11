import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.models.TaskModel;
import com.netflix.conductor.core.models.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.ArgumentMatchers.any;
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
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ConductorProperties conductorProperties = new ConductorProperties();
        conductorProperties.getSystemTaskWorkerCallbackDuration().setSeconds(10L);
        conductorProperties.getTaskExecutionPostponeDuration().setSeconds(5L);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void execute_taskDoesNotExist_logErrorAndReturn() {
        String taskId = "123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(LOGGER).error(anyString(), anyString(), any());
        verifyNoMoreInteractions(systemTask, queueDAO, executionDAOFacade);
    }

    @Test
    public void execute_taskIsTerminal_removeFromQueue() {
        String taskId = "123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = createTaskModel(taskId, TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(anyString(), anyString());
        verifyNoMoreInteractions(systemTask, executionDAOFacade);
    }

    @Test
    public void execute_taskExceedsInProgressLimit_postponeQuietly() {
        String taskId = "123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = createTaskModel(taskId, TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
        verifyNoMoreInteractions(systemTask, executionDAOFacade);
    }

    @Test
    public void execute_taskExceedsRateLimit_postponeQuietly() {
        String taskId = "123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = createTaskModel(taskId, TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
        verifyNoMoreInteractions(systemTask, executionDAOFacade);
    }

    @Test
    public void execute_workflowIsCanceled_updateTaskAndDecideWorkflow() {
        String taskId = "123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = createTaskModel(taskId, TaskModel.Status.CANCELED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).updateTask(task);
        verify(workflowExecutor).decide(task.getWorkflowInstanceId());
    }

    @Test
    public void execute_workflowIsCompleted_updateTaskAndRemoveFromQueue() {
        String taskId = "123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = createTaskModel(taskId, TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void execute_systemTaskIsAsyncCompleteAndTaskIsNotScheduled_removeFromQueue() {
        String taskId = "123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = createTaskModel(taskId, TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(anyString(), anyString());
        verifyNoMoreInteractions(systemTask, executionDAOFacade);
    }

    @Test
    public void execute_exceptionOccurs_logError() {
        String taskId = "123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = createTaskModel(taskId, TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        doThrow(new RuntimeException("test exception")).when(systemTask).execute(any(), any(), any());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(LOGGER).error(anyString(), anyString(), any());
        verifyNoMoreInteractions(systemTask, executionDAOFacade);
    }

    @Test
    public void postponeQuietly_exceptionOccurs_logError() {
        String queueName = "testQueue";
        String taskId = "123";
        TaskModel task = createTaskModel(taskId, TaskModel.Status.SCHEDULED);

        doThrow(new RuntimeException("test exception")).when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(LOGGER).error(anyString(), anyString(), any());
        verifyNoMoreInteractions(queueDAO);
    }

    @Test
    public void loadTaskQuietly_exceptionOccurs_returnNull() {
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException("test exception"));

        TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verifyNoMoreInteractions(executionDAOFacade);
        assertThat(task, is(nullValue()));
    }

    private TaskModel createTaskModel(String taskId, TaskModel.Status status) {
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(status);
        task.setWorkflowInstanceId("1");
        task.setTaskDefName("testTaskDef");
        task.setWorkflowPriority(0);
        task.setRateLimitPerFrequency(0);
        return task;
    }
}
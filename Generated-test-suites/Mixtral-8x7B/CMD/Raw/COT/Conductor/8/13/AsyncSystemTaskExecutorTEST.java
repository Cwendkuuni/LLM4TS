import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
        asyncSystemTaskExecutor =
                new AsyncSystemTaskExecutor(
                        executionDAOFacade, queueDAO, metadataDAO, new ConductorProperties(), workflowExecutor);
    }

    @Test
    public void testExecute_TaskNotFound() {
        String taskId = "task-123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(0)).remove(anyString(), anyString());
        verify(systemTask, times(1)).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verifyNoMoreInteractions(executionDAOFacade);
    }

    @Test
    public void testExecute_TaskCompleted() {
        String taskId = "task-123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verifyNoMoreInteractions(executionDAOFacade);
    }

    @Test
    public void testExecute_TaskTerminal() {
        String taskId = "task-123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.FAILED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verifyNoMoreInteractions(executionDAOFacade);
    }

    @Test
    public void testExecute_TaskScheduled() {
        String taskId = "task-123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(false);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(0)).remove(anyString(), anyString());
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
        verifyNoMoreInteractions(executionDAOFacade);
    }

    @Test
    public void testExecute_TaskInProgress() {
        String taskId = "task-123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(0)).remove(anyString(), anyString());
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
        verifyNoMoreInteractions(executionDAOFacade);
    }

    @Test
    public void testExecute_TaskAsyncComplete() {
        String taskId = "task-123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setEndTime(System.currentTimeMillis());

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verifyNoMoreInteractions(executionDAOFacade);
    }

    @Test
    public void testExecute_TaskAsyncComplete_TaskNotScheduled() {
        String taskId = "task-123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setEndTime(System.currentTimeMillis());
        task.setTaskType("asyncComplete");

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(0)).remove(anyString(), anyString());
        verifyNoMoreInteractions(executionDAOFacade);
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queue-123";
        TaskModel task = new TaskModel();

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verifyNoMoreInteractions(queueDAO);
    }

    @Test
    public void testLoadTaskQuietly_TaskFound() {
        String taskId = "task-123";

        TaskModel task = new TaskModel();
        task.setTaskId(taskId);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(task, result);
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
    }

    @Test
    public void testLoadTaskQuietly_TaskNotFound() {
        String taskId = "task-123";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(result);
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
    }
}
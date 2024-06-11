import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
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

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private WorkflowSystemTask workflowSystemTask;
    private TaskModel taskModel;
    private WorkflowModel workflowModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        workflowSystemTask = mock(WorkflowSystemTask.class);
        taskModel = new TaskModel();
        workflowModel = new WorkflowModel();
    }

    @Test
    public void testExecute() {
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(workflowSystemTask, times(1)).start(workflowModel, taskModel, workflowExecutor);
        verify(workflowSystemTask, times(1)).execute(workflowModel, taskModel, workflowExecutor);
        verify(executionDAOFacade, times(1)).updateTask(taskModel);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_taskNotFound() {
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(workflowSystemTask, never()).start(workflowModel, taskModel, workflowExecutor);
        verify(workflowSystemTask, never()).execute(workflowModel, taskModel, workflowExecutor);
        verify(executionDAOFacade, never()).updateTask(taskModel);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_terminalTask() {
        String taskId = "123";
        taskModel.setStatus(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(workflowSystemTask, never()).start(workflowModel, taskModel, workflowExecutor);
        verify(workflowSystemTask, never()).execute(workflowModel, taskModel, workflowExecutor);
        verify(executionDAOFacade, never()).updateTask(taskModel);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_taskInProgressLimit() {
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(workflowSystemTask, never()).start(workflowModel, taskModel, workflowExecutor);
        verify(workflowSystemTask, never()).execute(workflowModel, taskModel, workflowExecutor);
        verify(executionDAOFacade, never()).updateTask(taskModel);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_taskRateLimitPerFrequency() {
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(taskModel,
                new ConcurrentHashMap<>())).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(workflowSystemTask, never()).start(workflowModel, taskModel, workflowExecutor);
        verify(workflowSystemTask, never()).execute(workflowModel, taskModel, workflowExecutor);
        verify(executionDAOFacade, never()).updateTask(taskModel);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_workflowCompleted() {
        String taskId = "123";
        taskModel.setWorkflowInstanceId("workflow-1");
        workflowModel.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(workflowSystemTask, never()).start(workflowModel, taskModel, workflowExecutor);
        verify(workflowSystemTask, never()).execute(workflowModel, taskModel, workflowExecutor);
        verify(executionDAOFacade, times(1)).updateTask(taskModel);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_workflowInProgress() {
        String taskId = "123";
        taskModel.setWorkflowInstanceId("workflow-1");
        workflowModel.setStatus(WorkflowModel.WorkflowStatus.RUNNING);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(workflowSystemTask, times(1)).start(workflowModel, taskModel, workflowExecutor);
        verify(workflowSystemTask, times(1)).execute(workflowModel, taskModel, workflowExecutor);
        verify(executionDAOFacade, times(1)).updateTask(taskModel);
        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queue-1";
        String taskId = "123";

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        verify(queueDAO, times(1)).postpone(queueName, taskId,
                taskModel.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietly_taskFound() {
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        assertSame(taskModel, result);
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() {
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        assertNull(result);
    }
}
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAOImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    private WorkflowExecutor workflowExecutor;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ConductorProperties conductorProperties = new ConductorProperties();
        conductorProperties.setSystemTaskWorkerCallbackDuration(30);
        conductorProperties.setTaskExecutionPostponeDuration(10);

        workflowExecutor = new WorkflowExecutor(Executors.newSingleThreadExecutor(), null, null);

        asyncSystemTaskExecutor =
                new AsyncSystemTaskExecutor(
                        executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "12345";

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType("TestTaskType");
        taskModel.setWorkflowInstanceId("TestWorkflowInstanceId");

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask, times(1)).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(systemTask, times(1)).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
    }

    @Test
    public void testExecute_taskNotFound() {
        String taskId = "12345";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_completedTask() {
        String taskId = "12345";

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType("TestTaskType");
        taskModel.setWorkflowInstanceId("TestWorkflowInstanceId");
        taskModel.setStatus(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_scheduledTaskWithConcurrentExecutionLimit() {
        String taskId = "12345";

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType("TestTaskType");
        taskModel.setWorkflowInstanceId("TestWorkflowInstanceId");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testExecute_scheduledTaskWithRateLimit() {
        String taskId = "12345";

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType("TestTaskType");
        taskModel.setWorkflowInstanceId("TestWorkflowInstanceId");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setRateLimitPerFrequency(10);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(
                taskModel, new ConcurrentHashMap<>())).thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testExecute_exception() {
        String taskId = "12345";

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType("TestTaskType");
        taskModel.setWorkflowInstanceId("TestWorkflowInstanceId");
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        doThrow(new RuntimeException("Test exception")).when(systemTask).execute(any(), any(), any());

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(executionDAOFacade, times(1)).updateTask(taskModel);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(workflowExecutor, times(1)).decide(anyString());
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "TestQueue";
        String taskId = "12345";

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType("TestTaskType");
        taskModel.setWorkflowInstanceId("TestWorkflowInstanceId");
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        verify(queueDAO, times(1)).postpone(queueName, taskId, taskModel.getWorkflowPriority(), 10);
    }

    @Test
    public void testLoadTaskQuietly_taskFound() {
        String taskId = "12345";

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType("TestTaskType");
        taskModel.setWorkflowInstanceId("TestWorkflowInstanceId");
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        assertSame(taskModel, result);
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() {
        String taskId = "12345";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        assertNull(result);
    }
}
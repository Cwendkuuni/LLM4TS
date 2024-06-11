import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.TaskModel;
import com.netflix.conductor.dao.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        when(metadataDAO.getTaskDef(anyString())).thenReturn(new WorkflowModel());
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(new WorkflowModel());

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        asyncSystemTaskExecutor =
                new AsyncSystemTaskExecutor(
                        executionDAOFacade, queueDAO, metadataDAO, new ConductorProperties(), workflowExecutor);
        asyncSystemTaskExecutor.setExecutor(executorService);
    }

    @Test
    public void testExecuteAsyncSystemTaskExecutor() {
        WorkflowSystemTask systemTask = new WorkflowSystemTask() {};
        String taskId = "123";

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).postpone(
                anyString(), anyString(), anyInt(), anyLong());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(1)).decide(anyString());
    }

    @Test
    public void testExecuteAsyncSystemTaskExecutorWhenTaskNotFound() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        WorkflowSystemTask systemTask = new WorkflowSystemTask() {};
        String taskId = "123";

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(0)).postpone(
                anyString(), anyString(), anyInt(), anyLong());
        verify(executionDAOFacade, times(0)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(0)).decide(anyString());
    }

    @Test
    public void testExecuteAsyncSystemTaskExecutorWhenTaskAlreadyCompleted() {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);

        WorkflowSystemTask systemTask = new WorkflowSystemTask() {};
        String taskId = "123";

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(0)).decide(anyString());
    }

    @Test
    public void testExecuteAsyncSystemTaskExecutorWhenTaskExceedsInProgressLimit() {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setTaskDefName("test-task");
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class)))
                .thenReturn(true);

        WorkflowSystemTask systemTask = new WorkflowSystemTask() {};
        String taskId = "123";

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).postpone(
                anyString(), anyString(), anyInt(), anyLong());
        verify(executionDAOFacade, times(0)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(0)).decide(anyString());
    }

    @Test
    public void testExecuteAsyncSystemTaskExecutorWhenTaskExceedsRateLimitPerFrequency() {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setTaskDefName("test-task");
        taskModel.setRateLimitPerFrequency(1);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(
                any(TaskModel.class), any(WorkflowModel.class)))
                .thenReturn(true);

        WorkflowSystemTask systemTask = new WorkflowSystemTask() {};
        String taskId = "123";

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).postpone(
                anyString(), anyString(), anyInt(), anyLong());
        verify(executionDAOFacade, times(0)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(0)).decide(anyString());
    }

    @Test
    public void testExecuteAsyncSystemTaskExecutorWhenWorkflowCompleted() {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setWorkflowInstanceId("123");
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflowModel);

        WorkflowSystemTask systemTask = new WorkflowSystemTask() {};
        String taskId = "123";

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(1)).decide(anyString());
    }

    @Test
    public void testExecuteAsyncSystemTaskExecutorWhenWorkflowNotCompleted() {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setWorkflowInstanceId("123");
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.WorkflowStatus.RUNNING);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflowModel);

        WorkflowSystemTask systemTask = new WorkflowSystemTask() {};
        String taskId = "123";

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).postpone(
                anyString(), anyString(), anyInt(), anyLong());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(0)).decide(anyString());
    }

    @Test
    public void testExecuteAsyncSystemTaskExecutorWhenWorkflowNotExist() {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setWorkflowInstanceId("123");
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenThrow(new RuntimeException());

        WorkflowSystemTask systemTask = new WorkflowSystemTask() {};
        String taskId = "123";

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(0)).postpone(
                anyString(), anyString(), anyInt(), anyLong());
        verify(executionDAOFacade, times(0)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(0)).decide(anyString());
    }

    @Test
    public void testExecuteAsyncSystemTaskExecutorWhenTaskExecutionCompleted() {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setWorkflowInstanceId("123");
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(new WorkflowModel());

        WorkflowSystemTask systemTask = new WorkflowSystemTask() {
            @Override
            public boolean isAsyncComplete(TaskModel taskModel) {
                return true;
            }
        };
        String taskId = "123";

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(1)).decide(anyString());
    }

    @Test
    public void testPostponeQuietly() {
        asyncSystemTaskExecutor.postponeQuietly("test-queue", new TaskModel());

        verify(queueDAO, times(1)).postpone(
                anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testPostponeQuietlyWhenExceptionOccurs() {
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong()))
                .thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.postponeQuietly("test-queue", new TaskModel());

        verify(queueDAO, times(1)).postpone(
                anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly() {
        when(executionDAOFacade.getTaskModel(anyString()))
                .thenReturn(new TaskModel());

        asyncSystemTaskExecutor.loadTaskQuietly("test-task-id");

        verify(executionDAOFacade, times(1)).getTaskModel(anyString());
    }

    @Test
    public void testLoadTaskQuietlyWhenExceptionOccurs() {
        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.loadTaskQuietly("test-task-id");

        verify(executionDAOFacade, times(1)).getTaskModel(anyString());
    }
}
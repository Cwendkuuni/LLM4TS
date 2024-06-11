import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        String taskId = UUID.randomUUID().toString();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(executionDAOFacade, times(1)).updateTask(task);
        verify(workflowExecutor, times(1)).decide(anyString());
    }

    @Test
    public void testExecuteTaskAsyncComplete() {
        String taskId = UUID.randomUUID().toString();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(executionDAOFacade, times(1)).updateTask(task);
        verify(workflowExecutor, times(1)).decide(anyString());
    }

    @Test
    public void testExecuteTaskTerminalStatus() {
        String taskId = UUID.randomUUID().toString();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(systemTask.isAsyncComplete(task)).thenReturn(false);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(executionDAOFacade, times(1)).updateTask(task);
        verify(workflowExecutor, times(1)).decide(anyString());
    }

    @Test
    public void testExecuteTaskRateLimitPerFrequencyExceeded() {
        String taskId = UUID.randomUUID().toString();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setRateLimitPerFrequency(1);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(metadataDAO.getTaskDef(anyString()))
                .thenReturn(new com.netflix.conductor.dao.TaskDef());
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(), any()))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testExecuteTaskInProgressLimitExceeded() {
        String taskId = UUID.randomUUID().toString();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testExecuteTaskCallbackTimeExpired() {
        String taskId = UUID.randomUUID().toString();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(systemTask.isAsyncComplete(task)).thenReturn(false);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflow);
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(executionDAOFacade, times(1)).updateTask(task);
        verify(workflowExecutor, times(1)).decide(anyString());
    }

    @Test
    public void testExecuteTaskException() {
        String taskId = UUID.randomUUID().toString();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(systemTask.isAsyncComplete(task)).thenReturn(false);
        doThrow(new RuntimeException()).when(systemTask).execute(any(), any(), any());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(executionDAOFacade, times(1)).updateTask(task);
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queueName";
        TaskModel task = new TaskModel();
        task.setTaskId("taskId");
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
        verify(queueDAO, times(1)).postpone(queueName, "taskId", task.getWorkflowPriority(), 1);
    }

    @Test
    public void testPostponeQuietlyException() {
        String queueName = "queueName";
        TaskModel task = new TaskModel();
        task.setTaskId("taskId");
        doThrow(new RuntimeException()).when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyInt());
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
        verify(queueDAO, times(1)).postpone(queueName, "taskId", task.getWorkflowPriority(), 1);
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(task, result);
    }

    @Test
    public void testLoadTaskQuietlyException() {
        String taskId = UUID.randomUUID().toString();
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(null, result);
    }
}
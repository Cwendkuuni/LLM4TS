import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.test.TestWorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskModel;
import com.netflix.conductor.dao.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        workflowSystemTask = new TestWorkflowSystemTask();
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                new ConductorProperties(),
                workflowExecutor
        );
    }

    @Test
    public void testExecuteWithNullSystemTask() {
        String taskId = "123";
        asyncSystemTaskExecutor.execute(null, taskId);
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteWithNullTask() {
        String taskId = "123";
        asyncSystemTaskExecutor.execute(workflowSystemTask, null);
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteWithTaskNotFound() {
        String taskId = "123";
        TaskModel task = null;
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteWithTerminalTask() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteWithScheduledTask() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(TaskModel.class), anyString()))
                .thenReturn(false);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteWithInProgressTask() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteWithTaskCompletion() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setWorkflowInstanceId("workflowId");
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(TaskModel.class), anyString()))
                .thenReturn(false);
        doReturn(new WorkflowModel())
                .when(executionDAOFacade)
                .getWorkflowModel(anyString(), anyBoolean());

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteWithTaskError() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setWorkflowInstanceId("workflowId");
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(TaskModel.class), anyString()))
                .thenReturn(false);

        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenThrow(new RuntimeException("Test Exception"));

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteWithTaskPostponement() {
        String taskId = "123";
        String queueName = "testQueue";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setWorkflowInstanceId("workflowId");
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(true);
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong()))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).postpone(queueName, taskId, anyInt(), anyLong());
    }

    @Test
    public void testExecuteWithTaskRemoval() {
        String taskId = "123";
        String queueName = "testQueue";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setWorkflowInstanceId("workflowId");
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(false);
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong()))
                .thenReturn(false);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).remove(queueName, taskId);
    }

    @Test
    public void testExecuteWithTaskAndQueueRemoval() {
        String taskId = "123";
        String queueName = "testQueue";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setWorkflowInstanceId("workflowId");
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(false);
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong()))
                .thenReturn(false);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).remove(queueName, taskId);
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "testQueue";
        TaskModel task = new TaskModel();
        task.setTaskId("123");
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
        verify(queueDAO).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietlyWithTask() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        assertEquals(task, result);
    }

    @Test
    public void testLoadTaskQuietlyWithoutTask() {
        String taskId = "123";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        assertNull(result);
    }
}
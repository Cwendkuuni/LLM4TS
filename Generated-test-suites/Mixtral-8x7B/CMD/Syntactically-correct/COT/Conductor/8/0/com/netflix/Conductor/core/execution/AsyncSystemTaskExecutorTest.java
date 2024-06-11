import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Component
public class AsyncSystemTaskExecutorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutor.class);

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
    public void setup() {
        MockitoAnnotations.initMocks(this);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                mock(ConductorProperties.class),
                workflowExecutor
        );
    }

    @Test
    public void testExecute_TaskNotFound() {
        String taskId = "123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(LOGGER, times(1)).error(any(), any());
        verify(queueDAO, times(0)).remove(any(), any());
    }

    @Test
    public void testExecute_TaskCompleted() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setTaskType("TestTask");
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(1)).remove(any(), eq(taskId));
    }

    @Test
    public void testExecute_TaskTerminalState() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setTaskType("TestTask");
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.FAILED);
        WorkflowModel workflow = new WorkflowModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), true))
                .thenReturn(workflow);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(1)).remove(any(), eq(taskId));
    }

    @Test
    public void testExecute_TaskScheduledExceedsInProgressLimit() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setTaskType("TestTask");
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setRateLimitPerFrequency(1);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        doNothing().when(queueDAO).postpone(any(), eq(taskId), any(), any());

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(1)).postpone(any(), eq(taskId), any(), any());
    }

    @Test
    public void testExecute_TaskScheduledExceedsRateLimitPerFrequency() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setTaskType("TestTask");
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setRateLimitPerFrequency(1);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, mock(TaskModel.class)))
                .thenReturn(true);
        doNothing().when(queueDAO).postpone(any(), eq(taskId), any(), any());

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(1)).postpone(any(), eq(taskId), any(), any());
    }

    @Test
    public void testExecute_TaskCompletedNormally() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setTaskType("TestTask");
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setStartTime(System.currentTimeMillis() - 1000);
        WorkflowModel workflow = new WorkflowModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), true))
                .thenReturn(workflow);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(1)).remove(any(), eq(taskId));
        verify(workflowExecutor, times(1)).decide(eq(workflow.getInstanceId()));
    }

    @Test
    public void testExecute_TaskExecutionFails() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setTaskType("TestTask");
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        doThrow(new RuntimeException("Test exception")).when(executionDAOFacade)
                .updateTask(task);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(0)).remove(any(), eq(taskId));
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "TestQueue";
        TaskModel task = new TaskModel();
        task.setTaskId("123");
        when(queueDAO.postpone(eq(queueName), eq(task.getTaskId()), eq(task.getWorkflowPriority()), any()))
                .thenThrow(new RuntimeException("Test exception"));

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO, times(1)).postpone(eq(queueName), eq(task.getTaskId()), eq(task.getWorkflowPriority()), any());
    }

    @Test
    public void testLoadTaskQuietly_TaskExists() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(task, loadedTask);
    }

    @Test
    public void testLoadTaskQuietly_TaskDoesNotExist() {
        String taskId = "123";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(loadedTask);
    }
}
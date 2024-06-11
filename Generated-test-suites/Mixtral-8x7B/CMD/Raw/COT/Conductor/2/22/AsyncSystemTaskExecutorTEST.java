import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.TaskModel;
import com.netflix.conductor.dao.WorkflowModel;
import com.netflix.conductor.model.ConductorProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ConductorProperties conductorProperties = new ConductorProperties();
        conductorProperties.setSystemTaskWorkerCallbackDuration(Duration.ofSeconds(10));
        conductorProperties.setTaskExecutionPostponeDuration(Duration.ofSeconds(5));

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor
        );
    }

    @Test
    public void testExecute_TaskNotFound() {
        String taskId = "task-1";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
        verifyNoMoreInteractions(systemTask);
    }

    @Test
    public void testExecute_TaskInTerminalState() {
        String taskId = "task-1";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
        verifyNoMoreInteractions(systemTask);
    }

    @Test
    public void testExecute_TaskExceedsInProgressLimit() {
        String taskId = "task-1";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("test-task");
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
        verifyNoMoreInteractions(systemTask);
    }

    @Test
    public void testExecute_TaskExceedsRateLimitPerFrequency() {
        String taskId = "task-1";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("test-task");
        task.setRateLimitPerFrequency(2);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
        verifyNoMoreInteractions(systemTask);
    }

    @Test
    public void testExecute_WorkflowCompleted() {
        String workflowId = "workflow-1";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setWorkflowInstanceId(workflowId);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.COMPLETED);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, "irrelevant-task-id");

        verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
        verifyNoMoreInteractions(systemTask);
    }

    @Test
    public void testExecute_TaskNotScheduled() {
        String taskId = "task-1";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.CANCELED);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
        verifyNoMoreInteractions(systemTask);
    }

    @Test
    public void testExecute_TaskScheduled() {
        String taskId = "task-1";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("test-task");
        task.setStartTime(1000L);
        task.setPriority(10);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyInt()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
        verifyNoMoreInteractions(systemTask);
    }

    @Test
    public void testExecute_Exception() {
        String taskId = "task-1";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setTaskDefName("test-task");
        task.setWorkflowInstanceId("workflow-1");
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        doThrow(new RuntimeException("test exception")).when(systemTask).execute(any(), any(), any());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO);
        verify(systemTask, times(1)).execute(any(), any(), any());
        verify(workflowExecutor, times(1)).decide("workflow-1");
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "test-queue";
        TaskModel task = new TaskModel();
        task.setTaskId("task-1");
        task.setPriority(10);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO, times(1)).postpone(queueName, "task-1", 10, 5);
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
    }

    @Test
    public void testLoadTaskQuietly_TaskFound() {
        String taskId = "task-1";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(task, loadedTask);
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
    }

    @Test
    public void testLoadTaskQuietly_TaskNotFound() {
        String taskId = "task-1";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(loadedTask);
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
    }
}
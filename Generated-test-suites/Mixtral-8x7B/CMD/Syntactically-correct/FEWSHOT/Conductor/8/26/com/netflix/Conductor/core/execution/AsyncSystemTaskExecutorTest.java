package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    private QueueUtils queueUtils;

    @Mock
    private Monitors monitors;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private TaskModel task;
    private WorkflowModel workflow;
    private WorkflowSystemTask systemTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        task = new TaskModel();
        workflow = new WorkflowModel();
        systemTask = mock(WorkflowSystemTask.class);

        when(systemTask.isTaskRetrievalRequired()).thenReturn(true);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);
        when(queueUtils.getQueueName(task)).thenReturn("test-queue");
    }

    @Test
    public void testExecute_TaskNotFound() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, "task-id");

        verify(executionDAOFacade, times(1)).getTaskModel("task-id");
        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_TaskAlreadyCompleted() {
        task.setStatus(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, "task-id");

        verify(executionDAOFacade, times(1)).getTaskModel("task-id");
        verify(queueDAO, times(1)).remove("test-queue", "task-id");
    }

    @Test
    public void testExecute_TaskInTerminalState() {
        task.setStatus(TaskModel.Status.FAILED);
        workflow.setStatus(WorkflowModel.WorkflowStatus.FAILED);

        asyncSystemTaskExecutor.execute(systemTask, "task-id");

        verify(executionDAOFacade, times(1)).getTaskModel("task-id");
        verify(executionDAOFacade, times(1)).getWorkflowModel("workflow-id", true);
        verify(queueDAO, times(1)).remove("test-queue", "task-id");
    }

    @Test
    public void testExecute_TaskScheduledWithConcurrentExecutionLimit() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("test-task");
        task.setConcurrentExecutionLimit(1);
        workflow.setStatus(WorkflowModel.WorkflowStatus.RUNNING);

        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, "task-id");

        verify(executionDAOFacade, times(1)).getTaskModel("task-id");
        verify(executionDAOFacade, times(1)).exceedsInProgressLimit(task);
        verify(queueDAO, times(1)).postpone("test-queue", "task-id", 0, 60);
    }

    @Test
    public void testExecute_TaskScheduledWithRateLimitPerFrequency() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setRateLimitPerFrequency(1);

        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(new Object());
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, new Object()))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, "task-id");

        verify(executionDAOFacade, times(1)).getTaskModel("task-id");
        verify(executionDAOFacade, times(1))
                .exceedsRateLimitPerFrequency(task, new Object());
        verify(queueDAO, times(1)).postpone("test-queue", "task-id", 0, 60);
    }

    @Test
    public void testExecute_TaskInProgress() {
        task.setStatus(TaskModel.Status.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(systemTask, "task-id");

        verify(executionDAOFacade, times(1)).getTaskModel("task-id");
        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_TaskAsyncComplete() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setStartTime(System.currentTimeMillis() - 61000);

        asyncSystemTaskExecutor.execute(systemTask, "task-id");

        verify(executionDAOFacade, times(1)).getTaskModel("task-id");
        verify(queueDAO, times(1)).remove("test-queue", "task-id");
    }

    @Test
    public void testExecute_TaskNotAsyncComplete() {
        task.setStatus(TaskModel.Status.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(systemTask, "task-id");

        verify(executionDAOFacade, times(1)).getTaskModel("task-id");
        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_TaskUpdated() {
        task.setStatus(TaskModel.Status.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, "task-id");

        verify(executionDAOFacade, times(1)).getTaskModel("task-id");
        verify(executionDAOFacade, times(1)).updateTask(task);
    }

    @Test
    public void testExecute_WorkflowCompleted() {
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        workflow.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, "task-id");

        verify(executionDAOFacade, times(1)).getTaskModel("task-id");
        verify(executionDAOFacade, times(1)).getWorkflowModel("workflow-id", true);
        verify(queueDAO, times(1)).remove("test-queue", "task-id");
    }

    @Test
    public void testPostponeQuietly() {
        asyncSystemTaskExecutor.postponeQuietly("test-queue", task);

        verify(queueDAO, times(1)).postpone(
                "test-queue",
                task.getTaskId(),
                task.getWorkflowPriority(),
                asyncSystemTaskExecutor.queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietly_TaskFound() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("task-id");

        assertEquals(task, result);
    }

    @Test
    public void testLoadTaskQuietly_TaskNotFound() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("task-id");

        assertNull(result);
    }
}
package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO;
import org.junit.Before;
import org.junit.Test;
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
    private WorkflowExecutor workflowExecutor;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        asyncSystemTaskExecutor =
                new AsyncSystemTaskExecutor(
                        executionDAOFacade,
                        queueDAO,
                        metadataDAO,
                        new ConductorProperties(),
                        workflowExecutor);
    }

    @Test
    public void testExecuteTaskModel() {
        TaskModel task = new TaskModel();
        task.setTaskId("123");

        when(executionDAOFacade.getTaskModel("123")).thenReturn(task);
        asyncSystemTaskExecutor.execute(new WorkflowSystemTask(), "123");

        verify(executionDAOFacade, times(1)).getTaskModel("123");
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteTaskModelWithTerminalStatus() {
        TaskModel task = new TaskModel();
        task.setTaskId("123");
        task.setStatus(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel("123")).thenReturn(task);
        asyncSystemTaskExecutor.execute(new WorkflowSystemTask(), "123");

        verify(executionDAOFacade, times(1)).getTaskModel("123");
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteTaskModelWithNull() {
        when(executionDAOFacade.getTaskModel("123")).thenReturn(null);
        asyncSystemTaskExecutor.execute(new WorkflowSystemTask(), "123");

        verify(executionDAOFacade, times(1)).getTaskModel("123");
        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteTaskModelWithRateLimitPerFrequency() {
        TaskModel task = new TaskModel();
        task.setTaskId("123");
        task.setRateLimitPerFrequency(10);

        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, new TaskModel())).thenReturn(true);
        when(executionDAOFacade.getTaskModel("123")).thenReturn(task);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask(), "123");

        verify(executionDAOFacade, times(1)).exceedsRateLimitPerFrequency(task, new TaskModel());
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExecuteTaskModelWithInProgressLimit() {
        TaskModel task = new TaskModel();
        task.setTaskId("123");

        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        when(executionDAOFacade.getTaskModel("123")).thenReturn(task);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask(), "123");

        verify(executionDAOFacade, times(1)).exceedsInProgressLimit(task);
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExecuteTaskModelWithWorkflowCompleted() {
        TaskModel task = new TaskModel();
        task.setTaskId("123");
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);

        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflow);
        when(executionDAOFacade.getTaskModel("123")).thenReturn(task);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask(), "123");

        verify(executionDAOFacade, times(1)).getWorkflowModel(anyString(), anyBoolean());
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteTaskModelWithWorkflowNotCompleted() {
        TaskModel task = new TaskModel();
        task.setTaskId("123");
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.WorkflowStatus.STARTED);

        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflow);
        when(executionDAOFacade.getTaskModel("123")).thenReturn(task);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask(), "123");

        verify(executionDAOFacade, times(1)).getWorkflowModel(anyString(), anyBoolean());
        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteTaskModelWithTaskAsyncComplete() {
        TaskModel task = new TaskModel();
        task.setTaskId("123");
        task.setStatus(TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel("123")).thenReturn(task);

        asyncSystemTaskExecutor.execute(
                new WorkflowSystemTask() {
                    @Override
                    public void execute(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {
                        task.setStatus(TaskModel.Status.COMPLETED);
                    }
                },
                "123");

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteTaskModelWithTaskNotAsyncComplete() {
        TaskModel task = new TaskModel();
        task.setTaskId("123");
        task.setStatus(TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel("123")).thenReturn(task);

        asyncSystemTaskExecutor.execute(
                new WorkflowSystemTask() {
                    @Override
                    public void execute(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {
                        task.setStatus(TaskModel.Status.IN_PROGRESS);
                    }
                },
                "123");

        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteTaskModelWithPostponeQueue() {
        TaskModel task = new TaskModel();
        task.setTaskId("123");
        task.setStatus(TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel("123")).thenReturn(task);

        asyncSystemTaskExecutor.execute(
                new WorkflowSystemTask() {
                    @Override
                    public void execute(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {
                        task.setStatus(TaskModel.Status.IN_PROGRESS);
                    }
                },
                "123");

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }
}
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

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

        ConductorProperties conductorProperties = new ConductorProperties();
        conductorProperties.getSystemTaskWorkerCallbackDuration().setSeconds(10);
        conductorProperties.getTaskExecutionPostponeDuration().setSeconds(5);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor
        );
    }

    @Test
    public void executeTestWhenTaskDoesNotExist() {
        String taskId = "task-1";
        TaskModel task = null;

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(0)).remove(anyString(), anyString());
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
    }

    @Test
    public void executeTestWhenTaskIsTerminal() {
        String taskId = "task-2";
        TaskModel task = createTask(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
    }

    @Test
    public void executeTestWhenTaskIsScheduledAndExceedsInProgressLimit() {
        String taskId = "task-3";
        TaskModel task = createTask(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(executionDAOFacade, times(1)).exceedsInProgressLimit(any());
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
    }

    @Test
    public void executeTestWhenTaskIsScheduledAndExceedsRateLimit() {
        String taskId = "task-4";
        TaskModel task = createTask(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(), any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(executionDAOFacade, times(1))
                .exceedsRateLimitPerFrequency(any(), any());
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
    }

    @Test
    public void executeTest() {
        String taskId = "task-5";
        TaskModel task = createTask(TaskModel.Status.IN_PROGRESS);

        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(executionDAOFacade, times(1)).getWorkflowModel(anyString(), anyBoolean());
        verify(queueDAO, times(0)).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(queueDAO, times(0)).remove(anyString(), anyString());
        verifyNoMoreInteractions(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
    }

    private TaskModel createTask(TaskModel.Status status) {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(status);
        taskModel.setTaskId("task-id");
        taskModel.setTaskType("task-type");
        taskModel.setWorkflowInstanceId("workflow-id");
        taskModel.setTaskDefName("task-def-name");
        taskModel.setWorkflowPriority(10);
        taskModel.setPollCount(5);
        return taskModel;
    }
}
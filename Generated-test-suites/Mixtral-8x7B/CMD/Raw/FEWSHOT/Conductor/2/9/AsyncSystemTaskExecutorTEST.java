package com.netflix.conductor.core.execution;

import static org.mockito.Mockito.*;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.core.utils.TestUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class AsyncSystemTaskExecutorTest {

    @Mock private ExecutionDAOFacade executionDAOFacade;
    @Mock private QueueDAO queueDAO;
    @Mock private MetadataDAO metadataDAO;
    @Mock private QueueUtils queueUtils;
    @Mock private WorkflowExecutor workflowExecutor;
    @Mock private Logger logger;

    @InjectMocks private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private TaskModel taskModel;
    private WorkflowModel workflowModel;
    private WorkflowSystemTask workflowSystemTask;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        logger = LoggerFactory.getLogger(AsyncSystemTaskExecutor.class);

        taskModel = TestUtils.createTaskModel();
        workflowModel = TestUtils.createWorkflowModel();
        workflowSystemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor =
                new AsyncSystemTaskExecutor(
                        executionDAOFacade,
                        queueDAO,
                        metadataDAO,
                        new ConductorProperties(),
                        workflowExecutor);
    }

    @Test
    public void testExecute_TaskNotFound() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(logger, times(1))
                .error(
                        "TaskId: taskId could not be found while executing {}", "taskId", workflowSystemTask);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_TaskTerminalStatus() {
        taskModel.setStatus(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_TaskScheduledStatus() {
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(executionDAOFacade.exceedsInProgressLimit(any())).thenReturn(true);
        when(executionDAOFacade.getWorkflowModel(any(), anyBoolean())).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(workflowSystemTask, times(1)).isAsyncComplete(any());
        verify(taskModel, times(1)).incrementPollCount();
        verify(queueDAO, times(1)).postpone(
                anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testExecute_TaskInProgressStatus() {
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(any(), anyBoolean())).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(workflowSystemTask, times(1)).start(any(), any(), any());
        verify(workflowSystemTask, times(1)).execute(any(), any(), any());
    }

    @Test
    public void testExecute_TaskCompleted() {
        taskModel.setStatus(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(any(), anyBoolean())).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_TaskError() {
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(any(), anyBoolean())).thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO, times(1)).postpone(
                anyString(), anyString(), anyInt(), anyInt());
    }
}
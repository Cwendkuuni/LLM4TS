package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.SystemTaskQueueDAO;
import com.netflix.conductor.core.events.SystemTaskEventPublisher;
import com.netflix.conductor.core.execution.tasks.PollTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.model.workflow.WorkflowDef;
import com.netflix.conductor.core.orchestration.WorkflowExecutor;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    @Mock
    private SystemTaskQueueDAO systemTaskQueueDAO;

    @Mock
    private SystemTaskEventPublisher systemTaskEventPublisher;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private static final String TASK_ID = "123";
    private static final String WORKFLOW_ID = "workflow-123";
    private static final String TASK_TYPE = "TASK_TYPE";
    private static final String TASK_DEF_NAME = "TASK_DEF_NAME";
    private static final String QUEUE_NAME = "QUEUE_NAME";
    private static final String WORKFLOW_DEF_NAME = "WORKFLOW_DEF_NAME";
    private static final String WORKFLOW_DEF_VERSION = "1";

    private TaskModel taskModel;
    private WorkflowModel workflowModel;
    private WorkflowDef workflowDef;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        taskModel = new TaskModel();
        taskModel.setTaskId(TASK_ID);
        taskModel.setWorkflowInstanceId(WORKFLOW_ID);
        taskModel.setTaskType(TASK_TYPE);
        taskModel.setTaskDefName(TASK_DEF_NAME);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);

        workflowDef = new WorkflowDef();
        workflowDef.setName(WORKFLOW_DEF_NAME);
        workflowDef.setVersion(WORKFLOW_DEF_VERSION);

        when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, true)).thenReturn(workflowModel);
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(taskModel, metadataDAO.getTaskDef(TASK_DEF_NAME)))
                .thenReturn(false);
        when(queueDAO.postpone(QUEUE_NAME, TASK_ID, 0, 10L)).thenReturn();
        when(queueDAO.remove(QUEUE_NAME, TASK_ID)).thenReturn();
        when(metadataDAO.getTaskDef(TASK_DEF_NAME)).thenReturn(new WorkflowDef());
    }

    @Test
    public void testExecute() {
        asyncSystemTaskExecutor.execute(new PollTask(), TASK_ID);

        verify(executionDAOFacade, times(1)).getWorkflowModel(WORKFLOW_ID, true);
        verify(executionDAOFacade, times(1)).exceedsInProgressLimit(taskModel);
        verify(executionDAOFacade, times(1)).exceedsRateLimitPerFrequency(taskModel, new WorkflowDef());
        verify(queueDAO, times(1)).postpone(QUEUE_NAME, TASK_ID, 0, 10L);
        verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecuteWithTerminalTaskStatus() {
        taskModel.setStatus(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(new PollTask(), TASK_ID);

        verify(executionDAOFacade, times(1)).getWorkflowModel(WORKFLOW_ID, true);
        verify(executionDAOFacade, times(1)).exceedsInProgressLimit(taskModel);
        verify(executionDAOFacade, times(1)).exceedsRateLimitPerFrequency(taskModel, new WorkflowDef());
        verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecuteWithRateLimit() {
        when(executionDAOFacade.exceedsRateLimitPerFrequency(taskModel, new WorkflowDef()))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(new PollTask(), TASK_ID);

        verify(executionDAOFacade, times(1)).getWorkflowModel(WORKFLOW_ID, true);
        verify(executionDAOFacade, times(1)).exceedsInProgressLimit(taskModel);
        verify(executionDAOFacade, times(1)).exceedsRateLimitPerFrequency(taskModel, new WorkflowDef());
        verify(queueDAO, times(1)).postpone(QUEUE_NAME, TASK_ID, 0, 10L);
    }

    @Test
    public void testExecuteWithInProgressLimit() {
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(new PollTask(), TASK_ID);

        verify(executionDAOFacade, times(1)).getWorkflowModel(WORKFLOW_ID, true);
        verify(executionDAOFacade, times(1)).exceedsInProgressLimit(taskModel);
        verify(executionDAOFacade, times(1)).exceedsRateLimitPerFrequency(taskModel, new WorkflowDef());
        verify(queueDAO, times(1)).postpone(QUEUE_NAME, TASK_ID, 0, 10L);
    }

    @Test
    public void testPostponeQuietly() {
        asyncSystemTaskExecutor.postponeQuietly(QUEUE_NAME, taskModel);

        verify(queueDAO, times(1)).postpone(QUEUE_NAME, TASK_ID, 0, 10L);
    }

    @Test
    public void testLoadTaskQuietly() {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(TASK_ID);

        assertEquals(loadedTask, taskModel);
    }

    @Test
    public void testLoadTaskQuietlyWithException() {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenThrow(new RuntimeException());

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(TASK_ID);

        assertEquals(loadedTask, null);
    }
}
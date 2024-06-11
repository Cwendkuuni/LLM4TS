package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AsyncSystemTaskExecutorTest {

    @InjectMocks private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock private ExecutionDAOFacade executionDAOFacade;
    @Mock private QueueDAO queueDAO;
    @Mock private MetadataDAO metadataDAO;
    @Mock private WorkflowExecutor workflowExecutor;
    @Mock private QueueUtils queueUtils;
    @Mock private Monitors monitors;

    private WorkflowSystemTask workflowSystemTask;
    private TaskModel taskModel;
    private WorkflowModel workflowModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        workflowSystemTask = mock(WorkflowSystemTask.class);
        taskModel = new TaskModel();
        workflowModel = new WorkflowModel();

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(queueUtils.getQueueName(taskModel)).thenReturn("testQueue");
    }

    @Test
    public void testExecute_TaskDoesNotExist() {
        asyncSystemTaskExecutor.execute(workflowSystemTask, "nonExistentTaskId");

        verify(queueDAO, times(1)).remove("testQueue", "nonExistentTaskId");
    }

    @Test
    public void testExecute_TaskIsTerminal() {
        taskModel.setStatus(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO, times(1)).remove("testQueue", "taskId");
    }

    @Test
    public void testExecute_TaskIsScheduledAndExceedsInProgressLimit() {
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setTaskType("testTaskType");
        taskModel.setTaskDefName("testTaskDefName");

        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO, times(1)).postpone(
                "testQueue", "taskId", taskModel.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_TaskIsScheduledAndExceedsRateLimit() {
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setTaskType("testTaskType");
        taskModel.setTaskDefName("testTaskDefName");
        taskModel.setRateLimitPerFrequency(1);

        when(executionDAOFacade.exceedsRateLimitPerFrequency(taskModel, any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO, times(1)).postpone(
                "testQueue", "taskId", taskModel.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_TaskIsInProgress() throws Exception {
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(workflowSystemTask, times(1)).execute(workflowModel, taskModel, workflowExecutor);
        verify(executionDAOFacade, times(1)).updateTask(taskModel);
        verify(queueDAO, times(1)).remove("testQueue", "taskId");
    }

    @Test
    public void testExecute_TaskIsAsyncComplete() {
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskType("asyncCompleteTaskType");

        when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO, times(1)).remove("testQueue", "taskId");
    }

    @Test
    public void testExecute_TaskIsNotAsyncComplete() {
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskType("notAsyncCompleteTaskType");

        when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(false);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO, times(1)).postpone(
                "testQueue", "taskId", taskModel.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_WorkflowIsCompleted() {
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        workflowModel.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(executionDAOFacade, times(1)).updateTask(taskModel);
        verify(queueDAO, times(1)).remove("testQueue", "taskId");
    }

    @Test
    public void testExecute_WorkflowIsFailed() {
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        workflowModel.setStatus(WorkflowModel.WorkflowStatus.FAILED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(executionDAOFacade, times(1)).updateTask(taskModel);
        verify(queueDAO, times(1)).remove("testQueue", "taskId");
    }

    // Add more test cases as needed for other scenarios
}
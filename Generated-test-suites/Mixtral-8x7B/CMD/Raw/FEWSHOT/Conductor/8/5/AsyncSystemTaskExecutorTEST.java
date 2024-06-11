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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Component
public class AsyncSystemTaskExecutorTest {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(AsyncSystemTaskExecutor.class);

    @Mock private ExecutionDAOFacade executionDAOFacade;
    @Mock private QueueDAO queueDAO;
    @Mock private MetadataDAO metadataDAO;
    @Mock private WorkflowExecutor workflowExecutor;
    @Mock private QueueDAO mockQueueDAO;
    @Mock private QueueUtils queueUtils;

    @InjectMocks private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecuteTerminalTask() {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.FAILED);
        String taskId = "123";

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, taskId);

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteNonTerminalTask() {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        String taskId = "123";

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, taskId);

        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExceedsInProgressLimit() {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("testTaskDefName");
        String taskId = "123";

        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, taskId);

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExceedsRateLimitPerFrequency() {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("testTaskDefName");
        task.setRateLimitPerFrequency(1);
        String taskId = "123";

        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, taskId);

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testWorkflowCompleted() {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setWorkflowInstanceId("testWorkflowInstanceId");
        String taskId = "123";
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);

        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflow);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, taskId);

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testWorkflowNotCompleted() {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setWorkflowInstanceId("testWorkflowInstanceId");
        String taskId = "123";
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.WorkflowStatus.RUNNING);

        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflow);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, taskId);

        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testIsTaskAsyncCompleteTrue() {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.isTaskAsyncComplete(taskId);

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testIsTaskAsyncCompleteFalse() {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.isTaskAsyncComplete(taskId);

        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "testQueueName";
        TaskModel task = new TaskModel();
        task.setTaskId("123");

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO, times(1)).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), 10);
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(new TaskModel());

        asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
    }

    @Test
    public void testLoadTaskQuietlyNull() {
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, never()).remove(anyString(), anyString());
    }
}
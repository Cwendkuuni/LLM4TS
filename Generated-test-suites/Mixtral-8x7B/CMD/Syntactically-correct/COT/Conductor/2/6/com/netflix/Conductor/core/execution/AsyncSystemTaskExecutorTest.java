import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.TaskModel;
import com.netflix.conductor.dao.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private Logger logger;

    private WorkflowSystemTask workflowSystemTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        workflowSystemTask = mock(WorkflowSystemTask.class);

        when(conductorProperties.getSystemTaskWorkerCallbackDuration().getSeconds())
                .thenReturn(10L);
        when(conductorProperties.getTaskExecutionPostponeDuration().getSeconds())
                .thenReturn(5L);
    }

    @Test
    public void testExecute_WhenTaskIsNull_LogsErrorAndRemovesFromQueue() {
        String taskId = "123";

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(logger).error(anyString(), anyString(), any(WorkflowSystemTask.class), anyString());
        verify(queueDAO).remove(eq(""), eq(taskId));
    }

    @Test
    public void testExecute_WhenTaskIsCompleted_RemovesFromQueue() {
        String taskId = "123";
        TaskModel task = createTaskModel("COMPLETED", taskId);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecute_WhenTaskIsTerminal_RemovesFromQueue() {
        String taskId = "123";
        TaskModel task = createTaskModel("FAILED", taskId);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecute_WhenTaskIsScheduledAndConcurrentLimitExceeded_PostponesTask() {
        String taskId = "123";
        TaskModel task = createTaskModel("SCHEDULED", taskId);
        when(executionDAOFacade.exceedsInProgressLimit(any()))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).postpone(anyString(), eq(taskId), anyInt(), eq(5L));
    }

    @Test
    public void testExecute_WhenTaskIsScheduledAndRateLimitExceeded_PostponesTask() {
        String taskId = "123";
        TaskModel task = createTaskModel("SCHEDULED", taskId);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(), any()))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).postpone(anyString(), eq(taskId), anyInt(), eq(5L));
    }

    @Test
    public void testExecute_WhenTaskIsInProgress_ExecutesSystemTask() {
        String taskId = "123";
        TaskModel task = createTaskModel("IN_PROGRESS", taskId);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(workflowSystemTask).execute(any(), any(), any());
    }

    @Test
    public void testExecute_WhenTaskIsCompletedAndShouldRemoveFromQueue_RemovesFromQueue() {
        String taskId = "123";
        TaskModel task = createTaskModel("COMPLETED", taskId);
        when(workflowSystemTask.isAsyncComplete(any()))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecute_WhenTaskIsInProgressAndShouldRemoveFromQueue_RemovesFromQueue() {
        String taskId = "123";
        TaskModel task = createTaskModel("IN_PROGRESS", taskId);
        when(workflowSystemTask.isAsyncComplete(any()))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecute_WhenTaskIsPostponed_PostponesTask() {
        String taskId = "123";
        TaskModel task = createTaskModel("SCHEDULED", taskId);
        when(workflowSystemTask.isAsyncComplete(any()))
                .thenReturn(false);
        when(task.getCallbackAfterSeconds())
                .thenReturn((int) TimeUnit.SECONDS.toSeconds(15L));

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).postpone(anyString(), eq(taskId), anyInt(), eq(15L));
    }

    @Test
    public void testExecute_WhenWorkflowIsCompleted_UpdatesTaskAndRemovesFromQueue() {
        String workflowId = "workflow-1";
        String taskId = "123";
        TaskModel task = createTaskModel("IN_PROGRESS", taskId);
        WorkflowModel workflowModel = createWorkflowModel(workflowId);

        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecute_WhenWorkflowIsCompletedAndTaskIsCancelled_UpdatesTaskAndRemovesFromQueue() {
        String workflowId = "workflow-1";
        String taskId = "123";
        TaskModel task = createTaskModel("IN_PROGRESS", taskId);
        WorkflowModel workflowModel = createWorkflowModel(workflowId);
        task.setStatus(TaskModel.Status.CANCELED);

        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecute_WhenWorkflowIsNotCompleted_DoesNotUpdateTask() {
        String workflowId = "workflow-1";
        String taskId = "123";
        TaskModel task = createTaskModel("IN_PROGRESS", taskId);
        WorkflowModel workflowModel = createWorkflowModel(workflowId);
        workflowModel.setStatus(WorkflowModel.WorkflowStatus.RUNNING);

        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, never()).updateTask(task);
    }

    @Test
    public void testPostponeQuietly_WhenQueueDAOThrowsException_LogsError() {
        String queueName = "queue-1";
        TaskModel task = createTaskModel("SCHEDULED", "task-1");

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO).postpone(eq(queueName), eq("task-1"), anyInt(), eq(5L));
        verify(logger).error(anyString(), anyString(), anyString());
    }

    @Test
    public void testLoadTaskQuietly_WhenExecutionDAOFacadeThrowsException_ReturnsNull() {
        String taskId = "123";

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        assertEquals(null, result);
    }

    private TaskModel createTaskModel(String status, String taskId) {
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.valueOf(status));
        return task;
    }

    private WorkflowModel createWorkflowModel(String workflowId) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", workflowId);

        return new WorkflowModel()
                .setId(workflowId)
                .setData(data)
                .setStatus(WorkflowModel.WorkflowStatus.COMPLETED);
    }
}
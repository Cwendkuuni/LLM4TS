import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.TestWorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.metrics.Monitors;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@Component
public class AsyncSystemTaskExecutorTest {

    private final Logger log = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);

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

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private WorkflowSystemTask workflowSystemTask;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        workflowSystemTask = new TestWorkflowSystemTask();
    }

    @Test
    public void testExecute() throws Exception {
        String taskId = "1";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).updateTask(taskModel);
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecuteWithTerminalTaskStatus() throws Exception {
        String taskId = "1";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).updateTask(taskModel);
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecuteWithTaskRemovedFromQueue() throws Exception {
        String taskId = "1";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).updateTask(taskModel);
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecuteWithTaskExecutionCompleted() throws Exception {
        String taskId = "1";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(taskModel.getWorkflowInstanceId(), true))
                .thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).updateTask(taskModel);
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecuteWithTaskPostponed() throws Exception {
        String taskId = "1";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, times(1)).postpone(
                anyString(), eq(taskId), eq(taskModel.getWorkflowPriority()), anyLong());
    }

    @Test
    public void testExecuteWithTaskPostponedAfterException() throws Exception {
        String taskId = "1";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        doThrow(new RuntimeException()).when(workflowSystemTask).execute(any(), any(), any());

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, times(1)).postpone(
                anyString(), eq(taskId), eq(taskModel.getWorkflowPriority()), anyLong());
    }

    @Test
    public void testExecuteWithTaskExecutionCompletedAndWorkflowNotCompleted() throws Exception {
        String taskId = "1";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.WorkflowStatus.STARTED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(taskModel.getWorkflowInstanceId(), true))
                .thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).updateTask(taskModel);
        verify(queueDAO, never()).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecuteWithTaskExecutionCompletedAndWorkflowCancelled() throws Exception {
        String taskId = "1";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.WorkflowStatus.CANCELED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(taskModel.getWorkflowInstanceId(), true))
                .thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).updateTask(taskModel);
        verify(queueDAO, never()).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecuteWithTaskExecutionCompletedAndWorkflowFailed() throws Exception {
        String taskId = "1";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.WorkflowStatus.FAILED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(taskModel.getWorkflowInstanceId(), true))
                .thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, times(1)).updateTask(taskModel);
        verify(queueDAO, never()).remove(anyString(), eq(taskId));
    }

    @Test
    public void testLoadTaskQuietlyWithTaskModel() throws Exception {
        String taskId = "1";

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(result, taskModel);
    }

    @Test
    public void testLoadTaskQuietlyWithNull() throws Exception {
        String taskId = "1";

        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new Exception());

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(result, null);
    }

    @Test
    public void testPostponeQuietlyWithPostponedTask() throws Exception {
        String queueName = "test-queue";
        String taskId = "1";
        int workflowPriority = 1;
        long postponeTime = 10;

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskId, workflowPriority, postponeTime);

        verify(queueDAO, times(1)).postpone(queueName, taskId, workflowPriority, postponeTime);
    }

    @Test
    public void testPostponeQuietlyWithException() throws Exception {
        String queueName = "test-queue";
        String taskId = "1";
        int workflowPriority = 1;
        long postponeTime = 10;

        doThrow(new Exception()).when(queueDAO).postpone(queueName, taskId, workflowPriority, postponeTime);

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskId, workflowPriority, postponeTime);

        verify(queueDAO, times(1)).postpone(queueName, taskId, workflowPriority, postponeTime);
    }
}

class TestWorkflowSystemTask implements WorkflowSystemTask {

    @Override
    public void execute(WorkflowModel workflowModel, TaskModel taskModel, WorkflowExecutor workflowExecutor) {
        log.info("Executing test system task for workflow: {}, task: {}", workflowModel, taskModel);

        if (taskModel.getStatus().equals(TaskModel.Status.SCHEDULED)) {
            taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        }
    }

    @Override
    public void start(WorkflowModel workflowModel, TaskModel taskModel, WorkflowExecutor workflowExecutor) {
        log.info("Starting test system task for workflow: {}, task: {}", workflowModel, taskModel);
    }

    @Override
    public boolean isAsyncComplete(TaskModel taskModel) {
        return false;
    }

    @Override
    public boolean isTaskRetrievalRequired() {
        return true;
    }

    @Override
    public Long getEvaluationOffset(TaskModel taskModel, long systemTaskCallbackTime) {
        return null;
    }
}
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.TestWorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@Component
public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private WorkflowSystemTask workflowSystemTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        workflowSystemTask = new TestWorkflowSystemTask();
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade, queueDAO, metadataDAO, new ConductorProperties(), null);
    }

    @Test
    public void execute_taskDoesNotExist_shouldLogErrorAndRemoveFromQueue() {
        String taskId = "NonExistingTaskId";
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade, never()).getTaskModel(taskId);
        verify(queueDAO).remove(eq(QueueUtils.getQueueName(any(TaskModel.class))), eq(taskId));
    }

    @Test
    public void execute_taskIsTerminal_shouldRemoveFromQueue() {
        String taskId = "TerminalTask";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verify(queueDAO).remove(eq(QueueUtils.getQueueName(any(TaskModel.class))), eq(taskId));
    }

    @Test
    public void execute_taskIsScheduledAndExceedsInProgressLimit_shouldPostpone() {
        String taskId = "OverLimitTask";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void execute_taskIsScheduledAndExceedsRateLimitPerFrequency_shouldPostpone() {
        String taskId = "RateLimitTask";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setRateLimitPerFrequency(10);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(TaskModel.class), anyString()))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void execute_taskIsScheduled_shouldStartSystemTask() {
        String taskId = "ScheduledTask";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);

        WorkflowModel workflowModel = new WorkflowModel();

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(workflowSystemTask).start(workflowModel, task, any());
    }

    @Test
    public void execute_taskIsInProgress_shouldExecuteSystemTask() {
        String taskId = "InProgressTask";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.IN_PROGRESS);

        WorkflowModel workflowModel = new WorkflowModel();

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(workflowSystemTask).execute(workflowModel, task, any());
    }

    @Test
    public void execute_taskIsAsyncComplete_shouldRemoveFromQueue() {
        String taskId = "AsyncCompleteTask";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        doReturn(true).when(workflowSystemTask).isAsyncComplete(task);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).remove(eq(QueueUtils.getQueueName(any(TaskModel.class))), eq(taskId));
    }

    @Test
    public void execute_taskIsTerminalAndHasNotCompleted_shouldUpdateTaskAndRemoveFromQueue() {
        String taskId = "TerminalTaskNotCompleted";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setEndTime(0);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO).remove(eq(QueueUtils.getQueueName(any(TaskModel.class))), eq(taskId));
    }

    @Test
    public void postponeQuietly_shouldPostponeTask() {
        String queueName = "SampleQueue";
        String taskId = "SampleTask";
        int priority = 1;
        long postponeSeconds = 10;

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskId, priority, postponeSeconds);

        verify(queueDAO).postpone(eq(queueName), eq(taskId), eq(priority), eq(postponeSeconds));
    }

    @Test
    public void loadTaskQuietly_taskExists_shouldReturnTaskModel() {
        String taskId = "SampleTask";

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(taskModel, result);
    }

    @Test
    public void loadTaskQuietly_taskDoesNotExist_shouldReturnNull() {
        String taskId = "SampleTask";

        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(null, result);
    }
}

class TestWorkflowSystemTask implements WorkflowSystemTask {

    @Override
    public void start(WorkflowModel workflowModel, TaskModel taskModel, ExecutorService executorService) {
    }

    @Override
    public void execute(WorkflowModel workflowModel, TaskModel taskModel, ExecutorService executorService) {
    }

    @Override
    public boolean isAsyncComplete(TaskModel taskModel) {
        return true;
    }

    @Override
    public EvaluationOffset getEvaluationOffset(TaskModel taskModel, long systemTaskCallbackTime) {
        return EvaluationOffset.of(0);
    }
}

class EvaluationOffset {

    public static EvaluationOffset of(int offset) {
        return new EvaluationOffset();
    }

    public void ifPresentOrElse(Runnable runnable, Runnable fallback) {
        runnable.run();
    }
}
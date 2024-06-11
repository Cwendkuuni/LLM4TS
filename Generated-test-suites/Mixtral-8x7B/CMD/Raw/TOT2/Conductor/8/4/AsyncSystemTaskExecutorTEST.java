import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
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
    private QueueUtils queueUtils;

    @Mock
    private Logger logger;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private WorkflowSystemTask workflowSystemTask;

    @Before
    public void setUp() {
        when(logger.isDebugEnabled()).thenReturn(true);
        when(logger.isInfoEnabled()).thenReturn(true);
        when(logger.isWarnEnabled()).thenReturn(true);
        when(logger.isErrorEnabled()).thenReturn(true);

        workflowSystemTask = mock(WorkflowSystemTask.class);

        ConcurrentHashMap<String, WorkflowModel> workflowModels = new ConcurrentHashMap<>();
        workflowModels.put("workflowId", new WorkflowModel());
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModels.get("workflowId"));

        when(queueUtils.getQueueName(any(TaskModel.class))).thenReturn("testQueue");
    }

    @Test
    public void testExecute_TypicalUseCase() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("taskId");
        taskModel.setWorkflowInstanceId("workflowId");

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

        verify(executionDAOFacade, times(1)).getTaskModel(taskModel.getTaskId());
        verify(queueDAO, times(1)).remove(eq("testQueue"), eq(taskModel.getTaskId()));
        verify(workflowExecutor, times(1)).decide(eq("workflowId"));
    }

    @Test
    public void testExecute_TaskNotFound() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(logger, times(1))
                .error("TaskId: taskId could not be found while executing {}", "taskId", workflowSystemTask);
        verify(queueDAO, times(0)).remove(anyString(), anyString());
        verify(workflowExecutor, times(0)).decide(anyString());
    }

    @Test
    public void testExecute_TaskAlreadyCompleted() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("taskId");
        taskModel.setStatus(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

        verify(queueDAO, times(1)).remove(eq("testQueue"), eq(taskModel.getTaskId()));
        verify(workflowExecutor, times(0)).decide(anyString());
    }

    @Test
    public void testExecute_TaskRateLimited() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("taskId");
        taskModel.setRateLimitPerFrequency(1);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(TaskModel.class), any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

        verify(logger, times(1))
                .warn("RateLimit Execution limited for taskId:taskDefName, limit:1");
        verify(queueDAO, times(1)).postpone(
                anyString(),
                anyString(),
                anyInt(),
                eq(asyncSystemTaskExecutor.queueTaskMessagePostponeSecs));
        verify(workflowExecutor, times(0)).decide(anyString());
    }

    @Test
    public void testExecute_TaskConcurrentExecutionLimited() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("taskId");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

        verify(logger, times(1))
                .warn("Concurrent Execution limited for taskId:taskDefName");
        verify(queueDAO, times(1)).postpone(
                anyString(),
                anyString(),
                anyInt(),
                eq(asyncSystemTaskExecutor.queueTaskMessagePostponeSecs));
        verify(workflowExecutor, times(0)).decide(anyString());
    }

    @Test
    public void testExecute_WorkflowCompleted() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("taskId");
        taskModel.setWorkflowInstanceId("workflowId");
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);

        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

        verify(executionDAOFacade, times(1)).getTaskModel(taskModel.getTaskId());
        verify(queueDAO, times(1)).remove(eq("testQueue"), eq(taskModel.getTaskId()));
        verify(workflowExecutor, times(1)).decide(eq("workflowId"));
    }

    @Test
    public void testPostponeQuietly() {
        asyncSystemTaskExecutor.postponeQuietly("testQueue", new TaskModel());

        verify(queueDAO, times(1)).postpone(
                anyString(), anyString(), anyInt(), eq(asyncSystemTaskExecutor.queueTaskMessagePostponeSecs));
    }

    @Test
    public void testLoadTaskQuietly_TaskFound() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("taskId");

        when(executionDAOFacade.getTaskModel(taskModel.getTaskId())).thenReturn(taskModel);

        asyncSystemTaskExecutor.loadTaskQuietly(taskModel.getTaskId());

        verify(executionDAOFacade, times(1)).getTaskModel(taskModel.getTaskId());
    }

    @Test
    public void testLoadTaskQuietly_TaskNotFound() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        asyncSystemTaskExecutor.loadTaskQuietly("taskId");

        verify(executionDAOFacade, times(1)).getTaskModel(anyString());
    }
}
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

    private TaskModel taskModel;

    private WorkflowModel workflowModel;

    @Before
    public void setUp() {
        workflowSystemTask = mock(WorkflowSystemTask.class);
        taskModel = new TaskModel();
        taskModel.setTaskId("taskId");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);
    }

    @Test
    public void testExecute_TypicalUseCase() throws Exception {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(queueUtils.getQueueName(any(TaskModel.class))).thenReturn("queueName");
        when(workflowExecutor.decide(anyString())).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(workflowSystemTask, times(1)).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(workflowSystemTask, times(1)).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecute_TaskNotFound() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(logger, times(1)).error(anyString(), anyString(), any(WorkflowSystemTask.class));
        verify(workflowSystemTask, never()).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(workflowSystemTask, never()).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecute_TaskAlreadyCompleted() {
        taskModel.setStatus(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(logger, never()).error(anyString(), anyString(), any(WorkflowSystemTask.class));
        verify(workflowSystemTask, never()).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(workflowSystemTask, never()).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecute_ExceedsInProgressLimit() throws Exception {
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(true);
        when(queueUtils.getQueueName(any(TaskModel.class))).thenReturn("queueName");

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(logger, times(1)).warn(anyString(), any(TaskModel.class), anyString());
        verify(workflowSystemTask, never()).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(workflowSystemTask, never()).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecute_ExceedsRateLimitPerFrequency() throws Exception {
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setRateLimitPerFrequency(1);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(TaskModel.class), any())).thenReturn(true);
        when(queueUtils.getQueueName(any(TaskModel.class))).thenReturn("queueName");

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(logger, times(1)).warn(anyString(), any(TaskModel.class), anyInt(), anyString());
        verify(workflowSystemTask, never()).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(workflowSystemTask, never()).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecute_TaskAsyncComplete() throws Exception {
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        when(workflowSystemTask.isAsyncComplete(any(TaskModel.class))).thenReturn(true);
        when(queueUtils.getQueueName(any(TaskModel.class))).thenReturn("queueName");

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_ExceptionDuringExecution() throws Exception {
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(queueUtils.getQueueName(any(TaskModel.class))).thenReturn("queueName");
        doThrow(new Exception("Test Exception")).when(workflowSystemTask).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));

        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");

        verify(logger, times(1)).error(anyString(), any(Exception.class), any(WorkflowSystemTask.class), anyString());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(1)).decide(anyString());
    }

    @Test
    public void testPostponeQuietly_TypicalUseCase() throws Exception {
        asyncSystemTaskExecutor.postponeQuietly("queueName", taskModel);

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testPostponeQuietly_ExceptionDuringPostpone() throws Exception {
        doThrow(new Exception("Test Exception")).when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());

        asyncSystemTaskExecutor.postponeQuietly("queueName", taskModel);

        verify(logger, times(1)).error(anyString(), any(Exception.class));
    }

    @Test
    public void testLoadTaskQuietly_TypicalUseCase() throws Exception {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);

        asyncSystemTaskExecutor.loadTaskQuietly("taskId");

        verify(executionDAOFacade, times(1)).getTaskModel(anyString());
    }

    @Test
    public void testLoadTaskQuietly_ExceptionDuringLoad() throws Exception {
        doThrow(new Exception("Test Exception")).when(executionDAOFacade).getTaskModel(anyString());

        asyncSystemTaskExecutor.loadTaskQuietly("taskId");

        verify(logger, never()).error(anyString(), any(Exception.class));
    }
}
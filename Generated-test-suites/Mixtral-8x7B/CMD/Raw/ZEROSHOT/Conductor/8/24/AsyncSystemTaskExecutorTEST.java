import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskModel;
import com.netflix.conductor.dao.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Component
public class AsyncSystemTaskExecutorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private WorkflowSystemTask workflowSystemTask;

    private TaskModel taskModel;

    private WorkflowModel workflowModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                new ConductorProperties(),
                workflowExecutor);

        workflowSystemTask = mock(WorkflowSystemTask.class);

        taskModel = TaskModel.builder()
                .taskId("123")
                .workflowInstanceId("456")
                .build();

        workflowModel = WorkflowModel.builder()
                .workflowId("456")
                .build();
    }

    @Test
    public void testExecute_TaskDoesNotExist() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecute_TaskIsTerminal() {
        taskModel.setStatus(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecute_TaskIsScheduledAndExceedsInProgressLimit() {
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setTaskDefName("testTaskDef");
        taskModel.setConcurrentExecutionLimit(1);
        taskModel.setInProgressCount(1);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

        ArgumentCaptor<TaskModel> argument = ArgumentCaptor.forClass(TaskModel.class);
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(executionDAOFacade, times(1)).updateTask(argument.capture());
        assertEquals("123", argument.getValue().getTaskId());
    }

    @Test
    public void testExecute_TaskIsScheduledAndExceedsRateLimitPerFrequency() {
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setTaskDefName("testTaskDef");
        taskModel.setConcurrentExecutionLimit(1);
        taskModel.setInProgressCount(0);
        taskModel.setRateLimitPerFrequency(1);
        taskModel.setRateLimitFrequency("1m");

        TaskModel taskDef = TaskModel.builder()
                .taskDefName("testTaskDef")
                .rateLimitPerFrequency(1)
                .rateLimitFrequency("1m")
                .build();

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(TaskModel.class), eq(taskDef)))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

        ArgumentCaptor<TaskModel> argument = ArgumentCaptor.forClass(TaskModel.class);
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(executionDAOFacade, times(1)).updateTask(argument.capture());
        assertEquals("123", argument.getValue().getTaskId());
    }

    @Test
    public void testExecute_TaskIsScheduledAndNotExceedsInProgressLimitOrRateLimitPerFrequency() {
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setTaskDefName("testTaskDef");
        taskModel.setConcurrentExecutionLimit(1);
        taskModel.setInProgressCount(0);
        taskModel.setRateLimitPerFrequency(1);
        taskModel.setRateLimitFrequency("1m");

        TaskModel taskDef = TaskModel.builder()
                .taskDefName("testTaskDef")
                .rateLimitPerFrequency(1)
                .rateLimitFrequency("1m")
                .build();

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(TaskModel.class), eq(taskDef)))
                .thenReturn(false);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecute_WorkflowIsCompleted() {
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecute_WorkflowIsNotCompleted() {
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenThrow(new RuntimeException("Test exception"));

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
        executorService.shutdown();
    }

    @Test
    public void testPostponeQuietly() {
        asyncSystemTaskExecutor.postponeQuietly("testQueue", taskModel);

        verify(queueDAO, times(1)).postpone(
                eq("testQueue"),
                eq(taskModel.getTaskId()),
                eq(taskModel.getWorkflowPriority()),
                eq(asyncSystemTaskExecutor.queueTaskMessagePostponeSecs));
    }

    @Test
    public void testLoadTaskQuietly() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("123");

        assertEquals(taskModel, result);
    }

    @Test
    public void testLoadTaskQuietly_TaskDoesNotExist() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("123");

        assertEquals(null, result);
    }
}
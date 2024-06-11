import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                new ConductorProperties(),
                workflowExecutor
        );

        ReflectionTestUtils.setField(asyncSystemTaskExecutor, "queueTaskMessagePostponeSecs", 10);
        ReflectionTestUtils.setField(asyncSystemTaskExecutor, "systemTaskCallbackTime", 5);
    }

    @Test
    public void testExecute_taskDoesNotExist_shouldRemoveFromQueue() {
        String taskId = UUID.randomUUID().toString();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_taskIsCompleted_shouldRemoveFromQueue() {
        String taskId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.COMPLETED);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_taskIsScheduledAndExceedsInProgressLimit_shouldPostpone() {
        String taskId = UUID.randomUUID().toString();
        TaskModel task = createTaskModel(taskId, TaskModel.Status.SCHEDULED);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecute_taskIsScheduledAndExceedsRateLimitPerFrequency_shouldPostpone() {
        String taskId = UUID.randomUUID().toString();
        TaskModel task = createTaskModel(taskId, TaskModel.Status.SCHEDULED);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(TaskModel.class), any(TaskModel.class)))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
    }

    @Test
    public void testExecute_taskIsInProgress_shouldExecuteSystemTask() {
        String taskId = UUID.randomUUID().toString();
        TaskModel task = createTaskModel(taskId, TaskModel.Status.IN_PROGRESS);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask, times(1)).execute(any(WorkflowModel.class), any(TaskModel.class), any());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyInt());
    }

    private TaskModel createTaskModel(String taskId, TaskModel.Status status) {
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(status);
        task.setWorkflowInstanceId(UUID.randomUUID().toString());
        return task;
    }
}
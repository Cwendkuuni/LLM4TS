import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Initialize ConductorProperties with default values
        ConductorProperties conductorProperties = new ConductorProperties();
        ReflectionTestUtils.setField(asyncSystemTaskExecutor, "conductorProperties", conductorProperties);

        // Initialize queueTaskMessagePostponeSecs and systemTaskCallbackTime with default values
        Field queueTaskMessagePostponeSecsField =
                ReflectionTestUtils.findField(asyncSystemTaskExecutor, "queueTaskMessagePostponeSecs");
        queueTaskMessagePostponeSecsField.setAccessible(true);
        queueTaskMessagePostponeSecsField.set(asyncSystemTaskExecutor, 0L);

        Field systemTaskCallbackTimeField =
                ReflectionTestUtils.findField(asyncSystemTaskExecutor, "systemTaskCallbackTime");
        systemTaskCallbackTimeField.setAccessible(true);
        systemTaskCallbackTimeField.set(asyncSystemTaskExecutor, 0L);
    }

    @Test
    public void testExecute_givenTaskIdAndSystemTask_whenTaskIsNull_thenLogErrorAndReturn() {
        String taskId = "123";
        WorkflowSystemTask systemTask = new WorkflowSystemTask() {};

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(workflowExecutor, never()).decide(anyString());
    }

    @Test
    public void testExecute_givenTaskIdAndSystemTask_whenTaskIsTerminal_thenRemoveFromQueueAndReturn() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(workflowExecutor, never()).decide(anyString());
    }

    @Test
    public void testExecute_givenTaskIdAndSystemTask_whenTaskIsScheduledAndExceedsInProgressLimit_thenPostponeAndReturn() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("testTaskDef");

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(workflowExecutor, never()).decide(anyString());
    }

    @Test
    public void testExecute_givenTaskIdAndSystemTask_whenTaskIsScheduledAndExceedsRateLimit_thenPostponeAndReturn() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("testTaskDef");
        task.setRateLimitPerFrequency(1);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(TaskModel.class), any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(workflowExecutor, never()).decide(anyString());
    }

    @Test
    public void testExecute_givenTaskIdAndSystemTask_whenWorkflowIsCompleted_thenCancelTaskAndReturn() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setWorkflowInstanceId("1234");

        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(executionDAOFacade, times(1)).getWorkflowModel(anyString(), anyBoolean());
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(workflowExecutor, times(1)).decide(anyString());
    }

    @Test
    public void testExecute_givenTaskIdAndSystemTask_whenWorkflowIsNotCompleted_thenExecuteTaskAndReturn() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setWorkflowInstanceId("1234");

        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.RUNNING);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask() {}, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(executionDAOFacade, times(1)).getWorkflowModel(anyString(), anyBoolean());
        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(workflowExecutor, never()).decide(anyString());
    }

    @Test
    public void testPostponeQuietly_givenQueueNameAndTask_whenQueueDAOThrowsException_thenLogError() {
        String queueName = "testQueue";
        TaskModel task = new TaskModel();

        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong()))
                .thenThrow(new RuntimeException("QueueDAO postpone error"));

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly_givenTaskId_whenExecutionDAOFacadeThrowsException_thenReturnNull() {
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException("ExecutionDAOFacade getTaskModel error"));

        TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(null, task);
    }
}
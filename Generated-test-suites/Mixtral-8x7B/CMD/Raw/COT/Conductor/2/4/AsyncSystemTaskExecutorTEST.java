import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Component
public class AsyncSystemTaskExecutorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutor.class);

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private WorkflowSystemTask workflowSystemTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        workflowSystemTask = mock(WorkflowSystemTask.class);

        when(conductorProperties.getSystemTaskWorkerCallbackDuration().getSeconds())
                .thenReturn(1L);
        when(conductorProperties.getTaskExecutionPostponeDuration().getSeconds())
                .thenReturn(1L);
    }

    @Test
    public void testExecute_TaskDoesNotExist_LogsErrorAndRemovesTaskFromQueue() {
        String taskId = "123";

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
        verify(workflowExecutor, never()).decide(anyString());
    }

    @Test
    public void testExecute_TaskIsTerminal_LogsInfoAndRemovesTaskFromQueue() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(executionDAOFacade, never()).updateTask(any(TaskModel.class));
        verify(workflowExecutor, never()).decide(anyString());
    }

    @Test
    public void testExecute_TaskIsScheduledAndExceedsInProgressLimit_PostponesTask() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("testTask");

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, never()).decide(anyString());
    }

    @Test
    public void testExecute_TaskIsScheduledAndExceedsRateLimitPerFrequency_PostponesTask() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("testTask");
        task.setRateLimitPerFrequency(1);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(
                        task, metadataDAO.getTaskDef(task.getTaskDefName())))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, never()).decide(anyString());
    }

    @Test
    public void testExecute_TaskIsExecuted_UpdatesTaskAndDecidesWorkflow() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setTaskDefName("testTask");

        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), true))
                .thenReturn(workflow);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(1)).decide(task.getWorkflowInstanceId());
    }

    @Test
    public void testExecute_ExceptionOccurs_LogsErrorAndDecidesWorkflow() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setTaskDefName("testTask");

        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), true))
                .thenReturn(workflow);
        doThrow(new RuntimeException("test exception")).when(workflowSystemTask).execute(any(), any(), any());

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(1)).decide(task.getWorkflowInstanceId());
    }

    @Test
    public void testPostponeQuietly_TaskIsPostponed() {
        String queueName = "testQueue";
        String taskId = "123";
        TaskModel task = new TaskModel();

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO, times(1)).postpone(queueName, taskId, task.getWorkflowPriority(), 1);
    }

    @Test
    public void testLoadTaskQuietly_TaskIsLoaded() {
        String taskId = "123";
        TaskModel task = new TaskModel();

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(task, result);
    }

    @Test
    public void testLoadTaskQuietly_TaskIsNull() {
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(result);
    }
}
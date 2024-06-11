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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@Component
public class AsyncSystemTaskExecutorTest {

    private final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);

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

    private WorkflowSystemTask workflowSystemTask;
    private TaskModel taskModel;
    private WorkflowModel workflowModel;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        taskModel = new TaskModel();
        taskModel.setTaskId("123");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.WorkflowStatus.RUNNING);

        workflowSystemTask = mock(WorkflowSystemTask.class);
    }

    @Test
    public void execute_typicalUseCase() {
        when(executionDAOFacade.getTaskModel("123")).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel("workflow-1", true)).thenReturn(workflowModel);
        when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

        verify(queueDAO).remove("queue-name", "123");
        verify(workflowExecutor).decide("workflow-1");
    }

    @Test
    public void execute_edgeCase_taskNotFound() {
        when(executionDAOFacade.getTaskModel("123")).thenReturn(null);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(workflowExecutor, never()).decide(anyString());
    }

    @Test
    public void execute_exceptionScenario_exceptionDuringExecution() {
        when(executionDAOFacade.getTaskModel("123")).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel("workflow-1", true)).thenReturn(workflowModel);
        doThrow(new RuntimeException("Test exception")).when(workflowSystemTask).execute(workflowModel, taskModel, workflowExecutor);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(workflowExecutor, never()).decide(anyString());
    }

    @Test
    public void postponeQuietly_typicalUseCase() {
        asyncSystemTaskExecutor.postponeQuietly("queue-name", taskModel);

        verify(queueDAO).postpone("queue-name", "123", taskModel.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void postponeQuietly_exceptionScenario_queueException() {
        doThrow(new RuntimeException("Test exception")).when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());

        asyncSystemTaskExecutor.postponeQuietly("queue-name", taskModel);

        verify(queueDAO).postpone("queue-name", "123", taskModel.getWorkflowPriority(), queueTaskMessagePostponeSecs);
        LOGGER.error("Error postponing task: 123 in queue: queue-name");
    }

    @Test
    public void loadTaskQuietly_typicalUseCase() {
        when(executionDAOFacade.getTaskModel("123")).thenReturn(taskModel);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("123");

        assertEquals(taskModel, result);
    }

    @Test
    public void loadTaskQuietly_edgeCase_taskNotFound() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("123");

        assertEquals(null, result);
    }
}
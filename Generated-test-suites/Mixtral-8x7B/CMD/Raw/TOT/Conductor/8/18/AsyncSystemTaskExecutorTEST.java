
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.EntityNotFoundException;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private Logger logger;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // Set the logger on the asyncSystemTaskExecutor
        ReflectionTestUtils.setField(asyncSystemTaskExecutor, "logger", logger);
    }

    @Test
    public void testExecuteNonTerminalTaskSuccess() {
        // Initialize mock objects and setup expectations
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        // Execute the method under test
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Verify the results
        verify(systemTask, times(1)).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(systemTask, times(1)).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(executionDAOFacade, times(1)).updateTask(task);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(workflowExecutor, times(1)).decide(anyString());
    }

    @Test
    public void testPostponeQuietlyError() {
        // Initialize mock objects and setup expectations
        String queueName = "test-queue";
        TaskModel task = new TaskModel();
        task.setTaskId("123");

        // Expect an error when postponing the task in the queue
        doThrow(new RuntimeException()).when(queueDAO).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);

        // Execute the method under test
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        // Verify that an error was logged
        verify(logger, times(1)).error(anyString(), anyString(), any(Exception.class));
    }

    @Test
    public void testLoadTaskQuietlyNotFound() {
        // Initialize mock objects and setup expectations
        String taskId = "123";

        // Expect an exception when loading the task from the database
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new EntityNotFoundException());

        // Execute the method under test
        TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        // Verify that null is returned
        assertNull(task);
    }
}

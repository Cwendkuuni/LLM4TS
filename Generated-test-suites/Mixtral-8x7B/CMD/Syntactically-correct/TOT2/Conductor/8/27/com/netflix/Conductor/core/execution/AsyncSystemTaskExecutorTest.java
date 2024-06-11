import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
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

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private WorkflowSystemTask workflowSystemTask;
    private TaskModel taskModel;
    private WorkflowModel workflowModel;

    @Before
    public void setUp() {
        workflowSystemTask = mock(WorkflowSystemTask.class);
        taskModel = new TaskModel();
        taskModel.setTaskId("task-1");
        workflowModel = new WorkflowModel();
        workflowModel.setWorkflowInstanceId("workflow-1");
    }

    @Test
    public void testExecute() throws Exception {
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

        verify(executionDAOFacade, times(1)).getTaskModel(taskModel.getTaskId());
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(queueDAO, times(1)).postpone(
                anyString(),
                anyString(),
                anyInt(),
                anyLong()
        );
        verify(executionDAOFacade, times(1)).updateTask(taskModel);
    }

    @Test
    public void testExecuteWithTerminalTask() {
        taskModel.setStatus(TaskModel.Status.COMPLETED);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

        verify(executionDAOFacade, times(1)).getTaskModel(taskModel.getTaskId());
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteWithScheduledTask() {
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

        verify(executionDAOFacade, times(1)).getTaskModel(taskModel.getTaskId());
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(queueDAO, times(1)).postpone(
                anyString(),
                anyString(),
                anyInt(),
                anyLong()
        );
        verify(executionDAOFacade, times(1)).updateTask(taskModel);
    }

    @Test
    public void testExecuteWithNonScheduledTaskAsyncComplete() throws Exception {
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(true);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

        verify(executionDAOFacade, times(1)).getTaskModel(taskModel.getTaskId());
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(executionDAOFacade, times(1)).updateTask(taskModel);
    }

    @Test
    public void testExecuteWithException() throws Exception {
        doThrow(new Exception("Test Exception")).when(workflowSystemTask).execute(
                any(WorkflowModel.class),
                any(TaskModel.class),
                any(WorkflowExecutor.class)
        );

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

        verify(executionDAOFacade, times(1)).getTaskModel(taskModel.getTaskId());
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(queueDAO, times(1)).postpone(
                anyString(),
                anyString(),
                anyInt(),
                anyLong()
        );
        verify(executionDAOFacade, times(1)).updateTask(taskModel);
    }

    @Test
    public void testLoadTaskQuietly_taskFound() {
        when(executionDAOFacade.getTaskModel(taskModel.getTaskId())).thenReturn(taskModel);

        asyncSystemTaskExecutor.loadTaskQuietly(taskModel.getTaskId());

        verify(executionDAOFacade, times(1)).getTaskModel(taskModel.getTaskId());
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() {
        when(executionDAOFacade.getTaskModel(taskModel.getTaskId())).thenThrow(new Exception("Task not found"));

        asyncSystemTaskExecutor.loadTaskQuietly(taskModel.getTaskId());

        verify(executionDAOFacade, times(1)).getTaskModel(taskModel.getTaskId());
    }

    @Test
    public void testPostponeQuietly() {
        asyncSystemTaskExecutor.postponeQuietly("test-queue", taskModel);

        verify(queueDAO, times(1)).postpone(
                anyString(),
                anyString(),
                anyInt(),
                anyLong()
        );
    }

    @Test
    public void testPostponeQuietly_exception() {
        doThrow(new RuntimeException("Test exception")).when(queueDAO).postpone(
                anyString(),
                anyString(),
                anyInt(),
                anyLong()
        );

        asyncSystemTaskExecutor.postponeQuietly("test-queue", taskModel);

        verify(queueDAO, times(1)).postpone(
                anyString(),
                anyString(),
                anyInt(),
                anyLong()
        );
    }
}
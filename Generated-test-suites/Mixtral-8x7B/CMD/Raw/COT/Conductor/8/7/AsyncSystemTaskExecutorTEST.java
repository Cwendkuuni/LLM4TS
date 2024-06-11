import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private QueueUtils queueUtils;

    @Mock
    private WorkflowSystemTask workflowSystemTask;

    @Mock
    private TaskModel taskModel;

    @Mock
    private WorkflowModel workflowModel;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        asyncSystemTaskExecutor =
                new AsyncSystemTaskExecutor(
                        executionDAOFacade,
                        queueDAO,
                        metadataDAO,
                        mock(ConductorProperties.class),
                        workflowExecutor);
    }

    @Test
    public void testExecute() {
        when(executionDAOFacade.getTaskModel(any())).thenReturn(taskModel);
        when(queueDAO.get(any(), any())).thenReturn(taskModel);
        when(queueUtils.getQueueName(any())).thenReturn("test-queue");
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(workflowExecutor.decide(any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "test-task-id");

        verify(executionDAOFacade, times(1)).updateTask(any());
        verify(workflowExecutor, times(1)).decide(any());
    }

    @Test
    public void testExecuteTaskNotFound() {
        when(executionDAOFacade.getTaskModel(any())).thenReturn(null);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "test-task-id");

        verify(executionDAOFacade, times(0)).updateTask(any());
        verify(workflowExecutor, times(0)).decide(any());
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteWithException() {
        when(executionDAOFacade.getTaskModel(any())).thenReturn(taskModel);
        doThrow(RuntimeException.class).when(workflowSystemTask).execute(any(), any(), any());

        asyncSystemTaskExecutor.execute(workflowSystemTask, "test-task-id");
    }

    @Test
    public void testPostponeQuietly() {
        doNothing().when(queueDAO).postpone(any(), any(), any(), any());

        asyncSystemTaskExecutor.postponeQuietly("test-queue", taskModel);

        verify(queueDAO, times(1)).postpone(any(), any(), any(), any());
    }

    @Test
    public void testPostponeQuietlyException() {
        doThrow(RuntimeException.class).when(queueDAO).postpone(any(), any(), any(), any());

        asyncSystemTaskExecutor.postponeQuietly("test-queue", taskModel);

        verify(queueDAO, times(1)).postpone(any(), any(), any(), any());
    }

    @Test
    public void testLoadTaskQuietly() {
        when(executionDAOFacade.getTaskModel(any())).thenReturn(taskModel);

        TaskModel loadedTaskModel = asyncSystemTaskExecutor.loadTaskQuietly("test-task-id");

        verify(executionDAOFacade, times(1)).getTaskModel(any());
        assert(loadedTaskModel == taskModel);
    }

    @Test
    public void testLoadTaskQuietlyNotFound() {
        when(executionDAOFacade.getTaskModel(any())).thenReturn(null);

        TaskModel loadedTaskModel = asyncSystemTaskExecutor.loadTaskQuietly("test-task-id");

        verify(executionDAOFacade, times(1)).getTaskModel(any());
        assert(loadedTaskModel == null);
    }
}
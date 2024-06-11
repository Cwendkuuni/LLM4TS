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
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AsyncSystemTaskExecutorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutor.class);

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
    }

    @Test
    public void testExecute() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask, times(1)).isAsyncComplete(task);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(executionDAOFacade, times(1)).updateTask(task);
    }

    @Test
    public void testExecuteWithTerminalTask() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask, never()).isAsyncComplete(task);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(executionDAOFacade, times(1)).updateTask(task);
    }

    @Test
    public void testExecuteWithScheduledTask() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(executionDAOFacade, times(1)).updateTask(task);
    }

    @Test
    public void testExecuteWithException() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        doThrow(new RuntimeException()).when(systemTask).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask, times(1)).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(executionDAOFacade, times(1)).updateTask(task);
    }

    @Test
    public void testLoadTaskQuietlyWithException() {
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
    }

    @Test
    public void testPostponeQuietlyWithException() {
        String queueName = "123";
        TaskModel task = new TaskModel();

        doThrow(new RuntimeException()).when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }
}
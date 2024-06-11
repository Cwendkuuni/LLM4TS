import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ConductorProperties conductorProperties = new ConductorProperties();
        conductorProperties.getSystemTaskWorkerCallbackDuration().setSeconds(10);
        conductorProperties.getTaskExecutionPostponeDuration().setSeconds(5);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void testExecute() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflowInstanceId");
        task.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask, times(1)).isAsyncComplete(task);
        verify(systemTask, times(1)).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(systemTask, times(1)).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(executionDAOFacade, times(1)).updateTask(task);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(workflowExecutor, times(1)).decide(anyString());
    }

    @Test
    public void testExecuteTerminalStatus() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflowInstanceId");
        task.setStatus(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask, never()).isAsyncComplete(task);
        verify(systemTask, never()).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(systemTask, never()).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(executionDAOFacade, never()).updateTask(task);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(workflowExecutor, never()).decide(anyString());
    }

    @Test
    public void testExecuteAsyncComplete() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflowInstanceId");
        task.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask, times(1)).isAsyncComplete(task);
        verify(systemTask, never()).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(systemTask, never()).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(executionDAOFacade, times(1)).updateTask(task);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(workflowExecutor, never()).decide(anyString());
    }

    @Test
    public void testExecuteException() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflowInstanceId");
        task.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        doThrow(new RuntimeException("test exception")).when(systemTask).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask, times(1)).isAsyncComplete(task);
        verify(systemTask, times(1)).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(executionDAOFacade, times(1)).updateTask(task);
        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(workflowExecutor, never()).decide(anyString());
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queueName";
        TaskModel task = new TaskModel();
        task.setTaskId("taskId");

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO, times(1)).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), 5);
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(task, result);
    }

    @Test
    public void testLoadTaskQuietlyNotFound() {
        String taskId = "taskId";

        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(null, result);
    }
}
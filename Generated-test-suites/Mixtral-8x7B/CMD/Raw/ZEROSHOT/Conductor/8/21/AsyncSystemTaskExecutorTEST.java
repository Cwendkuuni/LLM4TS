import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskModel;
import com.netflix.conductor.dao.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        ConductorProperties conductorProperties = new ConductorProperties();
        conductorProperties.getSystemTaskWorkerCallbackDuration().setSeconds(10);
        conductorProperties.getTaskExecutionPostponeDuration().setSeconds(5);

        Constructor<AsyncSystemTaskExecutor> constructor =
                AsyncSystemTaskExecutor.class.getDeclaredConstructor(
                        ExecutionDAOFacade.class,
                        QueueDAO.class,
                        MetadataDAO.class,
                        ConductorProperties.class,
                        WorkflowExecutor.class);
        constructor.setAccessible(true);
        asyncSystemTaskExecutor =
                constructor.newInstance(
                        executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute() {
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        String taskId = "task1";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType("testTaskType");
        taskModel.setWorkflowInstanceId("workflow1");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(queueDAO.get(anyString(), anyString())).thenReturn(null);
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong()))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // add more asserts based on the behavior of the systemTask
    }

    @Test
    public void testExecuteWithTaskNotFound() {
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        String taskId = "task1";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        // no exception should be thrown
    }

    @Test
    public void testExecuteWithTerminalTask() {
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        String taskId = "task1";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType("testTaskType");
        taskModel.setWorkflowInstanceId("workflow1");
        taskModel.setStatus(TaskModel.Status.FAILED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        // no exception should be thrown
    }

    @Test
    public void testExecuteWithTaskRemoved() {
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        String taskId = "task1";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType("testTaskType");
        taskModel.setWorkflowInstanceId("workflow1");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(queueDAO.get(anyString(), anyString())).thenReturn(taskModel);
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // no exception should be thrown
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queue1";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("task1");
        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);
        // no exception should be thrown
    }

    @Test
    public void testPostponeQuietlyWithException() {
        String queueName = "queue1";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("task1");
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong()))
                .thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        // no exception should be thrown
    }

    @Test
    public void testLoadTaskQuietlyWithTaskFound() {
        String taskId = "task1";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(taskModel, result);
    }

    @Test
    public void testLoadTaskQuietlyWithTaskNotFound() {
        String taskId = "task1";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(result);
    }
}
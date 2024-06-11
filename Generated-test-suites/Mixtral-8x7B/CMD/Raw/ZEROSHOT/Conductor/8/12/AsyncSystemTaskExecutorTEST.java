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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
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

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() throws ExecutionException, InterruptedException {
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setWorkflowInstanceId("workflowId");
        taskModel.setTaskType("taskType");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(queueDAO.get(anyString(), anyString())).thenReturn(taskModel);
        doNothing().when(workflowSystemTask).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        when(workflowExecutor.decide(anyString())).thenReturn(null);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        assertEquals(TaskModel.Status.IN_PROGRESS, taskModel.getStatus());
    }

    @Test
    public void testExecuteWithTerminalTaskStatus() {
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setWorkflowInstanceId("workflowId");
        taskModel.setTaskType("taskType");
        taskModel.setStatus(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        assertEquals(TaskModel.Status.COMPLETED, taskModel.getStatus());
    }

    @Test
    public void testExecuteWithRemovedTask() {
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        String taskId = "123";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // No exception should be thrown
    }

    @Test
    public void testLoadTaskQuietlyWithTask() {
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNotNull(result);
    }

    @Test
    public void testLoadTaskQuietlyWithNoTask() {
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNotNull(result);
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queueName";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("taskId");

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        // No exception should be thrown
    }
}
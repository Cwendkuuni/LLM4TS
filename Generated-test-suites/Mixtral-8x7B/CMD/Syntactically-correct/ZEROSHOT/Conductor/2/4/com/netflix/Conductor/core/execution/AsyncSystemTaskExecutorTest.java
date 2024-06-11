import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.Tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.Tasks.WorkflowSystemTaskStub;
import com.netflix.conductor.core.execution.models.TaskModel;
import com.netflix.conductor.core.execution.models.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AsyncSystemTaskExecutorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    private WorkflowSystemTask workflowSystemTask;

    @Before
    public void setup() {
        workflowSystemTask = new WorkflowSystemTaskStub();
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade, queueDAO, metadataDAO, new ConductorProperties(), workflowExecutor);
    }

    @Test
    public void executeTest() throws Exception {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("task-123");

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "task-123");

        verify(executionDAOFacade, times(1)).getTaskModel("task-123");
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(executionDAOFacade, times(1)).updateTask(taskModel);
        verify(workflowExecutor, times(1)).decide(anyString());

        Mockito.clearInvocations(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
    }

    @Test
    public void loadTaskQuietlyTest() throws Exception {
        String taskId = "task-123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNotNull(result);
        assertEquals(taskId, result.getTaskId());

        Mockito.clearInvocations(executionDAOFacade);
    }

    @Test
    public void postponeQuietlyTest() throws Exception {
        String queueName = "test-queue";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("task-123");

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        verify(queueDAO, times(1)).postpone(queueName, taskModel.getTaskId(),
                taskModel.getWorkflowPriority(), queueTaskMessagePostponeSecs);

        Mockito.clearInvocations(queueDAO);
    }
}
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
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

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        String taskId = "123";
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);

        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setTaskType("testTaskType");

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(queueDAO, times(1)).getQueueName(any());
        verify(executionDAOFacade, times(1)).exceedsInProgressLimit(any());
        verify(executionDAOFacade, times(1)).exceedsRateLimitPerFrequency(any(), any());
        verify(executionDAOFacade, times(1)).getWorkflowModel(anyString(), anyBoolean());
        verify(executionDAOFacade, times(1)).updateTask(any());
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(workflowExecutor, times(1)).decide(anyString());
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "testQueue";
        TaskModel task = new TaskModel();
        task.setTaskId("123");

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "123";

        TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(taskId, task.getTaskId());
    }
}
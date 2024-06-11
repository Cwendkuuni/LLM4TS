import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.TaskModel;
import com.netflix.conductor.dao.WorkflowModel;
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
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

    @Mock
    private WorkflowSystemTask workflowSystemTask;

    @Mock
    private QueueUtils queueUtils;

    @Mock
    private TaskModel taskModel;

    @Mock
    private WorkflowModel workflowModel;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(queueUtils.getQueueName(taskModel)).thenReturn("TestQueue");
        doNothing().when(queueDAO).remove(anyString(), anyString());
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(), any())).thenReturn(false);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.WorkflowStatus.IN_PROGRESS);
        when(workflowSystemTask.execute(any(), any(), any())).thenReturn(true);
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong())).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "TestTaskId");

        verify(executionDAOFacade, times(1)).getTaskModel(anyString());
        verify(queueUtils, times(1)).getQueueName(taskModel);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(taskModel, times(1)).getStatus();
        verify(executionDAOFacade, times(1)).exceedsInProgressLimit(taskModel);
        verify(executionDAOFacade, times(1)).exceedsRateLimitPerFrequency(any(), any());
        verify(executionDAOFacade, times(1)).getWorkflowModel(anyString(), anyBoolean());
        verify(workflowModel, times(1)).getStatus();
        verify(workflowSystemTask, times(1)).execute(any(), any(), any());
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExecuteTerminalStatus() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(queueUtils.getQueueName(taskModel)).thenReturn("TestQueue");
        doNothing().when(queueDAO).remove(anyString(), anyString());
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "TestTaskId");

        verify(executionDAOFacade, times(1)).getTaskModel(anyString());
        verify(queueUtils, times(1)).getQueueName(taskModel);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(taskModel, times(1)).getStatus();
        verify(executionDAOFacade, times(0)).exceedsInProgressLimit(taskModel);
        verify(executionDAOFacade, times(0)).exceedsRateLimitPerFrequency(any(), any());
        verify(executionDAOFacade, times(0)).getWorkflowModel(anyString(), anyBoolean());
        verify(workflowModel, times(0)).getStatus();
        verify(workflowSystemTask, times(0)).execute(any(), any(), any());
        verify(queueDAO, times(0)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExecuteAsyncComplete() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(queueUtils.getQueueName(taskModel)).thenReturn("TestQueue");
        doNothing().when(queueDAO).remove(anyString(), anyString());
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(workflowSystemTask.isAsyncComplete(any())).thenReturn(true);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "TestTaskId");

        verify(executionDAOFacade, times(1)).getTaskModel(anyString());
        verify(queueUtils, times(1)).getQueueName(taskModel);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(taskModel, times(1)).getStatus();
        verify(executionDAOFacade, times(0)).exceedsInProgressLimit(taskModel);
        verify(executionDAOFacade, times(0)).exceedsRateLimitPerFrequency(any(), any());
        verify(executionDAOFacade, times(0)).getWorkflowModel(anyString(), anyBoolean());
        verify(workflowModel, times(0)).getStatus();
        verify(workflowSystemTask, times(0)).execute(any(), any(), any());
        verify(queueDAO, times(0)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testPostponeQuietly() {
        asyncSystemTaskExecutor.postponeQuietly("TestQueue", taskModel);

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("TestTaskId");

        assertEquals(taskModel, result);
    }

    @Test
    public void testLoadTaskQuietlyNull() {
        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(new RuntimeException());

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("TestTaskId");

        assertNotNull(result);
    }
}
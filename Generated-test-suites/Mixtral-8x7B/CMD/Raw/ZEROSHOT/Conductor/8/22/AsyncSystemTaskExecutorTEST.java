import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskModel;
import com.netflix.conductor.dao.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Component
public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

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

    private TaskModel taskModel;

    private Logger logger;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        logger = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                new ConductorProperties(),
                workflowExecutor
        );

        taskModel = new TaskModel();
        taskModel.setTaskId("123");
        taskModel.setTaskType("testTaskType");
        taskModel.setWorkflowInstanceId("testWorkflowInstanceId");
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
    }

    @Test
    public void testExecute() throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(queueDAO.get(anyString(), anyString())).thenReturn(taskModel);
        doNothing().when(workflowExecutor).decide(anyString());
        doNothing().when(workflowSystemTask).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        doNothing().when(workflowSystemTask).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        doNothing().when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
        doNothing().when(executionDAOFacade).updateTask(any(TaskModel.class));

        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

        verify(workflowSystemTask, times(1)).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(workflowSystemTask, times(1)).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(1)).decide(anyString());
    }

    @Test
    public void testExecute_taskNotFound() throws Exception {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

        verify(logger, times(1)).error("TaskId: 123 could not be found while executing {}", workflowSystemTask);
        verify(queueDAO, times(0)).remove(anyString(), anyString());
        verify(executionDAOFacade, times(0)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(0)).decide(anyString());
    }

    @Test
    public void testExecute_taskAlreadyCompleted() throws Exception {
        taskModel.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(0)).decide(anyString());
    }

    @Test
    public void testExecute_taskScheduled() throws Exception {
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(false);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(0)).decide(anyString());
    }

    @Test
    public void testExecute_taskScheduledExceedsInProgressLimit() throws Exception {
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class))).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

        verify(queueDAO, times(0)).remove(anyString(), anyString());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(0)).decide(anyString());
    }

    @Test
    public void testExecute_taskTerminal() throws Exception {
        taskModel.setStatus(TaskModel.Status.FAILED);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(0)).decide(anyString());
    }

    @Test
    public void testExecute_taskInProgress() throws Exception {
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

        verify(queueDAO, times(0)).remove(anyString(), anyString());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(0)).decide(anyString());
    }

    @Test
    public void testExecute_taskAsyncComplete() throws Exception {
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(workflowSystemTask.isAsyncComplete(any(TaskModel.class))).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(1)).decide(anyString());
    }

    @Test
    public void testExecute_taskAsyncCompleteNotScheduled() throws Exception {
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(workflowSystemTask.isAsyncComplete(any(TaskModel.class))).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

        verify(queueDAO, times(0)).remove(anyString(), anyString());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(0)).decide(anyString());
    }

    @Test
    public void testExecute_taskAsyncCompleteNotInProgress() throws Exception {
        taskModel.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(workflowSystemTask.isAsyncComplete(any(TaskModel.class))).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

        verify(queueDAO, times(0)).remove(anyString(), anyString());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(0)).decide(anyString());
    }

    @Test
    public void testExecute_postponeQuietly() throws Exception {
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong())).thenThrow(new Exception());

        asyncSystemTaskExecutor.execute(workflowSystemTask, "123");

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
        verify(workflowExecutor, times(0)).decide(anyString());
    }
}
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

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
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecuteTask() {
        WorkflowSystemTask workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(workflowSystemTask, Mockito.times(1)).start(Mockito.any(WorkflowModel.class),
                Mockito.any(TaskModel.class), Mockito.any(WorkflowExecutor.class));
    }

    @Test
    public void testExecuteTaskWithException() {
        WorkflowSystemTask workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        Mockito.doThrow(new RuntimeException()).when(workflowSystemTask).start(Mockito.any(WorkflowModel.class),
                Mockito.any(TaskModel.class), Mockito.any(WorkflowExecutor.class));
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(workflowSystemTask, Mockito.times(1)).start(Mockito.any(WorkflowModel.class),
                Mockito.any(TaskModel.class), Mockito.any(WorkflowExecutor.class));
        Mockito.verify(executionDAOFacade, Mockito.times(1)).updateTask(taskModel);
    }

    @Test
    public void testExecuteTaskWithTerminalStatus() {
        WorkflowSystemTask workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.COMPLETED);
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(queueDAO, Mockito.times(1)).remove(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testExecuteTaskWithScheduledStatus() {
        WorkflowSystemTask workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(queueDAO, Mockito.times(1)).postpone(Mockito.anyString(), Mockito.anyString(),
                Mockito.anyInt(), Mockito.anyLong());
    }

    @Test
    public void testExecuteTaskWithTaskDefName() {
        WorkflowSystemTask workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskDefName("testTaskDefName");
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(executionDAOFacade, Mockito.times(1)).exceedsInProgressLimit(taskModel);
    }

    @Test
    public void testExecuteTaskWithRateLimitPerFrequency() {
        WorkflowSystemTask workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setRateLimitPerFrequency(1);
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        Mockito.when(executionDAOFacade.exceedsRateLimitPerFrequency(taskModel,
                Mockito.any(TaskDAO.TaskDefinition.class))).thenReturn(true);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(queueDAO, Mockito.times(1)).postpone(Mockito.anyString(), Mockito.anyString(),
                Mockito.anyInt(), Mockito.anyLong());
    }

    @Test
    public void testExecuteTaskWithWorkflowStatusCompleted() {
        WorkflowSystemTask workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        Mockito.when(executionDAOFacade.getWorkflowModel(taskModel.getWorkflowInstanceId(),
                Mockito.anyBoolean())).thenReturn(workflowModel);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(queueDAO, Mockito.times(1)).remove(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testExecuteTaskWithWorkflowStatusFailed() {
        WorkflowSystemTask workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.WorkflowStatus.FAILED);
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        Mockito.when(executionDAOFacade.getWorkflowModel(taskModel.getWorkflowInstanceId(),
                Mockito.anyBoolean())).thenReturn(workflowModel);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(queueDAO, Mockito.times(1)).remove(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testExecuteTaskWithWorkflowStatusTerminated() {
        WorkflowSystemTask workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.WorkflowStatus.TERMINATED);
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        Mockito.when(executionDAOFacade.getWorkflowModel(taskModel.getWorkflowInstanceId(),
                Mockito.anyBoolean())).thenReturn(workflowModel);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(queueDAO, Mockito.times(1)).remove(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testExecuteTaskWithWorkflowStatusRunning() {
        WorkflowSystemTask workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.WorkflowStatus.RUNNING);
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        Mockito.when(executionDAOFacade.getWorkflowModel(taskModel.getWorkflowInstanceId(),
                Mockito.anyBoolean())).thenReturn(workflowModel);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(queueDAO, Mockito.times(1)).postpone(Mockito.anyString(), Mockito.anyString(),
                Mockito.anyInt(), Mockito.anyLong());
    }

    @Test
    public void testExecuteTaskWithAsyncComplete() {
        WorkflowSystemTask workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        Mockito.when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(true);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(queueDAO, Mockito.times(1)).remove(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testExecuteTaskWithAsyncCompleteAndTerminalStatus() {
        WorkflowSystemTask workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setEndTime(System.currentTimeMillis());
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        Mockito.when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(true);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(queueDAO, Mockito.times(1)).remove(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testExecuteTaskWithAsyncCompleteAndNonTerminalStatus() {
        WorkflowSystemTask workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        Mockito.when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(true);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(queueDAO, Mockito.times(1)).remove(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testExecuteTaskWithNonAsyncCompleteAndTerminalStatus() {
        WorkflowSystemTask workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setEndTime(System.currentTimeMillis());
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        Mockito.when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(false);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(queueDAO, Mockito.times(1)).postpone(Mockito.anyString(), Mockito.anyString(),
                Mockito.anyInt(), Mockito.anyLong());
    }

    @Test
    public void testExecuteTaskWithNonAsyncCompleteAndNonTerminalStatus() {
        WorkflowSystemTask workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        Mockito.when(workflowSystemTask.isAsyncComplete(taskModel)).thenReturn(false);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(queueDAO, Mockito.times(1)).postpone(Mockito.anyString(), Mockito.anyString(),
                Mockito.anyInt(), Mockito.anyLong());
    }

    @Test
    public void testLoadTaskQuietlyWithException() {
        String taskId = "123";
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());
        Assert.assertNull(asyncSystemTaskExecutor.loadTaskQuietly(taskId));
    }

    @Test
    public void testPostponeQuietlyWithException() {
        String queueName = "testQueueName";
        String taskId = "123";
        Mockito.doThrow(new RuntimeException()).when(queueDAO).postpone(queueName, taskId,
                Mockito.anyInt(), Mockito.anyLong());
        asyncSystemTaskExecutor.postponeQuietly(queueName, new TaskModel());
    }
}
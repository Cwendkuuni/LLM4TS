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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        asyncSystemTaskExecutor =
                new AsyncSystemTaskExecutor(
                        executionDAOFacade, queueDAO, metadataDAO, new ConductorProperties(), workflowExecutor);
    }

    @Test
    public void execute_taskDoesNotExist_shouldRemoveDeadTaskFromQueue() {
        String taskId = "123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(eq(systemTask.getTaskType()), eq(taskId));
    }

    @Test
    public void execute_taskIsTerminal_shouldRemoveTaskFromQueue() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(anyString(), eq(taskId));
    }

    @Test
    public void execute_taskExceedsInProgressLimit_shouldPostponeTask() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("testTaskDef");
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).postpone(anyString(), eq(taskId), anyInt(), eq(queueTaskMessagePostponeSecs));
    }

    @Test
    public void execute_taskExceedsRateLimit_shouldPostponeTask() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("testTaskDef");
        task.setRateLimitPerFrequency(1);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, metadataDAO.getTaskDef(task.getTaskDefName())))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).postpone(anyString(), eq(taskId), anyInt(), eq(queueTaskMessagePostponeSecs));
    }

    @Test
    public void execute_taskIsCompleted_shouldUpdateTaskAndRemoveFromQueue() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), systemTask.isTaskRetrievalRequired()))
                .thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO).remove(anyString(), eq(taskId));
    }

    @Test
    public void execute_taskIsScheduled_shouldStartTask() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.WorkflowStatus.RUNNING);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), systemTask.isTaskRetrievalRequired()))
                .thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask).start(workflow, task, workflowExecutor);
    }

    @Test
    public void execute_taskIsInProgress_shouldExecuteTask() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.WorkflowStatus.RUNNING);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), systemTask.isTaskRetrievalRequired()))
                .thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask).execute(workflow, task, workflowExecutor);
    }

    @Test
    public void execute_taskIsAsyncComplete_shouldRemoveTaskFromQueue() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.WorkflowStatus.RUNNING);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), systemTask.isTaskRetrievalRequired()))
                .thenReturn(workflow);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(anyString(), eq(taskId));
    }

    @Test
    public void postponeQuietly_shouldPostponeTask() {
        String queueName = "testQueue";
        TaskModel task = new TaskModel();
        task.setTaskId("123");

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO).postpone(eq(queueName), eq(task.getTaskId()), anyInt(), eq(queueTaskMessagePostponeSecs));
    }

    @Test
    public void loadTaskQuietly_shouldReturnTask() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(task, result);
    }

    @Test
    public void loadTaskQuietly_shouldReturnNull() {
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(null, result);
    }
}
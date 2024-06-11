import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.TaskModel;
import com.netflix.conductor.dao.WorkflowModel;
import com.netflix.conductor.config.ConductorProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Component
public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                new ConductorProperties(),
                workflowExecutor
        );
    }

    @Test
    public void testExecute_TaskNotFound() {
        String taskId = "task-123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(any(), eq(taskId));
    }

    @Test
    public void testExecute_TaskTerminalStatus() {
        String taskId = "task-123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
        verify(queueDAO, times(1)).remove(any(), eq(taskId));
    }

    @Test
    public void testExecute_TaskScheduled() {
        String taskId = "task-123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(systemTask, times(1)).isTaskRetrievalRequired();
    }

    @Test
    public void testExecute_TaskInProgress() {
        String workflowId = "workflow-123";
        String taskId = "task-123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId(workflowId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflow);
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
        verify(systemTask, times(1)).execute(workflow, task, workflowExecutor);
    }

    @Test
    public void testExecute_TaskCompleted() {
        String workflowId = "workflow-123";
        String taskId = "task-123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.COMPLETED);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId(workflowId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(
                workflowId,
                asyncSystemTaskExecutor.getSystemTaskCallbackTime() > 0
            )
        ).thenReturn(workflow);
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
        verify(queueDAO, times(1)).remove(any(), eq(taskId));
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queue-123";
        TaskModel task = new TaskModel();
        task.setTaskId("task-123");
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
        verify(queueDAO, times(1)).postpone(
                eq(queueName),
                eq("task-123"),
                anyInt(),
                eq(asyncSystemTaskExecutor.getQueueTaskMessagePostponeSecs())
        );
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "task-123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
    }

    @Test
    public void testLoadTaskQuietly_TaskNotFound() {
        String taskId = "task-123";
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());
        asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
    }
}
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAOTest;
import com.netflix.conductor.dao.TaskModelDao;
import com.netflix.conductor.dao.metadata.MetadataDAOTest;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private TaskModelDao taskModelDao;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(asyncSystemTaskExecutor, "taskModelDao", taskModelDao);
    }

    @Test
    public void testExecute() throws InterruptedException {
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel taskModel = createTaskModel(taskId);
        WorkflowModel workflowModel = createWorkflowModel();

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(taskModel.getWorkflowInstanceId(), false))
                .thenReturn(workflowModel);
        when(queueDAO.postpone(
                        "queueName",
                        taskId,
                        taskModel.getWorkflowPriority(),
                        QueueUtils.getQueuePostponeTimeInSecs()))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(workflowSystemTask, times(1)).start(workflowModel, taskModel, workflowExecutor);
        verify(workflowSystemTask, times(1)).execute(workflowModel, taskModel, workflowExecutor);
        verify(taskModelDao, times(1)).update(taskModel);
        verify(queueDAO, times(1)).remove("queueName", taskId);
        verify(workflowExecutor, times(1)).decide(workflowModel.getInstanceId());
    }

    @Test
    public void testExecuteWithTaskModelNotFound() throws InterruptedException {
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(workflowSystemTask, times(0)).start(workflowModel, taskModel, workflowExecutor);
        verify(workflowSystemTask, times(0)).execute(workflowModel, taskModel, workflowExecutor);
        verify(taskModelDao, times(0)).update(taskModel);
        verify(queueDAO, times(1)).remove("queueName", taskId);
        verify(workflowExecutor, times(0)).decide(workflowModel.getInstanceId());
    }

    private WorkflowModel createWorkflowModel() {
        Map<String, Object> input = new HashMap<>();
        input.put("key", "value");

        return new WorkflowModel()
                .setInstanceId("instanceId")
                .setName("workflowName")
                .setStatus(WorkflowModel.WorkflowStatus.RUNNING)
                .setInput(input);
    }

    private TaskModel createTaskModel(String taskId) {
        return new TaskModel()
                .setTaskId(taskId)
                .setTaskDefinitionName("taskDefinitionName")
                .setStatus(TaskModel.Status.SCHEDULED)
                .setWorkflowInstanceId("instanceId")
                .setWorkflowPriority(1)
                .setRetryCount(3)
                .setScheduledTime(1579014400000L);
    }
}
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAOImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private WorkflowSystemTask workflowSystemTask;

    private TaskModel taskModel;

    private WorkflowModel workflowModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);

        asyncSystemTaskExecutor =
                new AsyncSystemTaskExecutor(
                        executionDAOFacade, queueDAO, metadataDAO, new ConductorProperties(),
                        new WorkflowExecutor(new ConcurrentHashMap<>(), new ThreadPoolTaskExecutor()));

        workflowSystemTask = mock(WorkflowSystemTask.class);
        taskModel = new TaskModel();
        workflowModel = new WorkflowModel();
    }

    @Test
    public void execute_whenTaskDoesNotExist_shouldLogErrorAndRemoveFromQueue() {
        taskModel.setTaskId("1");
        String taskId = "1";

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).remove(anyString(), eq(taskId));
        verify(LOGGER).error(anyString(), anyString(), anyString());
    }

    @Test
    public void execute_whenTaskIsCompleted_shouldRemoveFromQueue() {
        taskModel.setTaskId("1");
        taskModel.setStatus(TaskModel.Status.COMPLETED);
        String taskId = "1";

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).remove(anyString(), eq(taskId));
    }

    @Test
    public void execute_whenTaskIsScheduledAndExceedsInProgressLimit_shouldPostpone() {
        taskModel.setTaskId("1");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setTaskDefName("testTask");
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class)))
                .thenReturn(true);
        String queueName = "testQueue";

        asyncSystemTaskExecutor.execute(workflowSystemTask, "1");

        verify(queueDAO).postpone(eq(queueName), eq("1"), anyInt(), eq(queueTaskMessagePostponeSecs));
    }

    @Test
    public void execute_whenTaskIsScheduledAndExceedsRateLimitPerFrequency_shouldPostpone() {
        taskModel.setTaskId("1");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setTaskDefName("testTask");
        taskModel.setRateLimitPerFrequency(1);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(any(TaskModel.class), anyString()))
                .thenReturn(true);
        String queueName = "testQueue";

        asyncSystemTaskExecutor.execute(workflowSystemTask, "1");

        verify(queueDAO).postpone(eq(queueName), eq("1"), anyInt(), eq(queueTaskMessagePostponeSecs));
    }

    @Test
    public void execute_whenWorkflowIsCompleted_shouldSetTaskToCanceledAndRemoveFromQueue() {
        taskModel.setTaskId("1");
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setWorkflowInstanceId("1");
        workflowModel.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "1");

        verify(taskModel).setStatus(TaskModel.Status.CANCELED);
        verify(executionDAOFacade).updateTask(taskModel);
        verify(queueDAO).remove(anyString(), eq("1"));
    }

    @Test
    public void execute_whenWorkflowIsFailed_shouldSetTaskToFailedAndRemoveFromQueue() {
        taskModel.setTaskId("1");
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setWorkflowInstanceId("1");
        workflowModel.setStatus(WorkflowModel.WorkflowStatus.FAILED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "1");

        verify(taskModel).setStatus(TaskModel.Status.FAILED);
        verify(executionDAOFacade).updateTask(taskModel);
        verify(queueDAO).remove(anyString(), eq("1"));
    }

    @Test
    public void execute_whenWorkflowIsTimedOut_shouldSetTaskToTimedOutAndRemoveFromQueue() {
        taskModel.setTaskId("1");
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setWorkflowInstanceId("1");
        workflowModel.setStatus(WorkflowModel.WorkflowStatus.TIMEDOUT);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "1");

        verify(taskModel).setStatus(TaskModel.Status.TIMEDOUT);
        verify(executionDAOFacade).updateTask(taskModel);
        verify(queueDAO).remove(anyString(), eq("1"));
    }

    @Test
    public void execute_whenWorkflowIsAborted_shouldSetTaskToAbortedAndRemoveFromQueue() {
        taskModel.setTaskId("1");
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setWorkflowInstanceId("1");
        workflowModel.setStatus(WorkflowModel.WorkflowStatus.ABORTED);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "1");

        verify(taskModel).setStatus(TaskModel.Status.ABORTED);
        verify(executionDAOFacade).updateTask(taskModel);
        verify(queueDAO).remove(anyString(), eq("1"));
    }

    @Test
    public void execute_whenAsyncCompleteAndNotScheduled_shouldRemoveFromQueue() {
        taskModel.setTaskId("1");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setStartTime(System.currentTimeMillis() - 1000);
        taskModel.setCallbackAfterSeconds(1);
        taskModel.setWorkflowInstanceId("1");
        taskModel.setTaskDefName("testTask");
        queueDAO = new QueueDAOImpl("testQueue", new ApplicationEventPublisher() {},
                new ConcurrentHashMap<>(), new CountDownLatch(0), new CountDownLatch(0));
        asyncSystemTaskExecutor =
                new AsyncSystemTaskExecutor(
                        executionDAOFacade, queueDAO, metadataDAO, new ConductorProperties(),
                        new WorkflowExecutor(new ConcurrentHashMap<>(), new ThreadPoolTaskExecutor()));

        asyncSystemTaskExecutor.execute(workflowSystemTask, "1");

        verify(queueDAO).remove(eq("testQueue"), eq("1"));
    }

    @Test
    public void execute_whenAsyncCompleteAndInProgress_shouldSetTaskToCompletedAndRemoveFromQueue() {
        taskModel.setTaskId("1");
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskDefName("testTask");
        taskModel.setStartTime(System.currentTimeMillis() - 1000);
        taskModel.setCallbackAfterSeconds(1);
        taskModel.setWorkflowInstanceId("1");

        asyncSystemTaskExecutor.execute(workflowSystemTask, "1");

        verify(taskModel).setStatus(TaskModel.Status.COMPLETED);
        verify(executionDAOFacade).updateTask(taskModel);
        verify(queueDAO).remove(anyString(), eq("1"));
    }

    @Test
    public void execute_whenTaskIsCompleted_shouldIncrementPollCountAndExecute() {
        taskModel.setTaskId("1");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setTaskDefName("testTask");
        taskModel.setStartTime(System.currentTimeMillis() - 1000);
        taskModel.setCallbackAfterSeconds(1);
        taskModel.setWorkflowInstanceId("1");
        taskModel.setPollCount(0);
        workflowModel.setStatus(WorkflowModel.WorkflowStatus.RUNNING);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "1");

        verify(taskModel).incrementPollCount();
        verify(systemWorkflowSystemTask).execute(any(WorkflowModel.class), any(TaskModel.class),
                any(WorkflowExecutor.class));
    }

    @Test
    public void execute_whenTaskIsScheduled_shouldIncrementPollCountAndExecute() {
        taskModel.setTaskId("1");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setTaskDefName("testTask");
        taskModel.setStartTime(System.currentTimeMillis() - 1000);
        taskModel.setCallbackAfterSeconds(1);
        taskModel.setWorkflowInstanceId("1");
        taskModel.setPollCount(0);
        workflowModel.setStatus(WorkflowModel.WorkflowStatus.RUNNING);

        asyncSystemTaskExecutor.execute(workflowSystemTask, "1");

        verify(taskModel).incrementPollCount();
        verify(systemWorkflowSystemTask).execute(any(WorkflowModel.class), any(TaskModel.class),
                any(WorkflowExecutor.class));
    }

    @Test
    public void postponeQuietly_whenTaskDoesNotExist_shouldDoNothing() {
        String queueName = "testQueue";
        String taskId = "1";

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        verifyNoInteractions(queueDAO);
    }

    @Test
    public void loadTaskQuietly_whenTaskDoesNotExist_shouldReturnNull() {
        String taskId = "1";

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verifyNoMoreInteractions(executionDAOFacade);
        assertNull(result);
    }
}
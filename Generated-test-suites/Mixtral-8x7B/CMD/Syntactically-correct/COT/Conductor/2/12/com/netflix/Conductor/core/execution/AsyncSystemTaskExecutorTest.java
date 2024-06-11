import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.SystemTaskDAO;
import com.netflix.conductor.core.execution.tasks.PollTaskSystemTask;
import com.netflix.conductor.core.execution.tasks.SleepSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkerTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueMessageDAO;
import com.netflix.conductor.metrics.Monitors;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AsyncSystemTaskExecutorTest {

    private static final String TASK_ID = "123";
    private static final String WORKFLOW_ID = "456";
    private static final String TASK_TYPE = "poll";
    private static final String QUEUE_NAME = "default";
    private static final String SYSTEM_TASK_TYPE = "workflow-execution";
    private static final String TASK_DEF_NAME = "poll-task";

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private QueueMessageDAO queueMessageDAO;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private SystemTaskDAO systemTaskDAO;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                new ConductorProperties(),
                new WorkflowExecutor(
                        executionDAOFacade,
                        applicationEventPublisher,
                        new ThreadPoolTaskExecutor(),
                        new ConcurrentHashMap<>(),
                        null,
                        null
                )
        );

        asyncSystemTaskExecutor.setQueueDAO(queueDAO);
        asyncSystemTaskExecutor.setQueueMessageDAO(queueMessageDAO);
        asyncSystemTaskExecutor.setSystemTaskDAO(systemTaskDAO);
    }

    @Test
    public void testExecute_TaskNotFound() throws Exception {
        WorkflowSystemTask systemTask = new WorkflowSystemTask(TASK_TYPE, null);

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, TASK_ID);

        verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_TaskCompleted() throws Exception {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        task.setTaskId(TASK_ID);

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask(TASK_TYPE, null), TASK_ID);

        verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_TaskTerminal() throws Exception {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.FAILED);
        task.setTaskId(TASK_ID);

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);
        when(queueDAO.getPendingMessageCount(QUEUE_NAME)).thenReturn(1L);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask(TASK_TYPE, null), TASK_ID);

        verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_TaskConcurrentExecutionLimitExceeded() throws Exception {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskId(TASK_ID);

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask(TASK_TYPE, null), TASK_ID);

        verify(queueDAO, never()).remove(QUEUE_NAME, TASK_ID);
        verify(queueDAO, times(1)).postpone(
                QUEUE_NAME,
                TASK_ID,
                SystemTaskDAO.DEFAULT_QUEUE_PRIORITY,
                asyncSystemTaskExecutor.queueTaskMessagePostponeSecs
        );
    }

    @Test
    public void testExecute_TaskRateLimitExceeded() throws Exception {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskId(TASK_ID);

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, null)).thenReturn(true);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask(TASK_TYPE, null), TASK_ID);

        verify(queueDAO, never()).remove(QUEUE_NAME, TASK_ID);
        verify(queueDAO, times(1)).postpone(
                QUEUE_NAME,
                TASK_ID,
                SystemTaskDAO.DEFAULT_QUEUE_PRIORITY,
                asyncSystemTaskExecutor.queueTaskMessagePostponeSecs
        );
    }

    @Test
    public void testExecute_WorkflowCompleted() throws Exception {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setTaskId(TASK_ID);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask(TASK_TYPE, null), TASK_ID);

        verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_WorkflowFailed() throws Exception {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setTaskId(TASK_ID);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.WorkflowStatus.FAILED);

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask(TASK_TYPE, null), TASK_ID);

        verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_WorkflowRunning() throws Exception {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setTaskId(TASK_ID);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.WorkflowStatus.RUNNING);

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask(TASK_TYPE, null), TASK_ID);

        verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_PollTask() throws Exception {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskId(TASK_ID);
        task.setTaskType(TASK_TYPE);

        WorkerTask workerTask = new PollTask();

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);
        when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(null);

        asyncSystemTaskExecutor.execute(workerTask, TASK_ID);

        verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
        ArgumentCaptor<TaskModel> taskArgumentCaptor = ArgumentCaptor.forClass(TaskModel.class);
        verify(executionDAOFacade, times(1)).updateTask(taskArgumentCaptor.capture());
        TaskModel updatedTask = taskArgumentCaptor.getValue();
        assertEquals(updatedTask.getStatus(), TaskModel.Status.IN_PROGRESS);
    }

    @Test
    public void testExecute_SleepTask() throws Exception {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskId(TASK_ID);
        task.setTaskType(SYSTEM_TASK_TYPE);

        SleepSystemTask sleepSystemTask = new SleepSystemTask(Duration.ofSeconds(10));

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);
        when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenReturn(null);

        CountDownLatch latch = new CountDownLatch(1);
        doAnswer(invocation -> {
            Thread.sleep(100);
            latch.countDown();
            return null;
        }).when(sleepSystemTask).sleep();

        asyncSystemTaskExecutor.execute(sleepSystemTask, TASK_ID);

        assertTrue(latch.await(10, TimeUnit.SECONDS));
        verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
        ArgumentCaptor<TaskModel> taskArgumentCaptor = ArgumentCaptor.forClass(TaskModel.class);
        verify(executionDAOFacade, times(1)).updateTask(taskArgumentCaptor.capture());
        TaskModel updatedTask = taskArgumentCaptor.getValue();
        assertEquals(updatedTask.getStatus(), TaskModel.Status.COMPLETED);
    }

    @Test
    public void testExecute_NullTaskModel() throws Exception {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(null);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask(TASK_TYPE, null), TASK_ID);

        verify(queueDAO, times(1)).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testPostponeQuietly() throws Exception {
        asyncSystemTaskExecutor.postponeQuietly(QUEUE_NAME, new TaskModel());

        verify(queueDAO, times(1)).postpone(
                QUEUE_NAME,
                anyString(),
                SystemTaskDAO.DEFAULT_QUEUE_PRIORITY,
                asyncSystemTaskExecutor.queueTaskMessagePostponeSecs
        );
    }

    @Test
    public void testLoadTaskQuietly_TaskFound() throws Exception {
        TaskModel task = new TaskModel();
        task.setTaskId(TASK_ID);

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(TASK_ID);

        assertEquals(task, result);
    }

    @Test
    public void testLoadTaskQuietly_TaskNotFound() throws Exception {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(null);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(TASK_ID);

        assertNull(result);
    }
}
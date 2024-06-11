import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.model.tasks.TaskModel;
import com.netflix.conductor.common.model.workflow.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.events.EventModel;
import com.netflix.conductor.model.tasks.TaskEventType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.netflix.conductor.core.utils.TestUtils.randomString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflow_123";
    private static final String WORKFLOW_NAME = "test_workflow";
    private static final String TASK_DEF_NAME = "test_task_def";
    private static final String TASK_ID = "task_123";
    private static final String TASK_TYPE = "TASK_TYPE";
    private static final String QUEUE_NAME = "decider_queue";
    private static final String APP_ID = "test_app";
    private static final String EVENT_TYPE = "EventType";
    private static final String EVENT_DATA = "EventData";

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private Monitors monitors;

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(conductorProperties.getAppId()).thenReturn(APP_ID);
        when(conductorProperties.getWorkflowOffsetTimeout()).thenReturn(Optional.of(45));

        WorkflowExecutor executorSpy = spy(workflowExecutor);
        doNothing().when(executorSpy).decide(any());

        workflowSweeper = new WorkflowSweeper(
                executorSpy,
                Optional.of(mock(WorkflowRepairService.class)),
                conductorProperties,
                queueDAO);
    }

    @Test
    public void sweepAsync() throws InterruptedException, ExecutionException, TimeoutException {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
        future.get(10, TimeUnit.SECONDS);

        ArgumentCaptor<Runnable> runnableArgumentCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(workflowExecutor).decide(eq(WORKFLOW_ID));
        verify(workflowExecutor).executeAsync(runnableArgumentCaptor.capture());

        runnableArgumentCaptor.getValue().run();
    }

    @Test
    public void sweep() throws InterruptedException, ExecutionException, TimeoutException {
        AtomicBoolean taskRemoved = new AtomicBoolean(false);
        doAnswer(
                (invocation) -> {
                    TaskModel taskModel = (TaskModel) invocation.getArguments()[0];
                    taskModel.setStatus(TaskModel.Status.COMPLETED);
                    return null;
                })
                .when(workflowExecutor)
                .complete(any(TaskModel.class));

        doAnswer(
                (invocation) -> {
                    TaskModel taskModel = (TaskModel) invocation.getArguments()[0];
                    taskModel.setStatus(TaskModel.Status.FAILED);
                    return null;
                })
                .when(workflowExecutor)
                .fail(any(TaskModel.class));

        doAnswer(
                (invocation) -> {
                    TaskModel taskModel = (TaskModel) invocation.getArguments()[0];
                    taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
                    return null;
                })
                .when(workflowExecutor)
                .updateTask(any(TaskModel.class));

        doAnswer(
                (invocation) -> {
                    TaskModel taskModel = (TaskModel) invocation.getArguments()[0];
                    taskModel.setStatus(TaskModel.Status.SCHEDULED);
                    return null;
                })
                .when(workflowExecutor)
                .schedule(any(TaskModel.class));

        doAnswer(
                (invocation) -> {
                    WorkflowModel workflowModel = (WorkflowModel) invocation.getArguments()[0];
                    workflowModel.setStatus(WorkflowModel.Status.COMPLETED);
                    return null;
                })
                .when(workflowExecutor)
                .completeWorkflow(any(WorkflowModel.class));

        doAnswer(
                (invocation) -> {
                    WorkflowModel workflowModel = (WorkflowModel) invocation.getArguments()[0];
                    workflowModel.setStatus(WorkflowModel.Status.FAILED);
                    return null;
                })
                .when(workflowExecutor)
                .failWorkflow(any(WorkflowModel.class));

        doAnswer(
                (invocation) -> {
                    String workflowId = (String) invocation.getArguments()[0];
                    taskRemoved.set(true);
                    return new AsyncResult<>(null);
                })
                .when(queueDAO)
                .remove(any(), any());

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        workflowModel.setStatus(WorkflowModel.Status.IN_PROGRESS);

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(TASK_ID);
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskType(TASK_TYPE);

        workflowModel.addTask(taskModel);

        workflowSweeper.sweep(WORKFLOW_ID);

        assertTrue(taskRemoved.get());
        verify(queueDAO, times(1)).remove(QUEUE_NAME, WORKFLOW_ID);
    }

    @Test
    public void sweep_workflowNotFound() {
        doThrow(new NotFoundException("Test")).when(workflowExecutor).decide(any());

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, times(1)).remove(QUEUE_NAME, WORKFLOW_ID);
    }

    @Test
    public void unack_inProgressTask() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        workflowModel.setStatus(WorkflowModel.Status.IN_PROGRESS);

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(TASK_ID);
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskType(TASK_TYPE);

        workflowModel.addTask(taskModel);

        workflowSweeper.unack(workflowModel, 60);

        verify(queueDAO, times(1))
                .setUnackTimeout(QUEUE_NAME, WORKFLOW_ID, 60 * 1000L);
    }

    @Test
    public void unack_scheduledTask() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        workflowModel.setStatus(WorkflowModel.Status.IN_PROGRESS);

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(TASK_ID);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setTaskType(TASK_TYPE);

        workflowModel.addTask(taskModel);

        workflowSweeper.unack(workflowModel, 60);

        verify(queueDAO, times(1))
                .setUnackTimeout(QUEUE_NAME, WORKFLOW_ID, 60 * 1000L);
    }

    @Test
    public void unack_noTasks() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        workflowModel.setStatus(WorkflowModel.Status.IN_PROGRESS);

        workflowSweeper.unack(workflowModel, 60);

        verify(queueDAO, times(1))
                .setUnackTimeout(QUEUE_NAME, WORKFLOW_ID, 60 * 1000L);
    }

    @Test
    public void workflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        long minOffset = workflowOffsetTimeout - workflowOffsetTimeout / 3;
        long maxOffset = workflowOffsetTimeout + workflowOffsetTimeout / 3;

        assertTrue(jitter >= minOffset && jitter <= maxOffset);
    }
}
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskRunnerDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.tasks.Status;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflow-123";
    private static final String WORKFLOW_NAME = "test-workflow";
    private static final String TASK_DEF_NAME = "test-task-def";
    private static final String TASK_ID = "task-123";
    private static final String TASK_TYPE = TaskType.TASK_TYPE_SCHEDULED.name();

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private TaskRunnerDAO taskRunnerDAO;

    @Mock
    private Monitors monitors;

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        workflowSweeper = new WorkflowSweeper(
                workflowExecutor,
                Optional.of(mock(WorkflowRepairService.class)),
                mock(ConductorProperties.class),
                queueDAO);

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.initialize();

        ReflectionTestUtils.setField(workflowSweeper, "taskExecutor", executor);
    }

    @Test
    public void testSweepAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = new AsyncResult<>(null).completable();

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(getWorkflowModel());
        when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(true);

        CompletableFuture<Void> result = workflowSweeper.sweepAsync(WORKFLOW_ID).get();

        assertEquals(future, result);
        verify(workflowExecutor, times(1)).decide(WORKFLOW_ID);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweepAsyncNotFoundException() throws ExecutionException, InterruptedException {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException("Not found"));
        when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(true);

        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID).get();

        verify(workflowExecutor, times(1)).decide(WORKFLOW_ID);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweep() {
        workflowSweeper.sweep(WORKFLOW_ID);

        verify(workflowExecutor, times(1)).decide(WORKFLOW_ID);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = getWorkflowModel();
        workflowSweeper.unack(workflowModel, 60);

        verify(queueDAO, times(1)).setUnackTimeout(
                DECIDER_QUEUE, WORKFLOW_ID, 60 * 1000L);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long jitter = workflowSweeper.workflowOffsetWithJitter(45);

        assertTrue(jitter >= 30 && jitter <= 60);
    }

    private WorkflowModel getWorkflowModel() {
        List<TaskModel> tasks = Collections.singletonList(getTaskModel());
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        workflowModel.setTasks(tasks);
        return workflowModel;
    }

    private TaskModel getTaskModel() {
        TaskDef taskDef = new TaskDef();
        taskDef.setTaskType(TASK_TYPE);
        taskDef.setPollTimeoutSeconds(10);

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(TASK_ID);
        taskModel.setTaskDefinition(Optional.of(taskDef));
        taskModel.setTaskType(TASK_TYPE);
        taskModel.setStatus(Status.SCHEDULED);
        taskModel.setScheduledTime(Instant.now().toEpochMilli());
        return taskModel;
    }
}
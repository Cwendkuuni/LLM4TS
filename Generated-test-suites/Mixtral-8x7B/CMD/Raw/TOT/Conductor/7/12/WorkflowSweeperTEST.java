import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.model.tasks.TaskModel;
import com.netflix.conductor.common.model.workflow.WorkflowModel;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.events.WorkflowEventAttributes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private ConductorProperties conductorProperties;

    private WorkflowSweeper workflowSweeper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(workflowExecutor, "taskExecutor", new ThreadPoolTaskExecutor());

        when(conductorProperties.getAppId()).thenReturn("test-app");

        workflowSweeper = new WorkflowSweeper(
                workflowExecutor,
                Optional.of(workflowRepairService),
                conductorProperties,
                queueDAO
        );

        ReflectionTestUtils.setField(workflowSweeper, "sweeperExecutor", new ThreadPoolTaskExecutor());
    }

    @Test
    public void testSweepAsync() throws ExecutionException, InterruptedException {
        String workflowId = "workflow-1";

        CompletableFuture<Void> completableFuture = new AsyncResult<>(null).getNow(null);

        Mockito.when(workflowExecutor.decide(workflowId)).thenReturn(createWorkflowModel());

        CompletableFuture<Void> testFuture = workflowSweeper.sweepAsync(workflowId);

        completableFuture.get(1, TimeUnit.SECONDS);

        testFuture.get(1, TimeUnit.SECONDS);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepAsyncNotFound() {
        String workflowId = "workflow-1";

        Mockito.when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException("Test"));

        CompletableFuture<Void> testFuture = workflowSweeper.sweepAsync(workflowId);

        Assertions.assertTrue(testFuture.isCompletedExceptionally());

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "workflow-1";

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testUnackWithTerminalWorkflowModel() {
        String workflowId = "workflow-1";

        WorkflowModel workflowModel = createWorkflowModel();
        workflowModel.setStatus(WorkflowModel.Status.COMPLETED);

        workflowSweeper.unack(workflowModel, 30);

        verify(queueDAO, times(1)).setUnackTimeout(
                DECIDER_QUEUE, workflowId, 30 * 1000);
    }

    @Test
    public void testUnackWithScheduledTask() {
        String workflowId = "workflow-1";

        WorkflowModel workflowModel = createWorkflowModel();

        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        workflowModel.setTasks(Collections.singletonList(taskModel));

        workflowSweeper.unack(workflowModel, 30);

        verify(queueDAO, times(1)).setUnackTimeout(
                DECIDER_QUEUE, workflowId, 31 * 1000);
    }

    @Test
    public void testUnackWithInProgressWaitTask() {
        String workflowId = "workflow-1";

        WorkflowModel workflowModel = createWorkflowModel();

        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT.name());
        workflowModel.setTasks(Collections.singletonList(taskModel));

        workflowSweeper.unack(workflowModel, 30);

        verify(queueDAO, times(1)).setUnackTimeout(
                DECIDER_QUEUE, workflowId, 31 * 1000);
    }

    @Test
    public void testUnackWithInProgressHumanTask() {
        String workflowId = "workflow-1";

        WorkflowModel workflowModel = createWorkflowModel();

        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_HUMAN.name());
        workflowModel.setTasks(Collections.singletonList(taskModel));

        workflowSweeper.unack(workflowModel, 30);

        verify(queueDAO, times(1)).setUnackTimeout(
                DECIDER_QUEUE, workflowId, 31 * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long jitteredValue = workflowSweeper.workflowOffsetWithJitter(45);

        Assertions.assertTrue(jitteredValue >= 30 && jitteredValue <= 60);
    }

    private WorkflowModel createWorkflowModel() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(WorkflowModel.Status.STARTED);

        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_SIMPLE.name());
        workflowModel.setTasks(Collections.singletonList(taskModel));

        return workflowModel;
    }
}
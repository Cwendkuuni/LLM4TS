import com.netflix.conductor.core.config.TestConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutorImpl;
import com.netflix.conductor.core.metadata.tasks.TaskDef;
import com.netflix.conductor.core.metadata.tasks.TaskType;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.AsyncResult;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class WorkflowSweeperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowSweeperTest.class);

    private WorkflowSweeper workflowSweeper;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        TestConductorProperties properties = new TestConductorProperties();

        workflowSweeper = new WorkflowSweeper(
                workflowExecutor,
                Optional.of(workflowRepairService),
                properties,
                queueDAO
        );

        workflowSweeper = spy(workflowSweeper);

        doNothing().when(workflowSweeper).unack(any(WorkflowModel.class), anyLong());
    }

    @Test
    public void sweepAsync_whenWorkflowNotFound_shouldRemoveFromDeciderQueue() throws ExecutionException, InterruptedException {
        String workflowId = "workflow-not-found";

        doReturn(new AsyncResult<>(null)).when(workflowSweeper).sweepAsync(workflowId);

        CompletableFuture<Void> completableFuture = workflowSweeper.sweepAsync(workflowId);

        completableFuture.get();

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void sweep_whenWorkflowIsTerminal_shouldRemoveFromDeciderQueue() {
        String workflowId = "terminal-workflow";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowStatus.COMPLETED);

        doReturn(workflowModel).when(workflowExecutor).decide(workflowId);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void unack_whenWorkflowHasInProgressTask_shouldSetUnackTimeout() {
        String workflowId = "workflow-with-in-progress";
        WorkflowModel workflowModel = getSampleWorkflowModelWithInProgressTask();

        workflowSweeper.unack(workflowModel, 120);

        verify(queueDAO, times(1))
                .setUnackTimeout(DECIDER_QUEUE, workflowId, 120 * 1000L);
    }

    @Test
    public void unack_whenWorkflowHasScheduledTask_shouldSetUnackTimeout() {
        String workflowId = "workflow-with-scheduled";
        WorkflowModel workflowModel = getSampleWorkflowModelWithScheduledTask();

        workflowSweeper.unack(workflowModel, 30);

        verify(queueDAO, times(1))
                .setUnackTimeout(DECIDER_QUEUE, workflowId, 30 * 1000L);
    }

    @Test
    public void workflowOffsetWithJitter_whenWorkflowOffsetTimeoutIs45_shouldReturnRange30_60() {
        long workflowOffsetTimeout = 45;

        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(30 <= result && result <= 60);
    }

    private WorkflowModel getSampleWorkflowModelWithInProgressTask() {
        WorkflowModel workflowModel = new WorkflowModel();

        List<TaskModel> tasks = Collections.singletonList(
                new TaskModel()
                        .setStatus(Status.IN_PROGRESS)
                        .setTaskType(TaskType.TASK_TYPE_GENERIC)
                        .setScheduledTime(Instant.now().toEpochMilli())
        );

        workflowModel.setTasks(tasks);

        return workflowModel;
    }

    private WorkflowModel getSampleWorkflowModelWithScheduledTask() {
        WorkflowModel workflowModel = new WorkflowModel();

        List<TaskModel> tasks = Collections.singletonList(
                new TaskModel()
                        .setStatus(Status.SCHEDULED)
                        .setTaskType(TaskType.TASK_TYPE_GENERIC)
                        .setScheduledTime(Instant.now().toEpochMilli())
        );

        workflowModel.setTasks(tasks);

        return workflowModel;
    }
}
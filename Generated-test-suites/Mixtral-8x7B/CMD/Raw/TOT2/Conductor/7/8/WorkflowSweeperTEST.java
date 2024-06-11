import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.metadata.tasks.TaskModel;
import com.netflix.conductor.core.metadata.workflow.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel.Status;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.util.AsyncTestUtils;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@EnableAsync
public class WorkflowSweeperTest {

    private final ConductorProperties properties = mock(ConductorProperties.class);
    private final QueueDAO queueDAO = mock(QueueDAO.class);
    private final WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
    private final WorkflowRepairService workflowRepairService = mock(WorkflowRepairService.class);
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        workflowSweeper = new WorkflowSweeper(
                workflowExecutor,
                Optional.of(workflowRepairService),
                properties,
                queueDAO
        );

        ReflectionTestUtils.setField(workflowSweeper, "deciderQueue", DECIDER_QUEUE);
        ReflectionTestUtils.setField(workflowSweeper, "sweeperExecutorName", SWEEPER_EXECUTOR_NAME);

        // Initialize mocks
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(Optional.of(60));
    }

    @Test
    public void testSweepAsync() throws InterruptedException, ExecutionException, TimeoutException {
        String workflowId = "testWorkflowId";

        // Given
        when(workflowExecutor.decide(anyString())).thenReturn(getTestWorkflowModel());

        // When
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);

        // Then
        AsyncTestUtils.getFuture(future, 10, TimeUnit.SECONDS);
        Mockito.verify(queueDAO, Mockito.times(1))
                .remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepAsync_WorkflowNotFound() throws InterruptedException, ExecutionException, TimeoutException {
        String workflowId = "testWorkflowId";

        // Given
        when(workflowExecutor.decide(anyString())).thenThrow(new NotFoundException("Workflow not found", null));

        // When
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);

        // Then
        AsyncTestUtils.getFuture(future, 10, TimeUnit.SECONDS);
        Mockito.verify(queueDAO, Mockito.times(1))
                .remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweep_TerminalStatus() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflow = getTestWorkflowModel();

        // Given
        workflow.setStatus(Status.COMPLETED);

        when(workflowExecutor.decide(anyString())).thenReturn(workflow);

        // When
        workflowSweeper.sweep(workflowId);

        // Then
        Mockito.verify(queueDAO, Mockito.times(1))
                .remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweep_NonTerminalStatus() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflow = getTestWorkflowModel();

        // Given
        workflow.setStatus(Status.STARTED);

        when(workflowExecutor.decide(anyString())).thenReturn(workflow);

        // When
        workflowSweeper.sweep(workflowId);

        // Then
        Mockito.verify(queueDAO, Mockito.times(1))
                .setUnackTimeout(DECIDER_QUEUE, workflowId, 61 * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        // Ensure the result is within the expected range
        long jitterRange = workflowOffsetTimeout / 3;
        long min = workflowOffsetTimeout - jitterRange;
        long max = workflowOffsetTimeout + jitterRange;
        Assert.assertTrue(result >= min && result <= max);
    }

    private WorkflowModel getTestWorkflowModel() {
        TaskModel taskModel1 = new TaskModel();
        taskModel1.setStatus(Status.IN_PROGRESS);
        taskModel1.setTaskType(TaskType.TASK_TYPE_SCHEDULED);

        TaskModel taskModel2 = new TaskModel();
        taskModel2.setStatus(Status.IN_PROGRESS);
        taskModel2.setTaskType(TaskType.TASK_TYPE_WAIT);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setTasks(taskModel1, taskModel2);
        workflow.setWorkflowId("testWorkflowId");

        return workflow;
    }
}
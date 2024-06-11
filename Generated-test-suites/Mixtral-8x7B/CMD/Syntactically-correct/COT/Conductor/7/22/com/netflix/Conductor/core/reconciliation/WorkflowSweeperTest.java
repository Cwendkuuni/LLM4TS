import com.netflix.conductor.core.config.SchedulerConfiguration;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.metadata.tasks.TaskDef;
import com.netflix.conductor.core.metadata.tasks.TaskType;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.common.metadata.tasks.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.util.ReflectionTestUtils;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        workflowSweeper = new WorkflowSweeper(
                workflowExecutor,
                Optional.of(workflowRepairService),
                new ConductorProperties(),
                queueDAO
        );

        ReflectionTestUtils.setField(workflowSweeper, "sweeperExecutorName", SchedulerConfiguration.SWEEPER_EXECUTOR_NAME);
    }

    @Test
    public void testSweepAsync() throws ExecutionException, InterruptedException {
        String workflowId = "testWorkflow";

        @Async(SchedulerConfiguration.SWEEPER_EXECUTOR_NAME)
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);

        future.get();

        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testSweep() throws Exception {
        String workflowId = "testWorkflow";

        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(TaskModel.Status.COMPLETED);

        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepNotFoundException() throws Exception {
        String workflowId = "testWorkflow";

        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException("Test Exception"));

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepException() throws Exception {
        String workflowId = "testWorkflow";

        when(workflowExecutor.decide(workflowId)).thenThrow(new RuntimeException("Test Exception"));

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(0)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testUnack() throws Exception {
        String workflowId = "testWorkflow";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setTasks(Task.getTestTasksList());

        workflowSweeper.unack(workflowModel, 10);

        for (TaskModel taskModel : workflowModel.getTasks()) {
            if (taskModel.getStatus() == TaskModel.Status.IN_PROGRESS) {
                assertEquals("TASK_TYPE_WAIT", taskModel.getTaskType());
                assertEquals(11, taskModel.getUnackTimeout());
                break;
            }
        }
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(result >= (30 + workflowOffsetTimeout));
        assertTrue(result <= (60 + workflowOffsetTimeout));
    }
}
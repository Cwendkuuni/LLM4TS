import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.utils.TestUtils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.service.WorkflowRepairService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.AsyncResult;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflow-123";
    private static final String WORKFLOW_NAME = "test-workflow";
    private static final String TASK_DEF_NAME = "test-task-def";
    private static final String TASK_ID = "task-123";
    private static final String DECIDER_QUEUE = "decider-queue";

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties conductorProperties;

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(conductorProperties.getAppId()).thenReturn("test-app");
        when(conductorProperties.getWorkflowOffsetTimeout()).thenReturn(TestUtils.createDuration(30));

        workflowSweeper =
                new WorkflowSweeper(
                        workflowExecutor,
                        Optional.of(workflowRepairService),
                        conductorProperties,
                        queueDAO);
    }

    @Test
    public void testSweepAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
        future.get();
        verify(workflowExecutor, times(1)).decide(WORKFLOW_ID);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweepWithInProgressTask() {
        WorkflowModel workflowModel = createWorkflowModel();
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweepWithTerminalStatus() {
        WorkflowModel workflowModel = createWorkflowModel();
        workflowModel.setStatus(Status.COMPLETED);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweepWithNonExistentWorkflow() {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException("Workflow not found"));

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweepWithUnackTimeout() {
        WorkflowModel workflowModel = createWorkflowModel();

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(
                WORKFLOW_ID); // It won't remove from the queue since it's not a terminal state

        verify(queueDAO, never()).remove(DECIDER_QUEUE, WORKFLOW_ID);

        verify(queueDAO, times(1))
                .setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 31 * 1000); // 31 seconds as jitter value
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long jitterValue = workflowSweeper.workflowOffsetWithJitter(30);

        // Jitter value should be between -10 and +10
        assertTrue("Jitter value is not within the range", jitterValue >= -10 && jitterValue <= 10);
    }

    private WorkflowModel createWorkflowModel() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        workflowModel.addTask(createTaskModel());
        return workflowModel;
    }

    private TaskModel createTaskModel() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(TASK_ID);
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_SIMPLE);
        taskModel.setTaskDefName(TASK_DEF_NAME);
        return taskModel;
    }
}
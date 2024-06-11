import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.model.tasks.TaskModel;
import com.netflix.conductor.common.model.workflow.WorkflowModel;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.tasks.TestTask;
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

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflow-1";
    private static final String WORKFLOW_NAME = "test-workflow";
    private static final String APPLICATION_ID = "test-app";
    private static final String DECIDER_QUEUE = "decider-queue";
    private static final int TIMEOUT_SECONDS = 60;

    private WorkflowSweeper workflowSweeper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(conductorProperties.getAppId()).thenReturn(APPLICATION_ID);
        when(conductorProperties.getWorkflowOffsetTimeout()).thenReturn(TIMEOUT_SECONDS);

        workflowSweeper = new WorkflowSweeper(
                workflowExecutor,
                Optional.of(workflowRepairService),
                conductorProperties,
                queueDAO
        );
    }

    @Test
    public void testSweepAsync() throws ExecutionException, InterruptedException {
        when(workflowExecutor.decide(anyString()))
                .thenReturn(getTestWorkflowModel());

        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);

        future.get();

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweepAsyncNotFoundException() throws ExecutionException, InterruptedException {
        when(workflowExecutor.decide(anyString()))
                .thenThrow(new NotFoundException("Workflow not found"));

        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);

        future.get();

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweep() {
        WorkflowModel workflowModel = getTestWorkflowModel();

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = getTestWorkflowModel();

        workflowSweeper.unack(workflowModel, TIMEOUT_SECONDS);

        verify(queueDAO, times(1))
                .setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, TIMEOUT_SECONDS * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long jitter = workflowSweeper.workflowOffsetWithJitter(TIMEOUT_SECONDS);

        assertTrue(jitter >= (2 * TIMEOUT_SECONDS / 3));
        assertTrue(jitter <= (2 * TIMEOUT_SECONDS));
    }

    private WorkflowModel getTestWorkflowModel() {
        List<TaskModel> tasks = new ArrayList<>();
        tasks.add(getTestTaskModel(TaskType.TASK_TYPE_WAIT, 45));
        tasks.add(getTestTaskModel(TaskType.TASK_TYPE_SCHEDULED, 60));

        return new WorkflowModel()
                .setWorkflowId(WORKFLOW_ID)
                .setWorkflowName(WORKFLOW_NAME)
                .setStatus(TaskModel.Status.IN_PROGRESS)
                .setTasks(tasks);
    }

    private TaskModel getTestTaskModel(TaskType taskType, int timeoutSeconds) {
        return new TaskModel()
                .setTaskId("task-1")
                .setStatus(TaskModel.Status.IN_PROGRESS)
                .setTaskType(taskType)
                .setTaskDefinition(Optional.of(getTestTaskDef(timeoutSeconds)));
    }

    private TaskDef getTestTaskDef(int timeoutSeconds) {
        return new TaskDef()
                .setName("test-task")
                .setTimeoutSeconds(timeoutSeconds)
                .setPollTimeoutSeconds(timeoutSeconds);
    }
}
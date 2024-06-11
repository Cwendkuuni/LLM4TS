import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.util.AsyncTestUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflow-123";
    private static final String WORKFLOW_NAME = "test-workflow";
    private static final String TASK_DEF_NAME = "test-task-def";
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
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(conductorProperties.getAppId()).thenReturn("test-app");
        when(conductorProperties.getWorkflowOffsetTimeout()).thenReturn(Optional.of(30L));

        workflowSweeper = new WorkflowSweeper(
                workflowExecutor,
                Optional.of(workflowRepairService),
                conductorProperties,
                queueDAO
        );
    }

    @Test
    public void testSweepAsync() throws ExecutionException, InterruptedException {
        AsyncTestUtils.invokeAndAssertFuture(workflowSweeper.sweepAsync(WORKFLOW_ID), null);
        verify(workflowExecutor, times(1)).decide(WORKFLOW_ID);
    }

    @Test
    public void testSweepWithFoundWorkflow() {
        WorkflowContext workflowContext = new WorkflowContext("test-app");
        WorkflowContext.set(workflowContext);
        WorkflowModel workflowModel = createWorkflowModel();

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweepWithNotFoundException() {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException());

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweepWithException() {
        WorkflowContext workflowContext = new WorkflowContext("test-app");
        WorkflowContext.set(workflowContext);
        WorkflowModel workflowModel = createWorkflowModel();

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);
        doThrow(new RuntimeException()).when(workflowExecutor).decide(anyString());

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, never()).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testUnackWithInProgressTask() {
        WorkflowModel workflowModel = createWorkflowModel();
        workflowModel.getTasks().get(0).setStatus(TaskModel.Status.IN_PROGRESS);
        workflowModel.getTasks().get(0).setTaskType(TaskType.TASK_TYPE_WAIT);

        workflowSweeper.unack(workflowModel, 30L);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 31000L);
    }

    @Test
    public void testUnackWithScheduledTask() {
        WorkflowModel workflowModel = createWorkflowModel();
        workflowModel.getTasks().get(0).setStatus(TaskModel.Status.SCHEDULED);
        workflowModel.getTasks().get(0).setTaskType(TaskType.TASK_TYPE_SCHEDULED);

        workflowSweeper.unack(workflowModel, 30L);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 31000L);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45L;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        long min = workflowOffsetTimeout - (workflowOffsetTimeout / 3);
        long max = workflowOffsetTimeout + (workflowOffsetTimeout / 3);

        assertTrue(result >= min && result <= max);
    }

    private WorkflowModel createWorkflowModel() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setWorkflowName(WORKFLOW_NAME);

        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setTaskType(TaskType.TASK_TYPE_SCHEDULED);
        taskModel.setTaskDefName(TASK_DEF_NAME);

        workflowModel.setTasks(List.of(taskModel));

        return workflowModel;
    }
}
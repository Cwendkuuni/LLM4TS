import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowDefinition;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.runners.ParallelRunner;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.utils.TestUtils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.ReconciliationTaskDAO;
import com.netflix.conductor.dao.TaskDAO;
import com.netflix.conductor.dao.TaskRunEventDAO;
import com.netflix.conductor.metrics.Monitors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.powermock.reflect.Whitebox;

import java.time.Instant;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(ParallelRunner.class)
@PrepareForTest({WorkflowSweeper.class, WorkflowExecutor.class})
@PowerMockIgnore("javax.management.*")
public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflow-123";
    private static final String WORKFLOW_NAME = "test-workflow";
    private static final String TASK_DEF_NAME = "test-task";
    private static final String TASK_ID = "task-123";
    private static final String WORKFLOW_OFFSET_QUEUE = "workflow-offset-queue";

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

        when(conductorProperties.getAppId()).thenReturn("test-app");
        when(conductorProperties.getWorkflowOffsetTimeout()).thenReturn(TestUtils.seconds(45));

        Whitebox.setInternalState(WorkflowSweeper.class, "monitors", monitors);

        workflowSweeper = new WorkflowSweeper(
                workflowExecutor,
                Optional.of(PowerMockito.mock(WorkflowRepairService.class)),
                conductorProperties,
                queueDAO
        );

        PowerMockito.mockStatic(WorkflowExecutor.class);
        PowerMockito.when(WorkflowExecutor.getExecutor()).thenReturn(workflowExecutor);
    }

    @After
    public void tearDown() {
        WorkflowSweeper.monitors = null;
    }

    @Test
    public void testSweepAsync() throws Exception {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
        future.get();
        verify(workflowExecutor, times(1)).decide(WORKFLOW_ID);
    }

    @Test
    public void testSweepAsync_WorkflowNotFound() throws Exception {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException("Workflow not found"));

        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
        future.get();
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweepAsync_ExceptionDuringDecide() throws Exception {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new RuntimeException("Decide failed"));

        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
        future.get();
        verify(queueDAO, never()).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweepAsync_WorkflowTerminal() throws Exception {
        WorkflowModel workflow = createWorkflowModel(Status.COMPLETED);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflow);

        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
        future.get();
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweepAsync_WorkflowNotTerminal() throws Exception {
        WorkflowModel workflow = createWorkflowModel(Status.IN_PROGRESS);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflow);

        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
        future.get();
        verify(queueDAO, never()).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweepAsync_WorkflowRepairService() throws Exception {
        WorkflowModel workflow = createWorkflowModel(Status.IN_PROGRESS);
        WorkflowRepairService workflowRepairService = PowerMockito.mock(WorkflowRepairService.class);
        Whitebox.setInternalState(workflowSweeper, "workflowRepairService", workflowRepairService);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflow);

        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
        future.get();
        verify(workflowRepairService, times(1)).verifyAndRepairWorkflowTasks(WORKFLOW_ID);
    }

    @Test
    public void testSweep() throws Exception {
        WorkflowModel workflow = createWorkflowModel(Status.COMPLETED);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflow);

        workflowSweeper.sweep(WORKFLOW_ID);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweep_WorkflowNotFound() throws Exception {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException("Workflow not found"));

        workflowSweeper.sweep(WORKFLOW_ID);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweep_ExceptionDuringDecide() throws Exception {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new RuntimeException("Decide failed"));

        workflowSweeper.sweep(WORKFLOW_ID);
        verify(queueDAO, never()).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweep_WorkflowTerminal() throws Exception {
        WorkflowModel workflow = createWorkflowModel(Status.COMPLETED);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflow);

        workflowSweeper.sweep(WORKFLOW_ID);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweep_WorkflowNotTerminal() throws Exception {
        WorkflowModel workflow = createWorkflowModel(Status.IN_PROGRESS);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflow);

        workflowSweeper.sweep(WORKFLOW_ID);
        verify(queueDAO, never()).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweep_WorkflowRepairService() throws Exception {
        WorkflowModel workflow = createWorkflowModel(Status.IN_PROGRESS);
        WorkflowRepairService workflowRepairService = PowerMockito.mock(WorkflowRepairService.class);
        Whitebox.setInternalState(workflowSweeper, "workflowRepairService", workflowRepairService);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflow);

        workflowSweeper.sweep(WORKFLOW_ID);
        verify(workflowRepairService, times(1)).verifyAndRepairWorkflowTasks(WORKFLOW_ID);
    }

    @Test
    public void testUnack() throws Exception {
        WorkflowModel workflow = createWorkflowModel(Status.IN_PROGRESS, TASK_ID, TaskType.TASK_TYPE_TASK);
        when(workflow.getTasks().get(0).getResponseTimeoutSeconds()).thenReturn(10);

        long timeout = workflowSweeper.workflowOffsetWithJitter(TestUtils.seconds(45));
        workflowSweeper.unack(workflow, timeout);

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, captor.capture());
        assertEquals(timeout * 1000, (long) captor.getValue());
    }

    @Test
    public void testWorkflowOffsetWithJitter() throws Exception {
        long timeout = workflowSweeper.workflowOffsetWithJitter(TestUtils.seconds(45));

        long range = 45 / 3;
        long jitter = new Random().nextInt((int) (2 * range + 1)) - range;
        assertTrue(timeout >= (45 + jitter) && timeout <= (45 + jitter + range));
    }

    private WorkflowModel createWorkflowModel(Status status) {
        return createWorkflowModel(status, null, null);
    }

    private WorkflowModel createWorkflowModel(Status status, String taskId, TaskType taskType) {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId(WORKFLOW_ID);
        workflow.setWorkflowName(WORKFLOW_NAME);
        workflow.setStatus(status);

        if (taskId != null && taskType != null) {
            TaskModel task = new TaskModel();
            task.setTaskId(taskId);
            task.setTaskType(taskType);
            workflow.getTasks().add(task);
        }

        return workflow;
    }
}
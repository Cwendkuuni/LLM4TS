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
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @Mock
    private QueueDAO queueDAO;

    @Captor
    private ArgumentCaptor<WorkflowContext> workflowContextCaptor;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(workflowSweeper, "sweeperExecutorName", "testExecutor");
    }

    @Test
    public void testSweepAsync() throws Exception {
        String workflowId = "testWorkflow";

        workflowSweeper.sweepAsync(workflowId);

        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void testSweep() throws Exception {
        String workflowId = "testWorkflow";
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.ABORTED);

        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
        verify(workflowRepairService, never()).verifyAndRepairWorkflowTasks(workflowId);
    }

    @Test
    public void testSweepWithRepairService() throws Exception {
        String workflowId = "testWorkflow";
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.ABORTED);

        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);
        when(workflowRepairService.verifyAndRepairWorkflowTasks(workflowId))
                .thenReturn(Optional.of(new Object()));

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
        verify(workflowRepairService, times(1)).verifyAndRepairWorkflowTasks(workflowId);
    }

    @Test
    public void testSweepNotFoundException() throws Exception {
        String workflowId = "testWorkflow";

        doThrow(new NotFoundException("Workflow not found"))
                .when(workflowExecutor)
                .decide(workflowId);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testUnack() throws Exception {
        String workflowId = "testWorkflow";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(
                new WorkflowModel.WorkflowDefinition("testWorkflowDef", 10));

        TaskModel taskModel1 = new TaskModel();
        taskModel1.setStatus(TaskModel.Status.IN_PROGRESS);
        TaskModel taskModel2 = new TaskModel();
        taskModel2.setStatus(TaskModel.Status.SCHEDULED);

        workflowModel.getTasks().add(taskModel1);
        workflowModel.getTasks().add(taskModel2);

        ReflectionTestUtils.setField(workflowSweeper, "deciderQueue", DECIDER_QUEUE);

        workflowSweeper.unack(workflowModel, 15);

        ArgumentCaptor<String> queueArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> idArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> timeArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        verify(queueDAO, times(1))
                .setUnackTimeout(queueArgumentCaptor.capture(), idArgumentCaptor.capture(),
                        timeArgumentCaptor.capture());

        assertEquals(DECIDER_QUEUE, queueArgumentCaptor.getValue());
        assertEquals(workflowId, idArgumentCaptor.getValue());
        assertEquals(15000L, (long) timeArgumentCaptor.getValue());
    }

    @Test
    public void testWorkflowOffsetWithJitter() throws Exception {
        long workflowOffsetTimeout = 45;

        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        long range = workflowOffsetTimeout / 3;
        long minValue = workflowOffsetTimeout - range;
        long maxValue = workflowOffsetTimeout + range;

        assertTrue(result >= minValue && result <= maxValue);
    }

    @Async
    public void asyncMethod() {}

    @Test
    public void testAsyncMethod() throws Exception {
        assertFalse(workflowSweeper.sweepAsync(null).isDone());
        assertFalse(workflowSweeper.sweepAsync(null).isCancelled());
    }
}
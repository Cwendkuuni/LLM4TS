import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.events.EventQueueScheduler;
import com.netflix.conductor.core.utils.TestUtils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskDAO;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.service.SchedulerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private TaskDAO taskDAO;

    @Mock
    private SchedulerService schedulerService;

    @Mock
    private EventQueueScheduler eventQueueScheduler;

    @Mock
    private Monitors monitors;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties properties;

    private WorkflowContext workflowContext;

    private WorkflowModel workflowModel;

    private TaskModel taskModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        workflowContext = new WorkflowContext("testAppId");
        WorkflowContext.set(workflowContext);
        workflowModel = TestUtils.createWorkflowModel();
        taskModel = TestUtils.createTaskModel();
    }

    @Test
    public void testSweepAsync() throws Exception {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> future = new CompletableFuture<>();
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        workflowSweeper.sweepAsync(workflowId).get();
        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweep() throws Exception {
        String workflowId = "testWorkflowId";
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        workflowSweeper.sweep(workflowId);
        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testUnack() throws Exception {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = TestUtils.createWorkflowModel();
        workflowSweeper.unack(workflowModel, 100L);
        verify(queueDAO, times(1))
                .setUnackTimeout(DECIDER_QUEUE, workflowId, 100 * 1000L);
    }

    @Test
    public void testWorkflowOffsetWithJitter() throws Exception {
        long workflowOffsetTimeout = 45;
        long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        long min = workflowOffsetTimeout - (workflowOffsetTimeout / 3);
        long max = workflowOffsetTimeout + (workflowOffsetTimeout / 3);
        assertTrue(jitter >= min && jitter <= max);
    }

    @Test
    public void testSweepWithWorkflowNotFound() throws Exception {
        String workflowId = "testWorkflowId";
        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException(""));
        workflowSweeper.sweep(workflowId);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepWithException() throws Exception {
        String workflowId = "testWorkflowId";
        when(workflowExecutor.decide(workflowId)).thenThrow(new Exception(""));
        workflowSweeper.sweep(workflowId);
        verify(monitors, times(1)).error(WorkflowSweeper.class.getSimpleName(), "sweep");
    }

    @Test
    public void testWorkflowRepairServiceIsNull() throws Exception {
        String workflowId = "testWorkflowId";
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(
                workflowExecutor,
                Optional.ofNullable(null),
                properties,
                queueDAO
        );
        workflowSweeper.sweep(workflowId);
        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testWorkflowRepairServiceVerifyAndRepairWorkflowTasks() throws Exception {
        String workflowId = "testWorkflowId";
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(
                workflowExecutor,
                Optional.ofNullable(new WorkflowRepairService(taskDAO, metadataDAO)),
                properties,
                queueDAO
        );
        workflowSweeper.verifyAndRepairWorkflowTasks(workflowId);
        verify(workflowRepairService, times(1)).verifyAndRepairWorkflowTasks(workflowId);
    }
}